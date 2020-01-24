package com.market.userfunctionality;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

  private   Context context;
   private ArrayList<DataModel> listProduct;


    public ProductAdapter(Context context, ArrayList<DataModel> listProduct ) {
        this.context = context;
        this.listProduct = listProduct;
     }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);

    return new MyViewHolder(v);}

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


      //  holder.name_product.setText(listProduct.get(position).getProduct_name());
        holder.product_price.setText("RS. "+listProduct.get(position).getProduct_price());

        holder.product_des.setText(listProduct.get(position).getProduct_des());
        Glide.with(context).load(listProduct.get(position).getProduct_image_uri() ).into(holder.image_product)  ;



    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

   public  class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView  product_price,product_des;
    ImageView image_product;
         private MyViewHolder(@NonNull View itemView) {
            super(itemView);

             product_price=itemView.findViewById(R.id.tv_price_product);
             product_des=itemView.findViewById(R.id.tv_desc_product);

             image_product=itemView.findViewById(R.id.imageView_product);



//             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                image_product.setClipToOutline(true);
//            }

        }





    }




}