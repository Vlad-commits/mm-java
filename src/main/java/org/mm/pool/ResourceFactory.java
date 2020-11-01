package org.mm.pool;

public interface ResourceFactory<R> {
  R create();

  default void destroy(R resource) {
  }
}
