package io.eberlein.autobeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";


    private static final String TAG = "MainActivity";
    private Button btnStart;
    private boolean started = false;

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        if(!isNotificationServiceEnabled()) startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));

        btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!started) {
                    btnStart.setText(R.string.stop);
                    startService(new Intent(getApplicationContext(), AutoBeerService.class));
                    Toast.makeText(getApplicationContext(), "hooked", Toast.LENGTH_LONG).show();
                }
                else {
                    btnStart.setText(R.string.start);
                    stopService(new Intent(getApplicationContext(), AutoBeerService.class));
                    Toast.makeText(getApplicationContext(), "unhooked", Toast.LENGTH_LONG).show();
                }
                started = !started;
            }
        });

        Log.d(TAG, "onCreate done");
    }
}
