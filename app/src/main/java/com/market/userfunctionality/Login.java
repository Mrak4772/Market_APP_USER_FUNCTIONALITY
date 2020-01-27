package com.market.userfunctionality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText editTextMobile;
ProgressBar progressBar;
RelativeLayout relativeLayout;
    private String mobile;
    TextView registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         progressBar = findViewById(R.id.progress_bar);
         relativeLayout = findViewById(R.id.main_layout_login);
         registration=findViewById(R.id.Sign_up_reg);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try{


            if (user == null) {
            progressBar.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        editTextMobile = findViewById(R.id.editTextPhone);
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 mobile =  editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(Login.this, Verification.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("login","login");
                startActivity(intent);
            }
        });
        }
        else {
             startActivity(new Intent(Login.this, UserActivity.class));
        }

    }catch (Exception e){

        }
        finally {
            progressBar.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            editTextMobile = findViewById(R.id.editTextPhone);
            findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mobile =  editTextMobile.getText().toString().trim();

                    if(mobile.isEmpty() || mobile.length() < 10){
                        editTextMobile.setError("Enter a valid mobile");
                        editTextMobile.requestFocus();
                        return;
                    }

                    Intent intent = new Intent(Login.this, Verification.class);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("login","login");
                    startActivity(intent);
                }
            });
        }


        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Signup.class));
                finish();
            }
        });
    }
}
