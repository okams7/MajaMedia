package com.okams.majamedia.fragments;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okams.majamedia.R;
import com.okams.majamedia.activities.WebViewPage;
import com.okams.majamedia.methods.Gmc;
import com.okams.majamedia.models.Data;
import com.okams.majamedia.models.Tags;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsPage extends Fragment {

    TextView txt_details,txt_page_name,txt_page_details,txt_date,txt_section_name,
            txt_read_on_web;
    Data item = new Data();
    SimpleDraweeView img_page;
    LinearLayout llv_tags,llv_tags_scroll,llv_page_details,llv_page_texts;
    boolean animate = true;

    public NewsPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news_page, container, false);
        setViews(rootView);
        getPassedParameters();
        setData();
        setListeners();
        return rootView;
    }

    private void setListeners() {
        txt_read_on_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), WebViewPage.class);
                i.putExtra(Gmc.p_Link,item.getFullurl());
                startActivity(i);
            }
        });

        llv_page_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(animate) {
                    animate = false;
                    scaleView(img_page, 1f, 1.3f, 1f, 1.3f);
                    fadeText();
                }
            }
        });
    }

    void fadeText(){
        Gmc.toggleVisibility(true,txt_page_details);
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(3000);
        AnimationSet animation = new AnimationSet(false); // change to false

        ValueAnimator animator = ValueAnimator.ofInt(0,50);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                llv_page_texts.setPadding(0, 0,(Integer) valueAnimator.getAnimatedValue(),0);
            }
        });
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.start();
        animation.addAnimation(fadeIn);
        txt_page_details.setAnimation(animation);
    }


    private void setData() {

        txt_details.setText(getSpanned(item.getDetails()));
        Uri imageUri = Uri.parse(item.getPage_logo());
        img_page.setImageURI(imageUri);
        txt_page_name.setText(item.getPage_name());
        txt_page_details.setText(getSpanned(item.getPage_details()));

        txt_section_name.setText(item.getSection_name());

        if(item.getTags()!=null){
            createTagsButtons(item.getTags());
        }
        else{
            Gmc.toggleVisibility(false,llv_tags);
        }

//        long date = Gmc.getDateLong(item.getCreated_date());
        txt_date.setText(item.getCreated_date());
    }

    private void createTagsButtons(ArrayList<Tags> tags) {
        for(int i=0;i<tags.size();i++){
            Button btn = new Button(getActivity());
            btn.setText(tags.get(i).getTag_name());
            llv_tags_scroll.addView(btn);
        }
    }

    public void scaleView(View v, float fromX, float toX,float fromY,float toY) {
        Animation anim = new ScaleAnimation(
                fromX, toX, // Start and end values for the X axis scaling
                fromY, toY, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    @SuppressWarnings("deprecation")
    public Spanned getSpanned(String str){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(str,Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(str);
        }
    }

    private void getPassedParameters() {
        Bundle extras = this.getArguments();
        String value;
        if (extras != null) {
            value = extras.getString(Gmc.p_data);
            Gson gson = new Gson();
            Type type = new TypeToken<Data>(){}.getType();
            item= gson.fromJson(value, type);
        }
    }

    void setViews(View view){
        txt_details = (TextView) view.findViewById(R.id.txt_details);
        img_page = (SimpleDraweeView) view.findViewById(R.id.img_page);
        txt_page_name = (TextView) view.findViewById(R.id.txt_page_name);
        txt_page_details = (TextView) view.findViewById(R.id.txt_page_details);
        txt_date = (TextView) view.findViewById(R.id.txt_date);
        txt_section_name = (TextView) view.findViewById(R.id.txt_section_name);
        txt_read_on_web = (TextView) view.findViewById(R.id.txt_read_on_web);
        llv_tags = (LinearLayout) view.findViewById(R.id.llv_tags);
        llv_tags_scroll = (LinearLayout) view.findViewById(R.id.llv_tags_scroll);
        llv_page_details = (LinearLayout) view.findViewById(R.id.llv_page_details);
        llv_page_texts = (LinearLayout) view.findViewById(R.id.llv_page_texts);
        Gmc.toggleVisibility(false,txt_page_details);
    }

}
