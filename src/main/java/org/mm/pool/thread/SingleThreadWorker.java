package org.mm.pool.thread;


import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

class SingleThreadWorker implements Worker {
  private volatile Runnable runnable;
  private final AtomicBoolean running = new AtomicBoolean(false);
  private volatile boolean terminating = false;

  public SingleThreadWorker(boolean daemon) {
    Thread thread = new Thread(this::run);
    thread.setDaemon(daemon);
    thread.start();
  }

  @Override
  public void execute(Runnable runnable) throws RejectedExecutionException {
    if (runnable == null) {
      throw new NullPointerException();
    }

    if (this.running.compareAndSet(false, true)) {
      this.runnable = runnable;
    } else {
      throw new RejectedExecutionException("Worker is busy.");
    }
  }

  @Override
  public void shutdown() {
    this.terminating = true;
  }

  public void run() {
    while (!this.terminating) {
      while (this.runnable == null && !this.terminating) {
        Thread.onSpinWait();
      }
      if (runnable != null) {
        this.runnable.run();
        this.runnable = null;
        this.running.set(false);
      }
    }
  }
}
