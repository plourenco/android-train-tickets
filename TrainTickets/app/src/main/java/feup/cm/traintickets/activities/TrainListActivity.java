package feup.cm.traintickets.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import feup.cm.traintickets.R;
import feup.cm.traintickets.models.TrainTripModel;

public class TrainListActivity extends AppCompatActivity {

    public ListView listaComboios;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        ListView listaComboios = (ListView) findViewById(R.id.listViewTrainList);

        List<TrainTripModel> listTrain = new ArrayList<TrainTripModel>();


    }



}