package com.example.matbkhe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterCart extends ArrayAdapter<Carts> {

    Context c;
    ArrayList<Carts> ass;

    public AdapterCart(Context context, ArrayList<Carts> cont) {
        super(context, R.layout.cartlayout,cont);
        c=context;
        ass=cont;
    }

    class Holder
    {
        ImageView imgdept,imgdel;
        TextView txtname,txtq,txtp,txtt;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        final Carts data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cartlayout, parent, false);

            viewHolder.txtname = (TextView) convertView.findViewById(R.id.txtnamecart);
            viewHolder.txtp = (TextView) convertView.findViewById(R.id.txtpricecart);
            viewHolder.txtq = (TextView) convertView.findViewById(R.id.txtqtycart);
            viewHolder.txtt = (TextView) convertView.findViewById(R.id.txttotalcart);

           viewHolder.imgdept = (ImageView) convertView.findViewById(R.id.imgshopp);
            viewHolder.imgdel = (ImageView) convertView.findViewById(R.id.imgdel);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        PicassoClient.downloadImage(c,data.getImg(),viewHolder.imgdept);
        viewHolder.txtname.setText(data.getName());
        viewHolder.txtp.setText(data.getP()+" LE");
        viewHolder.txtq.setText(data.getQ()+" KG");
        viewHolder.txtt.setText(data.getT()+" LE");

        viewHolder.imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDB db=new SQLiteDB(c);
                db.DeleteItem(data.getOrderno());
                Toast.makeText(c, "Item deleted", Toast.LENGTH_SHORT).show();





            }
        });




        // Return the completed view to render on screen
        return convertView;
    }


}
