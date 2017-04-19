package feup.cm.traintickets.util;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import feup.cm.traintickets.datamanagers.SeatDataManager;
import feup.cm.traintickets.datamanagers.StationDataManager;
import feup.cm.traintickets.datamanagers.StepDataManager;
import feup.cm.traintickets.datamanagers.TrainDataManager;
import feup.cm.traintickets.datamanagers.TripDataManager;
import feup.cm.traintickets.runnables.SeatGetTask;
import feup.cm.traintickets.runnables.StationGetTask;
import feup.cm.traintickets.runnables.StepGetTask;
import feup.cm.traintickets.runnables.TrainGetTask;
import feup.cm.traintickets.runnables.TripGetTask;

public class ActivityHelper {

    private static List<String> prefsClean = new ArrayList<String>();

    static {
        addPref("LOGIN_TOKEN",
                "LOGIN_REFRESH",
                "LOGIN_EXPIRES",
                "LOGIN_ID",
                "LOGIN_ROLE",
                "CC_NUMBER",
                "CC_DATE",
                "CVV2"
        );
    }

    public static void cleanPrefs(SharedPreferences sharedPrefs) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        for(String key : prefsClean) {
            editor.remove(key);
        }
        editor.apply();
    }

    public static void addPref(String... keys) {
        Collections.addAll(prefsClean, keys);
    }

    /**
     * Cache persistent data
     */
    public static void cache(String token) {

        final TripGetTask tripGetTask = new TripGetTask(token) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success)
                    TripDataManager.setTrips(getTrips());
            }

            @Override
            protected void onCancelled() {

            }
        };
        final StepGetTask stepGetTask = new StepGetTask(token) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    StepDataManager.setSteps(getSteps());
                    tripGetTask.execute((Void) null);
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        final TrainGetTask trainGetTask = new TrainGetTask(token) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    TrainDataManager.setTrains(getTrains());
                    stepGetTask.execute((Void) null);
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        final SeatGetTask seatGetTask = new SeatGetTask(token) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    SeatDataManager.setSeats(getSeats());
                    trainGetTask.execute((Void) null);
                }
            }

            @Override
            protected void onCancelled() {

            }
        };
        StationGetTask stationGetTask = new StationGetTask(token) {
            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    StationDataManager.setStations(getStations());
                    seatGetTask.execute((Void) null);
                }

            }

            @Override
            protected void onCancelled() {

            }
        };

        stationGetTask.execute((Void) null);
    }
}
