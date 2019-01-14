package com.maya.yaswanththaluri.remindme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import com.bumptech.glide.Glide;
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

public class Schedule extends Fragment
{



    private ChildEventListener mChildEventListener;
    private ListView mMessageListView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private CaAdapter mMessageAdapter;
    private FirebaseAuth mFirebseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public View view;
    private ImageView progress;
    private ImageView nores;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.schedule, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebseAuth.getCurrentUser();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mMessageListView = (ListView) view.findViewById(R.id.listview);

        progress = (ImageView)view.findViewById(R.id.progressgif);
        Glide.with(this).load(R.drawable.pacman).into(progress);
        progress.setVisibility(View.VISIBLE);

        List<CaMessage> caMessages = new ArrayList<>();
        mMessageAdapter = new CaAdapter(getContext(), R.layout.listitem, caMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        FloatingActionButton f = (FloatingActionButton)view.findViewById(R.id.addodel);

        if(user.getUid().equals("ZSeY5tulBOW2rr4BeYV6z9yTkkX2") || user.getUid().equals("SzDmmCqWC6crBM8eYTw711vUt4o1"))
        {
            f.setVisibility(View.VISIBLE);
        }
        else
        {
            f.setVisibility(View.INVISIBLE);
        }

        nores = (ImageView)view.findViewById(R.id.noresultssch);


        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Edit Options");

                builder.setMessage("Select the Option");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i =new Intent(view.getContext(), DeleteTest.class);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(view.getContext(), AddTest.class);
                        startActivity(i);
                    }
                });

                builder.show();
            }
        });

        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        mFirebseAuth = FirebaseAuth.getInstance();
        attachDataBaseReadListener();
    }

    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        mMessageAdapter.clear();
    }

    private void attachDataBaseReadListener() {
        if (mChildEventListener == null) {
            Query myTopPostsQuery = mMessageDatabaseReference.orderByChild("date");
            mChildEventListener = myTopPostsQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    CaMessage friendlyMessage = dataSnapshot.getValue(CaMessage.class);
                    String datein = friendlyMessage.getDate();
                    Date strDate = null;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        strDate = sdf.parse(datein);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (new Date().before(strDate)) {
                        mMessageAdapter.add(friendlyMessage);
                    }
                    ListView l = (ListView) view.findViewById(R.id.listview);
                    l.setVisibility(View.VISIBLE);

                    progress.setVisibility(View.INVISIBLE);

                    if (l.getAdapter().getCount() == 0) {
                        nores.setVisibility(View.VISIBLE);
                    } else {
                        nores.setVisibility(View.INVISIBLE);
                    }
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

                    progress.setVisibility(View.INVISIBLE);
                }
            });

        }
    }
//
    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessageDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
