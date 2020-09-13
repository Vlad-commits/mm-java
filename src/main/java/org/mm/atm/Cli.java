package org.mm.atm;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

class Cli {
  private long[] denominations;
  private long value;
  private final Scanner scanner;
  private final PrintStream out;

  public Cli() {
    this(System.in, System.out);
  }

  public Cli(InputStream in, PrintStream out) {
    this.scanner = new Scanner(in);
    this.out = out;
  }

  public Cli start() {
    readDenominations();
    readValue();
    return this;
  }

  private void readDenominations() {
    boolean validDenominations;
    long[] denominations;
    do {
      validDenominations = true;
      out.println("Enter comma-separated available denomination: ");
      final var split = scanner.nextLine().split(",");
      denominations = new long[split.length];

      for (int i = 0, splitLength = split.length; i < splitLength; i++) {

        final var parsed = safeParseLong(split[i].trim());
        if (parsed.isEmpty()) {
          validDenominations = false;
          break;
        } else {
          final var denomination = parsed.get();
          if (denomination <= 0) {
            out.println("Not positive numbers are not allowed.");
            validDenominations = false;
            break;
          } else {
            denominations[i] = denomination;
          }
        }
      }
    } while (!validDenominations);
    this.denominations = denominations;
  }

  private void readValue() {
    boolean validValue;
    Optional<Long> parsed;
    do {
      validValue = true;
      out.println("Enter value to exchange: ");
      final var line = scanner.nextLine().trim();

      parsed = safeParseLong(line);
      if (parsed.isEmpty()) {
        validValue = false;
      } else {
        final var value = parsed.get();
        if (value < 0) {
          validValue = false;
          out.println("Negative number is not allowed.");
        }
      }

    } while (!validValue);
    this.value = parsed.get();
  }

  private Optional<Long> safeParseLong(String line) {
    try {
      final var parsedDouble = Double.parseDouble(line);
      if (!Double.isFinite(parsedDouble)) {
        out.printf("\"%s\" is not a finite number.\n", line);
        return Optional.empty();
      }

      if (parsedDouble % 1 != 0) {
        out.printf("Decimal part must be zero: \"%s\".\n", line);
        return Optional.empty();
      }
      return Optional.of((long) parsedDouble);

    } catch (NumberFormatException nfe) {
      out.printf("Provided string is not a number: \"%s\".\n", line);
      return Optional.empty();
    }

  }

  public long[] getDenominations() {
    return denominations;
  }

  public long getValue() {
    return value;
  }
}
