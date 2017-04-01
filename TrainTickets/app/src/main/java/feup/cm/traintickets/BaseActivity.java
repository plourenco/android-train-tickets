package feup.cm.traintickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Date;

import feup.cm.traintickets.activities.LoginActivity;
import feup.cm.traintickets.runnables.TokenRefreshTask;

public class BaseActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(!tokenValid()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private boolean tokenValid() {
        sharedPrefs = getSharedPreferences("feup.cm.traintickets", Context.MODE_PRIVATE);
        String token = sharedPrefs.getString("LOGIN_TOKEN", "");
        Date expires = new Date(sharedPrefs.getLong("LOGIN_EXPIRES", 0L));

        if(token.isEmpty() || expires.after(new Date())) {
            String refresh = sharedPrefs.getString("LOGIN_REFRESH", "");
            TokenRefreshTask task = new TokenRefreshTask(refresh);
            task.run();
            if(task.getToken() != null) {
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("LOGIN_TOKEN", task.getToken().getToken());
                editor.putLong("LOGIN_EXPIRES", task.getToken().getExpires().getTime());
                editor.apply();
            }
            else return false;
        }
        return true;
    }

    protected SharedPreferences getSharedPrefs() {
        return sharedPrefs;
    }
}
