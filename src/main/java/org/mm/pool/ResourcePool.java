package org.mm.pool;

import org.mm.pool.common.PooledResource;

import java.util.Optional;

public interface ResourcePool<R> {

  @Deprecated
  default PooledResource<R> acquireImmediately() {
    if (isTerminating()) {
      //todo exception instead?
      return null;
    }
    Optional<PooledResource<R>> resource;
    do {
      resource = acquire();
    } while (resource.isEmpty());
    return resource.get();
  }

  Optional<PooledResource<R>> acquire();

  void release(PooledResource<R> resource);

  boolean isTerminating();

  void shutdown();
}
