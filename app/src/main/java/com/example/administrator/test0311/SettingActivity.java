package com.example.administrator.test0311;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.test0311.View.TimeSelectView;
import com.example.administrator.test0311.receiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;


public class SettingActivity extends ActionBarActivity {

    private final int RESULT_LOAD_IMAGE = 1001;
    private final int[] RESULT_LOAD_AUDIO = {1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016};
    ListView.OnItemClickListener clickListener = new ListView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 1) {
                final CharSequence day[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
                dialog.setTitle("목소리를 바꿀 숫자를 선택하세요");
                dialog.setItems(day, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RESULT_LOAD_AUDIO[item]);
                    }
                });

                dialog.create();
                dialog.show();
            } else if (position == 2) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            } else if (position == 3) {
                AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this).setTitle("알람 설정").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                        Intent intent = new Intent(SettingActivity.this, AlarmReceiver.class);
                        PendingIntent sender = PendingIntent.getBroadcast(SettingActivity.this, 0,
                                intent, 0);
                        // We want the alarm to go off 30 seconds from now.
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        Calendar time = Calendar.getInstance();
                        time.setTimeInMillis(System.currentTimeMillis());

                        int hour = pref.getInt("hour", 0);
                        int minute = pref.getInt("minute", 0);
                        Boolean apm = pref.getBoolean("isPM", false);
                        Boolean alarm = pref.getBoolean("alarm", true);

                        Boolean day_switch[] = {pref.getBoolean("sun", false), pref.getBoolean("mon", false), pref.getBoolean("tue", false), pref.getBoolean("wed", false), pref.getBoolean("thu", false), pref.getBoolean("fri", false), pref.getBoolean("sat", false)};

                        if (alarm) {
                            time.set(Calendar.HOUR, hour);
                            time.set(Calendar.MINUTE, minute);
                            time.set(Calendar.SECOND, 0);
                            time.set(Calendar.AM_PM, (!apm) ? Calendar.AM : Calendar.PM);
                        }
                        for (int i = 0; i < 7; i++) {
                            if (day_switch[i].equals(true)) {
                                time.set(Calendar.DAY_OF_WEEK, i + 1);

//                                if (calendar.getTimeInMillis() > time.getTimeInMillis()) {
//                                    time.add(Calendar.HOUR, 24);
//                                }

                                // Schedule the alarm!
                                Log.d("asdf", "" + time.getTimeInMillis());
                                AlarmManager am = (AlarmManager) SettingActivity.this.getSystemService(Context.ALARM_SERVICE);
                                am.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, sender);
                            }
                        }


                        Toast.makeText(SettingActivity.this, (!apm ? "오전" : "오후") + hour + "시" + minute + "분", Toast.LENGTH_SHORT).show();
                    }
                }).create();

                dialog.setView(new TimeSelectView(SettingActivity.this));
                dialog.show();
            }
        }
    };
    private String[] audioPaths = new String[15];
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        listView = (ListView) findViewById(R.id.list);
        ArrayList<String> list = new ArrayList<>();
        list.add("소리 설정");
        list.add("목소리 선택");
        list.add("배경화면 선택");
        list.add("알람 설정");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(clickListener);
        SharedPreferences pref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String audioPath = pref.getString("audioPath", null);
        if (audioPath != null) audioPaths = audioPath.split("\\|\\|\\|");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == RESULT_LOAD_IMAGE && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            SharedPreferences pref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("picturePath", picturePath);
            ed.apply();
        } else if (data != null) {
            int num = 999;
            for (int i = 0; i < 15; i++) {
                if (requestCode == RESULT_LOAD_AUDIO[i]) num = i;
            }
            if (num != 999) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Audio.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String audioPath = cursor.getString(columnIndex);
                cursor.close();


                SharedPreferences pref = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = pref.edit();
                audioPaths[num] = audioPath;
                String path = "";
                for (String p : audioPaths) {
                    path += p + "|||";
                }
                path = path.substring(0, path.length() - 2);
                ed.putString("audioPath", path);
                ed.apply();

                Log.d("sound", "path = " + path);
            }
        }
    }

}
