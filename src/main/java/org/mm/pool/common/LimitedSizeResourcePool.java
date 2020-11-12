package org.mm.pool.common;

import org.mm.pool.ResourceFactory;
import org.mm.pool.ResourcePool;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class LimitedSizeResourcePool<R> implements ResourcePool<R> {
  protected final ResourceFactory<R> resourceFactory;
  protected final LimitedSizeCollection<PooledResourceDescriptor<R>> resources;
  protected volatile boolean terminating;

  public LimitedSizeResourcePool(ResourceFactory<R> resourceFactory, int maxPoolSize) {
    this.resourceFactory = resourceFactory;
    this.resources = new LimitedSizeCollection<>(maxPoolSize);
    this.terminating = false;
  }

  @Override
  public Optional<PooledResource<R>> acquire() {
    if (this.terminating) {
      return Optional.empty();
    }

    final var pooled = tryGetPooled();
    if (pooled.isPresent()) {
      return pooled.map(PooledResourceDescriptor::getResource);
    } else {
      final var newResource = tryCreateNew();
      if (newResource.isPresent()) {
        return newResource.map(PooledResourceDescriptor::getResource);
      } else {
        return Optional.empty();
      }
    }
  }

  @Override
  public void release(PooledResource<R> resource) {
    final var pooledResourceDescriptor = resources.stream()
        .filter(resourceDescriptor -> resourceDescriptor.getResource() == resource)
        .findAny()
        .orElseThrow(IllegalStateException::new);

    doRelease(pooledResourceDescriptor);

    pooledResourceDescriptor.getStatus().set(Status.FREE);
  }

  @Override
  public boolean isTerminating() {
    return this.terminating;
  }

  @Override
  public void shutdown() {
    this.terminating = true;
    while (resources.stream().anyMatch(r -> Status.BUSY.equals(r.getStatus().get()))) {
      Thread.onSpinWait();
    }
    resources.stream()
        .forEach(r -> {
          final R resource = r.getResource().getResource();
          resourceFactory.destroy(resource);
        });
  }

  protected void doRelease(PooledResourceDescriptor<R> pooledResourceDescriptor) {

  }

  private Optional<PooledResourceDescriptor<R>> tryGetPooled() {
    Optional<PooledResourceDescriptor<R>> freeResource;
    AtomicReference<Status> status;
    do {
      freeResource = resources
          .stream()
          .filter(resourceDescriptor -> Status.FREE.equals(resourceDescriptor.getStatus().get()))
          .findFirst();
      if (freeResource.isPresent()) {
        status = freeResource.get().getStatus();
      } else {
        break;
      }
    } while (!status.compareAndSet(Status.FREE, Status.BUSY));
    return freeResource;
  }

  private Optional<PooledResourceDescriptor<R>> tryCreateNew() {

    PooledResourceDescriptor<R> result = new PooledResourceDescriptor<>();
    if (resources.add(result)) {
      result.setResource(new PooledResource<>(resourceFactory.create(), this));
      return Optional.of(result);
    } else {
      return Optional.empty();
    }
  }

  protected static class PooledResourceDescriptor<R> {
    private final AtomicReference<Status> status = new AtomicReference<>(Status.BUSY);
    private volatile PooledResource<R> resource;

    public PooledResource<R> getResource() {
      return resource;
    }

    public void setResource(PooledResource<R> resource) {
      this.resource = resource;
    }

    public AtomicReference<Status> getStatus() {
      return status;
    }
  }
}
