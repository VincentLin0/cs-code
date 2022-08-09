package edu.uob.handler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class SqlHandler {
    protected String command;
    public static String path;

    public SqlHandler(String command){
        this.command = command;
        //this.path =  path;

    }

    public abstract String handle();
    public ArrayList<String[]> readTableContentToMemory(String path, String fileName) throws IOException {
        String name = path + File.separator + fileName;
        File fileToOpen = new File(name);
        ArrayList<String[]> tableContent = new ArrayList<>();
        if (fileToOpen.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(fileToOpen);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader buffReader = new BufferedReader(reader);
            String str = null;
            while ((str = buffReader.readLine()) != null) {
                //System.out.println(str);
                String[] data = str.split("\t");
                tableContent.add(data);

            }
            buffReader.close();

        }
        return tableContent;
    }

    public HashMap<String,Integer> readTableHeadToMemory(String path, String fileName){
        String name = path + File.separator + fileName;
        File fileToOpen = new File(name);
        FileReader reader = null;
        String firstLine = "";
        try {
            reader = new FileReader(fileToOpen);
            //BufferedReader buffReader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader buffReader = new BufferedReader(reader);
        try {
            firstLine = buffReader.readLine();
            buffReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String,Integer> fields = new HashMap<>();
        if(!firstLine.equals("") ){
            String[] tableName =  firstLine.split("\t");
            for (int i = 0; i < tableName.length; i++) {
                fields.put(tableName[i],i);
            }
        }

        return fields;
    }


    public void writeDataToFile(String path, ArrayList<String[]> resultList, String filename) throws IOException {
        String name = path + File.separator + filename;
        File fileToOpen = new File(name);
        fileToOpen.createNewFile();
        FileWriter writer = new FileWriter(fileToOpen);
        for (int i = 0; i < resultList.size(); i++) {
            for (int j = 0; j < resultList.get(i).length; j++) {
                if (j == resultList.get(i).length - 1) {
                    writer.write(resultList.get(i)[j]);
                    writer.flush();
                } else {
                    writer.write(resultList.get(i)[j]);
                    writer.write("\t");
                    writer.flush();
                }
            }
            if(i!=resultList.size()-1) {
                writer.write("\n");
                writer.flush();
            }
        }
        writer.close();
    }
    public Boolean checkTableExist(String tableName,String path){
        String name = path + File.separator + tableName;
        File fileToOpen = new File(name);
        if(fileToOpen.exists()) {
            return true;
        }
        return false;
    }

    public Boolean checkTableHeadExist(String tableName,String path,String tableHead){
        HashMap<String,Integer> Index = readTableHeadToMemory(path,tableName);
        return Index.containsKey(tableHead);
    }

    public HashSet<String[]> searchWhereAnd(String conditionWhereAndSymbol1,ArrayList<String[]> tableContentWhereAnd,
                                            int columnAnd1,String conditionWhereAndTail1,HashSet<String[]> conditionOne){
        if(conditionWhereAndSymbol1.equals("==")){
            for (int i = 1; i < tableContentWhereAnd.size(); i++) {
                if(tableContentWhereAnd.get(i)[columnAnd1].equals(conditionWhereAndTail1)){
                    conditionOne.add(tableContentWhereAnd.get(i));
                }
            }
        }
        else if(conditionWhereAndSymbol1.equals("!=")){
            for (int i = 1; i < tableContentWhereAnd.size(); i++) {
                if(!tableContentWhereAnd.get(i)[columnAnd1].equals(conditionWhereAndTail1)){
                    conditionOne.add(tableContentWhereAnd.get(i));
                }
            }
        }
        else if(conditionWhereAndSymbol1.equals(">")){
            for (int i = 1; i < tableContentWhereAnd.size(); i++) {
                if(Integer.parseInt(tableContentWhereAnd.get(i)[columnAnd1]) > Integer.parseInt(conditionWhereAndTail1)){
                    conditionOne.add(tableContentWhereAnd.get(i));
                }
            }
        }
        else if(conditionWhereAndSymbol1.equals(">=")){
            for (int i = 1; i < tableContentWhereAnd.size(); i++) {
                if(Integer.parseInt(tableContentWhereAnd.get(i)[columnAnd1]) >= Integer.parseInt(conditionWhereAndTail1)){
                    conditionOne.add(tableContentWhereAnd.get(i));
                }
            }
        }
        else if(conditionWhereAndSymbol1.equals("<")){
            for (int i = 1; i < tableContentWhereAnd.size(); i++) {
                if(Integer.parseInt(tableContentWhereAnd.get(i)[columnAnd1]) < Integer.parseInt(conditionWhereAndTail1)){
                    conditionOne.add(tableContentWhereAnd.get(i));
                }
            }
        }
        else if(conditionWhereAndSymbol1.equals("<=")){
            for (int i = 1; i < tableContentWhereAnd.size(); i++) {
                if(Integer.parseInt(tableContentWhereAnd.get(i)[columnAnd1]) <= Integer.parseInt(conditionWhereAndTail1)){
                    conditionOne.add(tableContentWhereAnd.get(i));
                }
            }
        }
        else if(conditionWhereAndSymbol1.equals("LIKE")||conditionWhereAndSymbol1.equals("like")){
            int num = conditionWhereAndTail1.length();
            for (int i = 1; i < tableContentWhereAnd.size(); i++) {
                if(tableContentWhereAnd.get(i)[columnAnd1].substring(tableContentWhereAnd.get(i)[columnAnd1].length() - num, tableContentWhereAnd.get(i)[columnAnd1].length()).equals(conditionWhereAndTail1)){
                    conditionOne.add(tableContentWhereAnd.get(i));
                }
            }
        }
        return conditionOne;
    }
    public void writeDataToFileArray(String path, ArrayList<ArrayList<String>> resultList, String filename) throws IOException {
        String name = path + File.separator + filename;
        File fileToOpen = new File(name);
        fileToOpen.createNewFile();
        FileWriter writer = new FileWriter(fileToOpen);
        for (int i = 0; i < resultList.size(); i++) {
            for (int j = 0; j < resultList.get(i).size(); j++) {
                if (j == resultList.get(i).size() - 1) {
                    writer.write(resultList.get(i).get(j));
                    writer.flush();
                } else {
                    writer.write(resultList.get(i).get(j));
                    writer.write("\t");
                    writer.flush();
                }
            }
            if(i!=resultList.size()-1) {
                writer.write("\n");
                writer.flush();
            }
        }
        writer.close();
    }
}
