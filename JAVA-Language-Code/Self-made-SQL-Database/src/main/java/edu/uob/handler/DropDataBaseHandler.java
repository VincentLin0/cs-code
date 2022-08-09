package edu.uob.handler;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropDataBaseHandler extends SqlHandler{
    public DropDataBaseHandler(String command) {
        super(command);

    }
    @Override
    public String handle() {
       // String command = "DROP DATABASE ;";
        String pattern = "(DROP DATABASE |drop database |DROP TABLE |drop table )(.*)(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher contentTmp= result.matcher(command);
        if (contentTmp.find()) {
            String tableName = contentTmp.group(2).trim();
            if(!checkTableExist(tableName,path)){
                return "[ERROR]: Table does not exist.";
            }
            File file = new File(path + File.separator + tableName);
            file.delete();
        }
        return "[OK]";
    }
}
