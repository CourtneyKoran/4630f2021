package com.example.litternavbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build());

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser user;
    Boolean successfulSignIn = Boolean.FALSE;
    String userName;
    long litterCount =  0;


    public void signOut(View v) {

        AuthUI.getInstance()
                .signOut(v.getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
// ...
                        startActivity(new Intent(v.getContext(), SignInActivity.class));
                        //finish();
                        Snackbar.make(v, "Sign Out Complete", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Long litterCount;

        user = FirebaseAuth.getInstance().getCurrentUser();
        userName = user.getDisplayName();

        Log.d("HomeFragment", "User Display Name: "+userName);

        DocumentReference docRef = db.collection("litterBugUsers").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Long litterCount = document.getLong("litterCount");
                        Log.d("Home DB-Get", "litterCount: " + litterCount);
                        TextView countTextView = (TextView) view.findViewById(R.id.countDisplay);
                        countTextView.setText(String.valueOf(litterCount));

                    } else {
                        Log.d("Home DB-Get", "No such document");
                    }
                } else {
                    Log.d("Home DB-Get", "get failed with ", task.getException());
                }
            }
        });


        final Button button = view.findViewById(R.id.button_id2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut(v);

                user = FirebaseAuth.getInstance().getCurrentUser();

            }
        });
        return view;
    }


}
