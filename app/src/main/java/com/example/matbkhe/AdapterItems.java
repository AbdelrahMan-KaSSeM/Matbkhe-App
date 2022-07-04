package com.example.matbkhe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterItems extends ArrayAdapter<Items> {

    Context c;
    ArrayList<Items> ass;

    public AdapterItems(Context context, ArrayList<Items> cont) {
        super(context, R.layout.itemslayout,cont);
        c=context;
        ass=cont;
    }

    class Holder
    {
        ImageView imgdept;
        TextView txtname,txtprice;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        Items data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.itemslayout, parent, false);

            viewHolder.txtname = (TextView) convertView.findViewById(R.id.txtitemname);
            viewHolder.txtprice = (TextView) convertView.findViewById(R.id.txtprice);
           viewHolder.imgdept = (ImageView) convertView.findViewById(R.id.imgitems);



            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        PicassoClient.downloadImage(c,data.getImageURL(),viewHolder.imgdept);
        viewHolder.txtname.setText(data.getItemName());
        viewHolder.txtprice.setText(data.getPrice()+" LE");

        // Return the completed view to render on screen
        return convertView;
    }


}
