package com.ltolentino.miacasa.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.ltolentino.miacasa.R;
import com.ltolentino.miacasa.firebase.FirebaseAbsActivityUtil;

import java.util.List;

public class MainUserActivity extends FirebaseAbsActivityUtil {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.menu, new MenuFragment())
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, new HomeFragment())
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f instanceof PostFragment) {
                    f.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
