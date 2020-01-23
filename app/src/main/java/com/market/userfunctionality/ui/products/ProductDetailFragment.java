package com.market.userfunctionality.ui.products;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
 import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.market.userfunctionality.DataModel;
import com.market.userfunctionality.MainActivity;
import com.market.userfunctionality.ProductAdapter;
import com.market.userfunctionality.R;
import com.market.userfunctionality.UserActivity;
import com.market.userfunctionality.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

 import androidx.recyclerview.widget.RecyclerView;

import static com.market.userfunctionality.ui.products.Tools.toggleArrow;

public class ProductDetailFragment extends Fragment {


    private View parent_view;

    FloatingActionButton add_to_cart;
    String catId,product_image_uri,product_desc,product_name,sku_product,product_price,product_id;
BackPressed backPressed;



    private ImageButton  bt_toggle_description;
    private View   lyt_expand_description;
    private NestedScrollView nested_scroll_view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        backPressed = (BackPressed) context;
    }

    ImageView product_detail_image;
    TextView sku_detail,prod_desc,price_detail;
    public ProductDetailFragment(String cat_id,
                                 String product_image_uri,
                                 String product_des,
                                 String product_name, String product_sku,
                                 String product_price,String product_id) {
      this. catId = cat_id;
        this. product_image_uri = product_image_uri;
        this. product_desc = product_des;
        this. product_name = product_name;
        this. sku_product = product_sku;

        this.product_id=product_id;
        this.product_price=product_price;

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = LayoutInflater.from(getActivity()).inflate (R.layout.product_detail_fragment, container, false);

        parent_view = root.findViewById(R.id.parent_view);

        // calling methods
        initToolbar(root);
        initComponent(root);






       //  prod_desc=root.findViewById(R.id.tv_desc_product_detail);

        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }

        });

        // expand first description
        toggleArrow(bt_toggle_description);
        lyt_expand_description.setVisibility(View.VISIBLE);


        Glide.with(getActivity()).load(product_image_uri).into(product_detail_image);
        price_detail.setText("RS. "+product_price);
//         prodnameDetail.setText(product_name);
         sku_detail.setText("SKU : "+sku_product);
//         String a = product_price;
//         String skiu = sku_product;
         prod_desc.setText(product_desc);

        return root;
    }

    private void initComponent(View root) {

        // expandable
        nested_scroll_view =root. findViewById(R.id.nested_scroll_view);
        bt_toggle_description = root. findViewById(R.id.bt_toggle_description);
        lyt_expand_description = root.findViewById(R.id.lyt_expand_description);
// description
        prod_desc=root.findViewById(R.id.description_details);

        //sku
        sku_detail=root.findViewById(R.id.sku_value);

        // product detatil name

//        prodnameDetail=root.findViewById(R.id.tv_name_product_detail);

        // price detaails
        price_detail=root.findViewById(R.id.tv_price_product_detail);

        // nested scrollview
        nested_scroll_view = root. findViewById(R.id.nested_scroll_view);
// image view

        product_detail_image=root.findViewById(R.id.imageView_product_detail);

// paren view



        // section description
       // bt_toggle_description = root. findViewById(R.id.tv_desc_product_detail);
        lyt_expand_description = root. findViewById(R.id.lyt_expand_description);
        bt_toggle_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSection(view, lyt_expand_description);
            }
        });

        // expand first description
        toggleArrow(bt_toggle_description);
        lyt_expand_description.setVisibility(View.VISIBLE);

         add_to_cart=root. findViewById(R.id.add_to_cart) ;
       add_to_cart .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parent_view, "Add to Cart", Snackbar.LENGTH_SHORT).show();


                Context context = getActivity();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        "cartItems", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(product_id, product_id);
                editor.commit();


            }
        });
    }


    private void initToolbar( View root) {
        Toolbar toolbar =root.findViewById(R.id.detail_toolbar);
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

           //     backPressed.backPressHandle() ;

            }
        });
         toolbar.setTitle(product_name);
          Tools.setSystemBarColor(getActivity());

    }

    public interface BackPressed{
        void backPressHandle();
    }


    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt);
                }
            });
        } else {
            ViewAnimation.collapse(lyt);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

}
