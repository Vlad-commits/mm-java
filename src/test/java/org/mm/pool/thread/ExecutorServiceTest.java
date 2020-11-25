package org.mm.pool.thread;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mm.pool.ConcurrentTestUtil;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExecutorServiceTest {
  @Test
  @Timeout(3)
  void shouldExecuteInParallel() {
    ExecutorService executorService = new ExecutorService(10);
    AtomicInteger counter = new AtomicInteger(0);
    Runnable waitTwoSecondsAndIncrementCounter = () -> {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
      counter.incrementAndGet();
    };
    Runnable submitWaiting = () -> executorService.execute(waitTwoSecondsAndIncrementCounter);
    List<Runnable> submitWaitingTenTimes = Collections.nCopies(10, submitWaiting);

    ConcurrentTestUtil.executeSimultaneously(submitWaitingTenTimes);
    executorService.shutdown();

    assertEquals(10,counter.get());

  }
}