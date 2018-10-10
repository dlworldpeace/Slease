package com.example.gaominyu.slease.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gaominyu.slease.Main.BrowseActivity;
import com.example.gaominyu.slease.Model.User;
import com.example.gaominyu.slease.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();
    private TextView details;
    private EditText inputDisplayName, inputDescription;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;

    private String userId;

    public SignupActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Declaration of layout elements
        details = findViewById(R.id.text_UserName);
        inputDisplayName = findViewById(R.id.editText_DisplayName);
        inputDescription = findViewById(R.id.editText_Description);
        btnSave = findViewById(R.id.button_save);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // load current user's data from facebook
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            // User is signed in.
            userId = user.getUid();

            // go to next activity if signed before
            mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(userId)) {
                        details.setText("Welcome! " + user.getDisplayName());
                        inputDisplayName.setText(user.getDisplayName());
                        startActivity(new Intent(SignupActivity.this, BrowseActivity.class));
                    }else {
                        // No user is signed in.
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError arg0) {}
            });
        }

        // Save / update the user
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String displayName = inputDisplayName.getText().toString();
                String description = inputDescription.getText().toString();

                createUser(displayName, description);
            }
        });
    }

    /**
     * Creating new user node under 'users'
     */
    private void createUser(String displayName, String description) {

        User user = new User(displayName, description);

        mFirebaseDatabase.child(userId).setValue(user);

        startActivity(new Intent(this, BrowseActivity.class));
    }

}
