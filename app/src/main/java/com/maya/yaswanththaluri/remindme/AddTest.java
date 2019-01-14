package com.maya.yaswanththaluri.remindme;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private SimpleDateFormat sdf;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;

    private FirebaseStorage mFirebaseStorage;

    private Button saveButton;
    private EditText course;
    private EditText catype;
    private EditText syllabus;

    private String date1;

    private String choice = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);


        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        Button b = (Button) findViewById(R.id.datepicker);
        final Calendar myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TextView t = (TextView) findViewById(R.id.date);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                sdf = new SimpleDateFormat(myFormat, Locale.US);
                t.setText(sdf.format(myCalendar.getTime()));
                date1 = sdf.format(myCalendar.getTime()).toString();
            }

        };

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddTest.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("messages");


        saveButton = (Button) findViewById(R.id.save);

        course = (EditText) findViewById(R.id.coursecode);
        catype = (EditText) findViewById(R.id.examtype);
        syllabus = (EditText) findViewById(R.id.syllabus);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.groups, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(course.getText().toString().equals("")
                        || catype.getText().toString().equals("") || syllabus.getText().equals(""))) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddTest.this);
                    alertDialog.setTitle("Confirm Changes?");
                    alertDialog.setMessage("Are you sure you want save Exam?");

                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            CaMessage message = new CaMessage(course.getText().toString().toUpperCase(), catype.getText().toString().toUpperCase(), syllabus.getText().toString(), date1, choice);
                            mMessageDatabaseReference.push().setValue(message);
                            course.setText("");
                            catype.setText("");
                            syllabus.setText("");
                            TextView t = (TextView) findViewById(R.id.date);
                            t.setText("YYYY-MM-DD");
                            Toast.makeText(AddTest.this, "Details Saved Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AddTest.this, DashBoard.class);
                            startActivity(i);
                        }
                    });

                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event

                            dialog.cancel();
                        }
                    });
                    alertDialog.show();

                } else {
                    Toast.makeText(AddTest.this, "Please fill all Fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button b2 = (Button)findViewById(R.id.reset);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course.setText("");
                catype.setText("");
                syllabus.setText("");
                TextView t = (TextView) findViewById(R.id.date);
                t.setText("YYYY-MM-DD");
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        choice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}