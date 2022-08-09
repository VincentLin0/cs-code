package edu.uob.handler;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectSqlHandler extends SqlHandler {
    public SelectSqlHandler(String command) {
        super(command);

    }


    @Override
    public String handle() {
        //SELECT * FROM marks;
        //SELECT * FROM marks WHERE name != 'Dave';
        String resultList = "";
        //SELECT * FROM marks WHERE (pass == FALSE) AND (mark > 35);
        String patternWhereAnd = "(SELECT|select)(.*)(FROM|from)(.*)(WHERE |where )(\\()(.*)(>|<|!=|==|LIKE|like)(.*)(\\))( AND | OR | and | or )(\\()(.*)(>|<|!=|==|LIKE|like)(.*)(\\))(;)";
        Pattern resultWhereAnd = Pattern.compile(patternWhereAnd);
        Matcher contentTmpWhereAnd= resultWhereAnd.matcher(command);
        if (contentTmpWhereAnd.find()) {
            String contentWhereAnd = contentTmpWhereAnd.group(2).trim();//*
            String fileNaneWhereAnd = contentTmpWhereAnd.group(4).trim();//marks
            String conditionWhereAndHead1 = contentTmpWhereAnd.group(7).trim();//pass
            String conditionWhereAndSymbol1= contentTmpWhereAnd.group(8).trim();//==
            String conditionWhereAndTail1 = contentTmpWhereAnd.group(9).trim();//FALSE
            String conditionWhereAndRelation = contentTmpWhereAnd.group(11).trim();//And
            String conditionWhereAndHead2 = contentTmpWhereAnd.group(13).trim();//mark
            String conditionWhereAndSymbol2 = contentTmpWhereAnd.group(14).trim();//>
            String conditionWhereAndTail2 = contentTmpWhereAnd.group(15).trim();//35
                if (conditionWhereAndTail1.substring(0, 1).equals("'")) {
                    conditionWhereAndTail1 = conditionWhereAndTail1.substring(1, conditionWhereAndTail1.length() - 1);
                }
                if (conditionWhereAndTail2.substring(0, 1).equals("'")) {
                    conditionWhereAndTail2 = conditionWhereAndTail2.substring(1, conditionWhereAndTail2.length() - 1);
                }
                    ArrayList<String[]> tableContentWhereAnd = new ArrayList<>();
                    HashMap<String, Integer> fileHeadIndexWhere = readTableHeadToMemory(path, fileNaneWhereAnd);
                    int columnAnd1 = fileHeadIndexWhere.get(conditionWhereAndHead1);
                    int columnAnd2 = fileHeadIndexWhere.get(conditionWhereAndHead2);
                    int columnAnd3 = 0;
                    if(!contentWhereAnd.equals("*")){
                    columnAnd3 = fileHeadIndexWhere.get(contentWhereAnd);
                    }
                    try {
                        tableContentWhereAnd = readTableContentToMemory(path, fileNaneWhereAnd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    HashSet<String[]> conditionOne = new HashSet<String[]>();
                    HashSet<String[]> conditionTwo = new HashSet<String[]>();
                    HashSet<String[]> conditionAnd = new HashSet<String[]>();
                    searchWhereAnd(conditionWhereAndSymbol1,tableContentWhereAnd, columnAnd1,conditionWhereAndTail1,conditionOne);
                    searchWhereAnd(conditionWhereAndSymbol2,tableContentWhereAnd, columnAnd2,conditionWhereAndTail2,conditionTwo);

                    if(conditionWhereAndRelation.equals("AND")||conditionWhereAndRelation.equals("and")) {
                        conditionAnd.addAll(conditionOne);
                        conditionAnd.retainAll(conditionTwo);
                        }

                    else if(conditionWhereAndRelation.equals("OR")||conditionWhereAndRelation.equals("or")){
                        conditionAnd.addAll(conditionOne);
                        conditionAnd.addAll(conditionTwo);
                    }
                    if(contentWhereAnd.equals("*")) {
                        for (int n = 0; n < tableContentWhereAnd.get(0).length; n++) {
                            if(n != tableContentWhereAnd.get(0).length-1){
                            resultList = resultList + tableContentWhereAnd.get(0)[n] + "\t";}
                            if (n==tableContentWhereAnd.get(0).length-1){
                                resultList = resultList + tableContentWhereAnd.get(0)[n];
                            }
                            if (n== tableContentWhereAnd.get(0).length-1&&conditionAnd.size() != 0) {
                                resultList = resultList + "\n";
                            }
                        }

                        for(String[] i:conditionAnd){
                            int m =conditionAnd.size();
                            for(int j = 0;j<i.length;j++){
                                if(j!=i.length-1) {
                                    resultList = resultList + i[j] + "\t";
                                }
                                if(j == i.length -1){
                                    resultList = resultList + i[j];
                                }
                                if(j==i.length-1&&m!=1){
                                    resultList= resultList+ "\n";
                                }
                            }
                            m--;
                        }
                    }
                    else if(!contentWhereAnd.equals("*")){
                        resultList = resultList + tableContentWhereAnd.get(0)[fileHeadIndexWhere.get(contentWhereAnd)] + "\n";
                        int m =conditionAnd.size();
                        for(String[] i :conditionAnd){
                            resultList = resultList + i[columnAnd3];
                            m--;
                            if(m!=0){
                                resultList = resultList +"\n";
                            }
                        }

            }
            return "[OK]"+"\n"+resultList;
        }




//SELECT * FROM marks WHERE name != 'Dave';
        String patternWhere = "(SELECT|select)(.*)(FROM|from)(.*)(WHERE|where)(.*)(;)";
        Pattern resultWhere = Pattern.compile(patternWhere);
        Matcher contentTmpWhere = resultWhere.matcher(command);
        if (contentTmpWhere.find()) {
            String contentWhere = contentTmpWhere.group(2).trim();
            String fileNaneWhere = contentTmpWhere.group(4).trim();
            String conditionWhere = contentTmpWhere.group(6).trim();
            //String[] conditionWhereList = conditionWhere.split(" ");
            String patternCondition = "(.*)(==|!=|>|<|>=|<=|LIKE|like)(.*)";
            Pattern resultCondition = Pattern.compile(patternCondition);
            Matcher contentTmpCondition = resultCondition.matcher(conditionWhere);
            if (contentTmpCondition.find()) {
                String conditionHead = contentTmpCondition.group(1).trim();//name
                String conditionCompare = contentTmpCondition.group(2).trim();//!=
                String conditionTail = contentTmpCondition.group(3).trim();//'Dave'
                if (conditionTail.substring(0, 1).equals("'")) {
                    conditionTail = conditionTail.substring(1, conditionTail.length() - 1);
                }
                if(!checkTableExist(fileNaneWhere,path)){
                    return "[ERROR]: Table does not exist.";
                }
                if(!contentWhere.equals("*")&&!checkTableHeadExist(fileNaneWhere,path,contentWhere)){
                    return "[ERROR]: TableHead "+ contentWhere +" does not exist.";
                }
                if(!conditionHead.equals("*")&&!checkTableHeadExist(fileNaneWhere,path,conditionHead)){
                    return "[ERROR]: TableHead "+ conditionHead +" does not exist.";
                }


                ArrayList<String[]> tableContentWhere = new ArrayList<>();
                HashMap<String, Integer> fileHeadIndexWhere = readTableHeadToMemory(path, fileNaneWhere);
                int column = fileHeadIndexWhere.get(conditionHead);
                try {
                    tableContentWhere = readTableContentToMemory(path, fileNaneWhere);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //String resultList = "";
                //=
                if (contentWhere.equals("*") && (conditionCompare.equals("=="))) {
                    for (int n = 0; n < tableContentWhere.get(0).length; n++) {
                        if(n!=tableContentWhere.get(0).length-1){
                        resultList = resultList + tableContentWhere.get(0)[n] + "\t";}
                        if(n==tableContentWhere.get(0).length-1){
                            resultList = resultList + tableContentWhere.get(0)[n];
                        }
                        if ((n == tableContentWhere.get(0).length - 1)){
                            resultList = resultList + "\n";
                        }
                    }
                    for (int i = 1; i < tableContentWhere.size(); i++) {
                        for (int j = 0; j < tableContentWhere.get(i).length; j++) {
                            if (tableContentWhere.get(i)[column].equals(conditionTail)) {
                                if (j == tableContentWhere.get(i).length - 1) {
                                    resultList = resultList + tableContentWhere.get(i)[j];
                                } else {
                                    resultList = resultList + tableContentWhere.get(i)[j] + "\t";
                                }
                            }
                            else{break;}
                        }
                        if (i + 1 < tableContentWhere.size() && tableContentWhere.get(i + 1)[column].equals(conditionTail)) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                //!=

                else if (contentWhere.equals("*") && (conditionCompare.equals("!="))) {
                    //int column = fileHeadIndexWhere.get(conditionHead);
                    for (int i = 0; i < tableContentWhere.size(); i++) {
                        for (int j = 0; j < tableContentWhere.get(i).length; j++) {
                            if (!tableContentWhere.get(i)[column].equals(conditionTail)) {
                                if (j == tableContentWhere.get(i).length - 1) {
                                    resultList = resultList + tableContentWhere.get(i)[j];
                                }
                                if(j!=tableContentWhere.get(i).length-1){
                                    resultList = resultList + tableContentWhere.get(i)[j] + "\t";
                                }
                            } else {
                                continue;
                            }
                        }
                        if (i + 1 < tableContentWhere.size() && !tableContentWhere.get(i + 1)[column].equals(conditionTail)) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (contentWhere.equals("*") && (conditionCompare.equals(">"))) {
                    for (int n = 0; n < tableContentWhere.get(0).length; n++) {
                        if(n!=tableContentWhere.get(0).length-1){
                        resultList = resultList + tableContentWhere.get(0)[n] + "\t";}
                        if(n==tableContentWhere.get(0).length-1){
                            resultList = resultList + tableContentWhere.get(0)[n];
                        }
                        if (n == tableContentWhere.get(0).length - 1) {
                            resultList = resultList + "\n";
                        }
                    }
                    for (int i = 1; i < tableContentWhere.size(); i++) {
                        for (int j = 0; j < tableContentWhere.get(i).length; j++) {
                            if (Integer.parseInt(tableContentWhere.get(i)[column]) > Integer.parseInt(conditionTail)) {
                                if (j == tableContentWhere.get(i).length - 1) {
                                    resultList = resultList + tableContentWhere.get(i)[j];
                                } else {
                                    resultList = resultList + tableContentWhere.get(i)[j] + "\t";
                                }
                            }
                        }
                        if ((i + 1 < tableContentWhere.size()) && (Integer.parseInt(tableContentWhere.get(i + 1)[column]) > Integer.parseInt(conditionTail))) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (contentWhere.equals("*") && (conditionCompare.equals(">="))) {
                    for (int n = 0; n < tableContentWhere.get(0).length; n++) {
                        resultList = resultList + tableContentWhere.get(0)[n] + "\t";
                        if (n == tableContentWhere.get(0).length - 1) {
                            resultList = resultList + "\n";
                        }
                    }
                    for (int i = 1; i < tableContentWhere.size(); i++) {
                        for (int j = 0; j < tableContentWhere.get(i).length; j++) {
                            if (Integer.parseInt(tableContentWhere.get(i)[column]) > Integer.parseInt(conditionTail)||Integer.parseInt(tableContentWhere.get(i)[column]) == Integer.parseInt(conditionTail)) {
                                if (j == tableContentWhere.get(i).length - 1) {
                                    resultList = resultList + tableContentWhere.get(i)[j];
                                } else {
                                    resultList = resultList + tableContentWhere.get(i)[j] + "\t";
                                }
                            }
                        }
                        if ((i + 1 < tableContentWhere.size()) && (Integer.parseInt(tableContentWhere.get(i + 1)[column]) > Integer.parseInt(conditionTail))) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (contentWhere.equals("*") && (conditionCompare.equals("<"))) {
                    for (int n = 0; n < tableContentWhere.get(0).length; n++) {
                        resultList = resultList + tableContentWhere.get(0)[n] + "\t";
                        if (n == tableContentWhere.get(0).length - 1) {
                            resultList = resultList + "\n";
                        }
                    }
                    for (int i = 1; i < tableContentWhere.size(); i++) {
                        for (int j = 0; j < tableContentWhere.get(i).length; j++) {
                            if (Integer.parseInt(tableContentWhere.get(i)[column]) < Integer.parseInt(conditionTail)) {
                                if (j == tableContentWhere.get(i).length - 1) {
                                    resultList = resultList + tableContentWhere.get(i)[j];
                                } else {
                                    resultList = resultList + tableContentWhere.get(i)[j] + "\t";
                                }
                            }
                        }
                        if ((i + 1 < tableContentWhere.size()) && (Integer.parseInt(tableContentWhere.get(i + 1)[column]) < Integer.parseInt(conditionTail)) ) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (contentWhere.equals("*") && (conditionCompare.equals("<="))) {
                    for (int n = 0; n < tableContentWhere.get(0).length; n++) {
                        resultList = resultList + tableContentWhere.get(0)[n] + "\t";
                        if (n == tableContentWhere.get(0).length - 1) {
                            resultList = resultList + "\n";
                        }
                    }
                    for (int i = 1; i < tableContentWhere.size(); i++) {
                        for (int j = 0; j < tableContentWhere.get(i).length; j++) {
                            if (Integer.parseInt(tableContentWhere.get(i)[column]) < Integer.parseInt(conditionTail)||Integer.parseInt(tableContentWhere.get(i)[column]) == Integer.parseInt(conditionTail)) {
                                if (j == tableContentWhere.get(i).length - 1) {
                                    resultList = resultList + tableContentWhere.get(i)[j];
                                } else {
                                    resultList = resultList + tableContentWhere.get(i)[j] + "\t";
                                }
                            }
                        }
                        if ((i + 1 < tableContentWhere.size()) && (Integer.parseInt(tableContentWhere.get(i + 1)[column]) < Integer.parseInt(conditionTail)) ) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (contentWhere.equals("*") && (conditionCompare.equals("LIKE")||(conditionCompare.equals("like")))) {
                    int num = conditionTail.length();
                    for (int n = 0; n < tableContentWhere.get(0).length; n++) {
                        resultList = resultList + tableContentWhere.get(0)[n] + "\t";
                        if ((n == tableContentWhere.get(0).length - 1)) {
                            resultList = resultList + "\n";
                        }
                    }
                    for (int i = 1; i < tableContentWhere.size(); i++) {
                        for (int j = 0; j < tableContentWhere.get(i).length; j++) {
                            //int column = fileHeadIndexWhere.get(conditionHead);
                            if (tableContentWhere.get(i)[column].substring(tableContentWhere.get(i)[column].length() - num, tableContentWhere.get(i)[column].length()).equals(conditionTail)  ) {
                                if (j == tableContentWhere.size() - 1) {
                                    resultList = resultList + tableContentWhere.get(i)[j];
                                } else {
                                    resultList = resultList + tableContentWhere.get(i)[j] + "\t";
                                }
                            }
                        }
                        if ((i + 1 < tableContentWhere.size()) && (tableContentWhere.get(i + 1)[column].substring(tableContentWhere.get(i + 1)[column].length() - 2, tableContentWhere.get(i + 1)[column].length()).equals(conditionTail) ) ) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (!contentWhere.equals("*") && (conditionCompare.equals("=="))) {
                    resultList = resultList + tableContentWhere.get(0)[fileHeadIndexWhere.get(contentWhere)] + "\n";
                    for (int i = 1; i < tableContentWhere.size(); i++) {
                            if (tableContentWhere.get(i)[column].equals(conditionTail)) {
                                    resultList = resultList + tableContentWhere.get(i)[fileHeadIndexWhere.get(contentWhere)];
                                }
                            else{continue;}
                        if (i + 1 < tableContentWhere.size() && tableContentWhere.get(i + 1)[column].equals(conditionTail)) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (!contentWhere.equals("*") && (conditionCompare.equals("!="))) {
                    //int column = fileHeadIndexWhere.get(conditionHead);
                    for (int i = 0; i < tableContentWhere.size(); i++) {
                            if (!tableContentWhere.get(i)[column].equals(conditionTail)) {

                                    resultList = resultList + tableContentWhere.get(i)[fileHeadIndexWhere.get(contentWhere)];
                                }
                            else{continue;}

                        if (i + 1 < tableContentWhere.size() && !tableContentWhere.get(i + 1)[column].equals(conditionTail)) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (!contentWhere.equals("*") && (conditionCompare.equals(">"))) {
                    resultList = resultList + tableContentWhere.get(0)[fileHeadIndexWhere.get(contentWhere)] + "\n";

                    for (int i = 1; i < tableContentWhere.size(); i++) {
                            if (Integer.parseInt(tableContentWhere.get(i)[column]) > Integer.parseInt(conditionTail)) {
                                    resultList = resultList + tableContentWhere.get(i)[fileHeadIndexWhere.get(contentWhere)];
                                }
                            else{continue;}
                        if ((i + 1 < tableContentWhere.size()) && (Integer.parseInt(tableContentWhere.get(i + 1)[column]) > Integer.parseInt(conditionTail))) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (!contentWhere.equals("*") && (conditionCompare.equals(">="))) {
                    resultList = resultList + tableContentWhere.get(0)[fileHeadIndexWhere.get(contentWhere)] + "\n";
                    for (int i = 1; i < tableContentWhere.size(); i++) {
                        if (Integer.parseInt(tableContentWhere.get(i)[column]) > Integer.parseInt(conditionTail)||Integer.parseInt(tableContentWhere.get(i)[column]) == Integer.parseInt(conditionTail)) {
                            resultList = resultList + tableContentWhere.get(i)[fileHeadIndexWhere.get(contentWhere)];
                        }
                        else{continue;}
                        if ((i + 1 < tableContentWhere.size()) && (Integer.parseInt(tableContentWhere.get(i + 1)[column]) > Integer.parseInt(conditionTail))) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (!contentWhere.equals("*") && (conditionCompare.equals("<"))) {
                    resultList = resultList + tableContentWhere.get(0)[fileHeadIndexWhere.get(contentWhere)] + "\n";

                    for (int i = 1; i < tableContentWhere.size(); i++) {
                            if (Integer.parseInt(tableContentWhere.get(i)[column]) < Integer.parseInt(conditionTail)) {
                                    resultList = resultList + tableContentWhere.get(i)[fileHeadIndexWhere.get(contentWhere)];
                        }
                            else{continue;}
                        if ((i + 1 < tableContentWhere.size()) && (Integer.parseInt(tableContentWhere.get(i + 1)[column]) < Integer.parseInt(conditionTail)) ) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                else if (!contentWhere.equals("*") && (conditionCompare.equals("<="))) {
                    resultList = resultList + tableContentWhere.get(0)[fileHeadIndexWhere.get(contentWhere)] + "\n";

                    for (int i = 1; i < tableContentWhere.size(); i++) {
                        if (Integer.parseInt(tableContentWhere.get(i)[column]) < Integer.parseInt(conditionTail)||Integer.parseInt(tableContentWhere.get(i)[column]) == Integer.parseInt(conditionTail)) {
                            resultList = resultList + tableContentWhere.get(i)[fileHeadIndexWhere.get(contentWhere)];
                        }
                        else{continue;}
                        if ((i + 1 < tableContentWhere.size()) && (Integer.parseInt(tableContentWhere.get(i + 1)[column]) < Integer.parseInt(conditionTail)) ) {
                            resultList = resultList + "\n";
                        }
                    }
                }

                else if (!contentWhere.equals("*") && ((conditionCompare.equals("LIKE"))||(conditionCompare.equals("like")))) {
                    resultList = resultList + tableContentWhere.get(0)[fileHeadIndexWhere.get(contentWhere)] + "\n";
                    for (int i = 1; i < tableContentWhere.size(); i++) {

                            if (tableContentWhere.get(i)[column].substring(tableContentWhere.get(i)[column].length() - 2, tableContentWhere.get(i)[column].length()).equals(conditionTail)  ) {
                                    resultList = resultList + tableContentWhere.get(i)[fileHeadIndexWhere.get(contentWhere)];
                        }
                            else{continue;}
                        if ((i + 1 < tableContentWhere.size()) && (tableContentWhere.get(i + 1)[column].substring(tableContentWhere.get(i + 1)[column].length() - 2, tableContentWhere.get(i + 1)[column].length()).equals(conditionTail) ) ) {
                            resultList = resultList + "\n";
                        }
                    }
                }
                //System.out.println(resultList);
                //return resultList;
            }
        } else {
            //SELECT * FROM marks;
            String pattern = "(SELECT|select)(.*)(FROM|from)(.*)(;)";
            Pattern result = Pattern.compile(pattern);
            Matcher contentTmp = result.matcher(command);
            if (contentTmp.find()) {
                String content = contentTmp.group(2).trim();
                String fileNane = contentTmp.group(4).trim();
                //System.out.println(content);
                ArrayList<String[]> tableContent = new ArrayList<>();
                if(!checkTableExist(fileNane,path)){
                    return "[ERROR]: Table does not exist.";
                }
                if(!content.equals("*")&&!checkTableHeadExist(fileNane,path,content)){
                    return "[ERROR]: TableHead "+ content +" does not exist.";
                }
                HashMap<String, Integer> fileHeadIndex = readTableHeadToMemory(path, fileNane);
                try {
                    tableContent = readTableContentToMemory(path, fileNane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //String resultList = "";
                if (content.equals("*")) {
                    for (int i = 0; i < tableContent.size(); i++) {
                        for (int j = 0; j < tableContent.get(i).length; j++) {
                            if (j == tableContent.get(i).length - 1) {
                                resultList = resultList + tableContent.get(i)[j];
                            } else {
                                resultList = resultList + tableContent.get(i)[j] + "\t";
                            }
                        }
                        if(i!=tableContent.size()-1)
                        resultList = resultList + "\n";
                    }
                    //System.out.println(resultList);
                    //return resultList;
                    //System.out.println("222");
                    //new DBServer(Paths.get(".").toAbsolutePath().toFile()).readDate(path,fileNane);
                    //readAndStoreDataToMemory(path,fileNane);
                } else {
                    content = content.trim();
                    //String resultList = "";
                    //tableContent.get(i)[fileHeadIndex.get(content)].length()
                    for (int i = 0; i < tableContent.size(); i++) {
                        if (i == tableContent.size() - 1) {
                            resultList = resultList + tableContent.get(i)[fileHeadIndex.get(content)];
                        } else {
                            resultList = resultList + tableContent.get(i)[fileHeadIndex.get(content)] + "\n";
                        }
                    }
                    //return  resultList;
                }
            } else {
                return "[ERROR]:Invalid query";
            }
        }
        return "[OK]"+"\n"+resultList;
    }
}





