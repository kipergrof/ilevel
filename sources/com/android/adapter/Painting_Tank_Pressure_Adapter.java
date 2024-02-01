package com.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ideasthatfloat.ilevel.R;
import java.util.ArrayList;
import java.util.List;

public class Painting_Tank_Pressure_Adapter extends BaseAdapter {
    Context context;
    private List<String> list;
    private List<View> list_of_views = new ArrayList();

    public Painting_Tank_Pressure_Adapter(Context context2, List<String> list2) {
        this.context = context2;
        this.list = list2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listrowView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.list_content_tank_pressure, parent, false);
        TextView txtcontent = (TextView) listrowView.findViewById(R.id.textView1);
        txtcontent.setText(this.list.get(position));
        txtcontent.setTextColor(Color.parseColor("#FFFFFF"));
        this.list_of_views.add((ImageView) listrowView.findViewById(R.id.imageView1));
        return listrowView;
    }

    public List<View> getListOfView() {
        return this.list_of_views;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
