package com.ltolentino.miacasa.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseDB {

    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String result;

    public ArrayList<Map<String, Object>> getPost(FirebaseAuth user) {
        final ArrayList<Map<String, Object>> result = new ArrayList<>();
        db.collection("users")
                .document(user.getUid())
                .collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //TODO document es la referencia para cada documento
                                result.add(document.getData());
                            }
                        } else {
                        }
                    }
                });
        return result;
    }

    public ArrayList<Map<String, Object>> getPosts() {
        final ArrayList<Map<String, Object>> result = new ArrayList<>();
        db.collection("allPosts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //TODO document es la referencia para cada documento
                                result.add(document.getData());
                            }
                        } else {
                        }
                    }
                });
        return result;
    }
}
