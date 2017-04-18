package feup.cm.traintickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Date;

import feup.cm.traintickets.activities.BuyTicketActivity;
import feup.cm.traintickets.activities.LoginActivity;
import feup.cm.traintickets.activities.SettingsActivity;
import feup.cm.traintickets.activities.TicketListActivity;
import feup.cm.traintickets.activities.TimetableActivity;
import feup.cm.traintickets.runnables.TokenRefreshTask;

public abstract class BaseActivity extends AppCompatActivity {

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
            if(getBottomNavId() != 0) bottomNav.setSelectedItemId(getBottomNavId());
            bottomNav.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    switch(item.getItemId()) {
                        case R.id.action_buyticket:
                            if(!(BaseActivity.this instanceof BuyTicketActivity)) {
                                intent = new Intent(getApplicationContext(), BuyTicketActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }
                            break;
                        case R.id.action_tickets:
                            if(!(BaseActivity.this instanceof TicketListActivity)) {
                                intent = new Intent(getApplicationContext(), TicketListActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                            }
                            break;
                        case R.id.action_settings:
                            if (!(BaseActivity.this instanceof SettingsActivity)) {
                                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                            }
                            break;
                        case R.id.action_timetables:
                            if (!(BaseActivity.this instanceof TimetableActivity)) {
                                intent = new Intent(getApplicationContext(), TimetableActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                            }
                            break;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
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

    /**
     * Override with an empty body if the activity has no bottomNavigation
     */
    protected abstract int getBottomNavId();
}
