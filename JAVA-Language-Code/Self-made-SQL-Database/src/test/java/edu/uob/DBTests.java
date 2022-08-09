package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class DBTests {

  private DBServer server;

  // we make a new server for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup(@TempDir File dbDir) throws IOException {
    // Notice the @TempDir annotation, this instructs JUnit to create a new temp directory somewhere
    // and proceeds to *delete* that directory when the test finishes.
    // You can read the specifics of this at
    // https://junit.org/junit5/docs/5.4.2/api/org/junit/jupiter/api/io/TempDir.html

    // If you want to inspect the content of the directory during/after a test run for debugging,
    // simply replace `dbDir` here with your own File instance that points to somewhere you know.
    // IMPORTANT: If you do this, make sure you rerun the tests using `dbDir` again to make sure it
    // still works and keep it that way for the submission.

    server = new DBServer(Paths.get(".").toAbsolutePath().toFile());


  }

  // Here's a basic test for spawning a new server and sending an invalid command,
  // the spec dictates that the server respond with something that starts with `[ERROR]`
  @Test
  void testInvalidCommandIsAnError() {
    assertTrue(server.handleCommand("foo").startsWith("[ERROR]"));
  }

  @Test
  void testCreateDatabase() {
    assertTrue(server.handleCommand("create database markbook;").startsWith("[OK]"));
  }

  @Test
  void testUseDatabase() {
    assertTrue(server.handleCommand("use markbook;").startsWith("[OK]"));
  }

  @Test
  void testCreateTable() {
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("CREATE TABLE marks (name, mark, pass);").startsWith("[OK]"));
  }

  @Test
  void testInertTable() {
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);").startsWith("[OK]"));
  }

  @Test
  void testJoinTable() {
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("JOIN coursework AND marks ON grade AND id;").equals("[OK]" + "\n" + "id\ttask\tname\tmark\tpass\t\n" +
            "1\tOXO\tBob\t35\tFALSE\t\n" +
            "2\tDB\tSteve\t65\tTRUE\t\n" +
            "3\tOXO\tClive\t20\tFALSE\t\n" +
            "4\tSTAG\tDave\t55\tTRUE\t"));
  }

  @Test
  void testUpdateTable() {
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("UPDATE marks SET mark = 38 WHERE name == 'Clive';").startsWith("[OK]"));
  }

  @Test
  void testSelectTable() {
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("SELECT * FROM marks;").equals("[OK]" + "\n" + "id\tname\tmark\tpass\n" +
            "1\tSteve\t65\tTRUE\n" +
            "2\tDave\t55\tTRUE\n" +
            "3\tBob\t35\tFALSE\n" +
            "4\tClive\t38\tFALSE"));
    assertTrue(server.handleCommand("SELECT * FROM marks WHERE name == 'Clive';").equals("[OK]" + "\n" + "id\tname\tmark\tpass\n" +
            "4\tClive\t38\tFALSE"));
    assertTrue(server.handleCommand("SELECT * FROM marks WHERE (pass == FALSE) AND (mark > 35);").equals("[OK]" + "\n" + "id\tname\tmark\tpass\n" +
            "4\tClive\t38\tFALSE"));
  }

  @Test
  void testDeleteTable() {
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("DELETE FROM marks WHERE name == 'Dave';").equals("[OK]"));
  }
  @Test
  void testAlterAddTable(){
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("ALTER TABLE marks ADD age;").equals("[OK]"));
}
    @Test
  void testAlterDropTable(){
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("ALTER TABLE marks DROP COLUMN mark;").startsWith("[OK]"));

  }

  @Test
  void reportError(){
    server.handleCommand("use markbook;");
    assertTrue(server.handleCommand("SELECT * FROM marks").equals("[ERROR]: Semi colon missing at end of line."));
    assertTrue(server.handleCommand("SELECT * FROM crew;").equals("[ERROR]: Table does not exist."));
    assertTrue(server.handleCommand("fdjslla;").equals("[ERROR]:Invalid query"));
    assertTrue(server.handleCommand("").equals("[ERROR]:command is null."));

  }
//  @Test
//  void testJoinTable(){
//    server.handleCommand("use markbook;");
//    //server.handleCommand("CREATE TABLE marks (name, mark, pass);");
//    assertTrue(server.handleCommand("JOIN coursework AND marks ON grade AND id;").startsWith("[OK]"));
//  }
  // Add more unit tests or integration tests here.
  // Unit tests would test individual methods or classes whereas integration tests are geared
  // towards a specific usecase (i.e. creating a table and inserting rows and asserting whether the
  // rows are actually inserted)

}
