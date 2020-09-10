package org.mm.atm;

import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

  @Test
  void evaluateExchangeCombinations1() {
    final var atm = new ATM(LongStream.of(3, 2).toArray());
    final var longsList = atm.evaluateExchangeCombinations(5);
    assertEquals(1, longsList.size());
    long[] combination1 = {1, 1};
    assertArrayEquals(combination1, longsList.get(0));

  }

  @Test
  void evaluateExchangeCombinations2() {
    final var atm = new ATM(LongStream.of(1, 2).toArray());
    final var longsList = atm.evaluateExchangeCombinations(5);
    assertEquals(3, longsList.size());
    long[] combination1 = {5, 0};
    long[] combination2 = {3, 1};
    long[] combination3 = {1, 2};
    assertArrayEquals(combination1, longsList.get(0));
    assertArrayEquals(combination2, longsList.get(1));
    assertArrayEquals(combination3, longsList.get(2));
  }

  @Test
  void evaluateExchangeCombinations3() {
    final var atm = new ATM(LongStream.of(1, 2).toArray());
    final var longsList = atm.evaluateExchangeCombinations(4);
    assertEquals(3, longsList.size());
    long[] combination1 = {4, 0};
    long[] combination2 = {2, 1};
    long[] combination3 = {0, 2};
    assertArrayEquals(combination1, longsList.get(0));
    assertArrayEquals(combination2, longsList.get(1));
    assertArrayEquals(combination3, longsList.get(2));
  }
}