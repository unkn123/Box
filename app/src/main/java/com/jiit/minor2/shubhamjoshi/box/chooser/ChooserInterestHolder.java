package com.jiit.minor2.shubhamjoshi.box.chooser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.list_models.Categories;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham Joshi on 01-03-2016.
 */


public class ChooserInterestHolder extends RecyclerView.ViewHolder {

    public ImageView choicePhoto;
    public boolean flagFirsPressed = false;
    public RelativeLayout selector;
    private List<Categories> mList;
    private String pathPart;

    private ArrayList<String> likes = new ArrayList<>();

    public ChooserInterestHolder(View itemView, List<Categories> mlist, String pathPart) {
        super(itemView);


        choicePhoto = (ImageView) itemView.findViewById(R.id.choicePhoto);
        selector = (RelativeLayout) itemView.findViewById(R.id.selectedBg);
        this.mList = mlist;
        this.pathPart = pathPart;


    }


}





