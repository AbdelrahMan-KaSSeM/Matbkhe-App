package com.example.matbkhe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterDepartments extends ArrayAdapter<Departments> {

    Context c;
    ArrayList<Departments> ass;

    public AdapterDepartments(Context context, ArrayList<Departments> cont) {
        super(context, R.layout.departlayout,cont);
        c=context;
        ass=cont;
    }

    class Holder
    {
        ImageView imgdept;
        TextView txtname;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        Departments data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.departlayout, parent, false);

            viewHolder.txtname = (TextView) convertView.findViewById(R.id.txtdepart);
           viewHolder.imgdept = (ImageView) convertView.findViewById(R.id.imgdepart);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        PicassoClient.downloadImage(c,data.getImage(),viewHolder.imgdept);
        viewHolder.txtname.setText(data.getName());

        // Return the completed view to render on screen
        return convertView;
    }


}
