package com.jiit.minor2.shubhamjoshi.box.chooser;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jiit.minor2.shubhamjoshi.box.R;

/**
 * Created by Shubham Joshi on 01-03-2016.
 */


public class ChooserInterestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView choicePhoto;
    public boolean flagFirsPressed = false;
    RelativeLayout selector;

    public ChooserInterestHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        choicePhoto = (ImageView) itemView.findViewById(R.id.choicePhoto);
        selector = (RelativeLayout) itemView.findViewById(R.id.selectedBg);

    }


    @Override
    public void onClick(View view) {
        if (!flagFirsPressed) {
            Vibrator vibe = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(50);
            //do work to add shade
            selector.setVisibility(View.VISIBLE);
            selector.setBackground(view.getResources().getDrawable(R.drawable.selection_work));
            flagFirsPressed = true;
        } else {
            selector.setVisibility(View.INVISIBLE);
            selector.setBackground(null);
            flagFirsPressed = false;
            //do work to remove shade
        }
    }
}





