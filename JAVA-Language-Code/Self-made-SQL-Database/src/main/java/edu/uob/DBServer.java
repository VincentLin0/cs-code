package edu.uob;

import edu.uob.handler.SqlHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.io.*;
import java.lang.*;
import java.util.ArrayList;


/**
 * This class implements the DB server.
 */
public final class DBServer {
    private static File databaseDirectory;
    private static final char END_OF_TRANSMISSION = 4;

    public static void main(String[] args) throws IOException {
        new DBServer(Paths.get(".").toAbsolutePath().toFile()).blockingListenOn(8888);

    }

    /**
     * KEEP this signature (i.e. {@code edu.uob.DBServer(File)}) otherwise we won't be able to mark
     * your submission correctly.
     *
     * <p>You MUST use the supplied {@code databaseDirectory} and only create/modify files in that
     * directory; it is an error to access files outside that directory.
     *
     * @param databaseDirectory The directory to use for storing any persistent database files such
     *                          that starting a new instance of the server with the same directory will restore all
     *                          databases. You may assume *exclusive* ownership of this directory for the lifetime of this
     *                          server instance.
     */
    public DBServer(File databaseDirectory) throws IOException {
        // TODO implement your server logic here
        this.databaseDirectory = databaseDirectory;
        System.out.println("datadirectory1 = "+ databaseDirectory.getAbsolutePath());
//            ArrayList<String[]> peopleList = readDate(databaseDirectory.getAbsolutePath(), "people.tab");

//            writeDataToClassPeople(peopleList);
//            peopleList.get(1)[1] = "Tom";
//            peopleList.get(0)[0] = "Id";
//            writeDataToFile(databaseDirectory, peopleList, "people.tab");
//            ArrayList<String[]> shedsList = readDate(databaseDirectory.getAbsolutePath(), "sheds.tab");
//            writeDataToClassSheds(shedsList);



    }

    /**
     * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
     * able to mark your submission correctly.
     *
     * <p>This method handles all incoming DB commands and carry out the corresponding actions.
     */
    public String handleCommand(String command) {
        // TODO implement your server logic here
        if(command.equals("")){
            return "[ERROR]:command is null.";
        }
        if(!command.substring(command.length()-1,command.length()).equals(";")){
            return "[ERROR]: Semi colon missing at end of line.";
        }
        try {
            SqlHandler handler = SqlParser.getHandler(command, this.databaseDirectory.getAbsolutePath());
            return handler.handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "[OK] Thanks for your message: " + command;
    }

    //  === Methods below are there to facilitate server related operations. ===

    /**
     * Starts a *blocking* socket server listening for new connections. This method blocks until the
     * current thread is interrupted.
     *
     * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
     * you want to.
     *
     * @param portNumber The port to listen on.
     * @throws IOException If any IO related operation fails.
     */
    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.err.println("Server encountered a non-fatal IO error:");
                    e.printStackTrace();
                    System.err.println("Continuing...");
                }
            }
        }
    }

    /**
     * Handles an incoming connection from the socket server.
     *
     * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
     * * you want to.
     *
     * @param serverSocket The client socket to read/write from.
     * @throws IOException If any IO related operation fails.
     */
    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {

            System.out.println("Connection established: " + serverSocket.getInetAddress());
            while (!Thread.interrupted()) {
                String incomingCommand = reader.readLine();
                System.out.println("Received message: " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }

    public ArrayList<String[]> readDate(String databaseDirectory, String filename) throws IOException {

        String name = databaseDirectory + File.separator + filename;
        File fileToOpen = new File(name);
        ArrayList<String[]> resultList = null;
        if (fileToOpen.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(fileToOpen);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader buffReader = new BufferedReader(reader);

            String str = null;
            ArrayList<String[]> peoples = new ArrayList<>();

            while ((str = buffReader.readLine()) != null) {
                System.out.println(str);
                resultList = storeData(str, peoples);
            }
            buffReader.close();

        }
        //resultList.get(1)[1]="9";
        return resultList;
    }

    //public
    public ArrayList<String[]> storeData(String str, ArrayList<String[]> peoples) {
        String[] data = splitData(str);
        peoples.add(data);
        return peoples;
        //String i =data [0];

    }

    public String[] splitData(String str) {

        String[] temp;
        String delimiter = "\t";
        temp = str.split(delimiter);
        return temp;
    }


    public void writeDataToClassPeople(ArrayList<String[]> peopleList) {
        int i = 1, j = 0;
        ArrayList<People> peopleArrayList = new ArrayList<>();
        for (i = 1; i < peopleList.size() - 1; i++) {
            People people = new People();
            for (j = 0; j < 5; j++) {
                if (j == 0) {
                    people.setId(Integer.parseInt(peopleList.get(i)[j]));
                }
                if (j == 1) {
                    people.setName(peopleList.get(i)[j]);
                }
                if (j == 2) {
                    people.setAge(Integer.parseInt(peopleList.get(i)[j]));
                }
                if (j == 3) {
                    people.setEmail(peopleList.get(i)[j]);
                    peopleArrayList.add(people);
                }
            }
        }


    }

    public void writeDataToClassSheds(ArrayList<String[]> shedsList) {
        int i = 1, j = 0;
        ArrayList<Sheds> shedsArrayList = new ArrayList<>();
        for (i = 1; i < shedsList.size() - 1; i++) {
            Sheds sheds = new Sheds();
            for (j = 0; j < 5; j++) {
                if (j == 0) {
                    sheds.setId(Integer.parseInt(shedsList.get(i)[j]));
                }
                if (j == 1) {
                    sheds.setName(shedsList.get(i)[j]);
                }
                if (j == 2) {
                    sheds.setHeight(Integer.parseInt(shedsList.get(i)[j]));
                }
                if (j == 3) {
                    sheds.setPurchaserID(shedsList.get(i)[j]);
                    shedsArrayList.add(sheds);
                }
            }
        }


    }

    public void writeDataToFile(File databaseDirectory, ArrayList<String[]> resultList, String filename) throws IOException {
        String name = databaseDirectory.getAbsolutePath() + File.separator + filename;
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
                writer.write("\n");
                writer.flush();
            }
            writer.close();
    }


}
