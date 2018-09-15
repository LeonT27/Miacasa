package com.ltolentino.miacasa.firebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public abstract class FirebaseAbsFragmentUti extends Fragment {

    public FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }
}
