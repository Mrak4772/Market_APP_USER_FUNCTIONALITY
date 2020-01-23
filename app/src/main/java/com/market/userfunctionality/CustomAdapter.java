package com.market.userfunctionality;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;

    public CustomAdapter(Context context, ArrayList<DataModel> profiles) {
        this.context = context;
        this.profiles = profiles;

    }
      ArrayList<DataModel> profiles = new ArrayList ();


public CustomAdapter() {
}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        holder.name.setText(profiles.get(position).getCat_name().trim());

        Glide.with(context).asBitmap( ) .load(profiles.get(position).cat_image_uri ).into(holder.category_Image)  ;

        Random r = new Random();
        int red= r.nextInt(255 + 1);
        int green= r.nextInt(255 + 1);
        int blue= r.nextInt(255 + 1);

        GradientDrawable draw = new GradientDrawable();
      //  draw.setShape(GradientDrawable.OVAL);
        draw.setColor(Color.rgb(red,green,blue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.category_Image.setBackgroundTintList(draw.getColor() );
        }

//        Picasso.get().load(profiles.get(position).getCat_image_uri()).into(holder.profilePic);
//        if(profiles.get(position).g) {
//            holder.btn.setVisibility(View.VISIBLE);

//        }

    }
    @NonNull
    @Override
    public int getItemCount() {
        return profiles.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder   {
        TextView name;
        FloatingActionButton category_Image;
         public MyViewHolder(View itemView) {
            super(itemView);
            name =   itemView.findViewById(R.id.textview_category);
             category_Image =  itemView.findViewById(R.id.image_view_category);



         }

    }
}