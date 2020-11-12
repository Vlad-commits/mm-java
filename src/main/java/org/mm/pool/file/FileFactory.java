package org.mm.pool.file;

import org.mm.pool.ResourceFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileFactory implements ResourceFactory<File> {
  private final Path baseDir;
  private final String fileNamePrefix;
  private final AtomicInteger currentFileNumber = new AtomicInteger(0);

  public FileFactory(String baseDir, String fileNamePrefix) {
    this(Paths.get(baseDir), fileNamePrefix);
  }

  public FileFactory(Path baseDir, String fileNamePrefix) {
    this.baseDir = baseDir;
    this.fileNamePrefix = fileNamePrefix;
  }

  @Override
  public File create() {
    try {
      Files.createDirectories(baseDir);
      return tryCreateFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private File tryCreateFile() throws IOException {
    try {
      var currentFileNumber = this.currentFileNumber.getAndIncrement();
      final var path = Files.createFile(baseDir.resolve(fileNamePrefix + currentFileNumber));
      return path.toFile();
    } catch (FileAlreadyExistsException e) {
      return tryCreateFile();
    }
  }
}
