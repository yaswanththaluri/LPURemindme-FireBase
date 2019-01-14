package com.maya.yaswanththaluri.remindme;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNotification extends AppCompatActivity {

    private String subject;
    private String description;
    private String date;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private EditText des;
    private EditText sub;
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("notifications");

        sub = (EditText)findViewById(R.id.subject);


        des = (EditText)findViewById(R.id.description);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date = df.format(c);

        p = new ProgressDialog(this);
        p.setMessage("Adding Announcement to Database");

        Button b = (Button)findViewById(R.id.submitNotification);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subject = sub.getText().toString();
                description = des.getText().toString();
                if (!(subject.equals("")&&description.equals("")))
                {
                    p.show();
                    preview(subject, description);
                    sub.setText("");
                    des.setText("");
                }
                else
                {
                    Toast.makeText(AddNotification.this, "Fields should not be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void preview(String sub, String des)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Preview: "+subject);
        builder.setMessage(description);
        builder.setCancelable(false);
        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                upload();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    public void upload()
    {
        Notificaation no = new Notificaation(subject, description, date);
        mReference.push().setValue(no);
        p.dismiss();
        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show();
    }


}
