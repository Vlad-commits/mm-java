package org.mm.pool.thread;

import org.mm.pool.ResourcePool;
import org.mm.pool.common.DefaultResourcePool;
import org.mm.pool.common.PooledResource;

import java.util.concurrent.RejectedExecutionException;

public class ExecutorService implements Worker {
  private final ResourcePool<Worker> resourcePool;

  public ExecutorService(int maxPoolSize) {
    this(maxPoolSize, false);
  }

  public ExecutorService(int maxPoolSize, boolean daemon) {
    this(maxPoolSize, daemon, 10000);
  }

  public ExecutorService(int maxPoolSize, boolean daemon, long ttlMs) {
    resourcePool = new DefaultResourcePool<>(new WorkerFactory(daemon), maxPoolSize, ttlMs);
  }


  @Override
  public void execute(Runnable runnable) throws RejectedExecutionException {
    var worker = resourcePool.acquireImmediately();
    doExecute(runnable, worker);
  }

  @Override
  public void shutdown() {
    resourcePool.shutdown();
  }

  private void doExecute(Runnable runnable, PooledResource<Worker> worker) {
    worker.getResource().execute(() -> {
      try {
        runnable.run();
      } finally {
        resourcePool.release(worker);
      }
    });

  }
}
