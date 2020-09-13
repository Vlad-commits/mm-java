package org.mm.atm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Arrays;

class AtmTest {

  @Test
  void example1() {
    final var atm = new Atm(new long[] {3, 2});

    atm.evaluateExchangeCombinations(5)
        .as(StepVerifier::create)
        .expectNextMatches(c -> Arrays.equals(new long[] {1, 1}, c))
        .verifyComplete();
  }

  @Test
  void example2_1() {
    final var atm = new Atm(new long[] {1, 2});

    atm.evaluateExchangeCombinations(5)
        .as(StepVerifier::create)
        .expectNextMatches(c -> Arrays.equals(new long[] {5, 0}, c))
        .expectNextMatches(c -> Arrays.equals(new long[] {3, 1}, c))
        .expectNextMatches(c -> Arrays.equals(new long[] {1, 2}, c))
        .verifyComplete();
  }

  @Test
  void example2_2() {
    final var atm = new Atm(new long[] {1, 2});

    atm.evaluateExchangeCombinations(4)
        .as(StepVerifier::create)
        .expectNextMatches(c -> Arrays.equals(new long[] {4, 0}, c))
        .expectNextMatches(c -> Arrays.equals(new long[] {2, 1}, c))
        .expectNextMatches(c -> Arrays.equals(new long[] {0, 2}, c))
        .verifyComplete();
  }

  @Test
  void shouldReturnEmptyWhenDenominationsIsEmpty() {
    final var atm = new Atm(new long[] {});
    atm.evaluateExchangeCombinations(4)
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void shouldReturnOneWhenValueIsZero() {
    final var atm = new Atm(new long[] {1, 2, 3, 4});

    atm.evaluateExchangeCombinations(0)
        .as(StepVerifier::create)
        .expectNextMatches(c -> Arrays.equals(new long[] {0, 0, 0, 0}, c))
        .verifyComplete();
  }

  @Test
  void shouldReturnEmptyWhenDenominationIsGreaterThanValue() {
    final var atm = new Atm(new long[] {5});
    atm.evaluateExchangeCombinations(4)
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void shouldReturnEmptyWhenValueIsNotDivisibleByDenomination() {
    final var atm = new Atm(new long[] {2});

    atm.evaluateExchangeCombinations(5)
        .as(StepVerifier::create)
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  @Disabled("Takes too much time. Run manually and check memory usage.")
  void shouldNotFailOnBigValues() {
    final var atm = new Atm(new long[] {2, 1});

    atm.evaluateExchangeCombinations(Long.MAX_VALUE)
        .ignoreElements()
        .as(StepVerifier::create)
        .verifyComplete();
  }
}