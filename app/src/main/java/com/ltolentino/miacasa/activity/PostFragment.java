package com.ltolentino.miacasa.activity;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ltolentino.miacasa.R;
import com.ltolentino.miacasa.firebase.FirebaseAbsFragmentUti;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostFragment extends FirebaseAbsFragmentUti implements View.OnClickListener {


    Button crear;
    ImageView addImage;
    ImageView postImage;
    ImagePicker imagePicker;
    Uri imageUri;

    Location userLocation;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText homeLocation;
    Geocoder geocoder;

    EditText description;
    ProgressBar progressBar;

    TextView erroPost;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View real = inflater.inflate(R.layout.fragment_post, container, false);

        description = real.findViewById(R.id.description);
        progressBar = real.findViewById(R.id.progressBar);
        erroPost = real.findViewById(R.id.error_post);

        addImage = real.findViewById(R.id.add_image);
        postImage = real.findViewById(R.id.post_image);
        crear = real.findViewById(R.id.crear_post);

        addImage.setOnClickListener(this);
        crear.setOnClickListener(this);

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        homeLocation = real.findViewById(R.id.home_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationRequest = new LocationRequest();
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if ( locationResult != null) {
                    userLocation = locationResult.getLocations().get(0);
                    stopLocationUpdates();
                    setAddressNameFromLocation();
                }
            }
        };
        startLocationUpdates();
        return real;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_image:
                imagePicker = new ImagePicker(getActivity(), this, new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri image) {
                        imageUri = image;
                        postImage.setImageURI(image);
                    }
                });
                imagePicker.choosePicture(true);
                break;
            case R.id.crear_post:
                erroPost.setText("");
                if(imageUri == null) {
                    erroPost.setText("La imagen es requerida para publicar");
                    return;
                }
                final StorageReference riversRef = storageRef.child("post_img/"+imageUri.getLastPathSegment());
                riversRef.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            final Uri downloadUri = task.getResult();
                            db.collection("users")
                                    .document(auth.getUid())
                                    .collection("posts")
                                    .add(data(auth,downloadUri.toString(),description.getText().toString(),homeLocation.getText().toString())).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    db.collection("allPost")
                                            .add(data(auth,downloadUri.toString(),description.getText().toString(),homeLocation.getText().toString())).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressBar.setVisibility(View.GONE);
                                            erroPost.setText("Publicacion creada exitosamente");
                                            limpiarPost();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    erroPost.setText(e.getMessage());
                                    limpiarPost();
                                }
                            });
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    private void setAddressNameFromLocation() {
        try {
            List<Address> addresses = new ArrayList<>(geocoder.getFromLocation(
                    userLocation.getLatitude(),
                    userLocation.getLongitude(),
                    1));
            homeLocation.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public static Map<String, Object> data(FirebaseAuth user, String image, String desc, String location) {
        Map<String, Object> result = new HashMap<>();
        result.put("UID", user.getUid());
        result.put("email", user.getCurrentUser().getEmail());
        result.put("image", image);
        result.put("description", desc);
        result.put("location", location);

        return result;
    }

    public void limpiarPost()
    {
        postImage.setImageURI(null);
        description.setText("");
    }

}
