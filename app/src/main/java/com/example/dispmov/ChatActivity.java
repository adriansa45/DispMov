package com.example.dispmov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private Button BtnSend;
    private EditText TxtMsg;
    private RecyclerView ListMsg;
    private Adapter adapter;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ListMsg = (RecyclerView) findViewById(R.id.listChat);
        TxtMsg = (EditText) findViewById(R.id.chatinput);
        BtnSend = (Button) findViewById(R.id.chatsend);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");

        adapter = new Adapter(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        ListMsg.setLayoutManager(l);
        ListMsg.setAdapter(adapter);

        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date hour = new Date();
                String user = auth.getCurrentUser() == null ? "Offline" : auth.getCurrentUser().getEmail().toString();
                databaseReference.push().setValue(new Message(TxtMsg.getText().toString(),
                        user, ServerValue.TIMESTAMP.toString()));
                TxtMsg.setText("");
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
                adapter.addMsg(m);
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

    private void setScrollbar(){
        ListMsg.scrollToPosition(adapter.getItemCount()-1);
    }
}