package org.mm.pool.common;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class LimitedSizeCollection<T> {
  private final AtomicInteger currentSize;
  private final ConcurrentLinkedQueue<T> queue;
  private final AtomicInteger maxSize;

  public LimitedSizeCollection(int maxSize) {
    checkMaxSize(maxSize);
    this.maxSize = new AtomicInteger(maxSize);
    this.queue = new ConcurrentLinkedQueue<>();
    this.currentSize = new AtomicInteger(0);
  }

  private void checkMaxSize(int maxSize) {
    if (maxSize < 0) {
      throw new IllegalArgumentException("Max size can't be negative");
    }
  }

  public boolean add(T object) {
    do {
      int currentSize = this.currentSize.get();
      if (currentSize >= maxSize.get()) {
        return false;
      }
      final var added = queue.add(object);
      if (!added) {
        return false;
      } else {
        if (this.currentSize.compareAndSet(currentSize, currentSize + 1)) {
          if (this.currentSize.get() <= maxSize.get()) {
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

  public void incrementMaxSize(int increment) {
    int currentMaxSize;
    int newMaxSize;
    do {
      currentMaxSize = this.maxSize.get();
      newMaxSize = currentMaxSize + increment;
    } while (!this.maxSize.compareAndSet(currentMaxSize, newMaxSize));
  }


  public void decrementMaxSize(int decrement) throws IllegalArgumentException {
    int currentMaxSize;
    int newMaxSize;
    do {
      currentMaxSize = this.maxSize.get();
      newMaxSize = currentMaxSize - decrement;
      checkMaxSize(newMaxSize);
    } while (!this.maxSize.compareAndSet(currentMaxSize, newMaxSize));
  }

  public Stream<T> stream() {
    return queue.stream();
  }
}
