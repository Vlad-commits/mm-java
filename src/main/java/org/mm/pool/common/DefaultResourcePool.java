package org.mm.pool.common;

import org.mm.pool.ResourceFactory;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;

public class DefaultResourcePool<R> extends LimitedSizeResourcePool<R> {
  private final LimitedSizeCollection<Tuple2<PooledResourceDescriptor<R>, Long>> releasedTimestampsMillis;
  private final long unusedResourceTtlMs;
  private final Timer cleanUpTimer;

  public DefaultResourcePool(ResourceFactory<R> resourceFactory,
                             int maxPoolSize,
                             long unusedResourceTtlMs) {
    this(resourceFactory, maxPoolSize, unusedResourceTtlMs, unusedResourceTtlMs, 1000);
  }

  public DefaultResourcePool(ResourceFactory<R> resourceFactory,
                             int maxPoolSize,
                             long unusedResourceTtlMs,
                             long cleanUnDelayMs,
                             long cleanUpIntervalMs) {
    super(resourceFactory, maxPoolSize);
    this.releasedTimestampsMillis = new LimitedSizeCollection<>(maxPoolSize);
    this.unusedResourceTtlMs = unusedResourceTtlMs;
    cleanUpTimer = new Timer(true);
    startTimer(cleanUnDelayMs, cleanUpIntervalMs);

  }

  @Override
  protected void doRelease(PooledResourceDescriptor<R> pooledResourceDescriptor) {
    releasedTimestampsMillis.add(Tuples.of(pooledResourceDescriptor, System.currentTimeMillis()));
    super.doRelease(pooledResourceDescriptor);
  }

  private void startTimer(long delayMillis, long intervalMillis) {
    cleanUpTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        cleanUpUnusedResources();
      }
    }, intervalMillis, delayMillis);
  }

  private void cleanUpUnusedResources() {
    resources.stream()
        .filter(resourceDescriptor -> Status.FREE.equals(resourceDescriptor.getStatus().get()))
        .filter(resourceDescriptor1 -> findReleaseTimestampRecord(resourceDescriptor1)
            .map(Tuple2::getT2)
            .filter(ts -> System.currentTimeMillis() - ts > unusedResourceTtlMs)
            .isPresent())
        .forEach(resourceDescriptor -> {
          final var acquired = resourceDescriptor.getStatus().compareAndSet(Status.FREE, Status.BUSY);
          if (acquired) {
            final var releaseTimestampRecord = findReleaseTimestampRecord(resourceDescriptor);
            final var expired = releaseTimestampRecord
                .map(Tuple2::getT2)
                .filter(ts -> System.currentTimeMillis() - ts > unusedResourceTtlMs)
                .isPresent();
            if (expired) {
              resourceFactory.destroy(resourceDescriptor.getResource().getResource());
              resources.remove(resourceDescriptor);
              releasedTimestampsMillis.remove(releaseTimestampRecord.get());
            } else {
              resourceDescriptor.getStatus().set(Status.FREE);
            }
          }
        });
  }

  private Optional<Tuple2<PooledResourceDescriptor<R>, Long>> findReleaseTimestampRecord(PooledResourceDescriptor<R> resourceDescriptor) {
    return releasedTimestampsMillis.stream()
        .filter(tuple -> tuple.getT1() == resourceDescriptor)
        .findAny();
  }

}
