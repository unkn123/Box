package com.jiit.minor2.shubhamjoshi.box.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.SubModal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ParseAdapter extends RecyclerView.Adapter<ParseAdapter.MyViewHolder> {

    private final Context mContext;
    private List<SubModal> list;

    public ParseAdapter(Context mContext, List<SubModal> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.parse_result_layout, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        myViewHolder.tv1.setText(list.get(position).mHeader);
        Picasso.with(mContext).load(list.get(position).mImageView).into(myViewHolder.tv2);
        myViewHolder.tv1.setText(list.get(position).mHeader);
        myViewHolder.tv3.setText(list.get(position).mRating);
        //myViewHolder.tv2.setText(mData2.get(position));
//        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                clickListener.onClick(v, position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View v, int pos);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv1;
        protected ImageView tv2;
        protected TextView tv3;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.head);
            tv2 = (ImageView) itemView.findViewById(R.id.headPhoto);
            tv3 = (TextView)itemView.findViewById(R.id.rating);
            //tv2 = (TextView) itemView.findViewById(R.id.txt2);

        }

    }
}