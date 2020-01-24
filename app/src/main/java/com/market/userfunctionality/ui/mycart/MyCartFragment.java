package com.market.userfunctionality.ui.mycart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.market.userfunctionality.DataModel;
import com.market.userfunctionality.ProductAdapter;
import com.market.userfunctionality.R;
import com.market.userfunctionality.UserActivity;
import com.market.userfunctionality.ui.products.ProductDetailFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyCartFragment extends Fragment implements OnClickListener {

    private ArrayList<DataModel> listData = new ArrayList<>();
    String product_price;
    private CartAdapter adapter ;
    Toolbar toolbar;
    AppCompatButton cart_continue;
    private DatabaseReference dbref;
    TextView totlacart;
     ShowTotal showTotal;
    private int total  = 0;
    CheckOutDialogFragment.ResetMap resetMap;

    //  View.OnClickListener myClickListener;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mycart, container, false);

//        myClickListener =new MyClickListener(getContext());

                        loadProductList(root);
//        }

        resetMap.hashMapHandle();
        cart_continue=root.findViewById(R.id.cart_continue_btn);

        totlacart=root.findViewById(R.id.total_price);
        cart_continue.setOnClickListener(this);


//        totlacart.setText(c);

               return root;
    }
   private void loadProductList(final View root) {
       final SharedPreferences sharedPref = getActivity().getSharedPreferences("cartItems",Context.MODE_PRIVATE);
       final Map<String,?> keys = sharedPref.getAll();

    dbref = FirebaseDatabase.getInstance().getReference();

    dbref.child("products").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listData.clear();
            if(!keys.isEmpty()){
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
                }

            recyclerView = root.findViewById(R.id.cart_items_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            adapter=new CartAdapter(getActivity(),listData,totlacart);

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();


        //    recyclerView.setOnClickListener(myClickListener);

//            RecycleClick.addTo(recyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
//                @Override
//                public void onItemClicked(RecyclerView recyclerView, int i, View view) {
//
//                    Toast.makeText(getContext(), "item click"+i, Toast.LENGTH_SHORT).show();
//
//                }
//            });
//            foreach(List<DataModel> data: listData)
            for (DataModel data: listData) {
               total = total+Integer.parseInt(data.getProduct_price());
            }
            totlacart.setText(String.valueOf(total));

//            showTotal.showPrice(totlacart);







        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }



    });



}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resetMap = (CheckOutDialogFragment.ResetMap) context;

//        showTotal = (ShowTotal) context;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        //  Intent intent=new Intent(context, SecondActivity.Class);

        CheckOutDialogFragment fragment = new CheckOutDialogFragment(); // replace your custom fragment class
        //  Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        // use as per your need
        //    bundle.putParcelable("list_data", totlacart);
        bundle.putString("list_data", String.valueOf(totlacart.getText()));

        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_container,fragment);
        fragmentTransaction.commit();
//
//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_container,
//                        new CheckOutDialogFragment())
//                .commit();

    }


    interface ShowTotal {
        void showPrice(TextView textView);
    }

    private class MyClickListener implements OnClickListener
    {
Context context;
        public MyClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(context, ""+recyclerView.getChildPosition(v), Toast.LENGTH_SHORT).show();
        }
    }


}