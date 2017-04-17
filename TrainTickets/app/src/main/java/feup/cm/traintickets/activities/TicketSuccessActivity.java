package feup.cm.traintickets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import feup.cm.traintickets.FullActivity;
import feup.cm.traintickets.R;

public class TicketSuccessActivity extends FullActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_success);
    }

    @Override
    protected int getBottomNavId() {
        return 0;
    }
}
