package org.mm.atm;

import reactor.core.publisher.Flux;

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

    var copy = Arrays.copyOf(denominations, denominations.length);
    Arrays.sort(copy);
    this.sortedDenominations = copy;
  }

  public void solveAndPrint(long value) {
    AtomicReference<BigInteger> count = new AtomicReference<>(BigInteger.ZERO);
    System.out.println("Combinations: ");
    evaluateExchangeCombinations(value)
        .doOnNext(longs -> {
          System.out.print("( ");
          for (int i = 0; i < longs.length; i++) {
            for (long j = 0; j < longs[i]; j++) {
              System.out.print(sortedDenominations[i] + " ");
            }
          }
          System.out.print(")");
          System.out.println();
        })
        .doOnNext(__ -> count.accumulateAndGet(BigInteger.ONE, BigInteger::add))
        .blockLast();
    System.out.println("Number of combinations: ");
    System.out.println(count.get());
  }

  public Flux<long[]> evaluateExchangeCombinations(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("Negative number is not allowed.");
    }
    return evaluateExchangeCombinations(value, sortedDenominations, sortedDenominations.length);
  }

  private static Flux<long[]> evaluateExchangeCombinations(long value,
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
        .flatMap(i -> evaluateExchangeCombinations(
            value - maxDenomination * i,
            denominationsWithoutMax,
            totalDenominationsCount)
            .doOnNext(longs -> longs[length - 1] = i));
  }


  public static void main(String[] args) {
    final var cli = new Cli().start();
    final var atm1 = new Atm(cli.getDenominations());
    atm1.solveAndPrint(cli.getValue());
  }
}
