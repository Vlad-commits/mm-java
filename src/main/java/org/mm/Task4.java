package org.mm;

import java.io.IOException;
import java.nio.file.*;

public class Task4 {
  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      illegalArguments();
    }
    final var directory = args[0];
    final var file = args[1];

    final var path = Paths.get(directory);
    if (!Files.isDirectory(path)) {
      illegalArguments();
    }

    final var targetPath = Paths.get(file);

    Files.deleteIfExists(targetPath);
    Files.createFile(targetPath);

    try (var writer = Files.newBufferedWriter(targetPath)) {
      visit(path, p -> writer.write(p.toString() + "\n"));
    }
  }


  private static void visit(Path path, PathVisitor action) {
    try {
      action.accept(path);
      if (Files.isDirectory(path)) {
        Files.list(path).forEach(p -> visit(p, action));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private interface PathVisitor {
    void accept(Path path) throws IOException;
  }

  private static void illegalArguments() {
    throw new IllegalArgumentException("2 arguments expected: directory path and file path");
  }
}
