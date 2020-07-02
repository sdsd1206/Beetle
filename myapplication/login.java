package com.example.jack.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.AuthProvider;

public class login extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private static int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        fb();
        googleConfig();

//        Button login = (Button) findViewById(R.id.button2);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(login.this, buy.class);
//                startActivity(intent);
//            }
//        });
//
//        Button login2 = (Button) findViewById(R.id.button);
//        login2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(login.this, sell.class);
//                startActivity(intent);
//            }
//        });

//        Button signout = (Button) findViewById(R.id.button12);
//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firebaseSingOut(v);
//            }
//        });
    }

    public void fb() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile"); //設定權限

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        // 登入回傳資訊處理
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //成功登入 Facebook 時的處理
                AccessToken token = loginResult.getAccessToken();
                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 成功登入顯示使用者訊
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
//                                    message.setText(user.getDisplayName());
                                } else {
                                    // 登入失敗顯示
                                    Toast.makeText(login.this, "登入失",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancel() {
                // App code 取消登入時的處理
            }

            @Override
            public void onError(FacebookException exception) {
                // App code 登入錯誤時的處理
            }
        });
    }


    public void googleConfig() {
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode != RESULT_CANCELED) {
            if (requestCode == RC_SIGN_IN && data != null) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            } else {
                //fb
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(login.this, "Failed", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        } else {
//                            Toast.makeText(login.this, "SingIn name:" + account.getDisplayName(), Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
//            //取得使用者名稱
//            String name = user.getDisplayName();
//            //取得使用者 email
//            String email = user.getEmail();
//            //取得使用者頭像位址
//            Uri photoUrl = user.getPhotoUrl();
//            //檢查是否有經過 email 驗證
//            boolean emailVerified = user.isEmailVerified();
//            //取得使用者 uid
//            String uid = user.getUid();

//            Toast.makeText(login.this, uid, Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(login.this, buy.class);
            startActivity(intent);
        } else {

        }
    }

    public void firebaseSingOut(View view) {
        // Firebase 登出
        mAuth.signOut();

        // Google 登出
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignIn.getClient(this, gso).signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(login.this, "SingOut", Toast.LENGTH_LONG).show();
            }
        });
    }
}
