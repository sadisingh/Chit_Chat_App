package com.example.chit_chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Objects;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private UsersFirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toolbar mToolbar = findViewById(R.id.userAppBar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        mUsersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersList = (RecyclerView) findViewById(R.id.usersList);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Users> options
                = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mUsersDatabaseReference, Users.class)
                .build();

        mAdapter = new UsersFirebaseRecyclerAdapter(options, UsersActivity.this);
        mUsersList.setAdapter(mAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mUsersDatabaseReference.child(currentUser.getUid()).child("Online").setValue("true");
        mAdapter.startListening();
    }


    @Override protected void onStop()
    {
        super.onStop();
//        mUsersDatabaseReference.child(currentUser.getUid()).child("Online").setValue(ServerValue.TIMESTAMP);
        mAdapter.stopListening();

    }

}