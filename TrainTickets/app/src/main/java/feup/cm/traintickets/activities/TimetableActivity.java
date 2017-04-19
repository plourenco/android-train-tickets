package feup.cm.traintickets.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.adapters.TimetableAdapter;
import feup.cm.traintickets.datamanagers.TripDataManager;
import feup.cm.traintickets.models.StepModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.runnables.TripGetTask;

public class TimetableActivity extends BaseActivity {

    private TextView noTrainsView;

    ExpandableListAdapter expAdapter;
    ExpandableListView expView;
    List<TripModel> trips;
    private HashMap<TripModel, List<StepModel>> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        expView = (ExpandableListView) findViewById(R.id.listViewTimetable);
        noTrainsView = (TextView) findViewById(R.id.no_available_trains);

        trips = TripDataManager.getTrips();
        if(trips != null && !trips.isEmpty()) {
            noTrainsView.setVisibility(View.GONE);
        }
        steps = new HashMap<>();
        for (TripModel t : trips) {
            steps.put(t, t.getSteps());
        }

        expAdapter = new TimetableAdapter(this, trips, steps);
        expView.setAdapter(expAdapter);
    }


    @Override
    protected int getBottomNavId() {
        return R.id.action_timetables;
    }

    @Override
    protected ViewGroup getMainLayout() {
        return (ViewGroup) findViewById(R.id.timetable_main_layout);
    }
}