package feup.cm.traintickets.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.kogitune.activity_transition.ActivityTransition;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.adapters.TicketListAdapter;
import feup.cm.traintickets.datamanagers.SeatDataManager;
import feup.cm.traintickets.datamanagers.StationDataManager;
import feup.cm.traintickets.datamanagers.TripDataManager;
import feup.cm.traintickets.fragments.ActiveTicketsFragment;
import feup.cm.traintickets.fragments.ExpiredTicketsFragment;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.runnables.StationGetTask;
import feup.cm.traintickets.runnables.TicketGetTask;
import feup.cm.traintickets.runnables.TicketUserGetTask;
import feup.cm.traintickets.sqlite.TicketBrowser;
import feup.cm.traintickets.util.Callback;

public class TicketListActivity extends BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private boolean authCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.nav_bottom);
        bottomNav.setSelectedItemId(R.id.action_tickets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                getToken(), getUserId());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getBottomNavId() {
        return R.id.action_tickets;
    }

    @Override
    protected ViewGroup getMainLayout() {
        return (ViewGroup) findViewById(R.id.list);
    }

    @Override
    public boolean authCheck() {
        return authCheck;
    }

    public void setAuthCheck(boolean auth) { this.authCheck = auth; }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void download(ListAdapter adpt) {
        if(adpt instanceof TicketListAdapter) {
            TicketListAdapter adapter = (TicketListAdapter) adpt;
            TicketBrowser ticketBrowser = new TicketBrowser(getApplicationContext());
            ticketBrowser.deleteAll();
            for (TicketModel ticket : adapter.getDataSet()) {
                try {
                    ticketBrowser.create(ticket);
                } catch (android.database.SQLException ignored) {
                }
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String token;
        private int userid;

        public SectionsPagerAdapter(FragmentManager fm, String token, int userid) {
            super(fm);
            this.token = token;
            this.userid = userid;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position) {
                case 1:
                    return ExpiredTicketsFragment.newInstance(position + 1, token, userid);
                default:
                    return ActiveTicketsFragment.newInstance(position + 1, token, userid);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.display_ticket_active);
                case 1:
                    return getString(R.string.display_ticket_expired);
            }
            return null;
        }
    }
}
