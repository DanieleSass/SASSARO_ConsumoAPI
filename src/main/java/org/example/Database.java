package org.example;

import javax.xml.crypto.Data;

public class Database {

    private static Database instance = null;
    private String connection;

    private Database(){

    }

    public Database getInstance(){
        if(instance==null){
            return new Database();
        }
        return instance;
    }

}
