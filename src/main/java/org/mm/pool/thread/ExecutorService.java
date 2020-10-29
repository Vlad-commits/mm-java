package org.mm.pool.thread;

import org.mm.pool.ResourcePool;
import org.mm.pool.common.DefaultResourcePool;
import org.mm.pool.common.PooledResource;

import java.util.concurrent.RejectedExecutionException;

public class ExecutorService implements Worker {
  private final ResourcePool<Worker> resourcePool;

  public ExecutorService(int maxPoolSize, boolean daemon) {
    resourcePool = new DefaultResourcePool<>(() -> new SingleThreadWorker(daemon), maxPoolSize, 10000);
  }


  @Override
  //todo queue instead of rejecting
  public void execute(Runnable runnable) throws RejectedExecutionException {
    var worker = resourcePool.acquire().orElseThrow(RejectedExecutionException::new);
    doExecute(runnable, worker);
  }

  @Override
  public void shutdown() {
    //todo
  }

  private void doExecute(Runnable runnable, PooledResource<Worker> worker) {
    worker.getResource().execute(() -> {
      try (worker) {
        runnable.run();
      }
    });
  }
}
