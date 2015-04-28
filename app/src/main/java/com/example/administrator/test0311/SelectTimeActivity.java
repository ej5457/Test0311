package com.example.administrator.test0311;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SelectTimeActivity extends ActionBarActivity {

    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        Button EightButton = (Button) findViewById(R.id.eight);
        EightButton.setOnClickListener(onClickListener);
        Button TenButton = (Button) findViewById(R.id.ten);
        TenButton.setOnClickListener(onClickListener);
        Button FifteenButton = (Button) findViewById(R.id.fifteen);
        FifteenButton.setOnClickListener(onClickListener);
        Button EtcButton = (Button) findViewById(R.id.etc);
        EtcButton.setOnClickListener(onClickListener);

        final Intent intent = getIntent();
        final boolean alarm = intent.getBooleanExtra("alarm", false);
        if (alarm) {
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("운동시간입니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_count, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sound) {
            SharedPreferences pref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();

            isMute = "소리OFF".equals(item.getTitle());
            if (isMute) {
                item.setTitle("소리ON");
                ed.putBoolean("mute", false);
                ed.apply();
            } else {
                item.setTitle("소리OFF");
                ed.putBoolean("mute", true);
                ed.apply();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(SelectTimeActivity.this, CountingActivity.class);

            if (v.getId() == R.id.eight) {
                intent.putExtra("time", 8);
                startActivity(intent);
            } else if (v.getId() == R.id.ten) {
                intent.putExtra("time", 10);
                startActivity(intent);

            } else if (v.getId() == R.id.fifteen) {
                intent.putExtra("time", 15);
                startActivity(intent);

            } else {
                if (v.getId() == R.id.etc) {
                    final EditText edit = new EditText(SelectTimeActivity.this);
                    AlertDialog dialog = new AlertDialog.Builder(SelectTimeActivity.this).setTitle("숫자를 입력하세요").setView(edit).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int a = Integer.parseInt(edit.getText().toString());
                            intent.putExtra("time", a);
                            startActivity(intent);
                        }
                    }).create();
                    dialog.show();
                }
            }
        }
    };

}
