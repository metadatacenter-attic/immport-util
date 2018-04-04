package org.immport.data.airrstandard.util;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyDirectory extends SimpleFileVisitor<Path> {

    /** The logger. */
    private static final Logger log = LoggerFactory.getLogger(CopyDirectory.class);

    private Path source;
    private Path target;

    public CopyDirectory(Path source, Path target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
        log.debug("  Copying file = " + source.relativize(file).toString());
        Files.copy(file, target.resolve(source.relativize(file)));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {
        Path targetDirectory = target.resolve(source.relativize(directory));
        try {
            log.debug("  Creating directory = " + source.relativize(directory).toString());
            Files.copy(directory, targetDirectory);
        } catch (FileAlreadyExistsException e) {
            if (!Files.isDirectory(targetDirectory)) {
                throw e;
            }
        }
        return FileVisitResult.CONTINUE;
    }
}
