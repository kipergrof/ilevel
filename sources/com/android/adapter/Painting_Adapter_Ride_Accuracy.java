package com.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;
import java.util.ArrayList;
import java.util.List;

public class Painting_Adapter_Ride_Accuracy extends BaseAdapter {
    private ILevelApplication application;
    private Context context;
    private Boolean is_tank_pressure = false;
    private List<String> list;
    private List<View> list_of_views = new ArrayList();

    public Painting_Adapter_Ride_Accuracy(Context context2, List<String> list2, ILevelApplication application2, Boolean is_tank_pressure2) {
        this.context = context2;
        this.list = list2;
        this.application = application2;
        this.is_tank_pressure = is_tank_pressure2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listrowView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.list_content_ride_mode_accuracy, parent, false);
        TextView txtcontent = (TextView) listrowView.findViewById(R.id.textView1);
        ImageView imgcontent = (ImageView) listrowView.findViewById(R.id.imageView1);
        txtcontent.setText(this.list.get(position));
        txtcontent.setTextColor(Color.parseColor("#FFFFFF"));
        this.list_of_views.add(imgcontent);
        if (this.is_tank_pressure.booleanValue()) {
            if (this.list.get(position).contains(this.application.getTank_pressure_mode())) {
                imgcontent.setVisibility(0);
            } else {
                imgcontent.setVisibility(8);
            }
        } else if (this.list.get(position).contains(this.application.getRide_mode_accracy())) {
            imgcontent.setVisibility(0);
        }
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
