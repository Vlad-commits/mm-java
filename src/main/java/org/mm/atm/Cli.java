package org.mm.atm;

import java.util.Arrays;
import java.util.Scanner;

class Cli {
  private long[] denominations;
  private long value;

  public Cli start() {
    var scanner = new Scanner(System.in);


    boolean validDenominations;
    do {
      validDenominations = true;
      System.out.println("Enter comma-separated available denomination: ");
      {
        try {
          denominations = Arrays.stream(scanner.nextLine().split(","))
              .map(String::trim)
              .mapToLong((Long::parseLong))
              .toArray();
        } catch (NumberFormatException e) {
          System.out.println("Bad format.");
          validDenominations = false;
          continue;
        }

        if (Arrays.stream(denominations).anyMatch(l -> l <= 0)) {
          System.out.println("Not positive numbers are not allowed.");
          validDenominations = false;
        }
      }
    } while (!validDenominations);

    boolean validValue;

    do {
      validValue = true;
      System.out.println("Enter value to exchange: ");
      try {
        final var line = scanner.nextLine().trim();
        value = Long.parseLong(line);
      } catch (NumberFormatException e) {
        validValue = false;
        System.out.println("Bad input.");
        continue;
      }

      if (value < 0) {
        validValue = false;
        System.out.println("Negative number is not allowed.");
      }
    } while (!validValue);

    return this;
  }

  public long[] getDenominations() {
    return denominations;
  }

  public long getValue() {
    return value;
  }
}
