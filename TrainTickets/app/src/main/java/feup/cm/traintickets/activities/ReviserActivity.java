package feup.cm.traintickets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import feup.cm.traintickets.R;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.runnables.DirectionGetTask;
import feup.cm.traintickets.runnables.DownloadGetTask;
import feup.cm.traintickets.runnables.TripGetTask;
import feup.cm.traintickets.sqlite.TicketReviserBrowser;

public class ReviserActivity extends AppCompatActivity {

    Spinner direction;
    Spinner trip;

    Button dlTicket;
    Button scan;

    String directionToSend;
    String tripToSend;

    List<String> directions = new ArrayList<>();
    List<String> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviser);

        direction = (Spinner)findViewById(R.id.spDirection);
        trip = (Spinner)findViewById(R.id.spTrip);
        dlTicket = (Button)findViewById(R.id.dlTickets);
        scan = (Button)findViewById(R.id.scan);

        dlTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); //format time
                String time = df.format(Calendar.getInstance().getTime());
                downloadTickets(directionToSend, tripToSend, Date.valueOf(time));
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QRCodeReaderActivity.class);
                startActivity(intent);
            }
        });

        populateDirections();
        populateTrips();
    }

    private void populateTrips() {
        TripGetTask tripGetTask = new TripGetTask("") {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success){
                    for (TripModel tm : getTrips()){
                        if (tripList.indexOf(tm.getDescription()) == -1) {
                            tripList.add(tm.getDescription());
                        }
                    }
                    setTrips();
                }
            }
        };
        tripGetTask.execute((Void) null);
    }

    private void setTrips() {
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(ReviserActivity.this, R.layout.spinner_view, tripList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trip.setAdapter(dataAdapter2);
        trip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tripToSend = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tripToSend = parent.getSelectedItem().toString();
            }
        });
    }

    private void downloadTickets(String direction, String trip, Date date) {
        direction = direction.trim().replace(" ", "%20");
        trip = trip.trim().replace(" ", "%20");
        DownloadGetTask downloadGetTask = new DownloadGetTask("", direction, trip, date) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    List<TicketModel> tickets = getTickets();
                    TicketReviserBrowser ticketReviserBrowser = new TicketReviserBrowser(getApplicationContext());
                    for (TicketModel t : tickets) {
                        ticketReviserBrowser.create(t);
                    }
                    Toast.makeText(getApplicationContext(), "Downloaded " + tickets.size() + " tickets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        downloadGetTask.execute((Void) null);
    }

    private void populateDirections(){
        final DirectionGetTask directionGetTask = new DirectionGetTask("") {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success){
                    for (String s : getDirections()) {
                        directions.add(s);
                    }
                    setDirections();
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        directionGetTask.execute((Void) null);
    }

    private void setDirections() {
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(ReviserActivity.this, R.layout.spinner_view, directions);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        direction.setAdapter(dataAdapter1);
        direction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                directionToSend = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                directionToSend = parent.getSelectedItem().toString();
            }
        });
    }
}
