package com.bashirli.fastshop.view.activity.util;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bashirli.fastshop.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Util {

    public void setImageURL(ImageView imageView,
                            Context context,
                            @Nullable String url,
                            CircularProgressDrawable placeHolder){
        RequestOptions requestOptions=new RequestOptions();
       requestOptions=requestOptions.placeholder(placeHolder).error(R.drawable.circlelogo);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .into(imageView);

    }

    public CircularProgressDrawable placeHolder(Context context){
        CircularProgressDrawable circularProgressDrawable=new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(8f);
        circularProgressDrawable.setCenterRadius(40f);
        circularProgressDrawable.start();
        return circularProgressDrawable;


    }


}
