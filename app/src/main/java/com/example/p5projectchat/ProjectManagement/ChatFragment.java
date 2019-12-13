package com.example.p5projectchat.ProjectManagement;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.p5projectchat.Database.User;
import com.example.p5projectchat.R;

import com.example.p5projectchat.Security.AES;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChatFragment extends Fragment {

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;

    private String user_name, room_name;
    private DatabaseReference root;
    private String temp_key;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_layout, container, false);

        room_name = Global.global_room_name;

        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = database.getReference();
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = firebaseUser.getUid();
                User user = dataSnapshot.child(uid).getValue(User.class);
                user_name = user.getFirstName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_send_msg = view.findViewById(R.id.btn_send);
        input_msg = view.findViewById(R.id.msg_input);
        chat_conversation = view.findViewById(R.id.chat_conversation);

        if (room_name != null) {
            root = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_name).child("Messages");
            Log.d("roomname != null:", room_name);

            root.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_chat_conversation(dataSnapshot);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_chat_conversation(dataSnapshot);

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", user_name);
                String encryptedMessage = AES.encrypt(input_msg.getText().toString());
                map2.put("msg", encryptedMessage);
                input_msg.getText().clear();

                message_root.updateChildren(map2);


            }
        });


        return view;
    }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {

            String chat_msg = (String) ((DataSnapshot) i.next()).getValue();


            try {
                String decrypted_chat_msg = AES.decrypt(chat_msg);
                String chat_user_name = (String) ((DataSnapshot) i.next()).getValue();

                chat_conversation.append(chat_user_name + ": " + decrypted_chat_msg + " \n");
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

        }
    }


}
