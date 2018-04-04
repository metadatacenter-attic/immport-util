package org.immport.data.airrstandard.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Utility class for directory manipulation
 * 
 * @author BISC-Team
 */
public class DirectoryUtils {

	/** The logger. */
	private static final Logger log = LoggerFactory.getLogger(DirectoryUtils.class);

	private static final int smallBufferSize = 2048;

	private static final String COMMA_SPACE = ", ";
	private static final String EMPTY_STR = "";

	/**
	 * Copy a directory to another directory
	 * 
	 * @param source
	 *            source directory file
	 * @param dest
	 *            destination directory file
	 */
	public static Boolean copyDirectory(File source, File dest) throws IOException {
		Boolean ret = false;
		log.debug("Copying directory source      = " + source.getAbsolutePath());
		log.debug("Copying directory destination = " + dest.getAbsolutePath());
		if (!source.isDirectory() || !dest.getParentFile().isDirectory()) {
			return ret;
		}
		log.debug("Copying directory (removing destination)...");
		remove(dest);
		log.debug("Copying directory source to destination...");
		Path sourcePath = Paths.get(source.getAbsolutePath());
		Path destPath = Paths.get(dest.getAbsolutePath());
		Files.walkFileTree(sourcePath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,
				new CopyDirectory(sourcePath, destPath));
        ret = dest.exists();
        log.debug("Copying directory status = " + ret.toString());
        return ret;
    }

    /**
     * Zip the source folder into the destination zip file. All the folder subtree
     * of the source folder is added to the destination zip file archive. this
     * method excludes file path information.
     * 
     * @param srcDirectory
     *            String, the path of the srcDirectory
     * @param destZipFile
     *            String, the path of the destination zipFile. This file will be
     *            created or erased.
     * 
     */
    private static void addFile(File file, String fileName, ZipOutputStream out) throws Exception {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis, smallBufferSize);
            ZipEntry entry = new ZipEntry(fileName);
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[smallBufferSize];
            while ((count = bis.read(data, 0, smallBufferSize)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception exc) {
                log.error("Closing bis exception in zipDirectory (Exception,  message) = ("
                        + String.join(COMMA_SPACE, exc.getClass().getName(), exc.getMessage()) + ")");
            }
        }
    }

    private static void addDirectory(File directory, String directoryName, ZipOutputStream out) throws Exception {
        String[] files = directory.list();
        for (int i = 0; i < files.length; i++) {
            File file = new File(directory, files[i]);
            if (file.isFile()) {
                String fileName = files[i];
                if (directoryName != null && !directoryName.equals(EMPTY_STR)) {
                    fileName = directoryName + File.separatorChar + fileName;
                }
                addFile(file, fileName, out);
            } else if (file.isDirectory()) {
                addDirectory(file, file.getName(), out);
            }
        }
    }

    public static void zipDirectory(String srcDirectory, String destZipFile) throws Exception {
        FileOutputStream destination = null;
        ZipOutputStream out = null;
        try {
            destination = new FileOutputStream(destZipFile);
            out = new ZipOutputStream(new BufferedOutputStream(destination));
            File srcDir = new File(srcDirectory);
            addDirectory(srcDir, null, out);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                log.error("Closing out exception in zipDirectory (Exception,  message) = ("
                        + String.join(COMMA_SPACE, ex.getClass().getName(), ex.getMessage()) + ")");
            }
        }
    }

    // checks if dir is present.
    // if present it will delete all the content under that dir
    // Returns true if directory was created successfully.

    public static void cleanUpDirectory(File dir) throws IOException {
        remove(dir);
        Path start = Paths.get(dir.getCanonicalPath());
        Files.createDirectory(start);
    }

    public static void remove(File dir) throws IOException {
        Path start = Paths.get(dir.getCanonicalPath());
        if (dir.exists()) {
            log.debug("Removing Entity = " + dir.getAbsolutePath());
            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    log.debug("  Removing file = " + file.toString());
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                    if (e == null) {
                        log.debug("  Removing directory = " + dir.toString());
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        // directory iteration failed
                        throw e;
                    }
                }
            });
        }
    }

}
