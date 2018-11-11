package com.wwa.widgetweatheralarm.alarm;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wwa.widgetweatheralarm.util.Constants;
import com.wwa.widgetweatheralarm.R;

public class AlarmActivity extends AppCompatActivity {
    private Ringtone ringtone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);

        final Intent intent = getIntent();
        if (intent.getAction().equals(Constants.ALARM_START)) {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            ringtone = RingtoneManager.getRingtone(this, alarmUri);
            new RingtoneAsyncTask().execute(ringtone);
        }

        Button ringtoneBtn = findViewById(R.id.ringtoneBtn);
        ringtoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringtone != null) {
                    ringtone.stop();
                    finish();
                }
            }
        });
    }

    private static class RingtoneAsyncTask extends AsyncTask<Ringtone, Void, Void> {
        @Override
        protected Void doInBackground(Ringtone... ringings) {
            ringings[0].play();
            return null;
        }
    }
}
