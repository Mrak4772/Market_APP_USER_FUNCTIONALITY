package com.market.userfunctionality.ui.profile;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.market.userfunctionality.R;
import com.market.userfunctionality.Signup_Model;
import com.market.userfunctionality.UserActivity;
import com.market.userfunctionality.ui.home.HomeFragment;
import com.market.userfunctionality.ui.products.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

public class ProfileFragement extends Fragment {

TextView person_name,person_Address,person_phone,person_Gender,person_Email;
CircularImageView circularImageView;
FirebaseDatabase fbdb;
String currentUser;
DatabaseReference dbref_profile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        initToolbar(root);
        initView(root);

        getData();

          return root;
    }

    private void initToolbar( View root) {
        Toolbar toolbar =root.findViewById(R.id.toolbar_profile);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar());

        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, new HomeFragment())
                        .commit();


            }
        });
        toolbar.setTitle("Profile");
        Tools.setSystemBarColor(getActivity());

    }


    private void getData() {
 currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
         dbref_profile=FirebaseDatabase.getInstance().getReference("customer");
        dbref_profile.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String personName=dataSnapshot.child("fullName").getValue(String.class);
                String personAddress=dataSnapshot.child("address").getValue(String.class);
                String personPhone=dataSnapshot.child("phoneNumber").getValue(String.class);
                String personGender=dataSnapshot.child("gender").getValue(String.class);
                String personEmail=dataSnapshot.child("email").getValue(String.class);

                String personImage=dataSnapshot.child("imageUrl").getValue(String.class);
                person_name.setText(personName);
                person_phone.setText(personPhone);
                person_Address.setText(personAddress);
                person_Gender.setText(personGender);
                person_Email.setText(personEmail);

                if (personImage!=null)
              Glide.with(getActivity()).load(personImage).into(circularImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView(View root) {

        person_name=root.findViewById(R.id.person_name);
        person_Address=root.findViewById(R.id.person_address);
        person_phone=root.findViewById(R.id.person_phone);
        person_Gender=root.findViewById(R.id.person_gender);
        person_Email=root.findViewById(R.id.person_email);
        circularImageView=root.findViewById(R.id.person_image_uri);

    }


}