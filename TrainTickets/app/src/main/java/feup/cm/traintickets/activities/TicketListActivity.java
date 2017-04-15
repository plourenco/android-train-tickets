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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
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
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.runnables.StationGetTask;
import feup.cm.traintickets.runnables.TicketGetTask;
import feup.cm.traintickets.runnables.TicketUserGetTask;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.nav_bottom);
        bottomNav.setSelectedItemId(R.id.action_tickets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ticket_list, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_USER_ID = "user_id";
        private static final String ARG_USER_TOKEN = "user_token";

        private List<TicketModel> tickets = new ArrayList<TicketModel>();
        private ListView listView;
        private ProgressBar progressBar;
        private TextView noTicketsView;
        private TicketListAdapter adapter;

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String token, int userId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_USER_ID, userId);
            args.putString(ARG_USER_TOKEN, token);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ticket_list, container, false);
            listView = (ListView) rootView.findViewById(R.id.list);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            noTicketsView = (TextView) rootView.findViewById(R.id.no_available_tickets);
            Bundle args = getArguments();
            int userId = args.getInt(ARG_USER_ID, 0);
            String token = args.getString(ARG_USER_TOKEN, "");

            TicketUserGetTask task = new TicketUserGetTask(token, userId) {
                @Override
                protected void onPostExecute(Boolean success) {
                    tickets = getTickets();
                    adapter = new TicketListAdapter(tickets, getActivity().getApplicationContext());
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TicketModel dataModel = tickets.get(position);
                            Intent intent = new Intent(getActivity().getApplicationContext(),
                                    SingleTicketActivity.class);
                            intent.putExtra("TICKET_MODEL", new Gson().toJson(dataModel));
                            startActivity(intent);
                        }
                    });
                    progressBar.setVisibility(View.INVISIBLE);
                    if (tickets == null || tickets.isEmpty()) {
                        noTicketsView.setVisibility(View.VISIBLE);
                    }
                }
            };
            progressBar.setVisibility(View.VISIBLE);
            noTicketsView.setVisibility(View.INVISIBLE);
            task.execute((Void) null);
            return rootView;
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
            return PlaceholderFragment.newInstance(position + 1, token, userid);
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
