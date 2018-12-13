package com.toro.helper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toro.helper.R;

import org.w3c.dom.Text;

/**
 * Create By liujia
 * on 2018/11/3.
 **/
public class SafeguardRadiusItem extends RelativeLayout {

    private TextView titleView;
    private ImageView selectedView;
    private int value;

    public SafeguardRadiusItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleView = findViewById(R.id.title_text);
        selectedView = findViewById(R.id.selected_icon);
    }

    public void init(int textRes,int value){
        this.value = value;
        titleView.setText(textRes);
        selectedView.setVisibility(GONE);
    }

    public void setSelected(boolean flag) {
        if(flag) {
            selectedView.setVisibility(VISIBLE);
        } else {
            selectedView.setVisibility(GONE);
        }
    }

    public int getValue() {
        return value;
    }
}
