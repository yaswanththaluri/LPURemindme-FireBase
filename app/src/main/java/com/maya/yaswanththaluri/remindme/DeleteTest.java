package com.maya.yaswanththaluri.remindme;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class DeleteTest extends AppCompatActivity {

    private EditText dlcourse;
    private Button cal;
    private Button delete;
    private String date1;
    private SimpleDateFormat sdf;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private String course;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_test);

        cal = (Button)findViewById(R.id.dldtpicker);
        dlcourse = (EditText)findViewById(R.id.dlcourse);



        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TextView t = (TextView)findViewById(R.id.datedl);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                sdf = new SimpleDateFormat(myFormat, Locale.US);
                t.setText(sdf.format(myCalendar.getTime()));
                date1 = sdf.format(myCalendar.getTime()).toString();
            }

        };

        cal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(DeleteTest.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button b = (Button)findViewById(R.id.deletetst);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                course = dlcourse.getText().toString().toUpperCase();
                removeValue();

            }
        });

        Button b2 = (Button)findViewById(R.id.resettst);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t = (TextView)findViewById(R.id.datedl);
                t.setText("YYYY-MM-DD");
                dlcourse = (EditText)findViewById(R.id.dlcourse);
                dlcourse.setText("");
            }
        });





    }


    public void removeValue()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mMessageDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accessData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void accessData(DataSnapshot dataSnapshot)
    {
        int i = 0;
        for (DataSnapshot ds: dataSnapshot.getChildren())
        {

            CaMessage friendlyMessage = ds.getValue(CaMessage.class);
            String ncourse = friendlyMessage.getCourse();
            String ndate = friendlyMessage.getDate();
            Log.i("course",course);
            Log.i("date",date1);
            Log.i("ncourse",ncourse);
            Log.i("ndate",ndate);

            if (ncourse.equals(course)&&ndate.equals(date1))
            {

                ds.getRef().removeValue();
                i = 1;
                break;
            }
        }
        if (i==0)
        {
            Toast.makeText(this, "No Matching Data Found",Toast.LENGTH_SHORT).show();
            TextView t = (TextView)findViewById(R.id.datedl);
            t.setText("YYYY-MM-DD");
            dlcourse = (EditText)findViewById(R.id.dlcourse);
            dlcourse.setText("");

        }
        else
        {
            Toast.makeText(this, "Test Deleted Successfully",Toast.LENGTH_SHORT).show();
            Intent i1 = new Intent(DeleteTest.this, DashBoard.class);
            startActivity(i1);
            finish();
        }
    }




}
