package edu.uob.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateSqlHandler extends SqlHandler {
    public UpdateSqlHandler(String sql) {
        super(sql);

    }

    @Override
    public String handle() {
//UPDATE marks SET mark = 38 WHERE name == 'Clive';
        String pattern = "(UPDATE|update)(.*)(SET|set)(.*)( = )(.*)(WHERE|where)(.*)(==|!=|>=|<=|>|<|LIKE)(.*)(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher contentTmp = result.matcher(command);
        if (contentTmp.find()) {
            String fileNane =  contentTmp.group(2).trim();
            String setTableHead = contentTmp.group(4).trim();
            String setTableTail = contentTmp.group(6).trim();
            String whereTableHead = contentTmp.group(8).trim();
            String whereTableSymbol = contentTmp.group(9).trim();
            String whereTableTail = contentTmp.group(10).trim();
            if (whereTableTail.substring(0, 1).equals("'")) {
                whereTableTail = whereTableTail.substring(1, whereTableTail.length() - 1);
            }
            if(!checkTableExist(fileNane,path)){
                return "[ERROR]: Table does not exist.";
            }
            if(!checkTableHeadExist(fileNane,path,setTableHead)){
                return "[ERROR]: TableHead "+ setTableHead +" does not exist.";
            }
            if(!checkTableHeadExist(fileNane,path,whereTableHead)){
                return "[ERROR]: TableHead "+ whereTableHead +" does not exist.";
            }
            ArrayList<String[]> tableContent = new ArrayList<>();
            HashMap<String, Integer> fileHeadIndex = readTableHeadToMemory(path, fileNane);
            int setColumn = fileHeadIndex.get(setTableHead);
            int whereColumn = fileHeadIndex.get(whereTableHead);
            try {
                tableContent = readTableContentToMemory(path, fileNane);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int num = whereTableTail.length();
            if(whereTableSymbol.equals("=="))
            for (int i = 0; i < tableContent.size(); i++) {
                if(tableContent.get(i)[whereColumn].equals(whereTableTail)){
                    tableContent.get(i)[setColumn] = setTableTail;
                }
            }
            else if(whereTableSymbol.equals("!="))
                for (int i = 0; i < tableContent.size(); i++) {
                    if(!tableContent.get(i)[whereColumn].equals(whereTableTail)){
                        tableContent.get(i)[setColumn] = setTableTail;
                    }
                }
            else if(whereTableSymbol.equals(">"))
                for (int i = 1; i < tableContent.size(); i++) {
                    if(Integer.parseInt(tableContent.get(i)[whereColumn])> Integer.parseInt(whereTableTail)){
                        tableContent.get(i)[setColumn] = setTableTail;
                    }
                }
            else if(whereTableSymbol.equals(">="))
                for (int i = 1; i < tableContent.size(); i++) {
                    if(Integer.parseInt(tableContent.get(i)[whereColumn])> Integer.parseInt(whereTableTail)||Integer.parseInt(tableContent.get(i)[whereColumn])== Integer.parseInt(whereTableTail)){
                        tableContent.get(i)[setColumn] = setTableTail;
                    }
                }
            else if(whereTableSymbol.equals("<"))
                for (int i = 1; i < tableContent.size(); i++) {
                    if(Integer.parseInt(tableContent.get(i)[whereColumn])< Integer.parseInt(whereTableTail)){
                        tableContent.get(i)[setColumn] = setTableTail;
                    }
                }
            else if(whereTableSymbol.equals("<="))
                for (int i = 1; i < tableContent.size(); i++) {
                    if((Integer.parseInt(tableContent.get(i)[whereColumn])< Integer.parseInt(whereTableTail))||(Integer.parseInt(tableContent.get(i)[whereColumn]) == Integer.parseInt(whereTableTail))){
                        tableContent.get(i)[setColumn] = setTableTail;
                    }
                }
            else if(whereTableSymbol.equals("LIKE"))

                for (int i = 0; i < tableContent.size(); i++) {
                    if(tableContent.get(i)[whereColumn].substring(tableContent.get(i)[whereColumn].length() - num, tableContent.get(i)[whereColumn].length()).equals(whereTableTail)){
                        tableContent.get(i)[setColumn] = setTableTail;
                    }
                }
            try {
                writeDataToFile(path,tableContent,fileNane);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else{
        return "[ERROR]:Invalid query";
        }
        return "[OK]";
    }
}
