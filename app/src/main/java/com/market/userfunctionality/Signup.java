package com.market.userfunctionality;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

public class Signup extends AppCompatActivity {

    UploadTask  uploadTask;
    EditText name,email,phone,address;
    AppCompatRadioButton radioMale,radioFemale;

    String gender="";
     DatabaseReference dbRef;
    FirebaseDatabase fbdb;
    CircularImageView circularImageView;
    FloatingActionButton getImageFromPhone;
    MaterialRippleLayout sign_Up;
   FirebaseAuth fbAuth;
  //  DatabaseReference myRef;
    StorageReference storageProfref;
    private static final int PICK_IMAGE=1;

    String fullname,emailAddress,phoneNumber,uid,addresstxt;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        initView();


        storageProfref= FirebaseStorage.getInstance().getReference();

        dbRef =FirebaseDatabase.getInstance().getReference("customer");


        fbAuth=FirebaseAuth.getInstance();

        sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            getProviderData();




                //phoneauthCredentials();


            }

            private void getProviderData() {

                  fullname=name.getText().toString();
                 emailAddress=email.getText().toString();














                phoneNumber=phone.getText().toString();
                if(phoneNumber.isEmpty() || phoneNumber.length() < 10){
                    phone.setError("Enter a valid mobile");
                    phone.requestFocus();


                }


              addresstxt=address.getText().toString();

                if (radioMale.isChecked())
                    gender="male";
                if (radioFemale.isChecked())
                    gender="female";
                  uid="";

                  //   image=imageUri.toString();


           //   String imegeuri=image;

                uploadFile();

//
//                if(fullname.isEmpty() || emailAddress.isEmpty() || phoneNumber.isEmpty() || addresstxt.isEmpty()){
//                    Toast.makeText(Signup.this, "make sure all the data is put", Toast.LENGTH_SHORT).show();
//
//                }


//                else {
//            progressDialog.show();
//                    varifyPhoneNumber();

//                }
            }
//
//            private void varifyPhoneNumber() {
//
//                Signup_Model information=new Signup_Model(fullname,emailAddress,phoneNumber,gender,addresstxt,image,uid );
//                SharedPreferences sharedPreferences = getApplication().getSharedPreferences("mainActivity",MODE_PRIVATE);
//                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
//
//
//                Gson gson = new Gson();
//                 String json = gson.toJson(information);
//                prefsEditor.putString("information", json);
//                prefsEditor.commit();
//
//
//                getUserNumber();
//       Intent intent=new Intent(getApplicationContext(),Verification.class);
//                intent.putExtra("mobile", phoneNumber);
//                startActivity(intent);
//            }

            private void getUserNumber() {


            }


        });


getImageFromPhone.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent gallery=new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

         startActivityForResult(gallery,PICK_IMAGE);

    }
});

    }

    private void uploadFile() {
//
        if (imageUri != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

//            //getting the storage reference
            final StorageReference sRef = storageProfref.child("profilepic/"  + System.currentTimeMillis());

            //adding the file to reference
//             sRef.putFile(imageUri)

    uploadTask = sRef.putFile(imageUri);


    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
        @Override
        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
            if (!task.isSuccessful()) {
//                    throw task.getException();
//                loadingDialog.dismiss();
//                Toast.makeText(ClientRegistrationActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
            }
            // Continue with the task to get the download URL
            return sRef.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()) {
                final Uri downloadUri = task.getResult();
                try {

               String image=downloadUri.toString();
//                    add to share dat

                    Signup_Model information=new Signup_Model(fullname,emailAddress,phoneNumber,gender,addresstxt,image,uid );
                    SharedPreferences sharedPreferences = getApplication().getSharedPreferences("mainActivity",MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();


                    Gson gson = new Gson();
                    String json = gson.toJson(information);
                    prefsEditor.putString("information", json);
                    prefsEditor.commit();



                    Intent intent=new Intent(getApplicationContext(),Verification.class);
                    intent.putExtra("mobile", phoneNumber);
                    startActivity(intent);











                } catch (Exception e) {
//                    loadingDialog.dismiss();
//                        Toast.makeText(ClientRegistrationActivity.this, "line 162" + e, Toast.LENGTH_SHORT).show();
                }
            } else {
//                loadingDialog.dismiss();
//                Toast.makeText(ClientRegistrationActivity.this, "failed", Toast.LENGTH_SHORT).show();
                // Handle failures
                // ...
            }
        }
    });
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //dismissing the progress dialog
//                            progressDialog.dismiss();
//
//                            //displaying success toast
//                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
//
//                            //creating the upload object to store uploaded image details
//                            image=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                            //adding an upload to firebase database
//                          //  image = dbRef.push().getKey();
//                           // dbRef.child(image).setValue(I);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            progressDialog.dismiss();
//                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            //displaying the upload progress
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
//                        }
//                    });
//        } else {
//            //display an error if no file is selected
        }
//
    }


    //03479064562
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            if (data != null) {
                imageUri=data.getData();
            }
            try{

                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

                circularImageView.setImageBitmap(bitmap);

//                circularImageView.setImageURI(imageUri);



            }catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }

    private void initView() {
        name=findViewById(R.id.edit_name);


        email=findViewById(R.id.edit_email);

        phone=findViewById(R.id.edit_phone);


        address=findViewById(R.id.edit_address);

        radioMale=findViewById(R.id.et_male);
        radioFemale=findViewById(R.id.et_female);

        getImageFromPhone=findViewById(R.id.get_image_from_phone);
        circularImageView=findViewById(R.id.circular_image_view);
        sign_Up=findViewById(R.id.bt_create_account);

    }


    private String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(cr.getType(uri));
    }
}
