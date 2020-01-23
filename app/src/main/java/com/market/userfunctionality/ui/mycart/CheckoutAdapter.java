package com.market.userfunctionality.ui.mycart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.market.userfunctionality.DataModel;
import com.market.userfunctionality.R;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder> implements MyCartFragment.ShowTotal
  {


      Map map;

Data1 data;

     int total_price;
        Context context;


    ArrayList<DataModel> listProduct_cart;
    private   int totalPrice;
    private int price = 0;
    private String qtyA;


      //Price price;
    public CheckoutAdapter(Context context, ArrayList<DataModel> listProduct ) {
        this.context = context;
        this.listProduct_cart = listProduct;
        data = (Data1) context;
     }

      public CheckoutAdapter() {

      }

      @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //price= (Price) context;
        return new CheckoutAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.checout_cart_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        total_price= Integer.parseInt(listProduct_cart.get(position).getProduct_price());


         holder.name_product.setText( listProduct_cart.get(position).getProduct_name());
        holder.product_price.setText("Rs. " + total_price);
        Glide.with(context).load(listProduct_cart.get(position).getProduct_image_uri()).into(holder.image_product);


//                holder.qty.setText(map.toString());

                data.data(holder.qty,position);






    }

    @Override
    public int getItemCount() {
        return listProduct_cart.size();
    }

    @Override
    public void showPrice(TextView textView) {
        textView.setText(totalPrice);
    }






      public class MyViewHolder extends RecyclerView.ViewHolder  {
          TextView name_product,product_price,
          qty;
          ImageView image_product;

         public MyViewHolder(@NonNull View itemView) {


            super(itemView);
            name_product=itemView.findViewById(R.id.title_check_cart);

            product_price=itemView.findViewById(R.id.price_check_cart);


            image_product=itemView.findViewById(R.id.image_check_cart);

            qty=itemView.findViewById(R.id.qnttity_check);

   //         quantity=itemView.findViewById(R.id.quantiy_kart);











        }

    }


    public interface  Data1{
        void data(TextView a, int position);
    }



}
