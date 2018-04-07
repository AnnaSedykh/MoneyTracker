package com.annasedykh.moneytracker.auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.annasedykh.moneytracker.app.App;
import com.annasedykh.moneytracker.main.MainActivity;
import com.annasedykh.moneytracker.R;
import com.annasedykh.moneytracker.api.Api;
import com.annasedykh.moneytracker.api.AuthResult;
import com.annasedykh.moneytracker.api.Result;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 321;

    private GoogleSignInClient googleSignInClient;

    private Api api;
    private App app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        if (googleSignInClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        app = (App) getApplication();
        api = app.getApi();

        boolean isLogoutIntent = getIntent().getBooleanExtra(MainActivity.LOGOUT, false);

        if(app.isAuthorized() && isLogoutIntent){
            logout();
        }

        Button button = findViewById(R.id.sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void logout() {

        googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Call<Result> call = app.getApi().logout();
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        if (result != null && result.status.equals(getString(R.string.success_msg))) {
                            app.clearAuthToken();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            updateUI(account);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            showError(getString(R.string.null_account_error_msg));
            return;
        }

        String id = account.getId();
        api.auth(id).enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(@NonNull Call<AuthResult> call, @NonNull Response<AuthResult> response) {
                AuthResult result = response.body();
                if (result != null) {
                    app.saveAuthToken(result.token);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable t) {
                showError(getString(R.string.auth_failed_error_msg) + t.getMessage());
            }
        });

    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
