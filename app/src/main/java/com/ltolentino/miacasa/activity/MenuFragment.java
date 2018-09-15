package com.ltolentino.miacasa.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ltolentino.miacasa.MainActivity;
import com.ltolentino.miacasa.R;

public class MenuFragment extends Fragment implements View.OnClickListener {

    LinearLayout home;
    LinearLayout user;
    LinearLayout newPost;
    LinearLayout logOut;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View real = inflater.inflate(R.layout.fragment_menu, container, false);

        home = real.findViewById(R.id.home);
        user = real.findViewById(R.id.user);
        newPost = real.findViewById(R.id.new_post);
        logOut = real.findViewById(R.id.log_out);

        home.setOnClickListener(this);
        user.setOnClickListener(this);
        newPost.setOnClickListener(this);
        logOut.setOnClickListener(this);

        return real;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new HomeFragment())
                        .commit();
                break;
            case R.id.user:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new UserFragment())
                        .commit();
                break;
            case R.id.new_post:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new PostFragment())
                        .commit();
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
        }
    }
}
