package com.example.matbkhe;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.sql.Connection;
import java.util.Properties;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

      int Acvtivecode;
    EditText txtname,txtemail,txtpass,txtphone,txtaddress,txtactivation,txtuser;
    Button btnsend,btnregister;
    Switch sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtuser=findViewById(R.id.txtuser);
        txtemail=findViewById(R.id.txtemail);
        txtname=findViewById(R.id.txtname);
        btnsend=findViewById(R.id.btnsend);

        txtphone=findViewById(R.id.txtphone);
        txtactivation=findViewById(R.id.txtactivation);
        txtaddress=findViewById(R.id.txtaddress);



        txtpass=findViewById(R.id.txtpass);
btnregister=findViewById(R.id.btnregister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtuser.getText().toString().isEmpty())
                {
                    txtuser.setError("Enter username");
                    txtuser.requestFocus();
                }
                else
                {
                    if(txtpass.getText().toString().isEmpty())
                    {
                        txtpass.setError("Enter password");
                        txtpass.requestFocus();
                    }
                    else
                    {
                        if(txtphone.getText().toString().length()!=11)
                        {
                            txtphone.setError("invaild phone");
                            txtphone.requestFocus();
                        }
                        else {
                           // if (Acvtivecode == Integer.parseInt(txtactivation.getText().toString())) {

                                Database db = new Database();
                                Connection conn = db.ConnectDB();
                                if (conn == null)
                                    Toast.makeText(RegisterActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                                else {

                                    String ms = db.RunDML("insert into [users] values ('" + txtuser.getText() + "','" + txtpass.getText() + "','" + txtname.getText() + "','" + txtphone.getText() + "','" + txtemail.getText() + "','" + txtaddress.getText() + "','"+MapsActivity.lat+"','"+MapsActivity.lang+"','','User')");
                                    if (ms.equals("Ok")) {
                                        AlertDialog.Builder m = new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("Registeration")
                                                .setMessage("User has been crated ")
                                                .setIcon(R.drawable.logo)
                                                .setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    }
                                                });
                                        m.create().show();
                                    } else if (ms.contains("PK_Users")) {
                                        AlertDialog.Builder m = new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("Registeration")
                                                .setMessage("Sorry this user is exist ")
                                                .setIcon(R.drawable.logo)
                                                .setPositiveButton("Try again", null);
                                        m.create().show();
                                    } else if (ms.contains("EmailUQ")) {
                                        AlertDialog.Builder m = new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("Registeration")
                                                .setMessage("Sorry this email is used ")
                                                .setIcon(R.drawable.logo)
                                                .setPositiveButton("Try again", null);
                                        m.create().show();
                                    } else if (ms.contains("PhoneUQ")) {
                                        AlertDialog.Builder m = new AlertDialog.Builder(RegisterActivity.this)
                                                .setTitle("Registeration")
                                                .setMessage("Sorry this phone is used ")
                                                .setIcon(R.drawable.logo)
                                                .setPositiveButton("Try again", null);
                                        m.create().show();
                                    } else
                                        Toast.makeText(RegisterActivity.this, "" + ms, Toast.LENGTH_SHORT).show();

                                }

                          //  }
                           // else
                             //   Toast.makeText(RegisterActivity.this, "Sorry !!! invaild activation code , check your email", Toast.LENGTH_SHORT).show();

                        }



                    }
                }

            }
        });



        sw=findViewById(R.id.swit);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true)
                {
                    txtpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
                else
                {
                    txtpass.setTransformationMethod(PasswordTransformationMethod.getInstance());


                }
            }
        });


        txtpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false)
                {
                    String pass="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
                    if(txtpass.getText().toString().matches(pass))
                        ;
                    else
                        txtpass.setError("Password Is weak");
                }
            }
        });



        txtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String em="^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$";
                if(txtemail.getText().toString().matches(em))
                    ;
                else
                    txtemail.setError("Invaild Email");

            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Random r=new Random();
                Acvtivecode=r.nextInt(9999-1111+1)+1111;


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
                                        InternetAddress.parse(txtemail.getText().toString()));

                                message.setSubject("Activation Code Matbkhe Application");
                                message.setText("Dear : "+txtname.getText().toString()+"\n"+"Your Activation code is : "+Acvtivecode+"\n"+"Thanks for registeration :) ");
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
                Toast.makeText(getApplication(), "Activation code has been sent Check your Email", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void GotoMap(View view) {
        startActivity(new Intent(this,MapsActivity.class));
    }
}
