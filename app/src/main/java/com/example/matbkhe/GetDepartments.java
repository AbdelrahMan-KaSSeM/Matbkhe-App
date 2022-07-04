package com.example.matbkhe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetDepartments {



public List<Departments> GetData() {

    List<Departments> data=new ArrayList<>();

    Database db=new Database();
    db.ConnectDB();
    ResultSet rs=db.RunSearch("select * from departments");
    try {
        while (rs.next()) {

            Departments d=new Departments();
            d.setNo(rs.getString(1));
            d.setName(rs.getString(2));
            d.setImage(rs.getString(3));
            data.add(d);
        }
    }catch (SQLException ex)
    {
        ;
    }

  return data;

}



}
