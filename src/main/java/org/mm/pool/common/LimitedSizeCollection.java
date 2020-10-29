package org.mm.pool.common;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class LimitedSizeCollection<T> {
  private final AtomicInteger currentSize;
  private final ConcurrentLinkedQueue<T> queue;
  //todo increase\decrease
  private volatile int maxSize;

  public LimitedSizeCollection(int maxSize) {
    this.maxSize = maxSize;
    this.queue = new ConcurrentLinkedQueue<>();
    this.currentSize = new AtomicInteger(0);
  }

  public boolean add(T object) {
    do {
      int currentSize = this.currentSize.get();
      if (currentSize >= maxSize) {
        return false;
      }
      final var added = queue.add(object);
      if (!added) {
        return false;
      } else {
        if (this.currentSize.compareAndSet(currentSize, currentSize + 1)) {
          if (this.currentSize.get() <= maxSize) {
            return true;
          } else {
            this.queue.remove(object);
            this.currentSize.getAndDecrement();
          }
        } else {
          this.queue.remove(object);
        }
      }
    } while (true);
  }

  public void remove(T object) {
    queue.remove(object);
    currentSize.getAndDecrement();
  }

  public Stream<T> stream() {
    return queue.stream();
  }
}
