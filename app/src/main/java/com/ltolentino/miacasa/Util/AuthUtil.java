package com.ltolentino.miacasa.Util;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUtil {

    public static boolean isEmailValid(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPassValid(String pass) {
        return pass.length() >= 6;
    }

    public static boolean isSecondPassValid(String pass1, String pass2) {
        if(pass1.equals(pass2) && isPassValid(pass1) && isPassValid(pass2)) {
            return true;
        }
        return false;
    }

    public static boolean validateRegistro(String email, ArrayList<String> passwords, ArrayList<TextInputLayout> textInputLayouts) {

        // Reset errors.
        textInputLayouts.get(0).setError(null);
        textInputLayouts.get(1).setError(null);
        textInputLayouts.get(2).setError(null);

        if (email.isEmpty() || email == null) {
            textInputLayouts.get(0).setError("El correo electronico es requerido");
            return false;
        } else if (!isEmailValid(email)) {
            textInputLayouts.get(0).setError("Ingresar un correo electronico valido");
            return false;
        }

        if (passwords.get(0).isEmpty() || passwords.get(0) == null) {
            textInputLayouts.get(1).setError("La contraseña es requerida");
            return false;
        } else if (!isPassValid(passwords.get(0))) {
            textInputLayouts.get(1).setError("La contraseña debe tener mas de 6 caracteres");
            return false;
        }

        if( textInputLayouts.get(2) != null) {
            if(passwords.get(1).isEmpty() || passwords.get(1) == null) {
                textInputLayouts.get(2).setError("Es necesario confirmar la contraseña");
                return false;
            } else if(!isSecondPassValid(passwords.get(0),passwords.get(1))) {
                textInputLayouts.get(2).setError("Las contraseñas no son iguales");
                return false;
            }
        }
        return true;
    }

    public static boolean validateLogin(String email, ArrayList<String> passwords, ArrayList<TextInputLayout> textInputLayouts) {

        // Reset errors.
        textInputLayouts.get(0).setError(null);
        textInputLayouts.get(1).setError(null);

        if (email.isEmpty() || email == null) {
            textInputLayouts.get(0).setError("El correo electronico es requerido");
            return false;
        } else if (!isEmailValid(email)) {
            textInputLayouts.get(0).setError("Ingresar un correo electronico valido");
            return false;
        }

        if (passwords.get(0).isEmpty() || passwords.get(0) == null) {
            textInputLayouts.get(1).setError("La contraseña es requerida");
            return false;
        } else if (!isPassValid(passwords.get(0))) {
            textInputLayouts.get(1).setError("La contraseña debe tener mas de 6 caracteres");
            return false;
        }

        return true;
    }
}
