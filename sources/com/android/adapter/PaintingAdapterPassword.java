package com.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.application.ILevelApplication;
import com.ideasthatfloat.ilevel.R;
import java.util.List;

public class PaintingAdapterPassword extends BaseAdapter {
    private ILevelApplication application;
    private Context context;
    private List<String> list;

    public PaintingAdapterPassword(Context context2, List<String> list2, ILevelApplication application2) {
        this.context = context2;
        this.list = list2;
        this.application = application2;
    }

    public View getView(int postion, View convertView, ViewGroup parent) {
        View listrowView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.list_content_setting, parent, false);
        TextView txtcontent = (TextView) listrowView.findViewById(R.id.settingtextView1);
        TextView settingtextView2 = (TextView) listrowView.findViewById(R.id.settingtextView2);
        txtcontent.setText(this.list.get(postion));
        txtcontent.setTextColor(Color.parseColor("#FFFFFF"));
        if (postion == 0) {
            if (this.application.getRide_height_on_start() == null) {
                this.application.setRide_height_on_start("OFF");
            } else {
                settingtextView2.setText(this.application.getRide_height_on_start());
            }
        }
        if (postion == 1) {
            if (this.application.getRide_monitor_mode() == null) {
                this.application.setRide_monitor_mode("OFF");
            } else {
                settingtextView2.setText(this.application.getRide_monitor_mode());
            }
        }
        if (postion == 2) {
            if (this.application.getRide_mode_accracy() == null) {
                this.application.setRide_mode_accracy("2");
            } else {
                settingtextView2.setText(this.application.getRide_mode_accracy());
            }
        }
        if (postion == 3) {
            if (this.application.getTrim_mode() == null) {
                this.application.setTrim_mode("OFF");
            } else {
                settingtextView2.setText(this.application.getTrim_mode());
            }
        }
        if (postion == 4) {
            if (this.application.getTank_pressure_mode() == null) {
                this.application.setTank_pressure_mode("200");
            } else {
                settingtextView2.setText(this.application.getTank_pressure_mode() + " psi");
            }
        }
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
