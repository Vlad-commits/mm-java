package org.mm.pool.common;

public final class PooledResource<R> implements AutoCloseable {
  protected final R resource;
  protected final LimitedSizeResourcePool<R> pool;

  public PooledResource(R resource, LimitedSizeResourcePool<R> pool) {
    this.resource = resource;
    this.pool = pool;
  }

  @Override
  public void close() {
    pool.release(this);
  }

  public R getResource() {
    return resource;
  }

}
