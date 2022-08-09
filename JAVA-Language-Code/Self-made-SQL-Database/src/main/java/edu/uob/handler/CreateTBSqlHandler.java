package edu.uob.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTBSqlHandler extends SqlHandler {
    public CreateTBSqlHandler(String command)throws IOException {
        super(command);

    }

    @Override
    public String handle() {
        //CREATE TABLE marks (name, mark, pass);
        String pattern = "(CREATE TABLE|create table)(.*)(\\()(.*)(\\))(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher databaseNameTmp = result.matcher(command);

        if (databaseNameTmp.find()) {
            String fileName = databaseNameTmp.group(2).trim();
            String name = path + File.separator + fileName;
            File fileToOpen = new File(name);
            if(fileToOpen.exists()){
                return "[ERROR]:Table already exist.";
            }
            try {
                    fileToOpen.createNewFile();
                    FileWriter writer = new FileWriter(fileToOpen);
                    String[] splitStr = databaseNameTmp.group(4).split(",");
                    for (int i = 0; i < splitStr.length; i++) {
                        if (i == splitStr.length - 1) {
                            writer.write(splitStr[i].trim());
                            writer.flush();
                        } else if (i == 0) {
                            writer.write("id");
                            writer.write("\t");
                            writer.write(splitStr[i].trim());
                            writer.write("\t");
                            writer.flush();
                        } else {
                            writer.write(splitStr[i].trim());
                            writer.write("\t");
                            writer.flush();
                        }
                    }
                    //writer.write("\n");
                    //writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
         else {
            return "[ERROR]:Invalid query";
        }
        return "[OK]";
    }
}
