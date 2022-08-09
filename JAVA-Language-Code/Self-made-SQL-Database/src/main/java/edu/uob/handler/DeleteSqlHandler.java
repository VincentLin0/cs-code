package edu.uob.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteSqlHandler extends SqlHandler{
    public DeleteSqlHandler(String command) {
        super(command);

    }
    @Override
    public String handle() {
        //
        //DELETE FROM marks WHERE name == 'Dave';
        String pattern = "(DELETE FROM |delete from )(.*)( WHERE | where )(.*)( !=|==|>|<|LIKE)(.*)(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher contentTmp= result.matcher(command);
        if (contentTmp.find()) {
            String fileNane = contentTmp.group(2).trim();
            String conditionHead = contentTmp.group(4).trim();
            String conditionSymbol = contentTmp.group(5).trim();
            String conditionTail = contentTmp.group(6).trim();
            if (conditionTail.substring(0, 1).equals("'")) {
                conditionTail = conditionTail.substring(1, conditionTail.length() - 1);
            }

            if(!checkTableExist(fileNane,path)){
                return "[ERROR]: Table does not exist.";
            }
            if(!checkTableHeadExist(fileNane,path,conditionHead)){
                return "[ERROR]: TableHead "+ conditionHead +" does not exist.";
            }
            ArrayList<String[]> tableContent = new ArrayList<>();
            ArrayList<String[]> tableContentRemove = new ArrayList<>();
            try {
                tableContent = readTableContentToMemory(path,fileNane);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<String, Integer> fileHeadIndex = readTableHeadToMemory(path, fileNane);
            int column = fileHeadIndex.get(conditionHead);
            int i = 0;
            //ArrayList<String[]> newTableContent = new ArrayList<>();
            if(conditionSymbol.equals("!=")) {
                for (i = tableContent.size()-1; i >=0; i--) {
                    if(!tableContent.get(i)[column].equals(conditionTail)){
                        tableContentRemove.add(tableContent.get(i));
                    }
                }
                tableContent.removeAll(tableContentRemove);
            }
            else if(conditionSymbol.equals("==")){
                for (i = tableContent.size()-1; i >=0; i--) {
                    if(tableContent.get(i)[column].equals(conditionTail)){
                        tableContentRemove.add(tableContent.get(i));
                    }
                }
                tableContent.removeAll(tableContentRemove);
            }
            else if(conditionSymbol.equals(">")){
                for (i = tableContent.size()-1; i >=1; i--) {
                    if(Integer.parseInt(tableContent.get(i)[column]) > Integer.parseInt(conditionTail)){
                        tableContentRemove.add(tableContent.get(i));
                    }
                }
                tableContent.removeAll(tableContentRemove);
            }
            else if(conditionSymbol.equals(">=")){
                for (i = tableContent.size()-1; i >=1; i--) {
                    if(Integer.parseInt(tableContent.get(i)[column]) >= Integer.parseInt(conditionTail)){
                        tableContentRemove.add(tableContent.get(i));
                    }
                }
                tableContent.removeAll(tableContentRemove);
            }
            else if(conditionSymbol.equals("<")){
                for (i = tableContent.size()-1; i >=1; i--) {
                    if(Integer.parseInt(tableContent.get(i)[column]) < Integer.parseInt(conditionTail)){
                        tableContentRemove.add(tableContent.get(i));
                    }
                }
                tableContent.removeAll(tableContentRemove);
            }
            else if(conditionSymbol.equals("<=")){
                for (i = tableContent.size()-1; i >=1; i--) {
                    if(Integer.parseInt(tableContent.get(i)[column]) <= Integer.parseInt(conditionTail)){
                        tableContentRemove.add(tableContent.get(i));
                    }
                }
                tableContent.removeAll(tableContentRemove);
            }

            else if(conditionSymbol.equals("LIKE")){
                for (i = tableContent.size()-1; i >=0; i--) {
                    if(tableContent.get(i)[column].substring(tableContent.get(i)[column].length() - 2, tableContent.get(i)[column].length()).equals(conditionTail)){
                        tableContentRemove.add(tableContent.get(i));
                    }
                }
                tableContent.removeAll(tableContentRemove);
            }
            try {
                writeDataToFile(path,tableContent,fileNane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{return "[ERROR]:Invalid query";}
        return "[OK]";
    }
}
