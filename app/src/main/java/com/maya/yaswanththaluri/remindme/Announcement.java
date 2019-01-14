package com.maya.yaswanththaluri.remindme;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Announcement extends Fragment
{
    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ListView list;

    private ProgressDialog progressDialog;

    private NotifyAdapter mNotifyAdapter;

    private ImageView nores;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.announcement, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("notifications");

        list = (ListView)view.findViewById(R.id.annuncementList);

        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Retreiving Data");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        nores = (ImageView)view.findViewById(R.id.noresults);

        List<Notificaation> notify = new ArrayList<>();
        mNotifyAdapter = new NotifyAdapter(getContext(), R.layout.notifyitem, notify);

        list.setAdapter(mNotifyAdapter);

        FloatingActionButton f = (FloatingActionButton)view.findViewById(R.id.editann);

        if (user.getUid().equals("ZSeY5tulBOW2rr4BeYV6z9yTkkX2")  ||  user.getUid().equals("SzDmmCqWC6crBM8eYTw711vUt4o1"))
        {
            f.setVisibility(View.VISIBLE);
        }
        else
        {
            f.setVisibility(View.INVISIBLE);
        }

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());
                b.setTitle("Add/ Delete Announcement");

                b.setMessage("Choose the option you want");

                b.setPositiveButton("Delete Announcement", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(view.getContext(), DeleteNotification.class);
                        startActivity(i);
                    }
                });

                b.setNegativeButton("Add Announcement", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(view.getContext(), AddNotification.class);
                        startActivity(i);
                    }
                });

                b.show();
            }
        });
                return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        attachDataBaseReadListener();
    }

    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            auth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mNotifyAdapter.clear();
    }

    private void attachDataBaseReadListener() {
        if (mChildEventListener == null) {
            Query myTopPostsQuery = mReference.orderByChild("date");
            mChildEventListener = myTopPostsQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Notificaation notify = dataSnapshot.getValue(Notificaation.class);
                        mNotifyAdapter.add(notify);

//                    ListView l = (ListView) view.findViewById(R.id.listview);
//                    l.setVisibility(View.VISIBLE);
//
//                    progress.setVisibility(View.INVISIBLE);

                    if (list.getAdapter().getCount() == 0) {
                        nores.setVisibility(View.VISIBLE);

                    } else {
                        nores.setVisibility(View.INVISIBLE);
                    }
                    progressDialog.dismiss();
                }


                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

//                    progress.setVisibility(View.INVISIBLE);
                    progressDialog.dismiss();
                }
            });

        }
    }
    //
    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
