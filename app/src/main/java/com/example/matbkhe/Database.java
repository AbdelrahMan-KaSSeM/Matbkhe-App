package com.example.matbkhe;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    Connection conn=null;

    public Connection ConnectDB()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:jtds:sqlserver://sql5014.site4now.net/DB_A55161_abdelrahmanf522DB","DB_A55161_abdelrahmanf522DB_admin","aa1234567");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return conn;

    }


    public String RunDML(String st)
    {
        try {
            Statement statement=conn.createStatement();
            statement.executeUpdate(st);
            return "Ok";

        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }


    public ResultSet RunSearch(String st)
    {
        try {
            Statement statement=conn.createStatement();
            ResultSet rs=statement.executeQuery(st);
            return  rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

}
