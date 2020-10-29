package org.mm.pool.thread;

import java.util.concurrent.RejectedExecutionException;

public interface Worker {
  void execute(Runnable runnable) throws RejectedExecutionException;

  void shutdown();

}
