package com.ltolentino.miacasa.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.ltolentino.miacasa.R;
import com.ltolentino.miacasa.Util.AuthUtil;
import com.ltolentino.miacasa.firebase.FirebaseAbsActivityUtil;

import java.util.ArrayList;

public class SignUpActivity extends FirebaseAbsActivityUtil {

    TextInputLayout correoInputLayout;
    TextInputLayout passInputLayout;
    TextInputLayout valiPassInputLayout;

    EditText correoText;
    EditText passText;
    EditText validPassText;

    TextView error;

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressBar = findViewById(R.id.progress_bar);

        error = findViewById(R.id.error_registrando);

        correoInputLayout = findViewById(R.id.email_input_layout);
        passInputLayout = findViewById(R.id.contrasena_input_layout);
        valiPassInputLayout = findViewById(R.id.validar_contrasena_input_layout);
        passInputLayout.setPasswordVisibilityToggleEnabled(true);
        valiPassInputLayout.setPasswordVisibilityToggleEnabled(true);

        correoText = findViewById(R.id.email);
        passText = findViewById(R.id.contrasena);
        validPassText = findViewById(R.id.validar_contrasena);
    }

    public void crearUsuario(final View view) {
        ArrayList<TextInputLayout> textInputLayouts = new ArrayList<>();
        textInputLayouts.add(correoInputLayout);
        textInputLayouts.add(passInputLayout);
        textInputLayouts.add(valiPassInputLayout);

        ArrayList<String> passwords = new ArrayList<>();
        passwords.add(passText.getText().toString());
        passwords.add(validPassText.getText().toString());

        String correo = correoText.getText().toString();
        String pass = passwords.get(0);

        if(AuthUtil.validateRegistro(correoText.getText().toString(),passwords,textInputLayouts)) {
            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(correo, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                error.setTextColor(getResources().getColor(R.color.colorPrimary));
                                error.setText("Bienvenido " + user.getEmail());
                                startActivity(new Intent(view.getContext(), MainUserActivity.class));
                            } else {
                                error.setText(task.getException().getMessage());
                                progressBar.setVisibility(View.INVISIBLE);
                               //updateUI(null);
                            }
                        }
                    });
        }
    }
}
