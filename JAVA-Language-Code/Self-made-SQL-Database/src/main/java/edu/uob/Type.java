package edu.uob;

public enum Type {
    SELECT(1, "SELECT"),
    UPDATE(2, "UPDATE"),
    DELETE(3, "DELETE"),
    USE(4, "USE"),
    CREATEDATABASE(5, "CREATEDATABASE"),
    CREATETABLE(6, "CREATETABLE"),
    INSERT (7, "INSERT"),
    JOIN (8, "JOIN"),
    ALTER (9, "ALTER"),
    DROP (10, "DROP"),
    UNKNOWN(-1, "UNKNOWN");

    int value;
    String name;
    Type(int value,String name){
        this.value = value;
        this.name = name;
    }
}
