package edu.uob;

public class Sheds {
    private int id;
    private String name;
    private int height;
    private String purchaserID;
    public Sheds(){}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPurchaserID(String purchaserID) {
        this.purchaserID = purchaserID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public String getPurchaserID() {
        return purchaserID;
    }
}
