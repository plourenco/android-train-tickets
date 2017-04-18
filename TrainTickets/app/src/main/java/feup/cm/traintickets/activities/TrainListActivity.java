package feup.cm.traintickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.adapters.TrainListAdapter;
import feup.cm.traintickets.models.TrainTripModel;
import feup.cm.traintickets.runnables.TrainTripGetTask;

public class TrainListActivity extends BaseActivity {

    private List<TrainTripModel> trains = new ArrayList<TrainTripModel>();
    private TextView noTrainsView;
    private ListView trainListView;

    private TrainListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        trainListView = (ListView) findViewById(R.id.listViewTrainList);
        noTrainsView = (TextView) findViewById(R.id.no_available_trains);
        final int origin = getIntent().getIntExtra("origin", -1);
        final int destination = getIntent().getIntExtra("destination", -1);
        final String date = getIntent().getStringExtra("date");

        trainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), BuyTicketActivity.class);
                TrainTripModel model = (TrainTripModel) parent.getItemAtPosition(position);
                if (model != null) {
                    intent.putExtra("origin", origin);
                    intent.putExtra("destination", destination);
                    intent.putExtra("date", date);
                    intent.putExtra("train_id", model.getId());
                    intent.putExtra("train_desc", String.format(Locale.UK, "%1$s (%2$d)",
                            model.getDepartureTime().toString(), model.getId()));
                    startActivity(intent);
                }
            }
        });

        TrainTripGetTask task = new TrainTripGetTask(getToken(), origin, destination) {

            @Override
            protected void onPostExecute(Boolean success) {
                trains = getTraintrips();
                adapter = new TrainListAdapter((ArrayList<TrainTripModel>) trains, TrainListActivity.this);
                trainListView.setAdapter(adapter);
                if (trains == null || trains.isEmpty()) {
                    noTrainsView.setVisibility(View.VISIBLE);
                }
            }
        };
        noTrainsView.setVisibility(View.INVISIBLE);
        if (origin != -1 && destination != -1) {
            task.execute((Void) null);
        }
    }

    @Override
    protected int getBottomNavId() {
        return R.id.action_buyticket;
    }
}