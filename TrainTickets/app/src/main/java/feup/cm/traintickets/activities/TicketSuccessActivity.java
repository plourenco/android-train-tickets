package feup.cm.traintickets.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import feup.cm.traintickets.FullActivity;
import feup.cm.traintickets.R;

public class TicketSuccessActivity extends FullActivity {

    private TextView priceView;
    private CoordinatorLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_success);

        priceView = (TextView) findViewById(R.id.done_price);
        layout = (CoordinatorLayout) findViewById(R.id.main_layout);

        Bundle extras = getIntent().getExtras();
        String price = extras.getString("price");
        priceView.setText(price);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TicketListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getBottomNavId() {
        return 0;
    }
}
