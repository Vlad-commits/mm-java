package org.mm;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.nio.file.*;

public class Task4 {
  public static void main(String[] args) throws IOException {
    Tuple2<Path, Path> directoryAndFile = parseArgs(args);

    Path directory = directoryAndFile.getT1();
    Path targetFile = directoryAndFile.getT2();

    writeDirectoryStructureToFile(directory, targetFile);
  }

  static void writeDirectoryStructureToFile(Path directory, Path targetFile) throws IOException {
    Files.deleteIfExists(targetFile);
    Files.createDirectories(targetFile.getParent());
    Files.createFile(targetFile);

    try (var writer = Files.newBufferedWriter(targetFile)) {
      visit(directory, p -> writer.write(p.toString() + "\n"));
    }
  }

  static Tuple2<Path, Path> parseArgs(String[] args) {
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

    return Tuples.of(path, targetPath);
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
