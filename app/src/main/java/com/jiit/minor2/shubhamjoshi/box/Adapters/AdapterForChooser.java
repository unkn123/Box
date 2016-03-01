package com.jiit.minor2.shubhamjoshi.box.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.chooser.ChooserInterestHolder;
import com.jiit.minor2.shubhamjoshi.box.model.list_models.ChooserObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by Shubham Joshi on 28-02-2016.
 */

public class AdapterForChooser extends RecyclerView.Adapter<ChooserInterestHolder> {

    private List<ChooserObject> itemList;
    private Context context;

    public AdapterForChooser(Context context, List<ChooserObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ChooserInterestHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chooser_interest_display_init, null);
        ChooserInterestHolder rcv = new ChooserInterestHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ChooserInterestHolder holder, int position) {

        Picasso.with(context).load(itemList.get(position).getChooserImage()).into(holder.choicePhoto);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
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

                String add = itemList.get(position).getChooserImage();
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