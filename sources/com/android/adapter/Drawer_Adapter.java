package com.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ideasthatfloat.ilevel.R;
import java.util.ArrayList;

public class Drawer_Adapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> list;

    public Drawer_Adapter(Context context2, ArrayList<String> list2) {
        super(context2, R.layout.drawer_list_item, list2);
        this.context = context2;
        this.list = list2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.drawer_list_item, parent, false);
        ((TextView) rowView.findViewById(R.id.drawer_text)).setText(this.list.get(position));
        ImageView imgView = (ImageView) rowView.findViewById(R.id.drawer_item_icon);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.drawer_arrow);
        if (position == 0) {
            imgView.setImageResource(R.drawable.ic_action_calibration);
        }
        if (position == 1) {
            imgView.setImageResource(R.drawable.ic_action_saveheights);
        }
        if (position == 2) {
            imgView.setImageResource(R.drawable.ic_action_settings);
        }
        if (position == 3) {
            imgView.setImageResource(R.drawable.ic_action_password);
        }
        return rowView;
    }
}
