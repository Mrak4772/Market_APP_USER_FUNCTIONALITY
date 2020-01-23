package com.market.userfunctionality;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.market.userfunctionality.ui.home.HomeFragment;
import com.market.userfunctionality.ui.mycart.CartAdapter;
import com.market.userfunctionality.ui.mycart.CheckOutDialogFragment;
import com.market.userfunctionality.ui.mycart.CheckoutAdapter;
import com.market.userfunctionality.ui.mycart.MyCartFragment;
import com.market.userfunctionality.ui.products.ProductDetailFragment;
import com.market.userfunctionality.ui.profile.ProfileFragement;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class UserActivity extends AppCompatActivity implements ProductDetailFragment.BackPressed , CheckoutAdapter.Data1, CartAdapter.MapSetter, CheckOutDialogFragment.ResetMap {
    HashMap<Integer, String> quantityMap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
      @Override
    protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_user);


     quantityMap = new HashMap<Integer, String>();


     BottomNavigationView bottomNavigationView=findViewById(R.id.navigation_bottom);



     getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

     getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new HomeFragment()).commit();
       // viewPager=findViewById(R.id.slider);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

          bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                  Fragment selectFragment=null;
                  switch (menuItem.getItemId()){

                      case R.id.navigation_shop:
                          selectFragment=new HomeFragment();
                          break;

                      case R.id.navigation_cart:
                          selectFragment=new MyCartFragment();

                          break;

                      case R.id.navigation_profile:
                          selectFragment=new ProfileFragement();
                   }

                  getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,selectFragment).commit();
                  return true;
              }
          });


    }

    @Override
    public void backPressHandle() {
        onBackPressed();

    }


    @Override
    public void data(TextView a, int position) {
        if(quantityMap.containsKey(position)){
        a.setText(quantityMap.get(position).toString());
         }

         else{
             a.setText("1");

            }

    }


    @Override
    public void setMap(int position, int count) {
        quantityMap.put(position,String.valueOf(count));
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
      quantityMap.clear();


    }


    @Override
    public void hashMapHandle() {
        quantityMap.clear();
    }
}

