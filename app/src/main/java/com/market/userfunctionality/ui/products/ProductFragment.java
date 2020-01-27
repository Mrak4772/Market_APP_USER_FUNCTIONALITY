package com.market.userfunctionality.ui.products;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.market.userfunctionality.DataModel;
import com.market.userfunctionality.MainActivity;
import com.market.userfunctionality.ProductAdapter;
import com.market.userfunctionality.R;
import com.market.userfunctionality.UserActivity;
import com.market.userfunctionality.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Objects;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
     private ArrayList<DataModel> listData = new ArrayList<>();
    private ProductAdapter adapter_product;
  private   Toolbar toolbar;
   private DatabaseReference dbref;
  private String catId;
  private String cat_name;
  public static View.OnClickListener myClickListener;
    public ProductFragment(String id) {
        this.catId = id;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.product_fragment, container, false);

myClickListener = new MyClickListener(getContext());
        recyclerView=root.findViewById(R.id.recycler_product);

        toolbar = root.findViewById(R.id.toolbar_product1);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar());
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);


        }
//        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));

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


        dbref= FirebaseDatabase.getInstance().getReference("products");

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity() ,2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));


        loadProductList();



        return root;
    }

    private void loadProductList() {

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData.clear();

                 //Intent data = getActivity(). getIntent();
                if(!catId.isEmpty()){
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.child("cat_id").getValue(String.class).equals(catId)){
                            String product_name  = dataSnapshot1.child("product_name").getValue(String.class);
                            String product_id=dataSnapshot1.child("product_id").getValue(String.class);
                            String product_image_uri=dataSnapshot1.child("product_image_uri").getValue(String.class);
                            String product_price=dataSnapshot1.child("product_price").getValue(String.class);
                            String product_sku=dataSnapshot1.child("product_sku").getValue(String.class);
                            String product_brand=dataSnapshot1.child("product_brand").getValue(String.class);
                            String product_des=dataSnapshot1.child("product_des").getValue(String.class);
                            cat_name=dataSnapshot1.child("cat_name").getValue(String.class);
                            String cat_id=dataSnapshot1.getKey();
                            listData.add(new DataModel(product_id,product_name,product_image_uri,product_price,product_sku,product_brand,product_des,cat_name,cat_id));
                        }
                    }


                }else{
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String product_name = dataSnapshot1.child("product_name").getValue(String.class);
                        String product_id=dataSnapshot1.child("product_id").getValue(String.class);
                        String product_image_uri=dataSnapshot1.child("product_image_uri").getValue(String.class);
                        String product_price=dataSnapshot1.child("product_price").getValue(String.class);
                        String product_sku=dataSnapshot1.child("product_sku").getValue(String.class);
                        String product_brand=dataSnapshot1.child("product_brand").getValue(String.class);
                        String product_des=dataSnapshot1.child("product_des").getValue(String.class);
                          cat_name=dataSnapshot1.child("cat_name").getValue(String.class);
                        String cat_id=dataSnapshot1.getKey();
                        listData.add(new DataModel(product_id,product_name,product_image_uri,product_price,product_sku,product_brand,product_des,cat_name,cat_id));
                    }


                }
                adapter_product=new ProductAdapter(getActivity(),listData);

                recyclerView.setAdapter(adapter_product);




                RecycleClick.addTo(recyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int i, View view) {

                                   getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frame_container,
                                            new ProductDetailFragment(listData.get(i).getCat_id(),listData.get(i).getProduct_image_uri(),listData.get(i).getProduct_des(),listData.get(i).getProduct_name(),listData.get(i).getProduct_sku(),listData.get(i).getProduct_price()
                                                    ,listData.get(i).getProduct_id() ))
                                     .commit();

                    }
                });

                toolbar.setTitle(cat_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent=new Intent(getActivity(), UserActivity.class);

            startActivity(intent);

            // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

  public class MyClickListener implements View.OnClickListener{
Context context;
      public MyClickListener(Context context) {
          this.context = context;
      }

      @Override
      public void onClick(View v) {
          Toast.makeText(context, "item Click", Toast.LENGTH_SHORT).show();
      }
  }


    }

