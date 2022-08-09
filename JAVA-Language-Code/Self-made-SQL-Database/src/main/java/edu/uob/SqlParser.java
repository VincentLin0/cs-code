package edu.uob;

import edu.uob.handler.*;



import java.io.IOException;
import java.util.regex.Pattern;

import static edu.uob.Type.*;

public class SqlParser {

    public static SqlHandler getHandler(String command, String path) throws IOException {
        command = command.trim();
        Pattern pattern = Pattern.compile(" +");
        command = pattern.matcher(command).replaceAll(" ");
        //command = command.toUpperCase();
        String[] str = command.split(" ");

        if (str[0].equals(SELECT.toString()) || str[0].equals(SELECT.toString().toLowerCase())) {
            return new SelectSqlHandler(command);
        } else if (str[0].equals(UPDATE.toString()) || str[0].equals(UPDATE.toString().toLowerCase())) {
            return new UpdateSqlHandler(command);
        } else if ((str[0].equals("CREATE") && str[1].equals("DATABASE")) ||
                (str[0].equals("create") && str[1].equals("DATABASE")) ||
                (str[0].equals("CREATE") && str[1].equals("database")) ||
                (str[0].equals("create") && str[1].equals("database"))) {
            return new CreateDBSqlHandler(command,path);
        } else if ((str[0].equals("CREATE") && str[1].equals("TABLE")) ||
                (str[0].equals("create") && str[1].equals("TABLE")) ||
                (str[0].equals("CREATE") && str[1].equals("table")) ||
                (str[0].equals("create") && str[1].equals("table"))) {
            return new CreateTBSqlHandler(command);
        } else if (str[0].equals(DELETE.toString()) || str[0].equals(DELETE.toString().toLowerCase())) {
            return new DeleteSqlHandler(command);
        } else if (str[0].equals(USE.toString()) || str[0].equals(USE.toString().toLowerCase())) {
            return new UseSqlHandler(command,path);
        } else if (str[0].equals(INSERT.toString()) || str[0].equals(INSERT.toString().toLowerCase())) {
            return new INSERTSqlHandler(command);
        }else if (str[0].equals(JOIN.toString()) || str[0].equals(JOIN.toString().toLowerCase())) {
            return new JoinSqlHandler(command);
        }else if(str[0].equals(ALTER.toString()) || str[0].equals(ALTER.toString().toLowerCase())){
            return new AlterSqlHandler(command);
        }else if(str[0].equals(DROP.toString()) || str[0].equals(DROP.toString().toLowerCase())) {
            return new DropDataBaseHandler(command);
        }else {
            return new ErrorSqlHandler(command);
        }
    }

}