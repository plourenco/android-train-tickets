package feup.cm.traintickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Date;
import java.util.List;

import feup.cm.traintickets.activities.LoginActivity;
import feup.cm.traintickets.controllers.SeatController;
import feup.cm.traintickets.controllers.ServiceHandler;
import feup.cm.traintickets.datamanagers.SeatDataManager;
import feup.cm.traintickets.datamanagers.StationDataManager;
import feup.cm.traintickets.datamanagers.StepDataManager;
import feup.cm.traintickets.datamanagers.TrainDataManager;
import feup.cm.traintickets.datamanagers.TripDataManager;
import feup.cm.traintickets.models.SeatModel;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.StepModel;
import feup.cm.traintickets.models.TrainModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.runnables.SeatGetTask;
import feup.cm.traintickets.runnables.StationGetTask;
import feup.cm.traintickets.runnables.StepGetTask;
import feup.cm.traintickets.runnables.TokenRefreshTask;
import feup.cm.traintickets.runnables.TrainGetTask;
import feup.cm.traintickets.runnables.TripGetTask;
import feup.cm.traintickets.util.Callback;

public abstract class FullActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
