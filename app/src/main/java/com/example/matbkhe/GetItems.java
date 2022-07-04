package com.example.matbkhe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetItems {



public List<Items> GetData(String deptno) {

    List<Items> data=new ArrayList<>();

    Database db=new Database();
    db.ConnectDB();
    ResultSet rs=db.RunSearch("select * from Items where DepartmentNo ='"+deptno+"'");
    try {
        while (rs.next()) {

            Items d=new Items();
            d.setItemNo(rs.getString(1));
            d.setItemName(rs.getString(2));
            d.setPrice(rs.getString(3));
            d.setDetails(rs.getString(4));
            d.setImageURL(rs.getString(5));
            d.setDepartmentNo(rs.getString(6));
            data.add(d);
        }
    }catch (SQLException ex)
    {
        ;
    }

  return data;

}



}
