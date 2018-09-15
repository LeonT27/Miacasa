package com.ltolentino.miacasa;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.ltolentino.miacasa.Util.AuthUtil;
import com.ltolentino.miacasa.activity.LoadingFragment;
import com.ltolentino.miacasa.activity.LoginFragment;
import com.ltolentino.miacasa.activity.SignUpActivity;
import com.ltolentino.miacasa.firebase.FirebaseAbsActivityUtil;

import java.util.ArrayList;

public class MainActivity extends FirebaseAbsActivityUtil {
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, new LoadingFragment())
                .commit();


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, new LoginFragment())
                        .commit();
            }
        }, 5000);
    }
}
