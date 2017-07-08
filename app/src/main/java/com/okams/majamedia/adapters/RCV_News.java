package com.okams.majamedia.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.okams.majamedia.R;
import com.okams.majamedia.app.AppController;
import com.okams.majamedia.models.Data;

import java.util.ArrayList;

/**
 * Created by Ahmad Owais on 7/7/2017.
 */
public class RCV_News extends RecyclerView.Adapter<RCV_News.ItemsViewHolder>{

    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<Data> data;
    private ImageLoader imageLoader;
    private RCVClickListener clickListener;
    int even,odd;

    public RCV_News(Context context, ArrayList<Data> data)
    {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


    }

    public void setClickListener(RCVClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.cell_news,parent,false);

        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Data item = data.get(position);
        holder.txt_title.setText(item.getTitle());

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            holder.img_ic.setTransitionName(context.getString(R.string.share_alb_img));
//            holder.title.setTransitionName(context.getString(R.string.share_alb_txt));
//        }
        holder.txt_date.setText(item.getCreated_date());

        holder.txt_page.setText(item.getPage_name());
        holder.txt_view_count.setText(item.getView_count());

        Uri imageUri = Uri.parse(item.getMain_img());
        holder.img_news.setImageURI(imageUri);

        if(position%2 == 0){
            holder.llv_container.setBackgroundResource(R.drawable.list_item_bkg);
        }
        else
        {
            holder.llv_container.setBackgroundResource(R.drawable.list_item_bkg_b);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_title,txt_date,txt_page,txt_view_count;
        SimpleDraweeView img_news;
        LinearLayout llv_container;

        public ItemsViewHolder(View vi) {
            super(vi);
            txt_title = (TextView) vi.findViewById(R.id.txt_title);
            img_news = (SimpleDraweeView) vi.findViewById(R.id.img_news);
            llv_container = (LinearLayout) vi.findViewById(R.id.llv_container);
            txt_date = (TextView) vi.findViewById(R.id.txt_date);
            txt_page = (TextView) vi.findViewById(R.id.txt_page);
            txt_view_count = (TextView) vi.findViewById(R.id.txt_view_count);

            llv_container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == llv_container){
                int pos = getAdapterPosition();
                clickListener.itemClick(llv_container,pos);
            }
        }
    }

    public interface RCVClickListener{
        void itemClick(View view, int position);
    }

}
