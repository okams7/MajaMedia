package com.okams.majamedia.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.okams.majamedia.fragments.NewsPage;
import com.okams.majamedia.methods.Gmc;
import com.okams.majamedia.models.Data;

import java.util.ArrayList;
import java.util.Collections;

public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Data> data = new ArrayList<>();
    private Context context;
    private String TAG = Gmc.TagApp+getClass().getSimpleName();

    public NewsPagerAdapter(Context context, FragmentManager fm, ArrayList<Data> dataSender) {
		super(fm);
		// TODO Auto-generated constructor stub
        this.context = context;
        this.data = dataSender;
	}

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getTitle();
    }

    @Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub

//        Log.v(Tag,"index:"+index);
//        int reverseIndex = data.size()-1 - index;
//        Log.v(Tag,"reverseIndex:"+reverseIndex);

        Gson gson = new Gson();
        String jsonData = gson.toJson(data.get(index));

        Fragment frag = new NewsPage();
        Bundle args = new Bundle();
        args.putString(Gmc.p_data, jsonData);
        frag.setArguments(args);
        return frag;
	}

	@Override
	public int getCount() {
        return data.size();
	}

}
