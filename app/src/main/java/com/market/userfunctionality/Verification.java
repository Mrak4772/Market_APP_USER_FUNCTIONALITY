package com.market.userfunctionality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {


    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

     //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);



        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);

        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);


        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });


    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        //Getting the code sent by SMS
        String code = phoneAuthCredential.getSmsCode();

        //sometime the code is not detected automatically
        //in this case the code will be null
        //so user has to manually enter the code
        if (code != null) {
            editTextCode.setText(code);
            //verifying the code
            verifyVerificationCode(code);

        }
    }
    @Override
    public void onVerificationFailed(FirebaseException e) {

        Toast.makeText(Verification.this, e.getMessage(), Toast.LENGTH_LONG).show();

    }
    @Override
    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        super.onCodeSent(s, forceResendingToken);

        //storing the verification id that is sent to the user
        mVerificationId = s;
    }
};




    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }




    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Verification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if(!getIntent().hasExtra("login")){
                            SharedPreferences sharedPreferences = getApplication().getSharedPreferences("mainActivity",MODE_PRIVATE);
                             String json = sharedPreferences.getString("information", "");
                            Gson gson = new Gson();
                            Signup_Model obj = gson.fromJson(json, Signup_Model.class);
                            DatabaseReference users = FirebaseDatabase.getInstance().getReference("customer");
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(obj);
                         //  users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageUrl").setValue(obj.imageUrl);
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("uID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("phoneNumber").setValue(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

//923469345214
                                //verification successful we will start the profile activity
                            Intent intent = new Intent(Verification.this, UserActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(Verification.this, UserActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                     } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
}
