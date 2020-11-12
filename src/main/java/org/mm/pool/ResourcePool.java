package org.mm.pool;

import org.mm.pool.common.PooledResource;

import java.util.Optional;

public interface ResourcePool<R> {

  default PooledResource<R> acquireImmediately() {
    Optional<PooledResource<R>> resource;
    do {
      if (isTerminating()) {
        throw new RuntimeException("Pool is terminating");
      }
      resource = acquire();
    } while (resource.isEmpty());
    return resource.get();
  }

  Optional<PooledResource<R>> acquire();

  void release(PooledResource<R> resource);

  boolean isTerminating();

  void shutdown();
}
