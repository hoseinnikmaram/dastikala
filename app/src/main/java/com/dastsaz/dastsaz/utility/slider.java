package com.dastsaz.dastsaz.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dastsaz.dastsaz.R;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;

public class slider implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    Context context;

    public void slider(Context context, SliderLayout sliderShow, String []pics) {
        this.context = context;

        RequestOptions requestOptions = new RequestOptions();
        requestOptions
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);
        sliderShow.removeAllSliders();
        for (int i = 0; i<pics.length; i++) {
            TextSliderView sliderView = new TextSliderView(context);
            // initialize SliderLayout
            sliderView
                    .image(String.valueOf(pics[i]))
                    .setRequestOption(requestOptions)
                    .setBackgroundColor(Color.WHITE)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", String.valueOf(pics[i]));
            sliderView.getBundle().putInt("position",i);

            sliderShow.addSlider(sliderView);


        //    textSliderView.image(String.valueOf(pics[i])).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);

            //textSliderView.description(hash.get(i).get("s_title"));
         //   textSliderView.bundle(new Bundle());
          //  textSliderView.getBundle().putString("extra", String.valueOf(pics[i]));
           // textSliderView.getBundle().putInt("position",i);

         //   sliderShow.addSlider(textSliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);

        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setDuration(4000);
        sliderShow.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

       // mylib.show_det(hash,slider.getBundle().getInt("position") );
       // Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


}