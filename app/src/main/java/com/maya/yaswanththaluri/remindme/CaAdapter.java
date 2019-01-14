package com.maya.yaswanththaluri.remindme;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CaAdapter extends ArrayAdapter<CaMessage>
{
    public CaAdapter(Context context, int resource, List<CaMessage> objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.listitem, parent, false);

        }
        TextView course = (TextView) convertView.findViewById(R.id.course);
        TextView catype = (TextView) convertView.findViewById(R.id.type);
        TextView syllabus = (TextView) convertView.findViewById(R.id.syl);
        TextView date = (TextView) convertView.findViewById(R.id.dateexam);
        TextView ch = (TextView) convertView.findViewById(R.id.groupch);

        CaMessage message = getItem(position);

        course.setText(message.getCourse());
        catype.setText(message.getType());
        syllabus.setText(message.getSyllabus());
        date.setText(message.getDate());
        ch.setText(message.getGroup());

        String datein = message.getDate();
        Date strDate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            strDate = sdf.parse(datein);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (new Date().before(strDate))
        {

            LinearLayout l =(LinearLayout) convertView.findViewById(R.id.main);
            l.setVisibility(View.VISIBLE);

        }

        return convertView;

    }
}