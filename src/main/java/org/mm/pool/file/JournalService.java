package org.mm.pool.file;

import org.mm.pool.ResourceFactory;
import org.mm.pool.ResourcePool;
import org.mm.pool.common.DefaultResourcePool;

import java.io.IOException;
import java.io.Writer;


public class JournalService {
  private final ResourcePool<Writer> writersPool;

  public JournalService(String baseDir, String fileNamePrefix, int maxPoolSize) {
    this(new WriterFactory(baseDir, fileNamePrefix), maxPoolSize);
  }

  public JournalService(ResourceFactory<Writer> factory, int maxPoolSize) {
    this(new DefaultResourcePool<>(factory, maxPoolSize, 10000));
  }

  public JournalService(ResourcePool<Writer> writersPool) {
    this.writersPool = writersPool;
  }


  public void write(String string) throws IOException {
    final var writer = writersPool.acquireImmediately();
    try (writer) {
      writer.getResource().write(string);
    }
  }


  public void shutdown() {
    writersPool.shutdown();
  }
}

