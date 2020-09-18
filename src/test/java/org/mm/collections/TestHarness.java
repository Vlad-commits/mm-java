package org.mm.collections;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHarness {


  static <T> void testAction(T expected, T actual, Consumer<T> action) {

    Exception expectedException = null;
    Exception actualException = null;
    try {
      action.accept(expected);
    } catch (Exception e) {
      expectedException = e;
    }

    try {
      action.accept(actual);
    } catch (Exception e) {
      actualException = e;
    }

    if ((expectedException == null && actualException == null)) {
      return;
    }
    assertEquals(expectedException != null, actualException != null);
    assertEquals(expectedException.getClass(), actualException.getClass());
    assertEquals(expected, actual);
  }

  static <T, R> void testFunction(T expected, T actual, Function<T, R> function) {
    Exception expectedException = null;
    Exception actualException = null;
    R expectedResult = null;
    R actualResult = null;

    try {
      expectedResult = function.apply(expected);
    } catch (Exception e) {
      expectedException = e;
    }

    try {
      actualResult = function.apply(actual);
    } catch (Exception e) {
      actualException = e;
    }

    if ((expectedException == null && actualException == null)) {
      assertEquals(expectedResult, actualResult);
      return;
    }
    assertEquals(expectedException != null, actualException != null);
    assertEquals(expectedException.getClass(), actualException.getClass());
    assertEquals(expected, actual);
  }
}
