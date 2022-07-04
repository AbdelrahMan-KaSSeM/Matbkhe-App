package com.example.matbkhe;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {


    ListView lst;
    GetItems g=new GetItems();
    Items model;
    AdapterItems adapterItems;

    ArrayList<Items> d;
    public ItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vv= inflater.inflate(R.layout.fragment_items, container, false);
        lst=vv.findViewById(R.id.lstitems);
        d=new ArrayList<>(g.GetData(MainUsersActivity.deptno));
        adapterItems=new AdapterItems(getActivity(),d);
        lst.setAdapter(adapterItems);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                LayoutInflater inflater1=LayoutInflater.from(getActivity());
                View vx=inflater.inflate(R.layout.detailslayout,null);

                TextView txtnam=vx.findViewById(R.id.txtnamed);
                TextView txtprice=vx.findViewById(R.id.txtpriced);
                TextView txtdetails=vx.findViewById(R.id.txtdetails);
                ImageView imgpro=vx.findViewById(R.id.imgd);
                ImageView imgcart=vx.findViewById(R.id.imgcart);
                final EditText txtqty=vx.findViewById(R.id.txtqty);

                model=d.get(i);
                txtnam.setText(model.getItemName());
                txtprice.setText("Price : "+model.getPrice()+" LE");
                txtdetails.setText("Details : "+model.getDetails());
                PicassoClient.downloadImage(getActivity(),model.getImageURL(),imgpro);

             final    BottomSheetDialog sh=new BottomSheetDialog(getActivity());
                sh.setContentView(vx);
                sh.show();

                imgcart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SQLiteDB ds=new SQLiteDB(getActivity());
                        double t=Double.parseDouble(txtqty.getText().toString())*Double.parseDouble(model.getPrice());
                        ds.SaveOrders(model.getItemNo(),model.getItemName(),txtqty.getText().toString(),model.getPrice(),String.valueOf(t),model.getImageURL());
                        Toast.makeText(getActivity(), "Item has been added in cart", Toast.LENGTH_SHORT).show();
                        sh.dismiss();
                        startActivity(new Intent(getActivity(),MainUsersActivity.class));

                    }
                });



            }
        });



        return vv;



    }

}
