package com.example.administrator.test0311.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.test0311.R;

/**
 * TODO: document your custom view class.
 */
public class TimeSelectView extends LinearLayout {
    protected CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences pref = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            if (buttonView.getId() == R.id.alarm_switch) {
                ed.putBoolean("alarm", isChecked);
            } else if (buttonView.getId() == R.id.mon) {
                ed.putBoolean("mon", isChecked);
            } else if (buttonView.getId() == R.id.tue) {
                ed.putBoolean("tue", isChecked);
            } else if (buttonView.getId() == R.id.wed) {
                ed.putBoolean("wed", isChecked);
            } else if (buttonView.getId() == R.id.thu) {
                ed.putBoolean("thu", isChecked);
            } else if (buttonView.getId() == R.id.fri) {
                ed.putBoolean("fri", isChecked);
            } else if (buttonView.getId() == R.id.sat) {
                ed.putBoolean("sat", isChecked);
            } else if (buttonView.getId() == R.id.sun) {
                ed.putBoolean("sun", isChecked);
            }

            ed.apply();
        }
    };
    Context mContext;
    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            TextView hour = (TextView) findViewById(R.id.hour_txt);
            TextView minute = (TextView) findViewById(R.id.minute);
            Button apm = (Button) findViewById(R.id.apm);

            if (v.getId() == R.id.hup) {
                hour.setText("" + (Integer.parseInt((String) hour.getText()) + 13) % 12);
            } else if (v.getId() == R.id.hdown) {
                hour.setText("" + (Integer.parseInt((String) hour.getText()) + 11) % 12);
            } else if (v.getId() == R.id.mup) {
                minute.setText("" + (Integer.parseInt((String) minute.getText()) + 61) % 60);
            } else if (v.getId() == R.id.mdown) {
                minute.setText("" + (Integer.parseInt((String) minute.getText()) + 59) % 60);
            } else if (v.getId() == R.id.apm) {
                if ("오전".equals(apm.getText())) {
                    apm.setText("오후");
                } else {
                    apm.setText("오전");
                }
            }

            SharedPreferences pref = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            int hou = Integer.parseInt((String) hour.getText());
            int min = Integer.parseInt((String) minute.getText());
            String ap = (String) apm.getText();
            Log.d("alarmlog", "isalarm = " + pref.getBoolean("alarm", false) + ", hou = " + hou + ", min =" + min);

            ed.putInt("hour", hou);
            ed.putInt("minute", min);
            ed.putBoolean("isPM", "오후".equals(ap));
            ed.apply();
        }


    };


    public TimeSelectView(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public TimeSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public TimeSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {


        View v = inflate(mContext, R.layout.sample_time_select_view, null);
        addView(v);

        Button hup = (Button) findViewById(R.id.hup);
        Button hdown = (Button) findViewById(R.id.hdown);
        Button mup = (Button) findViewById(R.id.mup);
        Button mdown = (Button) findViewById(R.id.mdown);
        Button apmButton = (Button) findViewById(R.id.apm);
        CheckBox alarmSwitch = (CheckBox) findViewById(R.id.alarm_switch);
        TextView hourView = (TextView) findViewById(R.id.hour_txt);
        TextView minuteView = (TextView) findViewById(R.id.minute);
        CheckBox monSwitch = (CheckBox) findViewById(R.id.mon);
        CheckBox tueSwitch = (CheckBox) findViewById(R.id.tue);
        CheckBox wedSwitch = (CheckBox) findViewById(R.id.wed);
        CheckBox thuSwitch = (CheckBox) findViewById(R.id.thu);
        CheckBox friSwitch = (CheckBox) findViewById(R.id.fri);
        CheckBox satSwitch = (CheckBox) findViewById(R.id.sat);
        CheckBox sunSwitch = (CheckBox) findViewById(R.id.sun);


        SharedPreferences pref = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);

        int hour = pref.getInt("hour", -1);
        int minute = pref.getInt("minute", -1);
        Boolean isPM = pref.getBoolean("isPM", false);
        Boolean isAlarm = pref.getBoolean("alarm", true);
        Boolean isDaySwitch[] = {pref.getBoolean("sun", false), pref.getBoolean("mon", false), pref.getBoolean("tue", false), pref.getBoolean("wed", false), pref.getBoolean("thu", false), pref.getBoolean("fri", false), pref.getBoolean("sat", false)};


        if (hour >= 0 && minute >= 0) {
            hourView.setText(hour + "");
            minuteView.setText(minute + "");
            apmButton.setText((!isPM) ? "오전" : "오후");
            alarmSwitch.setChecked(isAlarm);
            sunSwitch.setChecked(isDaySwitch[0]);
            monSwitch.setChecked(isDaySwitch[1]);
            tueSwitch.setChecked(isDaySwitch[2]);
            wedSwitch.setChecked(isDaySwitch[3]);
            thuSwitch.setChecked(isDaySwitch[4]);
            friSwitch.setChecked(isDaySwitch[5]);
            satSwitch.setChecked(isDaySwitch[6]);
        }


        hup.setOnClickListener(clickListener);
        hdown.setOnClickListener(clickListener);
        mup.setOnClickListener(clickListener);
        mdown.setOnClickListener(clickListener);
        apmButton.setOnClickListener(clickListener);
        alarmSwitch.setOnCheckedChangeListener(mListener);
        monSwitch.setOnCheckedChangeListener(mListener);
        tueSwitch.setOnCheckedChangeListener(mListener);
        wedSwitch.setOnCheckedChangeListener(mListener);
        thuSwitch.setOnCheckedChangeListener(mListener);
        friSwitch.setOnCheckedChangeListener(mListener);
        satSwitch.setOnCheckedChangeListener(mListener);
        sunSwitch.setOnCheckedChangeListener(mListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
