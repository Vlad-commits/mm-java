package org.mm.pool.thread;

import org.mm.pool.ResourceFactory;

public class WorkerFactory implements ResourceFactory<Worker> {
  private final boolean daemon;

  public WorkerFactory(boolean daemon) {
    this.daemon = daemon;
  }

  @Override
  public Worker create() {
    return new SingleThreadWorker(daemon);
  }

  @Override
  public void destroy(Worker resource) {
    resource.shutdown();
  }
}
