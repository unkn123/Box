package com.jiit.minor2.shubhamjoshi.box.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.dialogs.DateDialogPicker;

public class login extends AppCompatActivity {

    private TextView dateTextView;
    private TextView genderTextView;

    @Override
    protected void onStart() {
        super.onStart();

        dateTextView = (TextView) findViewById(R.id.date);
        genderTextView = (TextView) findViewById(R.id.gender);

        dateTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                DateDialogPicker picker = new DateDialogPicker(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                picker.show(ft, getString(R.string.Date));

                return false;
            }
        });

        genderTextView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final CharSequence[] gender = {getString(R.string.Male), getString(R.string.Female)};

                AlertDialog.Builder alert = new AlertDialog.Builder(login.this, AlertDialog.THEME_HOLO_DARK);
                alert.setTitle(Html.fromHtml("<font color='#1db954'>"+getString(R.string.Gender)+"</font>"));

                        alert.setItems(gender, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (gender[which] == getString(R.string.Male)) {
                                    genderTextView.setText(getString(R.string.Male));
                                } else
                                    genderTextView.setText(getString(R.string.Female));
                            }
                        });

                /****HACK **/

                Dialog d = alert.show();
                int dividerId = d.getContext().getResources().getIdentifier(getString(R.string.hack), null, null);
                View divider = d.findViewById(dividerId);
                divider.setBackgroundColor(Color.parseColor("#1db954"));

                return false;
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
