package org.mm.atm;

import java.util.*;

public class ATM {
  private final long[] sortedDenominations;

  public ATM(long[] denominations) {
    var copy = Arrays.copyOf(denominations, denominations.length);
    Arrays.sort(copy);
    this.sortedDenominations = copy;
  }

  public void solveAndPrint(long value) {
    final var longsList = evaluateExchangeCombinations(value);
    System.out.println(longsList.size());
    longsList.forEach(longs -> {
      for (int i = 0; i < longs.length; i++) {
        for (long j = 0; j < longs[i]; j++) {
          System.out.print(sortedDenominations[i] + " ");
        }
      }
      System.out.println();

    });
  }

  public List<long[]> evaluateExchangeCombinations(long value) {
    return evaluateExchangeCombinations(value, sortedDenominations, sortedDenominations.length);
  }

  private static List<long[]> evaluateExchangeCombinations(long value,
                                                           long[] availableSortedDenominations,
                                                           int totalDenominationsCount) {
    if (value == 0) {
      long[] zeros = new long[totalDenominationsCount];
      return List.of(zeros);
    }

    if (availableSortedDenominations.length == 1) {
      if (value % availableSortedDenominations[0] == 0) {
        long[] combination = new long[totalDenominationsCount];
        combination[0] = value / availableSortedDenominations[0];
        return List.of(combination);
      } else {
        return Collections.emptyList();
      }
    }

    var result = new ArrayList<long[]>();

    final var length = availableSortedDenominations.length;
    final var maxDenomination = availableSortedDenominations[length - 1];

    final var denominationsWithoutMax = Arrays.copyOfRange(availableSortedDenominations, 0, length - 1);

    for (int i = 0; i <= value / maxDenomination; i++) {
      for (long[] longs : evaluateExchangeCombinations(value - maxDenomination * i,
          denominationsWithoutMax, totalDenominationsCount)) {
        longs[length - 1] = i;
        result.add(longs);
      }
    }
    return result;
  }

  public static void main(String[] args) {

    System.out.println("Enter comma-separated available denomination:");
    var scanner = new Scanner(System.in);
    final var denominations = Arrays.stream(scanner.next().split(","))
        .map(String::trim)
        .mapToLong((Long::parseLong))
        .toArray();

    System.out.println("Enter value to exchange:");
    final var value = scanner.nextLong();

    final var atm1 = new ATM(denominations);
    atm1.solveAndPrint(value);
  }
}
