package com.example.matbkhe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Shoppingcart extends AppCompatActivity {

    ListView lst;

    Carts model;
    AdapterCart adapterCart;
    ArrayList<Carts> d;
    TextView txtsun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);

        SQLiteDB db=new SQLiteDB(this);
        lst=findViewById(R.id.lstcart);
        d=new ArrayList<>(db.GetData());
        adapterCart=new AdapterCart(this,d);
        lst.setAdapter(adapterCart);

          txtsun=findViewById(R.id.txttotal);
        txtsun.setText("Total is "+db.GetTotal());



    }

}
