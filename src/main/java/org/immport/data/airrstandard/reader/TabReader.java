package org.immport.data.airrstandard.reader;

import org.apache.commons.lang3.StringUtils;
import org.immport.data.airrstandard.util.AirrStandardConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TabReader {

  /**
   * The logger.
   */
  private static final Logger log = LoggerFactory.getLogger(TabReader.class);

  private final BufferedReader bufferedReader;

  private int currentLineNumber = 0;

  public TabReader(InputStream inputStream) throws IOException {
    this.bufferedReader = new BufferedReader(
        new InputStreamReader(inputStream, AirrStandardConstants.FILE_ENCODING));
  }

  public int getCurrentLineNumber() {
    return currentLineNumber;
  }

  public String readFile() {
    StringBuffer str = new StringBuffer(AirrStandardConstants.EMPTY_STR);
    if (bufferedReader == null) {
      return str.toString();
    }
    String line;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        currentLineNumber++;
        line = StringUtils.trimToEmpty(line);
        str.append(line + AirrStandardConstants.NEWLINE);
      }
    } catch (Exception ex) {
      log.error("text file reader read error:");
      log.error(ex.getClass().getName() + ":" + ex.getMessage());
      ex.printStackTrace();
    } finally {
      close();
    }
    return str.toString();
  }

  public void close() {
    try {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
    } catch (Exception ex) {
      log.error("closing text file reader error");
      log.error(ex.getClass().getName() + ":" + ex.getMessage());
      ex.printStackTrace();
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e) {
        log.error("closing text file reader error");
        log.error(e.getClass().getName() + ":" + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public String[] readLine() {
    String line;
    if (bufferedReader == null) {
      return null;
    }
    try {
      while ((line = bufferedReader.readLine()) != null) {
        currentLineNumber++;
        line = StringUtils.trimToEmpty(line);
        if (line.equals(AirrStandardConstants.EMPTY_STR)) {
          continue;
        }
        String[] components = line.split(AirrStandardConstants.TAB);
        for (int i = 0; i < components.length; i++) {
          components[i] = StringUtils.trimToEmpty(components[i]);
        }
        return components;
      }
    } catch (Exception ex) {
      log.error("text file reader read error:");
      log.error(ex.getClass().getName() + ":" + ex.getMessage());
      ex.printStackTrace();
      close();
    }
    return null;
  }
}
