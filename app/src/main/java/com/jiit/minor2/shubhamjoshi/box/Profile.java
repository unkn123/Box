package com.jiit.minor2.shubhamjoshi.box;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class Profile extends AppCompatActivity {

    String pathPart;
    String ImageUrl;
    private ImageView profile;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");
        ImageUrl = sp.getString("ProfilePhoto", "ERROR");
        Log.e("SJSJ", ImageUrl);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");
        ImageUrl = sp.getString("ProfilePhoto", "ERROR");
        Picasso.with(this).load(ImageUrl).into(profile);

        ImageView relative = (ImageView) findViewById(R.id.profileBg);
        Picasso.with(this).load(ImageUrl).transform(new Blur(getBaseContext(), 20)).into(relative);

    }

    private void init() {
        profile = (ImageView) findViewById(R.id.fb123);
    }

    public class Blur implements Transformation {
        protected static final int UP_LIMIT = 25;
        protected static final int LOW_LIMIT = 1;
        protected final Context context;
        protected final int blurRadius;


        public Blur(Context context, int radius) {
            this.context = context;

            if (radius < LOW_LIMIT) {
                this.blurRadius = LOW_LIMIT;
            } else if (radius > UP_LIMIT) {
                this.blurRadius = UP_LIMIT;
            } else
                this.blurRadius = radius;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap sourceBitmap = source;

            Bitmap blurredBitmap;
            blurredBitmap = source.copy(source.getConfig(), true);

            RenderScript renderScript = RenderScript.create(context);

            Allocation input = Allocation.createFromBitmap(renderScript,
                    sourceBitmap,
                    Allocation.MipmapControl.MIPMAP_FULL,
                    Allocation.USAGE_SCRIPT);


            Allocation output = Allocation.createTyped(renderScript, input.getType());

            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript,
                    Element.U8_4(renderScript));

            script.setInput(input);
            script.setRadius(blurRadius);

            script.forEach(output);
            output.copyTo(blurredBitmap);

            source.recycle();
            return blurredBitmap;
        }

        @Override
        public String key() {
            return "blurred";
        }
    }
}
