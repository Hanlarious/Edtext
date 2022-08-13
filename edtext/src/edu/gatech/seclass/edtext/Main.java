package edu.gatech.seclass.edtext;


import org.apache.commons.cli.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;



public class Main {

    // Empty Main class for compiling Individual Project.
    // During Deliverable 1 and Deliverable 2, DO NOT ALTER THIS CLASS or implement it

    private static CommandLine commend = null;
    private static ArrayList<String> strs;
    private static String fileContent;
    //private static String para;


    public static void main(String[] args) throws IOException {
        // Empty Skeleton Method
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        if(args.length==0){
            usage();
            return;
        }

        String fileName = args[args.length-1];
        File file = new File(fileName);


        options.addOption("f", false, "Edit file in place");
        Option r = Option.builder("r").numberOfArgs(2).hasArg(true).desc("string").build();
        r.setArgs(2);
        options.addOption(r);
        options.addOption("g", false, "Replace all occurrences");
        options.addOption("a", false, "Convert to ASCII");
        options.addOption("p", true, "Add prefix");
        options.addOption("d", true, "Duplicate lines");
        options.addOption("n", true, "Number lines");

        //parse
        try {
            commend = parser.parse(options, args);
        }
        catch (ParseException e){
            usage();
            return;
        }

        //get file content
        try{
            fileContent = Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);

        }
        //file not found
        catch(FileNotFoundException e){
            usage();
            return;
        }
        //invalid path
        catch(InvalidPathException e){
            usage();
            return;
        }
        catch(IOException e){
            usage();
            return;
        }

        ArrayList<String> arglist = new ArrayList<String>(commend.getArgList());
        String[] splitContents = fileContent.split(System.getProperty("line.separator"));
        strs = new ArrayList<String>(Arrays.asList(splitContents));
        int strLength = strs.size();

        if (!fileContent.contains(System.lineSeparator())){
            if (fileContent.length() == 0) {
                if (!commend.hasOption("f")) {
                    System.out.print("");
                    return;
                }
                else{
                    String str = "";
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(str);
                    writer.close();
                    writer.flush();
                    return;
                }
            }
            else{
                usage();
                return;
            }
        }


        //error situations
        //filecontents no lineseperator
        if ((commend.getOptions().length) == 0 && (arglist.size() > 1)) {
            usage();
            return;
        }

        if (arglist.size() == 0) {
            usage();
            return;
        }

        if (commend.hasOption("f")){
            //String temp;
            //temp = commend.getOptionValues("p")[commend.getOptionValues("p").length - 1];
            //if(!temp.startsWith("-")){
            //    usage();
            //   return;
            int i = ArrayUtils.indexOf(args,"-f")+1;
            if(!args[i].startsWith("-") && args[i] != fileName){
                usage();
                return;
            }

        }

        if (commend.hasOption("g")){
            int i = ArrayUtils.indexOf(args,"-g")+1;
            if(!args[i].startsWith("-") && args[i] != fileName){
                usage();
                return;
            }
        }

        if (commend.hasOption("a")){
            int i = ArrayUtils.indexOf(args,"-a")+1;
            if(!args[i].startsWith("-") && args[i] != fileName){
                usage();
                return;
            }
        }

        //file not exist
        if(!file.exists()){
            usage();
            return;
        }
        //file not readable
        if(!file.canRead()){
            usage();
            return;
        }
        //no args
        if (args.length < 1) {
            usage();
            return;
        }
        //no file
        if (args[args.length-1] == null) {
            usage();
            return;
        }
        //file not match
        if (args[args.length-1] != fileName) {
            usage();
            return;
        }

        if (args.length > 1 && args[args.length-1] == null) {
            usage();
            return;
        }


        //-g without -r
        if (commend.hasOption("g") && !commend.hasOption("r")){
            usage();
            return;
        }

        //run flags
        if (commend.hasOption("r")){

            String _old;
            String _new;
            _old = commend.getOptionValues("r")[commend.getOptionValues("r").length -2];
            //if (_old == "" || !fileContent.contains(_old)){
            if (_old == ""){
                usage();
                return;
            }
            _new = commend.getOptionValues("r")[commend.getOptionValues("r").length -1];
            if (_new == null){
                usage();
                return;
            }
            else{
                strs = option_r(_old, _new);
            }
        }

        if (commend.hasOption("a")){
            ArrayList<String>  temps = new ArrayList<String> ();
            for (String str : strs) {
                String temp = "";
                char[] chars = str.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    int ascii = (int) chars[i];
                    if (ascii >= 32 && ascii <= 126) {
                        temp += ascii + " ";
                    }else{
                        temp += chars[i];
                    }
                }
                temps.add(temp);
            }
            //strs = option_a();
            strs = temps;
        }

        if (commend.hasOption("p")) {
            //if (commend.getOptionValues("p").length != 1 ){
            String arg;
            arg = commend.getOptionValues("p")[commend.getOptionValues("p").length - 1];
            if (arg.isEmpty() || arg == null || arg == "" || arg == fileName){
                usage();
                return;
            }else{
                strs = option_p(arg);
            }

        }

        if (commend.hasOption("d")) {
            String arg;
            arg = commend.getOptionValues("d")[commend.getOptionValues("d").length - 1];
            if (arg.isEmpty() || arg == null || arg == "" || arg == fileName || !isInt(arg)){
                usage();
                return;
            }else{
                Integer arg_int;
                arg_int = Integer.parseInt(arg);
                if (arg_int>=1 && arg_int <=10){
                    strs = option_d(arg_int);
                } else{
                    usage();
                    return;
                    }
            }

        }

        if (commend.hasOption("n")) {
            String arg;
            arg = commend.getOptionValues("n")[commend.getOptionValues("n").length - 1];
            if (arg.isEmpty() || arg == null || arg == "" || arg == fileName || !isInt(arg)) {
                usage();
                return;
            }else{
                Integer arg_int;
                arg_int = Integer.parseInt(arg);
                if (arg_int>=1 && arg_int <=5){
                    strs = option_n(arg_int);
                }else {
                    usage();
                    return;
                }
            }
        }


        //get the output string
        String output = String.join(System.lineSeparator(), strs);
        if ((strs.size() <= strLength || output.length() == 0 && output.length() > 0)) {
            output += System.lineSeparator();
        }
        if (commend.hasOption("d")){
            output += System.lineSeparator();
        }

        if (commend.hasOption("f")){
            //OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName),StandardCharsets.UTF_8);
            //writer.write(output + System.lineSeparator());
            //writer.close();
            PrintWriter writer = new PrintWriter(file);
            writer.write(output);
            writer.close();
            writer.flush();
        }
        else{
            System.out.print(output);
        }


    }

    //define options with parameters
    private static ArrayList<String> option_r(String _old, String _new) {
        ArrayList<String> temp = new ArrayList<String>();
        if (!commend.hasOption("g")){
            for (String str : strs){
                temp.add(str.replaceFirst(_old,_new));

            }
        }
        else{
            for (String str : strs){
                temp.add(str.replaceAll(_old,_new));

            }
        }

        return temp;
    }

    private static ArrayList<String> option_p(String arg) {
        ArrayList<String> temp = new ArrayList<String>();
        for (String str : strs) {
            temp.add(arg + str);
        }
        return temp;
    }

    private static ArrayList<String> option_d(Integer arg) {
        ArrayList<String> temp = new ArrayList<String>();
        for (String str : strs) {
            //String _str = str + System.lineSeparator();
            //temp.add(_str.repeat(arg));
            //temp.add(System.lineSeparator());
            for (int i = 0; i <= arg; i++){
                temp.add(str);
                }
           // temp.add(System.lineSeparator());
        }
        return temp;
    }

    private static ArrayList<String> option_n(Integer arg) {
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < strs.size(); i++) {
            if (arg > 0) {
                String str = String.format("%0" + arg + "d", i + 1);
                temp.add(str + " " + strs.get(i));
            } else {
                temp.add(" " + strs.get(i));
            }
        }
        return temp;
    }

    private static boolean isInt(String str) {
        if ((str != null) && (str !="")){
            return str.matches("[0-9.]+");
        }
        else{
            return false;
        }
    }


    private static void usage() {
        System.err.println("Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE");
    }
}
