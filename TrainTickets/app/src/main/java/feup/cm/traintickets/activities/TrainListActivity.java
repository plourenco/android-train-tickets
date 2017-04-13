package feup.cm.traintickets.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.adapters.TrainListAdapter;
import feup.cm.traintickets.models.TrainTripModel;

public class TrainListActivity extends BaseActivity {


    public ListView listaComboios;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        ListView listaComboios = (ListView) findViewById(R.id.listViewTrainList);

        //List<TrainTripModel> listTrain = new ArrayList<TrainTripModel>();

       TrainTripModel data[]= new TrainTripModel[]
               {
                       new TrainTripModel(1,"Morning Train",Time.valueOf("8:00:00"),Time.valueOf("10:00:00"),120),
                       new TrainTripModel(2,"Noon Train",Time.valueOf("12:00:00"),Time.valueOf("14:00:00"),120),
                       new TrainTripModel(3,"After Noon Train",Time.valueOf("16:00:00"),Time.valueOf("18:00:00"),120),
                       new TrainTripModel(4,"Night Train",Time.valueOf("20:00:00"),Time.valueOf("22:00:00"),120)
               };

        TrainListAdapter adp= new TrainListAdapter(
                new ArrayList<TrainTripModel>(Arrays.asList(data)), getApplicationContext());
        listaComboios.setAdapter(adp);


    }

    /*

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return id.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View listaComboios, ViewGroup parent) {
            listaComboios =getLayoutInflater().inflate(R.layout.content_train_list,null);

            TextView txtDescription=(TextView)findViewById(R.id.train_ticket_title_txt);
            TextView txtId=(TextView)findViewById(R.id.train_ticket_id_txt);
            TextView txtDepartureTime=(TextView)findViewById(R.id.train_list_departure_txt);
            TextView txtArrivalTime=(TextView)findViewById(R.id.train_list_arrival_txt);
            TextView txtDuration=(TextView)findViewById(R.id.train_list_duration_txt);

            txtDescription.setText(description[position]);
            txtId.setText(id[position]);
            txtDepartureTime.setText(departureTime[position]);
            txtArrivalTime.setText(arrivalTime[position]);
            txtDuration.setText(duration[position]);

            return null;
        }

    }
    */

}