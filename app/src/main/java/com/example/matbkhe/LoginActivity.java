package com.example.matbkhe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LoginActivity extends AppCompatActivity {

    EditText txtusername,txtpass;
    Button btnlogin;
    CheckBox chk;
    public static String username;
    public static String name,email,image,usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin=findViewById(R.id.btnlogin);
        txtpass=findViewById(R.id.txtpasslogin);
        txtusername=findViewById(R.id.txtuserlogin);
        chk=findViewById(R.id.chkloginme);


        SharedPreferences sh=getSharedPreferences("MatSH",MODE_PRIVATE);
        String u=sh.getString("User",null);
        if(u!=null) {
            username=u;
            startActivity(new Intent(LoginActivity.this, MainUsersActivity.class));
        }
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtusername.getText().toString().isEmpty())
                {
                    txtusername.setError("Enter username");
                    txtusername.requestFocus();
                }
                else {
                    if (txtpass.getText().toString().isEmpty()) {
                        txtpass.setError("Enter password");
                        txtpass.requestFocus();
                    } else {
                        Database db=new Database();
                        Connection conn=db.ConnectDB();
                        if(conn==null)
                            Toast.makeText(LoginActivity.this, "Check internet", Toast.LENGTH_SHORT).show();
                        else {
                            ResultSet rs=db.RunSearch("select * from users where username='"+txtusername.getText()+"'");
                            try {
                                if(rs.next())
                                {

                                    if(txtpass.getText().toString().equals(rs.getString(2))) {

                                        if(chk.isChecked())
                                        {

                                            getSharedPreferences("MatSH",MODE_PRIVATE)
                                                    .edit()
                                                    .putString("User",txtusername.getText().toString())

                                                    .commit();
                                        }
                                        username=txtusername.getText().toString();
                                        name=rs.getString(3);
                                        email=rs.getString(5);
                                        image=rs.getString(9);
                                        usertype=rs.getString(10);

                                        startActivity(new Intent(LoginActivity.this, MainUsersActivity.class));
                                    }
                                    else
                                        Toast.makeText(LoginActivity.this, "Invaild Password", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    AlertDialog.Builder m = new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("Login")
                                            .setMessage("Invaild username ")
                                            .setIcon(R.drawable.logo)
                                            .setPositiveButton("Goto Register", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                                }
                                            }).setNegativeButton("Try Again",null);
                                    m.create().show();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        });


    }

    public void GotoRegister(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    public void ForgetPass(View view) {

        LayoutInflater inflater=LayoutInflater.from(LoginActivity.this);
        View vv=inflater.inflate(R.layout.forgetpass,null);
        AlertDialog.Builder ms=new AlertDialog.Builder(LoginActivity.this);
        ms.setView(vv);
        final EditText txtem=vv.findViewById(R.id.txtemailforget);

        ms.setPositiveButton("send Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Database db=new Database();
                db.ConnectDB();
                final ResultSet rem=db.RunSearch("select * from users where email='"+txtem.getText()+"'");
                try {
                    if(rem.next())
                    {
                        Random r=new Random();
                        final int pass=r.nextInt(999-111+1)+111;
                        db.RunDML("update users set password='"+pass+"' where email='"+txtem.getText()+"'");
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(rem.getString(4), null, "Dear : "+rem.getString(3).toString()+"\n"+"Your New Password is : "+pass+"\n"+"Thanks :) ", null, null);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final String username = "yourmobileapp2017@gmail.com";
                                    final String password = "okok2017";
                                    Properties props = new Properties();
                                    props.put("mail.smtp.auth", "true");
                                    props.put("mail.smtp.starttls.enable", "true");
                                    props.put("mail.smtp.host", "smtp.gmail.com");
                                    props.put("mail.smtp.port", "587");

                                    Session session = Session.getInstance(props,
                                            new javax.mail.Authenticator() {

                                                protected PasswordAuthentication getPasswordAuthentication() {
                                                    return new PasswordAuthentication(username, password);
                                                }
                                            });

                                    try {
                                        Message message = new MimeMessage(session);
                                        message.setFrom(new InternetAddress("yourmobileapp2017@gmail.com"));
                                        message.setRecipients(Message.RecipientType.TO,
                                                InternetAddress.parse(txtem.getText().toString()));

                                        message.setSubject("New Password Matbkhe Application");
                                        message.setText("Dear : "+rem.getString(3).toString()+"\n"+"Your New Password is : "+pass+"\n"+"Thanks :) ");
                                        Transport.send(message);


                                    } catch (MessagingException e) {
                                        Toast.makeText(getApplication(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                                        throw new RuntimeException(e);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }).start();

                        Toast.makeText(LoginActivity.this, "New Password has been sent", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(LoginActivity.this, "This email not exist", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });
        ms.create().show();


    }
}
