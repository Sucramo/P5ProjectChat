package com.example.p5projectchat.ProjectManagement;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.p5projectchat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog extends DialogFragment {

    private EditText mInput;
    private TextView mActionOk, mActionCancel, mHeading;
    private String temp_key;
    private DatabaseReference databaseReference;
    private String room_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, container, false);
        mActionOk = view.findViewById(R.id.action_ok);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mInput = view.findViewById(R.id.input);
        mHeading = view.findViewById(R.id.heading);

        room_name = Global.global_room_name;
        mHeading.setText("Add " + Global.global_task_type.toLowerCase() + " task");

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mInput.getText().toString();
                if (!input.equals("")) {
                    if (Global.global_task_type.equals("To do")) {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_name).child("Tasks").child("To do");
                    } else if (Global.global_task_type.equals("Doing")) {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_name).child("Tasks").child("Doing");
                    } else if (Global.global_task_type.equals("Done")) {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_name).child("Tasks").child("Done");
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    temp_key = databaseReference.push().getKey();
                    databaseReference.updateChildren(map);

                    DatabaseReference message_root = databaseReference.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("task", input);
                    message_root.updateChildren(map2);
                }
                getDialog().dismiss();
            }
        });

        return view;
    }
}
