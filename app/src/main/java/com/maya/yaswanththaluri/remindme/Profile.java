package com.maya.yaswanththaluri.remindme;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment
{

    private View view;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseStorage storage;
    private StorageReference mStorageReference;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private ImageView i;
    private Context context;
    private Uri t;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        storage = FirebaseStorage.getInstance();
        mStorageReference = storage.getReference();

        String name="",mail="";
        Uri image;

        i =(ImageView)view.findViewById(R.id.profilepic);

        if(user!=null)
        {
            name = user.getDisplayName();
            mail = user.getEmail();
            if(user.getPhotoUrl()==null)
            {
                Glide.with(this).applyDefaultRequestOptions(RequestOptions.circleCropTransform()).load(R.drawable.userprofile).into(i);
            }
            else
            {
                image = user.getPhotoUrl();
                Log.i("urlimg",image.toString());
                Glide.with(this).applyDefaultRequestOptions(RequestOptions.circleCropTransform()).load(image).into(i);
            }
        }



        TextView t =(TextView)view.findViewById(R.id.profilename);
        t.setText(name);
        TextView t2 = (TextView)view.findViewById(R.id.profilemail);
        t2.setText(mail);


        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());

                dialog.setTitle("Confirmation");

                dialog.setMessage("Are You sure you want to change the profile pic");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });







        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();

                if (user == null)
                {
                    startActivity(new Intent(getContext(), Login.class));
                }
            }
        };

        Button b = (Button)view.findViewById(R.id.sinou);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), filePath);

                //Setting image to ImageView
//                i.setImageBitmap(bitmap);
                Glide.with(this).applyDefaultRequestOptions(RequestOptions.circleCropTransform()).load(bitmap).into(i);
                select();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void select()
    {
        final ProgressDialog pd = new ProgressDialog(view.getContext());
        pd.setMessage("Uploading.....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        if(filePath != null) {
            pd.show();

            FirebaseUser user = auth.getCurrentUser();

            final StorageReference childRef = mStorageReference.child("image.jpg").child(user.getUid());

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(view.getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri download = uri;
                            setUri(download);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(view.getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(view.getContext(), "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    public void setUri(Uri download)
    {
        FirebaseUser user = auth.getCurrentUser();

        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                .setPhotoUri(download).build();

        user.updateProfile(profile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(view.getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(view.getContext(), "Failed saving path", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
