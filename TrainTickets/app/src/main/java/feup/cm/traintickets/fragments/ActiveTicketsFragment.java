package feup.cm.traintickets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.activities.SingleTicketActivity;
import feup.cm.traintickets.activities.TicketListActivity;
import feup.cm.traintickets.adapters.TicketListAdapter;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.runnables.TicketUserGetTask;
import feup.cm.traintickets.sqlite.TicketBrowser;
import feup.cm.traintickets.util.Callback;

public class ActiveTicketsFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_USER_TOKEN = "user_token";

    private ListView listView;
    private ProgressBar progressBar;
    private TextView noTicketsView;
    private TicketListAdapter adapter;

    public ActiveTicketsFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ActiveTicketsFragment newInstance(int sectionNumber, String token, int userId) {
        ActiveTicketsFragment fragment = new ActiveTicketsFragment();
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
        final int userId = args.getInt(ARG_USER_ID, 0);
        final String token = args.getString(ARG_USER_TOKEN, "");

        final TicketBrowser ticketBrowser = new TicketBrowser(getActivity());
        BaseActivity base = (BaseActivity) getActivity();
        noTicketsView.setVisibility(View.INVISIBLE);

        base.hasActiveInternetConnection(getActivity(), new Callback() {

            TicketListActivity activity = (TicketListActivity) getActivity();
            List<TicketModel> ticketsList = new ArrayList<TicketModel>();

            @Override
            public void call(Boolean success) {
                if (success && userId != 0) { // has internet
                    // Normal verification scheme skipped before
                    activity.setAuthCheck(true);
                    if(!activity.tokenValid()) {
                        activity.refreshToken();
                        getActivity().recreate();
                        return;
                    }
                    TicketUserGetTask task = new TicketUserGetTask(token, userId) {
                        @Override
                        protected void onPostExecute(Boolean success) {
                            if (success) {
                                ticketsList = tickets;
                            }
                            initView(ticketsList);
                            // Resync existing tickets
                            activity.download(listView.getAdapter());
                        }
                    };
                    task.execute((Void) null);
                } else { // has no internet
                    ticketsList = ticketBrowser.getAll();
                    initView(ticketsList);
                }
            }
        });
        return rootView;
    }

    protected void initView(final List<TicketModel> tickets) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new TicketListAdapter(tickets, getActivity().getApplicationContext());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TicketModel dataModel = tickets.get(position);
                        if (dataModel != null) {
                            Intent intent = new Intent(getActivity().getApplicationContext(),
                                    SingleTicketActivity.class);
                            intent.putExtra("TICKET_MODEL", new Gson().toJson(dataModel));
                            startActivity(intent);
                        }
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
                if (tickets == null || tickets.isEmpty()) {
                    noTicketsView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
