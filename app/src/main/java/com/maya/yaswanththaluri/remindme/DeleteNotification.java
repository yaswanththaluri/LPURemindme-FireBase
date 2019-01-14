package com.maya.yaswanththaluri.remindme;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteNotification extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private EditText sub;
    private String s;
    private int i =0;
    private int count=0;
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notification);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("notifications");

        p = new ProgressDialog(this);
        p.setMessage("Loading...");
        p.setCanceledOnTouchOutside(false);

        Button b = (Button)findViewById(R.id.deletenoti);
        sub = (EditText)findViewById(R.id.subtodel);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = sub.getText().toString();
                if (s.equals(""))
                {
                    Toast.makeText(DeleteNotification.this, "Subject Field should not Be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    delete(s);
                    sub.setText("");
                    p.show();
                }
            }
        });
    }

    public void delete(final String subj)
    {

        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                remove(dataSnapshot, subj);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });

        Log.i("infor", ""+i);


    }

    public void remove(DataSnapshot dataSnapshot, String subj)
    {
        for (DataSnapshot ds: dataSnapshot.getChildren())
        {
            Notificaation n = ds.getValue(Notificaation.class);
            String s = n.getSubject().toUpperCase();

            if (s.equals(subj.toUpperCase()))
            {
                ds.getRef().removeValue();
                i =1;
                p.dismiss();
                break;
            }
        }

        if (i==1&&count==0)
        {
            AlertDialog.Builder d = new AlertDialog.Builder(DeleteNotification.this);
            d.setTitle("Confirmation");
            d.setMessage("Announcement Deleted Successfully");
            d.setCancelable(false);
            d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent(DeleteNotification.this, DashBoard.class);
                    startActivity(i);
                }
            });
            d.show();
        }
        else
        {
            p.dismiss();
            AlertDialog.Builder d = new AlertDialog.Builder(DeleteNotification.this);
            d.setTitle("Confirmation");
            d.setMessage("No data Found for this Subject");
            d.setCancelable(false);
            d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            d.show();
        }
    }

}
