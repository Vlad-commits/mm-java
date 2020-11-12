package org.mm.pool.thread;


import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReference;

class SingleThreadWorker implements Worker {
  private final AtomicReference<Runnable> runnable = new AtomicReference<>(null);
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
    do {
      Thread.onSpinWait();
    } while (!this.runnable.compareAndSet(null, runnable));
  }

  @Override
  public void shutdown() {
    this.terminating = true;
  }

  public void run() {
    while (!this.terminating) {
      while (this.runnable.get() == null && !this.terminating) {
        Thread.onSpinWait();
      }
      final var runnable = this.runnable.get();
      if (runnable != null) {
        try {
          runnable.run();
        } finally {
          this.runnable.set(null);
        }
      }
    }
  }
}
