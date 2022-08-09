package edu.uob.handler;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class INSERTSqlHandler extends SqlHandler{
    public INSERTSqlHandler(String command) throws IOException {
        super(command);

    }

    public String handle() {
        //INSERT INTO marks VALUES ('Steve', 65, TRUE);
        String pattern = "(INSERT INTO|insert into)(.*)(VALUES |values )(\\()(.*)(\\))(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher databaseNameTmp = result.matcher(command);
        if (databaseNameTmp.find()) {
            String fileName= databaseNameTmp.group(2).trim();
//            if(!checkTableExist(fileName,path)){
//                return "[ERROR]: Table"+fileName+" does not exist.";
//            }
            String name = path + File.separator + fileName;
            File fileToOpen = new File(name);

            if(!checkTableExist(fileName,path)){
                return "[ERROR]: Table does not exist.";
            }

            FileWriter writer = null;
            try {
                writer = new FileWriter(fileToOpen,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] splitStr = databaseNameTmp.group(5).split(",");
            if(splitStr[0].trim().substring(0,1).equals("'")){
                splitStr[0] = splitStr[0].substring(1,splitStr[0].length()-1);
            }

            int id = 0;
            ArrayList<String[]> tableContent = new ArrayList<>();
            try {
                tableContent = readTableContentToMemory(path,fileName);
                if(tableContent.get(tableContent.size()-1)[0].equals("id")){
                    id =0;
                }
                else {
                    id = Integer.parseInt(tableContent.get(tableContent.size()-1)[0]);
                }
                id =id+1;

            } catch (IOException e) {
                e.printStackTrace();
            }
            String strId = Integer.toString(id);
            try {
                writer.write("\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < splitStr.length; i++) {
                if (i == splitStr.length-1){
                    try {
                        writer.write(splitStr[i].trim());
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(i ==0){
                    try {
                        writer.write(strId);
                        writer.write("\t");
                        writer.write(splitStr[i].trim());
                        writer.write("\t");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        writer.write(splitStr[i].trim());
                        writer.write("\t");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(databaseName);

            // path = databaseDictionary+ File.separator+databaseName;

        }
        else{return "[ERROR]:Invalid query";}
        return "[OK]";
    }
}

