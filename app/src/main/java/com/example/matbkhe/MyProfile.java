package com.example.matbkhe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyProfile extends AppCompatActivity {

    EditText txtname,txtemail,txtpass,txtphone,txtaddress,txtactivation,txtuser;
    Button btnsend,btnregister;
  static   String oldpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        txtuser=findViewById(R.id.txtuserp);
        txtemail=findViewById(R.id.txtemailp);
        txtname=findViewById(R.id.txtnamep);
        txtpass=findViewById(R.id.txtpassp);
        txtphone=findViewById(R.id.txtphonep);

        txtaddress=findViewById(R.id.txtaddressp);



        Database db=new Database();
       db.ConnectDB();
        ResultSet rsm=db.RunSearch("select * from Users where username='"+LoginActivity.username+"'");
        try {
            if(rsm.next())
            {
                txtuser.setText(rsm.getString(1));
                txtpass.setText(rsm.getString(2));
                txtname.setText(rsm.getString(3));
                txtphone.setText(rsm.getString(4));
                txtemail.setText(rsm.getString(5));
                txtaddress.setText(rsm.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        oldpass=txtpass.getText().toString();

    }

    public void Update(View view) {

        Database db=new Database();
        db.ConnectDB();
        db.RunDML("update users set password='"+txtpass.getText()+"',name='"+txtname.getText()+"',email='"+txtemail.getText()+"',phone='"+txtphone.getText()+"',address='"+txtaddress.getText()+"' where username='"+txtuser.getText()+"'");
        Toast.makeText(this, "Your account has been updated", Toast.LENGTH_SHORT).show();

        if(oldpass.equals(txtpass.getText().toString()))
            ;
        else {
            getSharedPreferences("MatSH",MODE_PRIVATE)
                    .edit()
                    .clear()
                    .commit();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
