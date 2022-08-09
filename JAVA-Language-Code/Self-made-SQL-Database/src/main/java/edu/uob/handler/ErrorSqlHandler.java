package edu.uob.handler;

public class ErrorSqlHandler extends SqlHandler{
    public ErrorSqlHandler(String command) {
        super(command);

    }

    @Override
    public String handle() {
        return "[ERROR]:Invalid query";
    }
}
