package com.maya.yaswanththaluri.remindme;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email, password;
    private Button button;
    private TextView signup;
    private ProgressDialog dialog;
    private ProgressDialog dialog2;
    private TextView rst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText)findViewById(R.id.mail);
        password = (EditText)findViewById(R.id.password);
        button = (Button)findViewById(R.id.login);
        signup = (TextView)findViewById(R.id.signup);
        rst = (TextView)findViewById(R.id.forgotpswd);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Validating Details....Please wait!");
        dialog.setCanceledOnTouchOutside(false);

        dialog2 = new ProgressDialog(this);
        dialog2.setMessage("Checking for User...Please wait!");
        dialog2.setCanceledOnTouchOutside(false);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail;
                String pswd;

                dialog.show();

                mail = email.getText().toString();
                pswd = password.getText().toString();

                if (!(mail.equals("")&&pswd.equals("")))
                {
                    firebaseAuth.signInWithEmailAndPassword(mail, pswd)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(Login.this, "Invalid Details", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user.isEmailVerified())
                                        {
                                            dialog.dismiss();
                                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent i =new Intent(Login.this, DashBoard.class);
                                            startActivity(i);
                                        }
                                        else
                                        {
                                            dialog.dismiss();
                                            final AlertDialog.Builder alert= new AlertDialog.Builder(Login.this);
                                            alert.setTitle("Verification Error");
                                            alert.setCancelable(false);
                                            alert.setMessage("Email not Verified Yet. Do you want to send the Verificarion Link again");
                                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    user.sendEmailVerification()
                                                            .addOnCompleteListener(Login.this, new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (!task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(Login.this, "Account created successfully but failed to sent " +
                                                                                        "Verification mail"
                                                                                , Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(Login.this, "Account created successfully and verification sent to MailId"
                                                                                , Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            });

                                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });


                                            alert.show();
                                        }
                                    }
                                }
                            });
                }
                else
                {
                    dialog.dismiss();
                    Toast.makeText(Login.this, "Please Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        rst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder build = new AlertDialog.Builder(Login.this);
                View vi = getLayoutInflater().inflate(R.layout.resetpswd, null);
                final EditText mail = (EditText)vi.findViewById(R.id.resetmailid);
                Button b1 = (Button)vi.findViewById(R.id.rstcancel);
                Button b2 = (Button)vi.findViewById(R.id.submitrst);

                build.setView(vi);
                final AlertDialog dia = build.create();
                dia.setCanceledOnTouchOutside(false);
                dia.show();

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog2.show();

                        String s = mail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(s)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            dialog2.dismiss();
                                            Toast.makeText(Login.this, "Reset Mail has been sent to your registered Mail ID", Toast.LENGTH_LONG).show();
                                            dia.dismiss();
                                        }
                                        else
                                        {
                                            dialog2.dismiss();
                                            Toast.makeText(Login.this, "No User With this mail id", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.dismiss();
                    }
                });
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}
