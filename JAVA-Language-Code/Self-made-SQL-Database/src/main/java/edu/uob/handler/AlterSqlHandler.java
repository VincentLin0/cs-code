package edu.uob.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlterSqlHandler extends SqlHandler{
    public AlterSqlHandler(String command) {
        super(command);
    }
    @Override
    public String handle() {
        //ALTER TABLE marks ADD age;
        String pattern = "(ALTER TABLE|alter table)(.*)(ADD|add)(.*)(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher contentTmp= result.matcher(command);
        if (contentTmp.find()) {
            String tableName = contentTmp.group(2).trim();
            String columnStr = contentTmp.group(4).trim();
            String column = columnStr.replaceAll("`", "");
            if(!checkTableExist(tableName,path)){
                return "[ERROR]: Table does not exist.";
            }
            ArrayList<String[]> tableContent = new ArrayList<>();
            try {
                tableContent = readTableContentToMemory(path, tableName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<ArrayList<String>> tableContentNew = new ArrayList<>();
            for(String[] a:tableContent){
                List<String> list= Arrays.asList(a);

                ArrayList<String> aList=new ArrayList<String>(list);
                tableContentNew.add(aList);
            }
            //int len = tableContent.get(0).length;
            tableContentNew.get(0).add(column);
            try {
                writeDataToFileArray(path,tableContentNew,tableName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "[OK]";
        }
        //ALTER TABLE marks DROP COLUMN name;
        String patternDrop = "(ALTER TABLE|alter table)(.*)(DROP|drop)(.*)(COLUMN|column)(.*)(;)";
        Pattern resultDrop = Pattern.compile(patternDrop);
        Matcher contentTmpDrop= resultDrop.matcher(command);
        if (contentTmpDrop.find()) {
            String tableNameDrop = contentTmpDrop.group(2).trim();
            String columnStrDrop = contentTmpDrop.group(6).trim();
            String columnDrop = columnStrDrop.replaceAll("`", "");
            if(!checkTableExist(tableNameDrop,path)){
                return "[ERROR]: Table does not exist.";
            }
            if(!columnDrop.equals("*")&&!checkTableHeadExist(tableNameDrop,path,columnDrop)){
                return "[ERROR]: TableHead "+ columnDrop +" does not exist.";
            }
            ArrayList<String[]> tableContentDrop = new ArrayList<>();
            ArrayList<String[]> tableContentDropNew = new ArrayList<>();
            try {
                tableContentDrop = readTableContentToMemory(path, tableNameDrop);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<String, Integer> fileHeadIndexAlter = readTableHeadToMemory(path, tableNameDrop);
            int m = fileHeadIndexAlter.get(columnDrop);
            for(String[] i:tableContentDrop){
                String[] temp = new String[tableContentDrop.get(0).length-1];
                for(int j = 0,k=0;j<i.length;j++){
                    if(j!= m){
                        temp[k] = i[j];
                        k++;
                    }
                }
                tableContentDropNew.add(temp);
            }
            try {
                writeDataToFile(path,tableContentDropNew,tableNameDrop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "[OK]";
    }
}
