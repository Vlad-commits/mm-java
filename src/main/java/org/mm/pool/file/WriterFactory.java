package org.mm.pool.file;

import org.mm.pool.ResourceFactory;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class WriterFactory implements ResourceFactory<Writer> {
  private final FileFactory fileFactory;

  public WriterFactory(FileFactory fileFactory) {
    this.fileFactory = fileFactory;
  }

  public WriterFactory(Path baseDir, String fileNamePrefix) {
    this.fileFactory = new FileFactory(baseDir, fileNamePrefix);
  }

  public WriterFactory(String baseDir, String fileNamePrefix) {
    this.fileFactory = new FileFactory(baseDir, fileNamePrefix);
  }

  @Override
  public Writer create() {
    try {
      return Files.newBufferedWriter(fileFactory.create().toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void destroy(Writer resource) {
    try {
      resource.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
