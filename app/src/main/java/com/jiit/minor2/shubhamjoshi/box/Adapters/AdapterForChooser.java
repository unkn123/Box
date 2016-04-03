package com.jiit.minor2.shubhamjoshi.box.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.chooser.ChooserInterestHolder;
import com.jiit.minor2.shubhamjoshi.box.model.list_models.Categories;
import com.jiit.minor2.shubhamjoshi.box.model.list_models.GiantChooserModel;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shubham Joshi on 28-02-2016.
 */

public class AdapterForChooser extends RecyclerView.Adapter<ChooserInterestHolder> {

    private List<Categories> itemList;
    private Context context;
    private String pathPart;
    private  ChooserInterestHolder rcv;
    private ArrayList<GiantChooserModel> mGiantChooserModels;

    public AdapterForChooser(Context context, List<Categories> itemList, String pathPart, ArrayList<GiantChooserModel> giantChooserModels) {
        this.itemList = itemList;
        this.context = context;
        this.pathPart = pathPart;
        mGiantChooserModels=giantChooserModels;

    }

    @Override
    public ChooserInterestHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chooser_interest_display_init, null);
        ChooserInterestHolder rcv = new ChooserInterestHolder(layoutView,itemList,pathPart);
        return rcv;
    }



    @Override
    public void onBindViewHolder(final ChooserInterestHolder holder, final int position) {

        Picasso p = new Picasso.Builder(context)
                .memoryCache(new LruCache(24000))
                .build();
        p.with(context).load(itemList.get(position).getUrl()).fit().into(holder.choicePhoto);

        //getImage(holder,position);

        //Bugg Fixer
        if(mGiantChooserModels.get(position).isSelected())
            holder.selector.setVisibility(View.VISIBLE);
        else
            holder.selector.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                clickListener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View v, int pos);

    }

    private void getImage(final ChooserInterestHolder holders, final int position) {


        class GetImage extends AsyncTask<String, Void, Bitmap> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);

                holders.choicePhoto.setImageBitmap(b);

            }

            @Override
            protected Bitmap doInBackground(String... params) {

                String add = itemList.get(position).getUrl();
                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute();
    }
}