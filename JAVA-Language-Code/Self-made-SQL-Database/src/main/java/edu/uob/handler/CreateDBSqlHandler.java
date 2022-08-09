package edu.uob.handler;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateDBSqlHandler extends SqlHandler{
    private String databaseDictionary;
    public CreateDBSqlHandler(String command,String databaseDictionary) throws IOException {
        super(command);
        this.databaseDictionary = databaseDictionary;

            //System.out.println("111`");

    }
    @Override
    public String handle() {
        path = databaseDictionary;
        String pattern = "(CREATE DATABASE|create database|create DATABASE|CREATE database)(.*)(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher databaseNameTmp = result.matcher(command);
        if (databaseNameTmp.find()) {
            String databaseName= databaseNameTmp.group(2).trim();
            //System.out.println(databaseName);
            String name = path + File.separator + databaseName;
            File fileToOpen = new File(name);
            if(fileToOpen.exists()&&fileToOpen.isDirectory()){
                return "[ERROR]: Database Already exist.";
            }
            File emailFolder = new File(databaseName);
            emailFolder.mkdir();

        }
        else{
            return "[ERROR]:Invalid query";
        }
        return "[OK]";
    }
}
