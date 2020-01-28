package com.market.userfunctionality.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.market.userfunctionality.CustomAdapter;
import com.market.userfunctionality.CustomDialogCheck;
import com.market.userfunctionality.DataModel;
import com.market.userfunctionality.ProductAdapter;
import com.market.userfunctionality.R;
import com.market.userfunctionality.SLiderModel;
import com.market.userfunctionality.SlidingImage_Adapter;
import com.market.userfunctionality.ui.products.ProductDetailFragment;
import com.market.userfunctionality.ui.products.ProductFragment;
import com.market.userfunctionality.ui.products.SpacingItemDecoration;
import com.market.userfunctionality.ui.products.Tools;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.rey.material.app.SimpleDialog;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class HomeFragment extends Fragment {
    String imgeUri;
    DatabaseReference databaseReference;
    private ArrayList<DataModel> listData_random = new ArrayList<>();
    private ProductAdapter adapter_product;
    String product_name;
    private DatabaseReference dbref;
      // LinearLayout layout_dots;
//textviews
      TextView searchbtn, searchproduct;

    MaterialSearchView materialSearchView;
    ViewPager viewPager;
    // ViewFlipper viewFlipper;
    RelativeLayout viewpagerrelative;
    //  private RecyclerView rv;
    private CustomAdapter adapter1;

    private RecyclerView horizontal_recycler_view;
    private ArrayList<DataModel> listData;
    //     private List<>
    RecyclerView recycler_products;
  ArrayList<SLiderModel>Slide_list_images;
    WormDotsIndicator dotsIndicator;

    // ading search view
    //   ImageButton btn_search;

    //CardView btn_search;
    LinearLayout linearLayout_search;


    // CardView linearLayout_search;

    //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        final View root=inflater.inflate(R.layout.fragment_home,container,false);
        viewpagerrelative= root.findViewById(R.id.relativeLayout_forviewpager);
      //  layout_dots=root.findViewById(R.id.layout_dots);
        viewPager=root.findViewById(R.id.pager);
    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
 //       getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT );

        //    ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));


        linearLayout_search = root.findViewById(R.id.linear_search_second);
        //     btn_search=root.findViewById(R.id.btn_search);

        searchbtn = root.findViewById(R.id.btn_search);
        searchproduct = root.findViewById(R.id.et_search);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewPager.setClipToOutline(true);
        }
        recycler_products=root.findViewById(R.id.recycler_product_random);


        dbref= FirebaseDatabase.getInstance().getReference("products");

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity() ,2);

       recycler_products.setLayoutManager(gridLayoutManager);
        recycler_products.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));


        loadProductList();


        horizontal_recycler_view=root.findViewById(R.id.recycle_view_circle);
        horizontal_recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        horizontal_recycler_view.setLayoutManager(linearLayoutManager);
        final DatabaseReference dbref= FirebaseDatabase.getInstance().getReference("categories");

        listData =new ArrayList ();
        Slide_list_images=new ArrayList<>();
        loadSliderImages(root);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listData.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String name = dataSnapshot1.child("cat_name").getValue(String.class);
                    String image_uri=dataSnapshot1.child("cat_image_uri").getValue(String.class);
                    String cat_id=dataSnapshot1.getKey();
                    listData.add(new DataModel(name,cat_id,image_uri));

                }
                adapter1=new CustomAdapter(getActivity(),listData );
                horizontal_recycler_view.setAdapter(adapter1);
                RecycleClick.addTo(horizontal_recycler_view).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int i, View view) {
                        String id =  listData.get(i).cat_id;

                        Fragment fragment= new ProductFragment(id);
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, fragment)
                                .commit();


                    }
                });


             }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

    private void loadProductList() {

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData_random.clear();

                //Intent data = getActivity(). getIntent();

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        product_name = dataSnapshot1.child("product_name").getValue(String.class);
                            String product_id=dataSnapshot1.child("product_id").getValue(String.class);
                            String product_image_uri=dataSnapshot1.child("product_image_uri").getValue(String.class);
                            String product_price=dataSnapshot1.child("product_price").getValue(String.class);
                            String product_sku=dataSnapshot1.child("product_sku").getValue(String.class);
                            String product_brand=dataSnapshot1.child("product_brand").getValue(String.class);
                            String product_des=dataSnapshot1.child("product_des").getValue(String.class);
                             String cat_id=dataSnapshot1.getKey();
                            listData_random.add(new DataModel(product_id,product_name,product_image_uri,product_price,product_sku,product_brand,product_des,cat_id));


                        linearLayout_search.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                new SimpleSearchDialogCompat<>(getActivity(), "enter product name here", "search the products",

                                        null, listData_random, new SearchResultListener<DataModel>() {
                                    @Override


                                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, DataModel dataModel, int i) {


                                        getFragmentManager()

                                                .beginTransaction()
                                                .replace(R.id.frame_container,
                                                        new ProductDetailFragment(dataModel.getCat_id(), dataModel.getProduct_image_uri(), dataModel.getProduct_des(), dataModel.getProduct_name(), dataModel.getProduct_sku(), dataModel.getProduct_price()
                                                                , listData.get(i).getProduct_id()))
                                                .commit();
                                        baseSearchDialogCompat.dismiss();


                                        Toast.makeText(getActivity(), "clicked" + dataModel.getProduct_name(), Toast.LENGTH_SHORT).show();

                                    }
                                }).show();

                            }
                        });

                    }

                adapter_product = new ProductAdapter(getActivity(), listData_random);
                recycler_products.setAdapter(adapter_product);





                RecycleClick.addTo(recycler_products).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int i, View view) {

                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container,
                                        new ProductDetailFragment(listData_random.get(i).getCat_id(),listData_random.get(i).getProduct_image_uri(),listData_random.get(i).getProduct_des(),listData_random.get(i).getProduct_name(),listData_random.get(i).getProduct_sku(),listData_random.get(i).getProduct_price()
                                                ,listData_random.get(i).getProduct_id() ))
                                .commit();

                    }
                });

             }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });



    }


    private void loadSliderImages(final View root) {
Slide_list_images.clear();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference dbref=database.getReference("sliderImages");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    imgeUri=dataSnapshot1.child("imageUri").getValue(String.class);
                    Slide_list_images.add(new SLiderModel(imgeUri));

                }

                final SlidingImage_Adapter adapter = new SlidingImage_Adapter(getActivity() ,Slide_list_images,viewpagerrelative ) ;


                viewPager.setAdapter(adapter);
                 viewPager.setCurrentItem(0);


                adapter.setTimer(viewPager,7);

                dotsIndicator= root.findViewById(R.id.spring_dots_indicator);
                dotsIndicator.setViewPager(viewPager);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
        public void onStart() {
            super.onStart();
    }




}

