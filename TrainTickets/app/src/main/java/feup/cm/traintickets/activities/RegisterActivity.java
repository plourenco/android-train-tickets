package feup.cm.traintickets.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import feup.cm.traintickets.BaseActivity;
import feup.cm.traintickets.R;
import feup.cm.traintickets.models.UserModel;
import feup.cm.traintickets.runnables.UserLoginTask;
import feup.cm.traintickets.runnables.UserRegisterTask;
import feup.cm.traintickets.util.ActivityHelper;
import feup.cm.traintickets.util.StringCheck;

/**
 * A register activity for new users.
 */
public class RegisterActivity extends AppCompatActivity {

    private UserRegisterTask mRegisterTask;
    private UserLoginTask mAuthTask;

    private EditText mUsernameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.action_register);
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(null);
        }
        // Form Views
        mUsernameView = (EditText) findViewById(R.id.username);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
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

    public void attemptRegister() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String confirmPassword = mConfirmPasswordView.getText().toString();

        final SharedPreferences sharedPrefs =
                getSharedPreferences("feup.cm.traintickets", Context.MODE_PRIVATE);

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

        if(TextUtils.isEmpty(confirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_field_required));
            focusView = mConfirmPasswordView;
            cancel = true;
        } else if(!password.equals(confirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_match_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

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
            final UserModel model = new UserModel(0, username, email, password,
                    getResources().getInteger(R.integer.role_user));

            if(mRegisterTask != null) {
                // Already registering
                return;
            }

            mRegisterTask = new UserRegisterTask(model) {
                @Override
                protected void onPostExecute(Boolean success) {
                    mRegisterTask = null;
                    showProgress(false);
                    if (success) {
                        // Login after register
                        final UserLoginTask mAuthTask = new UserLoginTask(email, password) {
                            @Override
                            protected void onPostExecute(Boolean success) {
                                RegisterActivity.this.mAuthTask = null;
                                showProgress(false);
                                if (success) {
                                    SharedPreferences.Editor editor = sharedPrefs.edit();
                                    editor.putString("LOGIN_TOKEN", token.getToken());
                                    editor.putString("LOGIN_REFRESH", token.getRefresh());
                                    editor.putLong("LOGIN_EXPIRES", token.getExpires().getTime());
                                    editor.putInt("LOGIN_ID", token.getUserId());
                                    editor.putInt("LOGIN_ROLE", token.getRole());
                                    editor.apply();

                                    ActivityHelper.cache(token.getToken());
                                    successRedirect(token.getRole());
                                }
                                else {
                                    forwardError();
                                }
                            }
                            @Override
                            protected void onCancelled() {
                                RegisterActivity.this.mAuthTask = null;
                                showProgress(true);
                                forwardError();
                            }
                        };
                        mAuthTask.execute((Void) null);
                    }
                    else {
                        mPasswordView.setError(getString(R.string.error_invalid_register));
                        mPasswordView.requestFocus();
                    }
                }
                @Override
                protected void onCancelled() {
                    mRegisterTask = null;
                    showProgress(true);
                }
            };
            mRegisterTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     * @param show boolean
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    private void forwardError() {
        // Something unexpected happened
        Toast.makeText(getApplicationContext(), "Something unexpected happened. Login manually",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void successRedirect(int role) {
        Intent intent = null;
        switch (role) {
            case 1:
                intent = new Intent(getApplicationContext(), ReviserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case 2:
                intent = new Intent(getApplicationContext(), TicketListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            default:
                break;
        }
        if (intent != null){
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Internal account error", Toast.LENGTH_SHORT).show();
        }
    }
}
