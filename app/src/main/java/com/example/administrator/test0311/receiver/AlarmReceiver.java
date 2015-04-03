package com.example.administrator.test0311.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.test0311.SelectTimeActivity;
import com.example.administrator.test0311.SettingActivity;
import com.example.administrator.test0311.View.TimeSelectView;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        SharedPreferences pref = context.getSharedPreferences("com.example.administrator.test0311", Context.MODE_PRIVATE);

        if (pref.getBoolean("alarm", false)) {

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();
            Intent intents = new Intent(context, SelectTimeActivity.class);
            intents.putExtra("alarm", true);
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intents);

        }

    }
}
