package feup.cm.traintickets.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.adapters.TimetableAdapter;
import feup.cm.traintickets.datamanagers.TripDataManager;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.StepModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.runnables.TripGetTask;

public class TimetableActivity extends BaseActivity {

    ExpandableListAdapter expAdapter;
    ExpandableListView expView;
    List<TripModel> trips;
    private HashMap<TripModel, List<StepModel>> steps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        expView = (ExpandableListView) findViewById(R.id.listViewTimetable);

        trips = TripDataManager.getTrips();
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

   /* private ArrayList<TripModel> listDataHeader;
    private ArrayList<ArrayList<StepModel>> listDatachild;

    ExpandableListAdapter listAdapter;
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        //getListView
        expandableListView = (ExpandableListView) findViewById(R.id.listViewTimetable);


        listDataHeader = new ArrayList<TripModel>();
        listDatachild = new ArrayList<ArrayList<StepModel>>();

        new TripGetTask(getToken()) {
            @Override
            protected void onPostExecute(Boolean success) {
                listAdapter = new TimetableAdapter(TimetableActivity.this, listDataHeader, listDatachild);

                expandableListView.setAdapter(listAdapter);

                expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return false;
                    }
                });
            }
        }.execute();

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                /*Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDatachild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }*/


}
