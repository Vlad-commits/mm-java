package org.mm.pool;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ConcurrentTestUtil {
    public static void executeSimultaneously(Collection<Runnable> runnableCollection) {
        final var startCountDown = new CountDownLatch(1);
        final var finishCountDown = new CountDownLatch(runnableCollection.size());
        runnableCollection.forEach(r ->
                new Thread(() -> {
                    try {
                        startCountDown.await(10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                    r.run();
                    finishCountDown.countDown();
                }).start()
        );
        startCountDown.countDown();

        try {
            finishCountDown.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
