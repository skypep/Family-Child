package com.toro.helper.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toro.helper.R;

/**
 * Create By liujia
 * on 2018/10/23.
 **/
public class ToroProgressView extends RelativeLayout {

    private TextView progressText;

    public ToroProgressView(Context context) {
        super(context);
    }

    public ToroProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressText = findViewById(R.id.progress_text);
    }

    public void setProgressText(String text) {
        progressText.setText(text);
    }

    private void setVisibility(Activity activity,boolean visibility) {
        if(visibility) {
            setVisibility(View.VISIBLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            setVisibility(View.GONE);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    public void show(Activity activity) {
        setVisibility(activity,true);
    }

    public void hide(Activity activity) {
        setVisibility(activity,false);
    }
}
