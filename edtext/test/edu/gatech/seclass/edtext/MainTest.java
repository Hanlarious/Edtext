package edu.gatech.seclass.edtext;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// DO NOT ALTER THIS CLASS. Use it as an example for MyMainTest.java

public class MainTest {

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();
    private final Charset charset = StandardCharsets.UTF_8;
    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    /*
     *  TEST UTILITIES
     */

    // Create File Utility
    private File createTmpFile() throws Exception {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Write File Utility
    private File createInputFile(String input) throws Exception {
        File file = createTmpFile();
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        fileWriter.write(input);
        fileWriter.close();
        return file;
    }

    private String getFileContent(String filename) {
        String content = null;
        try {
            content = Files.readString(Paths.get(filename), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /*
     *   TEST CASES
     */

    // Instructor Example 1 - test the -f flag
    @Test
    public void mainTest1() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-f", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 2 - test the -r flag
    @Test
    public void mainTest2() throws Exception {
        String input = "alphanumeric123foobar123" + System.lineSeparator();
        String expected = "alphanumeric456foobar123" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "123", "456", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 3 - test the -g flag with -r
    @Test
    public void mainTest3() throws Exception {
        String input = "alphanumeric123foobar123" + System.lineSeparator();
        String expected = "alphanumeric456foobar456" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-r", "123", "456", "-g", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 4 - test the -a flag
    @Test
    public void mainTest4() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "97 108 112 104 97 110 117 109 101 114 105 99 49 50 51 102 111 111 98 97 114 " + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-a", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 5 - test the -p flag
    @Test
    public void mainTest5() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "##alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-p", "##", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 6 - test the -d flag
    @Test
    public void mainTest6() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-d", "3", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 7 - test the -n flag
    @Test
    public void mainTest7() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator();

        String expected = "01 alphanumeric123foobar" + System.lineSeparator() +
                "02 alphanumeric123foobar" + System.lineSeparator() +
                "03 alphanumeric123foobar" + System.lineSeparator() +
                "04 alphanumeric123foobar" + System.lineSeparator() +
                "05 alphanumeric123foobar" + System.lineSeparator() +
                "06 alphanumeric123foobar" + System.lineSeparator() +
                "07 alphanumeric123foobar" + System.lineSeparator() +
                "08 alphanumeric123foobar" + System.lineSeparator() +
                "09 alphanumeric123foobar" + System.lineSeparator() +
                "10 alphanumeric123foobar" + System.lineSeparator() +
                "11 alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "2", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 8 - test a valid combination of all flags
    @Test
    public void mainTest8() throws Exception {
        String input = "foobar0Foobar1" + System.lineSeparator() +
                "foobar2foobar3" + System.lineSeparator() +
                "foobar4Foobar5" + System.lineSeparator() +
                "foobar6foobar7" + System.lineSeparator() +
                "foobar8Foobar9" + System.lineSeparator();

        String expected = "001 !!!70 79 79 98 97 114 48 70 111 111 98 97 114 49 " + System.lineSeparator() +
                "002 !!!70 79 79 98 97 114 48 70 111 111 98 97 114 49 " + System.lineSeparator() +
                "003 !!!70 79 79 98 97 114 50 70 79 79 98 97 114 51 " + System.lineSeparator() +
                "004 !!!70 79 79 98 97 114 50 70 79 79 98 97 114 51 " + System.lineSeparator() +
                "005 !!!70 79 79 98 97 114 52 70 111 111 98 97 114 53 " + System.lineSeparator() +
                "006 !!!70 79 79 98 97 114 52 70 111 111 98 97 114 53 " + System.lineSeparator() +
                "007 !!!70 79 79 98 97 114 54 70 79 79 98 97 114 55 " + System.lineSeparator() +
                "008 !!!70 79 79 98 97 114 54 70 79 79 98 97 114 55 " + System.lineSeparator() +
                "009 !!!70 79 79 98 97 114 56 70 111 111 98 97 114 57 " + System.lineSeparator() +
                "010 !!!70 79 79 98 97 114 56 70 111 111 98 97 114 57 " + System.lineSeparator();


        File inputFile = createInputFile(input);
        String[] args = {"-n", "3", "-r", "foo", "FOO", "-f", "-g", "-p", "!!!", "-d", "1", "-a", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 9 - test a valid combination of duplicate flags
    @Test
    public void mainTest9() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator();
        String expected = "01 ##alphanumeric123foobar" + System.lineSeparator() +
                "02 ##alphanumeric123foobar" + System.lineSeparator();

        File inputFile = createInputFile(input);
        String[] args = {"-n", "3", "-d", "5", "-d", "1", "-n", "2", "-p", "!!!", "-p", "##", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

    // Instructor Example 10 - test an invalid flag scenario
    @Test
    public void mainTest10() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).
        String[] args = new String[0];
        Main.main(args);
        assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
    }
}
