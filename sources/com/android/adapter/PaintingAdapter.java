package com.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ideasthatfloat.ilevel.R;
import java.util.List;

public class PaintingAdapter extends BaseAdapter {
    Context context;
    List<String> list;

    PaintingAdapter(Context context2, List<String> list2) {
        this.context = context2;
        this.list = list2;
    }

    public View getView(int postion, View convertView, ViewGroup parent) {
        View listrowView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.list_content_layout, parent, false);
        TextView txtcontent = (TextView) listrowView.findViewById(R.id.textView1);
        txtcontent.setText(this.list.get(postion));
        txtcontent.setTextColor(Color.parseColor("#FFFFFF"));
        return listrowView;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
