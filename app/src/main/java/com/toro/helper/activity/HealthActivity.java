package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.modle.BaseResponeData;
import com.toro.helper.modle.DataModleParser;
import com.toro.helper.modle.data.HealthInfo;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.utils.StringUtils;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.calendarView.OnCalendarClickListener;
import com.toro.helper.view.calendarView.WeekCalendarView;

import org.joda.time.DateTime;

/**
 * Create By liujia
 * on 2018/11/5.
 **/
public class HealthActivity extends ToroActivity {

    private static final String EXTRA_UID = "extra_uid";

    private MainActionBar mainActionBar;
    private int uid;
    private WeekCalendarView weekCalendarView;
    private TextView curDateText;
    private TextView stepUpdateTimeText,heartRateUpdateTimeText,bloodOxygenUpdateTimeText;
    private TextView stepValueText,heartRateValueText,bloodOxygenValueText;
    private TextView stepUnitText,heartRateUnitText,bloodOxygenUnitText;
    private TextView stepEmptyText,heartRateEmptyText,bloodOxygenEmptyText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_health);
        uid = getIntent().getIntExtra(EXTRA_UID,0);
        initView();
        mainActionBar.updateView(getString(R.string.health_data), R.mipmap.action_back_icon, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, null);

        DateTime dt = new DateTime();
        curDateText.setText(dt.getYear() + getString(R.string.date_time_year) + dt.getMonthOfYear() + getString(R.string.date_time_mouth) + dt.getDayOfMonth() + getString(R.string.date_time_day) + " " + getString(R.string.date_time_week) + StringUtils.getChiniseNumber(this,dt.getDayOfWeek()));
        updateData(dt.getYear(),dt.getMonthOfYear(),dt.getDayOfMonth());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        weekCalendarView = findViewById(R.id.mWcvCalendar);
        mainActionBar = findViewById(R.id.main_action_view);
        curDateText = findViewById(R.id.mToDayTv);
        stepUpdateTimeText = findViewById(R.id.textView14);
        heartRateUpdateTimeText = findViewById(R.id.textView18);
        bloodOxygenUpdateTimeText = findViewById(R.id.textView22);
        stepValueText = findViewById(R.id.textView13);
        heartRateValueText = findViewById(R.id.textView17);
        bloodOxygenValueText = findViewById(R.id.textView21);
        stepUnitText = findViewById(R.id.textView12);
        heartRateUnitText = findViewById(R.id.textView16);
        bloodOxygenUnitText = findViewById(R.id.textView20);
        stepEmptyText = findViewById(R.id.empty_step);
        heartRateEmptyText = findViewById(R.id.textView31);
        bloodOxygenEmptyText = findViewById(R.id.textView32);
        weekCalendarView.setOnCalendarClickListener(new OnCalendarClickListener() {
            @Override
            public void onClickDate(int year, int month, int day) {
                updateData(year,month+1,day);
            }

            @Override
            public void onPageChange(int year, int month, int day) {
                updateData(year,month+1,day);
            }
        });
        resetView();
    }

    private void resetView() {
        showStepEmpty();
        showHeartRateEmpty();
        showBloodOxygenEmpty();
    }

    private void updateView(HealthInfo info) {
        stepEmptyText.setVisibility(View.GONE);
        stepValueText.setVisibility(View.VISIBLE);
        stepUpdateTimeText.setVisibility(View.VISIBLE);
        stepUnitText.setVisibility(View.VISIBLE);
        stepValueText.setText(info.getStep()+"");
        stepUpdateTimeText.setText(info.getStepDate());

        heartRateEmptyText.setVisibility(View.GONE);
        heartRateValueText.setVisibility(View.VISIBLE);
        heartRateUpdateTimeText.setVisibility(View.VISIBLE);
        heartRateUnitText.setVisibility(View.VISIBLE);
        heartRateValueText.setText(info.getHrrest());
        heartRateUpdateTimeText.setText(info.getMeasureDate());

        bloodOxygenEmptyText.setVisibility(View.GONE);
        bloodOxygenValueText.setVisibility(View.VISIBLE);
        bloodOxygenUnitText.setVisibility(View.VISIBLE);
        bloodOxygenUpdateTimeText.setVisibility(View.VISIBLE);
        bloodOxygenValueText.setText(info.getBloodOxygen());
        bloodOxygenUpdateTimeText.setText(info.getMeasureDate());
    }

    private void updateData(int year,int mouth,int day) {
        String mouthString = mouth < 10?"0" + mouth:mouth + "";
        String dayString = day < 10?"0" + day:day + "";
        ConnectManager.getInstance().getHealthData(this,uid,year + "-" + mouthString + "-" + dayString, ToroDataModle.getInstance().getLocalData().getToken());
    }

    private void showStepEmpty() {
        stepEmptyText.setVisibility(View.VISIBLE);
        stepValueText.setVisibility(View.GONE);
        stepUpdateTimeText.setVisibility(View.GONE);
        stepUnitText.setVisibility(View.GONE);
    }

    private void showHeartRateEmpty() {
        heartRateEmptyText.setVisibility(View.VISIBLE);
        heartRateValueText.setVisibility(View.GONE);
        heartRateUpdateTimeText.setVisibility(View.GONE);
        heartRateUnitText.setVisibility(View.GONE);
    }

    private void showBloodOxygenEmpty() {
        bloodOxygenEmptyText.setVisibility(View.VISIBLE);
        bloodOxygenValueText.setVisibility(View.GONE);
        bloodOxygenUnitText.setVisibility(View.GONE);
        bloodOxygenUpdateTimeText.setVisibility(View.GONE);
    }

    @Override
    public boolean bindData(int tag, Object object) {
        boolean status = super.bindData(tag, object);
        if(status) {
            BaseResponeData data = DataModleParser.parserBaseResponeData((String) object);
            HealthInfo info = HealthInfo.newInstance(data.getEntry());
            if(info != null) {
                updateView(info);
            }else {
                resetView();
            }
        } else {
            resetView();
        }
        return status;
    }

    public static Intent createIntent(Context context, int uid) {
        Intent intent = new Intent();
        intent.setClass(context,HealthActivity.class);
        intent.putExtra(EXTRA_UID,uid);
        return intent;
    }
}
