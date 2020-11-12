package org.mm.pool;

import org.mm.pool.file.JournalService;
import org.mm.pool.thread.ExecutorService;

import java.io.IOException;

public class Task5 {
  public static void main(String[] args) throws InterruptedException {
    final var executorService = new ExecutorService(100);
    final var journalService = new JournalService("test-files", "log", 5);
    for (int i = 0; i < 10000; i++) {
      double a = (int) (1000 * Math.random());
      double b = (int) (1000 * Math.random());
      double c = (int) (1000 * Math.random());

      executorService.execute(() -> {
        final var solution = solve(a, b, c);
        final var formattedOutput = String.format("%fx^2+%fx+%f=0,solution: %s\n", a, b, c, solution);
        try {
          journalService.write(formattedOutput);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    }

    executorService.shutdown();
    journalService.shutdown();
  }

  private static String solve(double a, double b, double c) {
    final var d = b * b - 4 * a * c;
    if (d < 0) {
      return "D<0";
    } else if (d == 0) {
      return String.format("x_1 = %f", -b / (2 * a));
    } else {
      final var x1 = (-b - Math.sqrt(d)) / (2 * a);
      final var x2 = (-b + Math.sqrt(d)) / (2 * a);
      return String.format("x_1 = %f, x_2 = %f", x1, x2);
    }
  }
}
