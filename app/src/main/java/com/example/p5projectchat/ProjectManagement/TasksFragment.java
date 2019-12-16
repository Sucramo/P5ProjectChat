package com.example.p5projectchat.ProjectManagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.p5projectchat.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TasksFragment extends Fragment {


    private DatabaseReference databaseReference;
    private String room_name;
    private ImageButton add_to_do_task_button, add_doing_task_button, add_done_task_button;
    private TextView textViewToDoTasks, textViewDoingTasks, textViewDoneTasks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_layout, container, false);

        room_name = Global.global_room_name;

        add_to_do_task_button = view.findViewById(R.id.btn_add_to_do_task);
        add_doing_task_button = view.findViewById(R.id.btn_add_doing_task);
        add_done_task_button = view.findViewById(R.id.btn_add_done_task);

        textViewToDoTasks = view.findViewById(R.id.text_view_to_do_tasks);
        textViewDoingTasks = view.findViewById(R.id.text_view_doing_tasks);
        textViewDoneTasks = view.findViewById(R.id.text_view_done_tasks);


        if (room_name != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_name).child("Tasks").child("To do");
            Log.d("ROOT", String.valueOf(databaseReference));

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_tasks_to_do(dataSnapshot);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_tasks_to_do(dataSnapshot);

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

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_name).child("Tasks").child("Doing");
            Log.d("ROOT", String.valueOf(databaseReference));

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_tasks_doing(dataSnapshot);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_tasks_doing(dataSnapshot);

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

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_name).child("Tasks").child("Done");
            Log.d("ROOT", String.valueOf(databaseReference));

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_tasks_done(dataSnapshot);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    append_tasks_done(dataSnapshot);

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


            add_to_do_task_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Global.global_task_type = "To do";

                    Dialog dialog = new Dialog();
                    dialog.show(getFragmentManager(), "My custom dialog");
                }
            });
            add_doing_task_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Global.global_task_type = "Doing";

                    Dialog dialog = new Dialog();
                    dialog.show(getFragmentManager(), "My custom dialog");
                }
            });
            add_done_task_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Global.global_task_type = "Done";

                    Dialog dialog = new Dialog();
                    dialog.show(getFragmentManager(), "My custom dialog");
                }
            });

        }

        return view;
    }


    private void append_tasks_to_do(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {

            String chat_msg = (String) ((DataSnapshot) i.next()).getValue();


            textViewToDoTasks.append(chat_msg + " \n");

        }
    }

    private void append_tasks_doing(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {

            String chat_msg = (String) ((DataSnapshot) i.next()).getValue();


            textViewDoingTasks.append(chat_msg + " \n");

        }
    }

    private void append_tasks_done(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {

            String chat_msg = (String) ((DataSnapshot) i.next()).getValue();


            textViewDoneTasks.append(chat_msg + " \n");

        }
    }


}
