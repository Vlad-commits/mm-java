package org.mm.pool.common;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mm.pool.ConcurrentTestUtil;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class LimitedSizeCollectionTest {

  @Nested
  class singleThread {
    @Test
    void shouldAddWhenMaxSizeIsNotExceeded() {
      final var collection = new LimitedSizeCollection<>(3);
      final var object = new Object();

      final var add = collection.add(object);

      assertTrue(add);
      assertTrue(collection.stream().anyMatch(object::equals));
    }

    @Test
    void shouldNotAddThenMaxSizeIsExceeded() {
      final var collection = new LimitedSizeCollection<>(3);
      final var object = new Object();

      collection.add(new Object());
      collection.add(new Object());
      collection.add(new Object());
      final var add = collection.add(object);

      assertFalse(add);
      assertFalse(collection.stream().anyMatch(object::equals));
    }
  }

  @Nested
  class concurrent {
    @Test
    void shouldAddWhenMaxSizeIsNotExceeded() {
      final var collection = new LimitedSizeCollection<>(1000);
      final var runnableList = IntStream.range(0, 1000)
          .boxed()
          .<Runnable>map(i -> () -> collection.add(i))
          .collect(Collectors.toList());

      ConcurrentTestUtil.executeSimultaneously(runnableList);

      assertEquals(1000, collection.stream().count());
    }

    @Test
    void shouldNotAddThenMaxSizeIsExceeded() {
      final var collection = new LimitedSizeCollection<>(1000);
      final var runnableList = IntStream.range(0, 1001)
          .boxed()
          .<Runnable>map(i -> () -> collection.add(i))
          .collect(Collectors.toList());

      ConcurrentTestUtil.executeSimultaneously(runnableList);

      assertEquals(1000, collection.stream().count());
    }
  }
}