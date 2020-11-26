package org.mm;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class Task4Test {

  static Path tempDir = Path.of(".", "temp_task4_test");


  @BeforeEach
  void createTempDir() throws IOException {
    cleanUp();
    Files.createDirectories(tempDir);
  }

  @AfterEach
  void cleanUp() throws IOException {
    if (!Files.exists(tempDir)) {
      return;
    }
    Files.walkFileTree(tempDir,
        new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
          }
        });
  }


  @Test
  void shouldWriteFlatDirToFile() throws IOException {
    Files.createFile(tempDir.resolve("temp1"));
    Files.createFile(tempDir.resolve("temp2"));
    Files.createFile(tempDir.resolve("temp3"));

    Path resultFile = tempDir.resolve("result");
    Task4.writeDirectoryStructureToFile(tempDir, resultFile);

    List<String> resultFileLines = Files.lines(resultFile)
        .collect(toList());
    assertEquals(5, resultFileLines.size());
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/result")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/temp1")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/temp2")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/temp3")));
  }

  @Test
  void shouldWriteNestedDirStructureToFile() throws IOException {
    Files.createFile(tempDir.resolve("temp1"));
    Path nestedDir = tempDir.resolve("tempdir");
    Files.createDirectories(nestedDir);
    Files.createFile(nestedDir.resolve("temp2"));
    Files.createFile(nestedDir.resolve("temp3"));

    Path resultFile = tempDir.resolve("result");
    Task4.writeDirectoryStructureToFile(tempDir, resultFile);

    List<String> resultFileLines = Files.lines(resultFile)
        .collect(toList());
    assertEquals(6, resultFileLines.size());
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/result")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/temp1")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/tempdir")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/tempdir/temp2")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/tempdir/temp3")));
  }


  @Test
  void shouldOverwriteTargetFile() throws IOException {
    Path resultFile = tempDir.resolve("resultoverwrite");
    Files.createFile(resultFile);
    Files.write(resultFile, "something".getBytes());
    assert Files.lines(resultFile).allMatch("something"::equals) : "Test setup failed";


    Task4.writeDirectoryStructureToFile(tempDir, resultFile);

    List<String> resultFileLines = Files.lines(resultFile)
        .collect(toList());
    assertEquals(2, resultFileLines.size());
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/resultoverwrite")));
  }

  @Test
  void shouldCreateDirectoriesForTargetFile() throws IOException {
    Path resultFile = tempDir.resolve("notexistingfolder").resolve("result");

    Task4.writeDirectoryStructureToFile(tempDir, resultFile);

    List<String> resultFileLines = Files.lines(resultFile).collect(toList());
    assertEquals(3, resultFileLines.size());
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/notexistingfolder")));
    assertTrue(resultFileLines.contains(fixSeparators("./temp_task4_test/notexistingfolder/result")));
  }

  private static String fixSeparators(String s) {
    return s.replaceAll("/", File.separator);
  }

}