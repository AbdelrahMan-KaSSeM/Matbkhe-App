package com.example.matbkhe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDB extends SQLiteOpenHelper {


    public SQLiteDB(  Context context) {
        super(context, "MatbkheDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE  Orders  ( OrderNo INTEGER, ItemNo TEXT, ItemName TEXT, Qty NUMERIC, Price  NUMERIC, SubTotal NUMERIC ,Image TEXT,PRIMARY KEY( OrderNo ))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void SaveOrders(String no,String name,String q,String p,String t,String img)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues data=new ContentValues();
        data.put("ItemNo",no);
        data.put("ItemName",name);
        data.put("Qty",q);
        data.put("Price",p);
        data.put("SubTotal",t);
        data.put("Image",img);
        db.insert("Orders",null,data);
    }


    public int GetCount()
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cd=db.rawQuery("select count(*) from Orders",null);
        cd.moveToFirst();
        return  cd.getInt(0);

    }


    public double GetTotal()
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cd=db.rawQuery("select Sum(SubTotal) from Orders",null);
        cd.moveToFirst();
        return  cd.getDouble(0);

    }


    public void DeleteItem(String pk)
    {
        String Key="OrderNo";
        SQLiteDatabase db=getWritableDatabase();
        db.delete("Orders",Key + " = ?",
                new String[] { String.valueOf(pk) });
        db.close();
    }

    public List<Carts> GetData() {

        List<Carts> data=new ArrayList<>();

        SQLiteDatabase db=getReadableDatabase();
        Cursor ccart=db.rawQuery("Select * from Orders",null);

            while (ccart.moveToNext()) {

                Carts d=new Carts();
               d.setOrderno(ccart.getString(0));
                d.setNo(ccart.getString(1));
                d.setName(ccart.getString(2));
                d.setQ(ccart.getString(3));
                d.setP(ccart.getString(4));
                d.setT(ccart.getString(5));
                d.setImg(ccart.getString(6));
                data.add(d);
            }
        return data;
    }



}
