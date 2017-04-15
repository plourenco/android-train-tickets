package feup.cm.traintickets.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import feup.cm.traintickets.R;
import feup.cm.traintickets.adapters.TicketListAdapter;
import feup.cm.traintickets.datamanagers.SeatDataManager;
import feup.cm.traintickets.datamanagers.StationDataManager;
import feup.cm.traintickets.datamanagers.StepDataManager;
import feup.cm.traintickets.datamanagers.TrainDataManager;
import feup.cm.traintickets.datamanagers.TripDataManager;
import feup.cm.traintickets.models.StationModel;
import feup.cm.traintickets.models.TicketModel;
import feup.cm.traintickets.models.TripModel;
import feup.cm.traintickets.runnables.SeatGetTask;
import feup.cm.traintickets.runnables.StationGetTask;
import feup.cm.traintickets.runnables.StepGetTask;
import feup.cm.traintickets.runnables.TokenRefreshTask;
import feup.cm.traintickets.runnables.TrainGetTask;
import feup.cm.traintickets.runnables.TripGetTask;
import feup.cm.traintickets.runnables.UserLoginTask;
import feup.cm.traintickets.sqlite.SQLiteManager;
import feup.cm.traintickets.sqlite.TicketBrowser;
import feup.cm.traintickets.util.StringCheck;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;
    private UserLoginTask mAuthTask = null;
    private SharedPreferences sharedPrefs;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(null);
        }
        // Login Form
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    formLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                formLogin();
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterClick();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        sharedPrefs = getSharedPreferences("feup.cm.traintickets", Context.MODE_PRIVATE);
        String token = sharedPrefs.getString("LOGIN_TOKEN", "");
        Date expires = new Date(sharedPrefs.getLong("LOGIN_EXPIRES", 0L));
        int userid = sharedPrefs.getInt("LOGIN_ID", 0);

        cache(token);

        if(!token.isEmpty() && expires.after(new Date()) && userid != 0) {
            successRedirect();
        }
        else {
            refreshToken();
        }
    }

    /**
     * Populate autocomplete
     */
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Check if it's possible to request the contacts permission
     * @return boolean
     */
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void formLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!StringCheck.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!StringCheck.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            login(email, password);
        }
    }

    /**
     * Simply login with the API
     * @param email String
     * @param password String
     */
    private void login(final String email, final String password) {
        mAuthTask = new UserLoginTask(email, password) {
            @Override
            protected void onPostExecute(Boolean success) {
                mAuthTask = null;
                showProgress(false);
                if (success) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("LOGIN_TOKEN", token.getToken());
                    editor.putString("LOGIN_REFRESH", token.getRefresh());
                    editor.putLong("LOGIN_EXPIRES", token.getExpires().getTime());
                    editor.putInt("LOGIN_ID", token.getUserId());
                    editor.apply();

                    successRedirect();
                } else {
                    mPasswordView.setError(getString(R.string.error_incorrect_credentials));
                    mPasswordView.requestFocus();
                }
            }
            @Override
            protected void onCancelled() {
                mAuthTask = null;
                showProgress(true);
            }
        };
        mAuthTask.execute((Void) null);
    }

    /**
     * Start new register activity with a shared element transition (transition_icon)
     */
    public void onRegisterClick() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Retrieve data rows for the device user's 'profile' contact.
     * @param i int
     * @param bundle Bundle
     * @return Loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    /**
     * Prepare emails to forward to autocomplete
     * @param cursorLoader Loader
     * @param cursor Cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * Create an adapter to tell the AutoCompleteTextView what to show in its dropdown list
     * @param emailAddressCollection List
     */
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    protected void refreshToken() {
        String refresh = sharedPrefs.getString("LOGIN_REFRESH", "");
        TokenRefreshTask task = new TokenRefreshTask(refresh) {
            @Override
            protected void onPostExecute(Boolean success) {
                if(success) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("LOGIN_TOKEN", getToken().getToken());
                    editor.putLong("LOGIN_EXPIRES", getToken().getExpires().getTime());
                    editor.apply();
                    
                    successRedirect();
                }
            }
        };
        task.execute((Void) null);
    }

    private void successRedirect() {
        Intent intent = new Intent(getApplicationContext(), TicketListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Cache persistent data
     */
    protected void cache(String token) {

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
