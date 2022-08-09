package edu.uob.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinSqlHandler extends SqlHandler {
    public JoinSqlHandler(String command) throws IOException {
        super(command);

    }

    @Override
    public String handle() {
//JOIN coursework AND marks ON grade AND id;
        String pattern = "(JOIN|join)(.*)(AND|and)(.*)(ON|on)(.*)(AND|and)(.*)(;)";
        Pattern result = Pattern.compile(pattern);
        Matcher databaseNameTmp = result.matcher(command);
        String resultList = "";
        if (databaseNameTmp.find()) {
            String fileNameOne = databaseNameTmp.group(2).trim();
            String fileNameTwo = databaseNameTmp.group(4).trim();
            String tableHeadOne = databaseNameTmp.group(6).trim();
            String tableHeadTwo = databaseNameTmp.group(8).trim();
            ArrayList<String[]> tableContentOne = new ArrayList<>();
            ArrayList<String[]> tableContentTwo = new ArrayList<>();

            if(!checkTableExist(fileNameOne,path)){
                return "[ERROR]: Table "+fileNameOne+" does not exist.";
            }
            if(!checkTableExist(fileNameTwo,path)){
                return "[ERROR]: Table "+fileNameTwo+" does not exist.";
            }
            if(!checkTableHeadExist(fileNameOne,path,tableHeadOne)){
                return "[ERROR]: TableHead "+ tableHeadOne +" does not exist.";
            }
            if(!checkTableHeadExist(fileNameTwo,path,tableHeadTwo)){
                return "[ERROR]: TableHead "+ tableHeadTwo +" does not exist.";
            }


            HashMap<String, Integer> fileHeadIndexOne = readTableHeadToMemory(path, fileNameOne);
            HashMap<String, Integer> fileHeadIndexTwo = readTableHeadToMemory(path, fileNameTwo);
            int indexHead = fileHeadIndexOne.get(tableHeadOne);
            int indexTail = fileHeadIndexTwo.get(tableHeadTwo);
            try {
                tableContentOne = readTableContentToMemory(path, fileNameOne);
                tableContentTwo = readTableContentToMemory(path, fileNameTwo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(int a = 0; a < tableContentOne.get(0).length;a++){
                if(a == indexHead)
                    for (int b = 1; b < tableContentTwo.get(0).length; b++) {
                        resultList = resultList + tableContentTwo.get(0)[b]+"\t";
                    }
                else {resultList = resultList + tableContentOne.get(0)[a] +"\t";}
                if(a == tableContentOne.get(0).length-1){
                    resultList = resultList + "\n";
                }

            }

            for (int i = 1; i < tableContentOne.size(); i++) {
                for (int j = 0; j < tableContentOne.get(i).length; j++) {
                    if (j == indexHead) {
                        for (int n = 0; n < tableContentTwo.size(); n++) {
                        //for(String[] b:tableContentTwo){
                                if (tableContentTwo.get(n)[indexTail].equals(tableContentOne.get(i)[j])) {
                                    for (int m = 1; m < tableContentTwo.get(n).length; m++) {
                                        resultList = resultList + tableContentTwo.get(n)[m] + "\t";
                                    }

                                }

                        }
                    } else {
                        resultList = resultList + tableContentOne.get(i)[j] + "\t";

                    }
                    if(j == tableContentOne.get(i).length-1&&i!=tableContentOne.size()-1){
                        resultList = resultList + "\n";
                    }
                }
            }

        }
        else{return "[ERROR]:Invalid query";}
        return "[OK]"+"\n"+resultList;
    }
}
