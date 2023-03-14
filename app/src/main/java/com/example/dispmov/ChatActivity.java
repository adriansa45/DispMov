package com.example.dispmov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dispmov.Chat.Adapter;
import com.example.dispmov.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private Button BtnSend, BtnVideo;
    private EditText TxtMsg;
    private RecyclerView ListMsg;
    private Adapter adapter;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private static final int REQUEST_PERMISSION = 200;
    private String[] permissions = {android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListMsg = (RecyclerView) findViewById(R.id.listChat);
        TxtMsg = (EditText) findViewById(R.id.chatinput);
        BtnSend = (Button) findViewById(R.id.chatsend);
        BtnVideo = (Button) findViewById(R.id.videocall);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");

        adapter = new Adapter(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        ListMsg.setLayoutManager(l);
        ListMsg.setAdapter(adapter);

        for (String s : permissions){
            checkPermissions(s);
        }

        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date hour = new Date();
                String user = auth.getCurrentUser() == null ? "Offline" : auth.getCurrentUser().getEmail().toString().split("@")[0];
                databaseReference.push().setValue(new Message(TxtMsg.getText().toString(),
                        user, ServerValue.TIMESTAMP.toString()));
                TxtMsg.setText("");
            }
        });

        BtnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,
                        Videocall.class);
                intent.putExtra("username", auth.getCurrentUser().getEmail().split("@")[0]);
                startActivity(intent);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message m = dataSnapshot.getValue(Message.class);
                //adapter.addMsg(m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean checkPermissions(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        if (result != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION);
        }

        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void setScrollbar(){
        ListMsg.scrollToPosition(adapter.getItemCount()-1);
    }
}