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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyMainTest {

    // Place all  of your tests in this class, optionally using MainTest.java as an example.



// DO NOT ALTER THIS CLASS. Use it as an example for MyMainTest.java


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

        // Frame #: 1. Empty file
        @Test
        public void edtextTest1() throws Exception {
            String input = "";
            String expected = "";

            File inputFile = createInputFile(input);
            String[] args = {"-f", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
    }


        // Frame #: 2. No file name
        @Test
        public void edtextTest2() throws Exception {
            String[] args = {"-f"};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }



        // Frame #: 3. No file found
        @Test
        public void edtextTest3() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-f", inputFile.getPath() + "123"};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 4. Not newline generated
        @Test
        public void edtextTest4() throws Exception {
            String input = "alphanumeric123foobar";

            File inputFile = createInputFile(input);
            String[] args = {"-f", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 5. with -f option
        @Test
        public void edtextTest5() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();
            String expected = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-f", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
        }

        // Frame #: 6. Repeated -f option
        @Test
        public void edtextTest6() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();
            String expected = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-f", "-f", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("File differs from expected", expected, getFileContent(inputFile.getPath()));
        }

        // Frame #: 7. -r <old><new> :  with -r option w empty old para w new para
        @Test
        public void edtextTest7() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "", "321", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 8. -r <old><new> :  with -r option w empty old para w/o new para
        @Test
        public void edtextTest8() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 9. -r <old><new> :  with -r option w/o either parameter
        @Test
        public void edtextTest9() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 10. -r <old><new> :  Repeated valid -r option
        @Test
        public void edtextTest10() throws Exception {
            String input = "alphanumeric123foobar123" + System.lineSeparator();
            String expected = "alphanumeric321foobar123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r","123","321","-r","123","321", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 11. -g :  -g & w/o -r options
        @Test
        public void edtextTest11() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 12. -g :  Repeated valid -g option
        @Test
        public void edtextTest12() throws Exception {
            String input = "alphanumeric123foobar123" + System.lineSeparator();
            String expected = "alphanumeric321foobar321" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-g", "-r","123","321", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 13. -g :  Repeated not valid -g option
        @Test
        public void edtextTest13() throws Exception {
            String input = "alphanumeric123foobar123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g","-g","-r","", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 14. -a :  Repeated -a option
        @Test
        public void edtextTest14() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();
            String expected = "97 108 112 104 97 110 117 109 101 114 105 99 49 50 51 102 111 111 98 97 114 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-a","-a", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 15. with -p option w/o parameter
        @Test
        public void edtextTest15() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-p", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 16. with -p option w empty parameter
        @Test
        public void edtextTest16() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-p","", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }


        // Frame #: 17. no -p option
        @Test
        public void edtextTest17() throws Exception {
            String input = "alphanumeric123foobar123" + System.lineSeparator();
            String expected = "alphanumeric321foobar321" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g","-r","123","321", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 18. Repeated valid -p option
        @Test
        public void edtextTest18() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();
            String expected = "####alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-p","####","-p","####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 19. with -d option w non-integer parameter
        @Test
        public void edtextTest19() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d","####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 20. with -d option w integer parameter <1
        @Test
        public void edtextTest20() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d","-1", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 21. with -d option w integer parameter >10
        @Test
        public void edtextTest21() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d","100", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 22. with -d option w/o parameter
        @Test
        public void edtextTest22() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }


        // Frame #: 23. -d <integer> :  Repeated valid -d option
        @Test
        public void edtextTest23() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();
            String expected = "alphanumeric123foobar" + System.lineSeparator()+
                                "alphanumeric123foobar" + System.lineSeparator() +
                                "alphanumeric123foobar" + System.lineSeparator() +
                                "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d","3","-d","3", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 24. -n <integer> :  with -n option w non-integer parameter
        @Test
        public void edtextTest24() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-n","####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 25. -n <integer> :  with -n option w integer parameter <1
        @Test
        public void edtextTest25() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-n","0", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 26. -n <integer> :  with -n option w integer parameter >5
        @Test
        public void edtextTest26() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-n","10", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 27. -n <integer> :  with -n option w/o parameter
        @Test
        public void edtextTest27() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-n", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // Frame #: 28. -n <integer> :  Repeated valid -n option
        @Test
        public void edtextTest28() throws Exception {
        String input = "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() +
                "alphanumeric123foobar" + System.lineSeparator() ;

        String expected = "001 alphanumeric123foobar" + System.lineSeparator() +
                "002 alphanumeric123foobar" + System.lineSeparator() +
                "003 alphanumeric123foobar" + System.lineSeparator() +
                "004 alphanumeric123foobar" + System.lineSeparator() ;

        File inputFile = createInputFile(input);
        String[] args = {"-n", "3", "-n", "3", inputFile.getPath()};
        Main.main(args);
        assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
        assertEquals("Output differs from expected", expected, outStream.toString());
        assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
    }

        // Frame #: 29. no -f
        @Test
        public void edtextTest29() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####97 108 " + System.lineSeparator() +
                    "0002 ####97 108 " + System.lineSeparator() +
                    "0003 ####97 108 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "", "-a", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 30. no -f no -n
        @Test
        public void edtextTest30() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####97 108 " + System.lineSeparator() +
                    "####97 108 " + System.lineSeparator() +
                    "####97 108 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "", "-a", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 31. no -f no -d
        @Test
        public void edtextTest31() throws Exception {
            String input = "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator();

            String expected = "0001 ####97 108 " + System.lineSeparator() +
                    "0002 ####97 108 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123","", "-a", "-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 32. grap
        @Test
        public void edtextTest32() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####97 108 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123","", "-a", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 33. grpdn
        @Test
        public void edtextTest33() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####al" + System.lineSeparator() +
                    "0002 ####al" + System.lineSeparator() +
                    "0003 ####al" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123","", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 34.grpd
        @Test
        public void edtextTest34() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####al" + System.lineSeparator() +
                    "####al" + System.lineSeparator() +
                    "####al" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 35. grpn
        @Test
        public void edtextTest35() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####al" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "", "-p", "####", "-n","4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 36. grp
        @Test
        public void edtextTest36() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####al" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 37. rapdn
        @Test
        public void edtextTest37() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####97 108 49 50 51 " + System.lineSeparator() +
                    "0002 ####97 108 49 50 51 " + System.lineSeparator() +
                    "0003 ####97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "", "-a", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 38. rapd
        @Test
        public void edtextTest38() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####97 108 49 50 51 " + System.lineSeparator() +
                    "####97 108 49 50 51 " + System.lineSeparator() +
                    "####97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "", "-a", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 39. rapn
        @Test
        public void edtextTest39() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "", "-a", "-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 40. rap
        @Test
        public void edtextTest40() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "", "-a", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 41. rpdn
        @Test
        public void edtextTest41() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####al123" + System.lineSeparator() +
                    "0002 ####al123" + System.lineSeparator() +
                    "0003 ####al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 42. rpd
        @Test
        public void edtextTest42() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####al123" + System.lineSeparator() +
                    "####al123" + System.lineSeparator() +
                    "####al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 43. rpn
        @Test
        public void edtextTest43() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "", "-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 44. rp
        @Test
        public void edtextTest44() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 45 no -f
        @Test
        public void edtextTest45() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####51 50 49 97 108 51 50 49 " + System.lineSeparator() +
                    "0002 ####51 50 49 97 108 51 50 49 " + System.lineSeparator() +
                    "0003 ####51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321", "-a", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 46. no -f no -n
        @Test
        public void edtextTest46() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####51 50 49 97 108 51 50 49 " + System.lineSeparator() +
                    "####51 50 49 97 108 51 50 49 " + System.lineSeparator() +
                    "####51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321", "-a", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 47. no -f no -d
        @Test
        public void edtextTest47() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321", "-a", "-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 48. rgap
        @Test
        public void edtextTest48() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321", "-a", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 49. rgpdn
        @Test
        public void edtextTest49() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####321al321" + System.lineSeparator() +
                    "0002 ####321al321" + System.lineSeparator() +
                    "0003 ####321al321" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 50. rgpd
        @Test
        public void edtextTest50() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####321al321" + System.lineSeparator() +
                    "####321al321" + System.lineSeparator() +
                    "####321al321" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 51. rgpn
        @Test
        public void edtextTest51() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####321al321" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321", "-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 52. rgp
        @Test
        public void edtextTest52() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####321al321" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 53. rapdn
        @Test
        public void edtextTest53() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####51 50 49 97 108 49 50 51 " + System.lineSeparator() +
                    "0002 ####51 50 49 97 108 49 50 51 " + System.lineSeparator() +
                    "0003 ####51 50 49 97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123","321", "-a", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 54. rapd
        @Test
        public void edtextTest54() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####51 50 49 97 108 49 50 51 " + System.lineSeparator() +
                    "####51 50 49 97 108 49 50 51 " + System.lineSeparator() +
                    "####51 50 49 97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123","321", "-a", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 55. rapn
        @Test
        public void edtextTest55() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####51 50 49 97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123","321", "-a", "-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 56. rap
        @Test
        public void edtextTest56() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####51 50 49 97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123","321", "-a", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 57. rpdn
        @Test
        public void edtextTest57() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####321al123" + System.lineSeparator() +
                    "0002 ####321al123" + System.lineSeparator() +
                    "0003 ####321al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123","321", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 58. rpd
        @Test
        public void edtextTest58() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####321al123" + System.lineSeparator() +
                    "####321al123" + System.lineSeparator() +
                    "####321al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123","321", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 59. rpn
        @Test
        public void edtextTest59() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####321al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123","321", "-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 60. rp
        @Test
        public void edtextTest60() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####321al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123","321", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 61. apdn
        @Test
        public void edtextTest61() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####49 50 51 97 108 49 50 51 " + System.lineSeparator() +
                    "0002 ####49 50 51 97 108 49 50 51 " + System.lineSeparator() +
                    "0003 ####49 50 51 97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-a", "-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 62. apd
        @Test
        public void edtextTest62() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####49 50 51 97 108 49 50 51 " + System.lineSeparator() +
                    "####49 50 51 97 108 49 50 51 " + System.lineSeparator() +
                    "####49 50 51 97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-a", "-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 63. apn
        @Test
        public void edtextTest63() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####49 50 51 97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-a", "-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 64. ap
        @Test
        public void edtextTest64() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####49 50 51 97 108 49 50 51 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-a", "-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 65. pdn
        @Test
        public void edtextTest65() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####123al123" + System.lineSeparator() +
                    "0002 ####123al123" + System.lineSeparator() +
                    "0003 ####123al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-p", "####", "-d","2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 66. pd
        @Test
        public void edtextTest66() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####123al123" + System.lineSeparator() +
                    "####123al123" + System.lineSeparator() +
                    "####123al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-p", "####", "-d","2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 67. pn
        @Test
        public void edtextTest67() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 ####123al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-p", "####", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 68. pn
        @Test
        public void edtextTest68() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "####123al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-p", "####", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Frame #: 69. no p no f
        @Test
        public void edtextTest69() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 51 50 49 97 108 51 50 49 " + System.lineSeparator()+
                    "0002 51 50 49 97 108 51 50 49 " + System.lineSeparator()+
                    "0003 51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321","-a", "-d", "2", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 70. no p no f no n
        @Test
        public void edtextTest70() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "51 50 49 97 108 51 50 49 " + System.lineSeparator()+
                    "51 50 49 97 108 51 50 49 " + System.lineSeparator()+
                    "51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321","-a", "-d", "2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


         // Frame #: 71. no p no f no d
        @Test
        public void edtextTest71() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "0001 51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321","-a", "-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 72. no p no f no d
        @Test
        public void edtextTest72() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r", "123", "321","-a", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


    // Frame #: 73. -g & valid -r
        @Test
        public void edtextTest73() throws Exception {
            String input = "alphanumeric123foobar123" + System.lineSeparator();
            String expected = "alphanumeric321foobar321" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-g", "-r","123","321", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Instructor Example 2 - test the -r flag
        @Test
        public void edtextTest74() throws Exception {
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
        public void edtextTest75() throws Exception {
            String input = "alphanumeric123foobar123" + System.lineSeparator();
            String expected = "alphanumeric456foobar456" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "123", "456", "-g", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Instructor Example 6 - test the -d flag
        @Test
        public void edtextTest76() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();
            String expected = "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d", "2", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Instructor Example 7 - test the -n flag
        @Test
        public void edtextTest77() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator() ;

            String expected = "0001 alphanumeric123foobar" + System.lineSeparator() +
                    "0002 alphanumeric123foobar" + System.lineSeparator() +
                    "0003 alphanumeric123foobar" + System.lineSeparator() +
                    "0004 alphanumeric123foobar" + System.lineSeparator() +
                    "0005 alphanumeric123foobar" + System.lineSeparator() ;

            File inputFile = createInputFile(input);
            String[] args = {"-n", "4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        // Instructor Example 10 - test an invalid flag scenario
        @Test
        public void edtextTest78() throws Exception {
            //no arguments on the command line will pass an array of length 0 to the application (not a null).
            String[] args = new String[0];
            Main.main(args);
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
        }

        // 79： unknown flag
        @Test
        public void edtextTest79() throws Exception {
            String input = "alphanumeric123foobar";

            File inputFile = createInputFile(input);
            String[] args = {"-b", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        // 80： file parameter not in the end
        @Test
        public void edtextTest80() throws Exception {
            String input = "alphanumeric123foobar";

            File inputFile = createInputFile(input);
            String[] args = {inputFile.getPath(), "-a"};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        //81: r old parameter could not be found in the input
        @Test
        public void edtextTest81() throws Exception {
            String input = "alphanumeric123foobar";

            File inputFile = createInputFile(input);
            String[] args = {"-r", "654","123", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }


        // -n with greater than width
        @Test
        public void edtextTest82() throws Exception {
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
                    "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator() +
                    "alphanumeric123foobar" + System.lineSeparator();

            String expected = "1 alphanumeric123foobar" + System.lineSeparator() +
                    "2 alphanumeric123foobar" + System.lineSeparator() +
                    "3 alphanumeric123foobar" + System.lineSeparator() +
                    "4 alphanumeric123foobar" + System.lineSeparator() +
                    "5 alphanumeric123foobar" + System.lineSeparator() +
                    "6 alphanumeric123foobar" + System.lineSeparator() +
                    "7 alphanumeric123foobar" + System.lineSeparator() +
                    "8 alphanumeric123foobar" + System.lineSeparator() +
                    "9 alphanumeric123foobar" + System.lineSeparator() +
                    "10 alphanumeric123foobar" + System.lineSeparator() +
                    "11 alphanumeric123foobar" + System.lineSeparator() +
                    "12 alphanumeric123foobar" + System.lineSeparator() +
                    "13 alphanumeric123foobar" + System.lineSeparator() +
                    "14 alphanumeric123foobar" + System.lineSeparator() +
                    "15 alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-n", "1", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        //83: wrong flag + no file name
        @Test
        public void edtextTest83() throws Exception {
            String[] args = {"-r"};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        //84: sequential -d
        @Test
        public void edtextTest84() throws Exception {
            String input = "123al123" + System.lineSeparator()+
                            "al123" + System.lineSeparator();

            String expected = "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "al123" + System.lineSeparator()+
                    "al123" + System.lineSeparator()+
                    "al123" + System.lineSeparator()+
                    "al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d","3", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        //85: 1 -d
        @Test
        public void edtextTest85() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d","1", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }
        //86: 10 -d
        @Test
        public void edtextTest86() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-d","10", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        //87: 1 sentence -n 5
        @Test
        public void edtextTest87() throws Exception {
            String input = "123al123" + System.lineSeparator();

            String expected = "00001 123al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-n","5", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        //88: 10 sentences -n 5
        @Test
        public void edtextTest88() throws Exception {
            String input = "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator()+
                    "123al123" + System.lineSeparator();

            String expected = "00001 123al123" + System.lineSeparator()+
                    "00002 123al123" + System.lineSeparator()+
                    "00003 123al123" + System.lineSeparator()+
                    "00004 123al123" + System.lineSeparator()+
                    "00005 123al123" + System.lineSeparator()+
                    "00006 123al123" + System.lineSeparator()+
                    "00007 123al123" + System.lineSeparator()+
                    "00008 123al123" + System.lineSeparator()+
                    "00009 123al123" + System.lineSeparator()+
                    "00010 123al123" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-n","5", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        //89: unicode out of range
        @Test
        public void edtextTest89() throws Exception {
            String input = "¢" + System.lineSeparator();
            String expected = "¢" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-a", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        //90: no new para
        @Test
        public void edtextTest90() throws Exception {
            String input = "alphanumeric123foobar"+ System.lineSeparator();

            File inputFile = createInputFile(input);

            String[] args = {"-r", "123",inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        //90: no new para
        @Test
        public void edtextTest91() throws Exception {
            String input = "alphanumeric123foobar"+ System.lineSeparator();

            File inputFile = createInputFile(input);

            String[] args = {"-f", "123",inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }

        //90: no new para
        @Test
        public void edtextTest92() throws Exception {
            String input = "al"+ System.lineSeparator();
            String expected = "al" + System.lineSeparator();

            File inputFile = createInputFile(input);

            String[] args = {"-r", "AL","123",inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        //91: no f no p
        @Test
        public void edtextTest93() throws Exception {
            String input = "123al123"+ System.lineSeparator();
            String expected = "0001 51 50 49 97 108 51 50 49 " + System.lineSeparator()+
                    "0002 51 50 49 97 108 51 50 49 " + System.lineSeparator()+
                    "0003 51 50 49 97 108 51 50 49 " + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r","123","321","-g","-a", "-d","2","-n","4", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }

        // Frame #: 7. -r <old><new> :  with -r option w empty old para w new para
        @Test
        public void edtextTest94() throws Exception {
            String input = "alphanumeric123foobar" + System.lineSeparator();
            String expected = "alphanumeric123foobar" + System.lineSeparator();

            File inputFile = createInputFile(input);
            String[] args = {"-r", "456", "321", inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stderr output", errStream.toString().isEmpty());
            assertEquals("Output differs from expected", expected, outStream.toString());
            assertEquals("input file modified", input, getFileContent(inputFile.getPath()));
        }


        //95: empty args
        @Test
        public void edtextTest95() throws Exception {
            String[] args = {""};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }


        @Test
        public void edtextTest96() throws Exception {
            String input = "alphanumeric123foobar"+ System.lineSeparator();

            File inputFile = createInputFile(input);

            String[] args = {"-r", "123","456","-f","-g","-p","####","-d","2","-n","4","-l",inputFile.getPath()};
            Main.main(args);
            assertTrue("Unexpected stdout output", outStream.toString().isEmpty());
            assertEquals("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE", errStream.toString().trim());
        }







}


