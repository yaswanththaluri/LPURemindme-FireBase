package com.maya.yaswanththaluri.remindme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    private EditText mail, password, cnf, name;
    private Button create;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();


        mail = (EditText)findViewById(R.id.mailup);
        password = (EditText)findViewById(R.id.passwordup);
        cnf = (EditText)findViewById(R.id.cnfup);
        name = (EditText)findViewById(R.id.nameup);

        create = (Button)findViewById(R.id.upsignup);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Account...Please Wait!");
        dialog.setCanceledOnTouchOutside(false);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();


                final String email, pswd, cnfpswd, na;

                email = mail.getText().toString();
                pswd = password.getText().toString();
                cnfpswd = cnf.getText().toString();
                na = name.getText().toString();


                if(pswd.equals(cnfpswd)&&!email.equals("")&&!pswd.equals(""))
                {
                    firebaseAuth.createUserWithEmailAndPassword(email, pswd)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(!task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(SignUp.this, "Failed in creating account"
                                                , Toast.LENGTH_SHORT).show();
                                    }

                                    else
                                    {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(na).build();
                                        user.updateProfile(update);

                                        user.sendEmailVerification()
                                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful())
                                                        {
                                                            dialog.dismiss();
                                                            Toast.makeText(SignUp.this, "Account created successfully but failed to sent " +
                                                                            "Verification mail"
                                                                    , Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {
                                                            dialog.dismiss();
                                                            Toast.makeText(SignUp.this, "Account created successfully and verification sent to MailId"
                                                                    , Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                        dialog.dismiss();
                                        Intent i = new Intent(SignUp.this, Login.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(SignUp.this, "Please fill all details correctly"
                            , Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
