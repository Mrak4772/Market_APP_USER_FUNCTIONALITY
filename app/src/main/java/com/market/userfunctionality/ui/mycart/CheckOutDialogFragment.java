package com.market.userfunctionality.ui.mycart;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.market.userfunctionality.DataModel;
import com.market.userfunctionality.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CheckOutDialogFragment extends BottomSheetDialogFragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        resetMap= (ResetMap) context;
    }

    private ArrayList<DataModel> listData = new ArrayList<>();
     String product_price;
    Toolbar toolbar;
      private DatabaseReference dbref;
    CheckoutAdapter adapter ;
    MyCartFragment.ShowTotal showTotal;
     private RecyclerView recyclerView;
     Double lat,longitud;
    TextView shippingAddress;
    int PERMISSION_ALL = 1;
    ResetMap resetMap;

    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
            // android.Manifest.permission.READ_SMS,
            // android.Manifest.permission.CAMERA
    };


    //  TextView shippingAddress;

    TextView totalCart;
   // private FusedLocationProviderClient fusedLocationClient;

  //  private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;



    //private TextView currentAddTv;

    private Location currentLocation;

    private LocationCallback locationCallback;

      @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_check_out, container, false);
          if (!hasPermissions(getActivity(), PERMISSIONS)) {
              ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
          }


        initToolbar(root);


        loadProductList(root);


         Bundle bundle = new Bundle();
 String tv=getArguments().getString("list_data");


           totalCart = root.findViewById(R.id.totla_price_checkout);


             totalCart.setText( tv );
              setHasOptionsMenu(true);

     FusedLocationProviderClient  fusedLocationClient= LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

            shippingAddress =root.findViewById(R.id.shipping_current_addresss);

          final ImageView getCureentclick=root.findViewById(R.id.get_current_address_click);
          getCureentclick.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  shippingAddress.setText(getCompleteAddress(lat,longitud));
              }
          });

          //    shippingAddress.setText(getCompleteAddress(la));


          fusedLocationClient.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
                  @Override
                  public void onSuccess(Location location) {
                 if (location!=null)
                 {

                     lat=location.getLatitude();
                     longitud=location.getLongitude();

                                 shippingAddress.setText(getCompleteAddress(lat,longitud));



                  }


                  }
              });

          final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
          DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("customer").child(currentUserID);
mRef.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    String      personAddress=dataSnapshot.child("address").getValue(String.class);
     TextView   personAddress_firebaseaddress=root.findViewById(R.id.firebass_address);
        personAddress_firebaseaddress.setText(personAddress);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});



        return root;
    }

    private void initToolbar(View root) {
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar_product1);
         toolbar.setNavigationIcon(R.drawable.ic_menu);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar());
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("CheckOut");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetMap.hashMapHandle();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, new MyCartFragment())
                        .commit();
             }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu) ;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
         } else {
            Toast.makeText(getActivity(), "button clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadProductList(final View root) {
        final SharedPreferences sharedPref = getActivity().getSharedPreferences("cartItems", Context.MODE_PRIVATE);
        final Map<String,?> keys = sharedPref.getAll();

        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for(Map.Entry<String,?> entry : keys.entrySet()){

                        String id = dataSnapshot1.getKey();
                        String id2 = entry.getKey();
                        if(dataSnapshot1.child("product_id").getValue(String.class).equals(entry.getKey())){
                            String product_name  = dataSnapshot1.child("product_name").getValue(String.class);
                            String product_id=dataSnapshot1.child("product_id").getValue(String.class);
                            String product_image_uri=dataSnapshot1.child("product_image_uri").getValue(String.class);
                            product_price=dataSnapshot1.child("product_price").getValue(String.class);
                            String product_brand=dataSnapshot1.child("product_brand").getValue(String.class);
                            String product_des=dataSnapshot1.child("product_des").getValue(String.class);
                            listData.add(new DataModel(product_id,product_name,product_image_uri,product_price,product_brand,product_des));
                        }
                    }
                }

                recyclerView = root.findViewById(R.id.recycler_checkout_cart);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




                adapter=new CheckoutAdapter(getActivity(),listData);
              recyclerView.setAdapter(adapter);

                 //  Toast.makeText(getActivity(), adapter.getItemCount(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });



    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private String getCompleteAddress(double latitude,double longitude){


          String adress="";
        Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address>addresses=geocoder.getFromLocation(latitude,longitude,1);

            if (adress!=null){

                Address returnAddress=addresses.get(0);
                StringBuilder stringBuilder=new StringBuilder("");
                for (int i=0;i<=returnAddress.getMaxAddressLineIndex();i++){

                    stringBuilder.append(returnAddress.getAddressLine(i)).append("\n");

                }

                adress=stringBuilder.toString();

            }else {
                Toast.makeText(getActivity(), "address not found" , Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return adress;
    }

    public interface ResetMap{
        void hashMapHandle();
    }



}



