package com.ltolentino.miacasa.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ltolentino.miacasa.R;
import com.ltolentino.miacasa.Util.AuthUtil;
import com.ltolentino.miacasa.firebase.FirebaseAbsFragmentUti;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends FirebaseAbsFragmentUti implements View.OnClickListener {

    private static final String AUTH = "auth";

    TextInputLayout correoLayout;
    TextInputLayout passLayout;
    EditText correo;
    EditText pass;

    TextView error;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View real = inflater.inflate(R.layout.fragment_login, container, false);
        correoLayout = real.findViewById(R.id.correo_layoyut);
        passLayout = real.findViewById(R.id.pass_layout);
        passLayout.setPasswordVisibilityToggleEnabled(true);
        correo = real.findViewById(R.id.correo_text);
        pass = real.findViewById(R.id.pass_text);

        error = real.findViewById(R.id.error_login);

        real.findViewById(R.id.entrar).setOnClickListener(this);
        real.findViewById(R.id.registrarse).setOnClickListener(this);

        return real;
    }

    public void clickEntrar(final View v) {
        ArrayList<TextInputLayout> textInputLayouts = new ArrayList<>();
        textInputLayouts.add(correoLayout);
        textInputLayouts.add(passLayout);

        ArrayList<String> passwords = new ArrayList<>();
        passwords.add(pass.getText().toString());
        if(AuthUtil.validateLogin(correo.getText().toString(),passwords,textInputLayouts)) {
            auth.signInWithEmailAndPassword(correo.getText().toString(), passwords.get(0))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = auth.getCurrentUser();
                                error.setTextColor(getResources().getColor(R.color.colorPrimary));
                                error.setText("Bienvenido " + user.getEmail());
                                startActivity(new Intent(v.getContext(), MainUserActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                error.setText(task.getException().getMessage());
                                //updateUI(null);
                            }
                        }
                    });
        }
    }

    public void clickRegistrarse(View view) {
        startActivity(new Intent(view.getContext(), SignUpActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.entrar:
                clickEntrar(v);
                break;
            case R.id.registrarse:
                clickRegistrarse(v);
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if( currentUser != null) {
            startActivity(new Intent(getContext(), MainUserActivity.class));
        }
    }
}
