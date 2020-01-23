package com.market.userfunctionality;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import jp.wasabeef.glide.transformations.BlurTransformation;

 public class SlidingImage_Adapter extends PagerAdapter {

    public Timer swipeTimer ;

    final Handler handler = new Handler();

  //  private ArrayList<DataModel> IMAGES;
       ArrayList<SLiderModel> sliding_images;

      LayoutInflater inflater;
     FragmentActivity activity;
    RelativeLayout viewpagerrelative;
     public SlidingImage_Adapter(FragmentActivity activity,ArrayList<SLiderModel> IMAGES, RelativeLayout relativeLayout1) {
        this.activity= activity;
        this.sliding_images=IMAGES;
         this.viewpagerrelative = relativeLayout1;
     }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return sliding_images.size();
    }
@NonNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup view, int position) {
        View imageLayout = inflater.from(activity).inflate(R.layout.view_pager_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.viewpager_image);

    // RelativeLayout relativeLayout;
       // relativeLayout = context.fin


              Glide.with(activity).load(sliding_images.get(position).getImageUri()) .into(imageView) ;
     //   imageView.setImageResource(IMAGES.get(position));
//        relativeLayout.setBackground(imageView.getDrawable());


              Glide.with(activity)
                .load(sliding_images.get(position).getImageUri()) .apply(RequestOptions.bitmapTransform(new BlurTransformation(35,3)))
                 .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                         viewpagerrelative.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void setTimer(final ViewPager myPager, int time){
        final int size =getCount();


        final Runnable Update = new Runnable() {
            int NUM_PAGES =size;
            int currentPage = 0 ;
            public void run() {
                if (currentPage == NUM_PAGES ) {
                    currentPage = 0;
                }
                myPager.setCurrentItem(currentPage++, true);
            }
        };

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, time*1000);
    }
}
