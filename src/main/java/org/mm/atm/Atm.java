package org.mm.atm;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.LongStream;

public class Atm {
  private final long[] sortedDenominations;

  public Atm(long[] denominations) {
    if (Arrays.stream(denominations).
        anyMatch(l -> l <= 0)) {
      throw new IllegalArgumentException("Not positive numbers are not allowed.");
    }

    var copy = Arrays.stream(denominations).distinct().toArray();
    Arrays.sort(copy);
    this.sortedDenominations = copy;
  }


  /**
   * @param value - value to exchange
   * @return Flux that "contains" output for solution.
   */
  public Flux<String> solve(long value) {
    AtomicReference<BigInteger> count = new AtomicReference<>(BigInteger.ZERO);
    final var combinations = getExchangeCombinationCharacteristicVectors(value)
        .doOnNext(__ -> count.accumulateAndGet(BigInteger.ONE, BigInteger::add))
        .flatMapSequential(longs -> Flux.<String>create(fluxSink -> {
          fluxSink.next("( ");
          for (int i = 0; i < longs.length; i++) {
            for (long j = 0; j < longs[i]; j++) {
              fluxSink.next(sortedDenominations[i] + " ");
            }
          }
          fluxSink.next(")\n");
          fluxSink.complete();
        }));
    return Flux.concat(
        Mono.just("Combinations: \n"),
        combinations,
        Mono.just("Number of combinations: \n"),
        Mono.fromCallable(() -> count.get().toString() + "\n")
    );
  }

  /**
   * @param value - value to exchange
   * @return Flux that "contains" characteristic vectors of exchange combination.
   */
  Flux<long[]> getExchangeCombinationCharacteristicVectors(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("Negative number is not allowed.");
    }
    return getExchangeCombinationCharacteristicVectors(value, sortedDenominations, sortedDenominations.length);
  }

  private static Flux<long[]> getExchangeCombinationCharacteristicVectors(long value,
                                                                          long[] availableSortedDenominations,
                                                                          int totalDenominationsCount) {
    if (availableSortedDenominations.length == 0) {
      return Flux.empty();
    }

    if (value == 0) {
      long[] zeros = new long[totalDenominationsCount];
      return Flux.just(zeros);
    }

    if (availableSortedDenominations.length == 1) {
      if (value % availableSortedDenominations[0] == 0) {
        long[] combination = new long[totalDenominationsCount];
        combination[0] = value / availableSortedDenominations[0];
        return Flux.just(combination);
      } else {
        return Flux.empty();
      }
    }

    final var length = availableSortedDenominations.length;
    final var maxDenomination = availableSortedDenominations[length - 1];
    final var denominationsWithoutMax = Arrays.copyOfRange(availableSortedDenominations, 0, length - 1);

    final var longStream = LongStream.range(0L, (value / maxDenomination) + 1L).boxed();

    return Flux.fromStream(longStream)
        .flatMap(i -> getExchangeCombinationCharacteristicVectors(
            value - maxDenomination * i,
            denominationsWithoutMax,
            totalDenominationsCount)
            .doOnNext(longs -> longs[length - 1] = i));
  }
}
