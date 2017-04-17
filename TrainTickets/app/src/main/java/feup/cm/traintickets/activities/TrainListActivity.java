package feup.cm.traintickets.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.adapters.TrainListAdapter;
import feup.cm.traintickets.models.TrainTripModel;
import feup.cm.traintickets.runnables.TrainTripGetTask;

public class TrainListActivity extends BaseActivity {

    private List<TrainTripModel> trains = new ArrayList<TrainTripModel>();
    private ListView trainList;
    private TrainListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        trainList = (ListView) findViewById(R.id.listViewTrainList);

        TrainTripGetTask task = new TrainTripGetTask(getToken(),1,4) {


            @Override
            protected void onPostExecute(Boolean success) {
                trains = getTraintrips();
                adapter = new TrainListAdapter((ArrayList<TrainTripModel>) trains, TrainListActivity.this);
                trainList.setAdapter(adapter);

            }
        };
        task.execute((Void)null);

    }

    @Override
    protected int getBottomNavId() {
        return R.id.action_buyticket;
    }
}