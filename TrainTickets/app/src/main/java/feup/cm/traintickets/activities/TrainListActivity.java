package feup.cm.traintickets.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.R;
import feup.cm.traintickets.adapters.TrainListAdapter;
import feup.cm.traintickets.models.TrainTripModel;

public class TrainListActivity extends AppCompatActivity {

    ArrayList<TrainTripModel> dataModels;
    ListView listView;
    private static TrainListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        listView = (ListView)findViewById(R.id.listViewTrainList) ;

        dataModels = new ArrayList<>();

        dataModels.add(new TrainTripModel(1,"Morning Train",Time.valueOf("8:00:00"),Time.valueOf("10:00:00"),120));
        dataModels.add(new TrainTripModel(2,"Noon Train",Time.valueOf("12:00:00"),Time.valueOf("14:00:00"),120));
        dataModels.add(new TrainTripModel(3,"After Noon Train",Time.valueOf("16:00:00"),Time.valueOf("18:00:00"),120));
        dataModels.add(new TrainTripModel(4,"Night Train",Time.valueOf("20:00:00"),Time.valueOf("22:00:00"),120));

        adapter= new TrainListAdapter(dataModels,getApplicationContext());
        listView.setAdapter(adapter);

    }


}