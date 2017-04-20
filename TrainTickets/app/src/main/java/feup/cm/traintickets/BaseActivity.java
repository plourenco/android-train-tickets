package feup.cm.traintickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import feup.cm.traintickets.controllers.ServiceHandler;
import feup.cm.traintickets.runnables.TokenRefreshTask;
import feup.cm.traintickets.util.ActivityHelper;
import feup.cm.traintickets.util.Callback;

public abstract class BaseActivity extends AppCompatActivity {

    private BottomNavigationViewEx bottomNav;
    private SharedPreferences sharedPrefs;
    private int userId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = getSharedPreferences("feup.cm.traintickets", Context.MODE_PRIVATE);
        this.token = sharedPrefs.getString("LOGIN_TOKEN", "");
        this.userId = sharedPrefs.getInt("LOGIN_ID", 0);

        if (authCheck() && !tokenValid()) {
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
            bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.
                    OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent;
                    Class[] nav = {
                            BuyTicketActivity.class, TicketListActivity.class,
                            TimetableActivity.class, SettingsActivity.class
                    };

                    if(!authCheck()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.guest_mode),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

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
    protected void overrideTransition(Class[] nav, int dest) {
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

    public boolean tokenValid() {
        Date expires = new Date(sharedPrefs.getLong("LOGIN_EXPIRES", 0L));

        return !(token.isEmpty() || expires.before(new Date()));
    }

    public void refreshToken() {
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

    /**
     * By rule, every activity needs to check if user is authenticated
     * but if by any means you don't need that, override this method
     */
    public boolean authCheck() {
        return true;
    }

    protected void logout() {
        ActivityHelper.cleanPrefs(sharedPrefs);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void hasActiveInternetConnection(Context context, final Callback callback) {
        if (isNetworkAvailable(context)) {
            AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection)
                                (new URL(ServiceHandler.apiUrl + "application.wadl")
                                        .openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(500);
                        urlc.connect();
                        return (urlc.getResponseCode() == 200);
                    }
                    catch (Exception e) {
                        Log.e("INTERNET", "Error checking internet connection");
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    callback.call(success);
                }
            };
            task.execute();
        }
        else {
            Log.d("INTERNET", "No network available!");
            callback.call(false);
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
