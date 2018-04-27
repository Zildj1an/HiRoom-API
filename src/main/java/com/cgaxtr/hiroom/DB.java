package com.cgaxtr.hiroom;

import com.cgaxtr.hiroom.Utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private Connection connection;
    private static DB INSTANCE = null;

    private DB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String newConnectionURL = "jdbc:mysql://" + Config.HOST + "/" + Config.DDBB + "?"
                    + "user=" + Config.USER + "&password=" + Config.PASS;
            this.connection = DriverManager.getConnection(newConnectionURL);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DB getInstance(){

        if (INSTANCE == null){
            INSTANCE = new DB();
        }
        return INSTANCE;
    }

    public Connection getConnection(){
        return this.connection;
    }
}
