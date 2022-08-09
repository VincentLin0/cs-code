package edu.uob.handler;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UseSqlHandler extends SqlHandler{
    private String databaseDictionary;

    public UseSqlHandler(String command, String databaseDictionary) {
        super(command);
        this.databaseDictionary = databaseDictionary;
        //path = databaseDictionary+ File.separator+databaseName;
    }

    @Override
    public String handle() {
        String pattern = "(USE|use)(.*)(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher databaseNameTmp = result.matcher(command);
        if (databaseNameTmp.find()) {
            String databaseName= databaseNameTmp.group(2).trim();
            //System.out.println(databaseName);
            System.out.println("datadirectory = "+ databaseDictionary);
            path = databaseDictionary;
            if(!checkTableExist(databaseName,path)){
                return "[ERROR]: Database does not exist.";
            }

            System.out.println(path);
            path = databaseDictionary+ File.separator+databaseName;
            System.out.println(path);
        }
        else{
            return "[ERROR]:Invalid query";
        }
        return "[OK]";
    }
}
