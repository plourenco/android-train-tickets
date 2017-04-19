package feup.cm.traintickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import feup.cm.traintickets.activities.BuyTicketActivity;
import feup.cm.traintickets.activities.LoginActivity;
import feup.cm.traintickets.activities.ReviserActivity;
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

        if (!tokenValid()) {
            refreshToken();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav = (BottomNavigationViewEx) findViewById(R.id.nav_bottom);
        if (bottomNav != null) {
            bottomNav.enableAnimation(false);
            bottomNav.enableShiftingMode(false);
            bottomNav.enableItemShiftingMode(true);
            bottomNav.setTextVisibility(false);
            bottomNav.setIconSize(26, 26);
            int size = (int) (getResources().getDimension(R.dimen.bottom_bar_height) /
                    getResources().getDisplayMetrics().density);
            bottomNav.setItemHeight(size);
            if (getBottomNavId() != 0) bottomNav.setSelectedItemId(getBottomNavId());
            // Fix bottom Navigation top margin
            ViewGroup main = getMainLayout();
            if (main != null) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                        main.getLayoutParams();
                params.setMargins(0, 0, 0, bottomNav.getItemHeight());
                main.requestLayout();
            }
            bottomNav.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            Intent intent;
                            Class[] nav = {
                                    BuyTicketActivity.class, TicketListActivity.class,
                                    TimetableActivity.class, SettingsActivity.class
                            };

                            switch (item.getItemId()) {
                                case R.id.action_buyticket:
                                    if (!(nav[0].isInstance(BaseActivity.this))) {
                                        intent = new Intent(getApplicationContext(), BuyTicketActivity.class);
                                        startActivity(intent);
                                        overrideTransition(nav, 0);
                                    }
                                    break;
                                case R.id.action_tickets:
                                    if (!(nav[1].isInstance(BaseActivity.this))) {
                                        intent = new Intent(getApplicationContext(), TicketListActivity.class);
                                        startActivity(intent);
                                        overrideTransition(nav, 1);
                                    }
                                    break;
                                case R.id.action_timetables:
                                    if (!(nav[2].isInstance(BaseActivity.this))) {
                                        intent = new Intent(getApplicationContext(), TimetableActivity.class);
                                        startActivity(intent);
                                        overrideTransition(nav, 2);
                                    }
                                    break;
                                case R.id.action_settings:
                                    if (!(nav[3].isInstance(BaseActivity.this))) {
                                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                                        overrideTransition(nav, 3);
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

    /**
     * This method calculates either the animation should go left or right
     *
     * @param nav  Class[]
     * @param dest int
     */
    private void overrideTransition(Class[] nav, int dest) {
        int current = 0;
        for (int i = 0; i < nav.length; i++) {
            if (nav[i].isInstance(BaseActivity.this)) {
                current = i;
            }
        }
        if (current < dest) {
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
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
                if (success) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("LOGIN_TOKEN", getToken().getToken());
                    editor.putLong("LOGIN_EXPIRES", getToken().getExpires().getTime());
                    editor.apply();
                } else {
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

    protected String getToken() {
        return token;
    }

    protected int getUserId() {
        return userId;
    }

    /**
     * Override with an empty body if the activity has no bottomNavigation
     */
    protected abstract int getBottomNavId();

    /**
     * Get the main layout in order to apply a margin to prevent bottom nav overlap
     * Return null if you don't need it
     */
    protected ViewGroup getMainLayout() {
        return null;
    }

    protected void logout() {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove("LOGIN_TOKEN");
        editor.remove("LOGIN_REFRESH");
        editor.remove("LOGIN_EXPIRES");
        editor.remove("LOGIN_ID");
        editor.remove("LOGIN_EMAIL");
        editor.remove("LOGIN_PASSWORD");
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("INTERNET", "Error checking internet connection", e);
            }
        } else {
            Log.d("INTERNET", "No network available!");
        }
        return false;
    }

    protected boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
