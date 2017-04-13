package feup.cm.traintickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Date;

import feup.cm.traintickets.activities.LoginActivity;
import feup.cm.traintickets.datamanagers.SeatDataManager;
import feup.cm.traintickets.datamanagers.StationDataManager;
import feup.cm.traintickets.datamanagers.StepDataManager;
import feup.cm.traintickets.datamanagers.TrainDataManager;
import feup.cm.traintickets.datamanagers.TripDataManager;
import feup.cm.traintickets.runnables.SeatGetTask;
import feup.cm.traintickets.runnables.StationGetTask;
import feup.cm.traintickets.runnables.StepGetTask;
import feup.cm.traintickets.runnables.TokenRefreshTask;
import feup.cm.traintickets.runnables.TrainGetTask;
import feup.cm.traintickets.runnables.TripGetTask;

public class BaseActivity extends AppCompatActivity {

    private BottomNavigationViewEx bottomNav;
    private SharedPreferences sharedPrefs;
    private int userId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!tokenValid()) {
            refreshToken();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav = (BottomNavigationViewEx) findViewById(R.id.nav_bottom);
        if(bottomNav != null) {
            bottomNav.enableAnimation(false);
            bottomNav.enableShiftingMode(false);
            bottomNav.enableItemShiftingMode(true);
            bottomNav.setTextVisibility(false);
            bottomNav.setIconSize(26, 26);
            bottomNav.setItemHeight(125);
            bottomNav.setSelectedItemId(R.id.action_settings);
        }
    }

    protected boolean tokenValid() {
        sharedPrefs = getSharedPreferences("feup.cm.traintickets", Context.MODE_PRIVATE);
        this.token = sharedPrefs.getString("LOGIN_TOKEN", "");
        this.userId = sharedPrefs.getInt("LOGIN_ID", 0);
        Date expires = new Date(sharedPrefs.getLong("LOGIN_EXPIRES", 0L));

        return !(token.isEmpty() || expires.before(new Date()));
    }

    protected void refreshToken() {
        String refresh = sharedPrefs.getString("LOGIN_REFRESH", "");
        TokenRefreshTask task = new TokenRefreshTask(refresh) {
            @Override
            protected void onPostExecute(Boolean success) {
                if(success) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("LOGIN_TOKEN", getToken().getToken());
                    editor.putLong("LOGIN_EXPIRES", getToken().getExpires().getTime());
                    editor.apply();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        task.execute((Void) null);
    }

    protected SharedPreferences getSharedPrefs() {
        return sharedPrefs;
    }

    protected String getToken() { return token; }

    protected int getUserId() { return userId; }
}
