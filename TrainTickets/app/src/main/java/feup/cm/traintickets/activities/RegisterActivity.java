package feup.cm.traintickets.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;

/**
 * A register activity for new users.
 */
public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.action_register);
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(null);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            View sharedView = findViewById(R.id.imageView);
            String transitionName = getString(R.string.transition_icon);
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    this, sharedView, transitionName);
            getWindow().setExitTransition(null);
            startActivity(intent, transitionActivityOptions.toBundle());
        }
        else {
            startActivity(intent);
        }
    }
}
