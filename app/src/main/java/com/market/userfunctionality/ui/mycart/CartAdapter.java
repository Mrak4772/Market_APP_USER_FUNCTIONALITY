package com.market.userfunctionality.ui.mycart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.market.userfunctionality.DataModel;
import com.market.userfunctionality.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> implements MyCartFragment.ShowTotal
{
    MapSetter mapSetter;

    public interface MapSetter {
        public void setMap(int position,int count);
    }






    HashMap<Integer, String> quantityMap = new HashMap<Integer, String>();






    int total_price;
    int count=1 ;
    TextView tvTotal;
    Activity activity;
    Context context;




    ArrayList<DataModel> listProduct_cart;
    private   int totalPrice;
    private int price = 0;

    public CartAdapter(Context context , ArrayList<DataModel> listProduct  ) {
        this.context = context;
        this.listProduct_cart = listProduct;



    }

    public CartAdapter(Context context, ArrayList<DataModel> listProduct, TextView totlacart) {
        this.context = context;
        this.listProduct_cart = listProduct;
        this.tvTotal=totlacart;
        mapSetter = (MapSetter) context;
    }

    public CartAdapter() {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //price= (Price) context;
        return new CartAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        total_price= Integer.parseInt(listProduct_cart.get(position).getProduct_price());



        holder.name_product.setText( listProduct_cart.get(position).getProduct_name());
        holder.addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int textViewPrice = Integer.parseInt(tvTotal.getText().toString());
                int itemPrice = Integer.parseInt(listProduct_cart.get(position).getProduct_price());
                int totalPrice = textViewPrice + itemPrice;
                tvTotal.setText(String.valueOf(totalPrice));

                count++;
//            final SharedPreferences sharedPref = context.getSharedPreferences("itemsqty",Context.MODE_PRIVATE);
//            final Map<String,?> keys = sharedPref.getAll();
                holder. quantity.setText(String.valueOf(count));

                mapSetter.setMap(position, count);


            }
        });
        holder.subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count>1){
                    int textViewPrice = Integer.parseInt(tvTotal.getText().toString());
                    int itemPrice = Integer.parseInt(listProduct_cart.get(position).getProduct_price());
                    int totalPrice = textViewPrice - itemPrice;
                    tvTotal.setText(String.valueOf(totalPrice));


                    count--;
                    holder. quantity.setText(String.valueOf(count));

                }}
        });
        //  holder. quantity.setText(String.valueOf(count));




        holder.product_price.setText("Rs. " + total_price);

        int oneTimePrice=(Integer.valueOf(listProduct_cart.get(position).getProduct_price())*count);

        totalPrice=totalPrice+oneTimePrice;





        final int current_position=position;

        Glide.with(context).load(listProduct_cart.get(position).getProduct_image_uri()).into(holder.image_product);

        holder.removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                removePosition(current_position);
            }
        });
    }

    private void removePosition(int current_position) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(
                "cartItems", Context.MODE_PRIVATE).edit();

        String productId = listProduct_cart.get(current_position).getProduct_id();
        sharedPref.remove(productId).commit();
        notifyItemRemoved(current_position);
        listProduct_cart.remove(current_position);


    }

    @Override
    public int getItemCount() {
        return listProduct_cart.size();
    }

    @Override
    public void showPrice(TextView textView) {
        textView.setText(totalPrice);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder   {
        ImageButton addition,subtraction;
        TextView name_product,product_price;
        ImageView image_product;

        TextView quantity;

        ImageView removeCart;


        public MyViewHolder(@NonNull final View itemView) {


            super(itemView);
            removeCart=itemView.findViewById(R.id.removeitemform_cart);
            name_product=itemView.findViewById(R.id.name_of_cartProduct);

            product_price=itemView.findViewById(R.id.price_product_cart);


            image_product=itemView.findViewById(R.id.cart_image_view);

            addition=itemView.findViewById(R.id.addition_buton);
            subtraction= itemView.findViewById(R.id.remove_item_fromcart);

            quantity=itemView.findViewById(R.id.quantiy_kart);






        }


    }



}
