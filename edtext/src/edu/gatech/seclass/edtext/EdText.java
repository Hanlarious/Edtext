package edu.gatech.seclass.edtext;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class EdText implements EdTextInterface{
    private String filePath;
    private String oldString;
    private String newString;
    private boolean replaceString;
    private boolean globalReplace;
    private boolean asciiConvert;
    private String prefix;
    private boolean addPrefix;
    private int duplicateFactor;
    private boolean duplicateLine;
    private int width;
    private boolean addLineNumber;
    private boolean inplaceEdit;
    private static String errorMessage = "Usage: edtext [ -f | -r old new | -g | -a | -p prefix | -d n | -n width ] FILE";


    @Override
    public void reset() {
        this.filePath = null;
        this.replaceString = false;
        this.oldString = null;
        this.newString = null;
        this.globalReplace = false;
        this.asciiConvert = false;
        this.prefix = null;
        this.addPrefix = false;
        this.duplicateFactor = 0;
        this.duplicateLine = false;
        this.width = 0;
        this.addLineNumber = false;
        this.inplaceEdit = false;


    }

    @Override
    public void setFilepath(String filepath) {
        this.filePath = filepath;

    }

    @Override
    public void setReplaceString(String oldString, String newString) {
        this.oldString = oldString;
        this.newString = newString;
        this.replaceString = true;
    }

    @Override
    public void setGlobalReplace(boolean globalReplace) {
        this.globalReplace = globalReplace;

    }

    @Override
    public void setAsciiConvert(boolean asciiConvert) {
        this.asciiConvert = asciiConvert;

    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.addPrefix = true;

    }

    @Override
    public void setDuplicateLines(boolean duplicateLine, int duplicateFactor) {
        if (duplicateLine == true){
            this.duplicateFactor = duplicateFactor;
        }
        this.duplicateLine = duplicateLine;
    }

    @Override
    public void setAddLineNumber(boolean addLineNumber, int width) {
        if (addLineNumber == true) {
            this.width = width;
        }
        this.addLineNumber = addLineNumber;
    }

    @Override
    public void setInplaceEdit(boolean inplaceEdit) {
        this.inplaceEdit = inplaceEdit;

    }

    @Override
    public void edtext() throws EdTextException {
        if (emptyFile() == true){
            if (!inplaceEdit){
                System.out.print("");
                return;
            } else {
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(filePath);
                } catch (Exception e) {
                    throw new EdTextException(errorMessage);
                }
                writer.write("");
                writer.close();
                writer.flush();
                return;
            }
        }
        if (validFlag() == false){
            throw new EdTextException(errorMessage);
        }
        String str = "";
        List<String> strs = new ArrayList<>();

        //List<String> tempList = new ArrayList<>();

        try {
            strs = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            //-r
            if (replaceString == true) {
                int num_lines = strs.size();
                List<String> tempList = new ArrayList<>();
                if (globalReplace == true) {
                    for (int i = 0; i < num_lines; i++) {
                        str = strs.get(i);
                        tempList.add(str.replaceAll(oldString, newString));
                        //String temp1 = String.join(System.lineSeparator(), tempList);
                        //strs.add(temp1);
                        //strs = tempList;
                    }
                } else {
                    for (int i = 0; i < num_lines; i++) {
                        str = strs.get(i);
                        tempList.add(str.replaceFirst(oldString, newString));
                        //String temp1 = String.join(System.lineSeparator(), tempList);
                        //strs.add(temp1);
                        //strs = tempList;
                    }
                }strs = tempList;
            }


            //-a
            if (asciiConvert == true) {
                int num_lines = strs.size();
                List<String> tempList = new ArrayList<>();
                for (int i = 0; i < num_lines; i++) {
                    String temp = "";
                    str = strs.get(i);
                    char[] chars = str.toCharArray();
                    for (int j = 0; j < chars.length; j++) {
                        int ascii = (int) chars[j];
                        if (ascii >= 32 && ascii <= 126) {
                            temp += ascii + " ";
                        } else {
                            temp += chars[j];
                        }
                    }
                    tempList.add(temp);
                    //String temp1 = String.join(System.lineSeparator(), tempList);
                    //strs.add(temp1);

                } strs = tempList;
            }
            //-p
            if (addPrefix == true && prefix != "") {
                int num_lines = strs.size();
                List<String> tempList = new ArrayList<>();
                for (int i = 0; i < num_lines; i++) {
                    //str = str + prefix;
                    str = strs.get(i);
                    tempList.add(prefix + str);
                    //String temp1 = String.join(System.lineSeparator(), tempList);
                    //strs.add(temp1);

                }strs = tempList;
            }
            //-d
            if (duplicateLine == true && (duplicateFactor >=1 && duplicateFactor <=10)){
                int num_lines = strs.size();
                List<String> tempList = new ArrayList<>();
                //ArrayList<String> temp2 = new ArrayList<String>();
                for (int i = 0; i < num_lines; i++) {
                    //List<String> temp = new ArrayList<String>();
                    str = strs.get(i);
                    for (int j =0; j <= duplicateFactor; j++){
                        tempList.add(str);
                        //String temp1 = String.join(System.lineSeparator(), tempList);
                        //strs.add(temp1);
                    }
                }strs = tempList;
            }
            //-n
            if (addLineNumber == true && (width >=1 && width <=5)){
                int num_lines = strs.size();
                List<String> tempList = new ArrayList<>();
                for (int i = 0; i < num_lines; i++) {
                    //str = strs.get(i);
                    if (width > 0) {
                        str = String.format("%0" + width + "d", i + 1);
                        tempList.add(str + " " + strs.get(i));
                        //String temp1 = String.join(System.lineSeparator(), tempList);
                        //strs.add(temp1);
                        //strs = tempList;
                    } else {
                        tempList.add(" " + strs.get(i));
                        //String temp1 = String.join(System.lineSeparator(), tempList);
                        //strs.add(temp1);
                        //strs = tempList;
                    }
                }strs = tempList;
            }


            String res = String.join(System.lineSeparator(), strs);
            if (!inplaceEdit){
                System.out.println(res);
            } else {
                PrintWriter writer = new PrintWriter(filePath);
                writer.write(res+System.lineSeparator());
                writer.close();
                writer.flush();
            }
            }catch(Exception e){
                throw new EdTextException(errorMessage);
            }

        }

    public boolean emptyFile() throws EdTextException {
        String fileContent;
        try {
            fileContent = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            if (fileContent.equals("")){
                return true;
            }else{
                return false;
            }
        } catch (Exception exception) {
            throw new EdTextException(errorMessage);
        }
    }

    public boolean validFlag(){
        // g withou r
        if (globalReplace == true && replaceString == false){
            return false;
        }
        // r with oldString == "" or newString == null
        else if (replaceString == true && (oldString == "" || newString == null)){
            return false;
        }
        // p with empty parameter
        else if (addPrefix == true && (prefix == "")){
            return false;
        }
        // d with empty, non-integer or out-of-boundary integer parameter
        //else if (duplicateLine  == true &&( duplicateFactor.isEmpty() || ))
        else if (duplicateLine == true && (duplicateFactor < 1 || duplicateFactor >10)){
            return false;
        }
        // n with out-of-boundary integer parameter
        else if (addLineNumber == true && (width < 1 || width >5)){
            return false;
        }

        return true;
    }

}
