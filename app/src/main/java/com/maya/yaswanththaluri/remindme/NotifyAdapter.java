package com.maya.yaswanththaluri.remindme;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NotifyAdapter extends ArrayAdapter<Notificaation>
{
    public NotifyAdapter(@NonNull Context context, int resource, @NonNull List<Notificaation> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
        {
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.notifyitem, parent, false);
        }

        TextView sub = (TextView)convertView.findViewById(R.id.subjectList);
        TextView des = (TextView)convertView.findViewById(R.id.desc);
        TextView date = (TextView)convertView.findViewById(R.id.uploaddate);

        Notificaation notify = getItem(position);

        sub.setText(notify.getSubject());
        des.setText(notify.getDescription());
        date.setText(notify.getDate());


        return convertView;
    }
}
