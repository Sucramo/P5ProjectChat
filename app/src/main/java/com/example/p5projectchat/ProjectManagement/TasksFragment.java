package com.example.p5projectchat.ProjectManagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.p5projectchat.R;
import com.example.p5projectchat.UserSettings.NonScrollListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TasksFragment extends Fragment {

    private Button add_task;
    private EditText add_task_text;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private NonScrollListView listViewToDo;
    private NonScrollListView listViewDoing;
    private NonScrollListView listViewDone;
    private ArrayList<String> toDoArrayList = new ArrayList<>();
    private ArrayList<String> doingArrayList = new ArrayList<>();
    private ArrayList<String> doneArrayList = new ArrayList<>();
    private String toDoList;
    private ArrayAdapter<String> listViewAdapterToDo;
    private ArrayAdapter<String> listViewAdapterDoing;
    private ArrayAdapter<String> listViewAdapterDone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_layout, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ChatRooms");

        add_task = view.findViewById(R.id.btn_add_task);
        add_task_text = view.findViewById(R.id.et_add_task);

        listViewToDo = view.findViewById(R.id.toDoList);
        listViewDoing = view.findViewById(R.id.doingList);
        listViewDone = view.findViewById(R.id.doneList);

        //adding titles to listviews
        TextView titleToDo = new TextView(getContext());
        titleToDo.setText("To Do");
        listViewToDo.addHeaderView(titleToDo);

        TextView titleDoing = new TextView(getContext());
        titleDoing.setText("Doing");
        listViewDoing.addHeaderView(titleDoing);

        TextView titleDone = new TextView(getContext());
        titleDone.setText("Done");
        listViewDone.addHeaderView(titleDone);


        //To do listview
        listViewAdapterToDo = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, toDoArrayList
        );

        listViewToDo.setAdapter(listViewAdapterToDo);
        databaseReference.child("Chat room 1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.child("Tasks").child("To do").getValue());
                toDoArrayList.add(value);
                listViewAdapterToDo.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //add a task button listener - DOESNT WORK
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Chat room 1").child("Tasks").child("To do").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String result = add_task_text.getText().toString();
                        databaseReference.child("Chat room 1").child("Tasks").child("To do").setValue(result);
                        listViewAdapterToDo.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        //Doing listview
        listViewAdapterDoing = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, doingArrayList
        );

        listViewDoing.setAdapter(listViewAdapterDoing);
        databaseReference.child("Chat room 1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.child("Tasks").child("Doing").getValue());
                doingArrayList.add(value);
                listViewAdapterDoing.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //done listview
        listViewAdapterDone = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, doneArrayList
        );

        listViewDone.setAdapter(listViewAdapterDone);
        databaseReference.child("Chat room 1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.child("Tasks").child("Done").getValue());
                doneArrayList.add(value);
                listViewAdapterDone.notifyDataSetChanged();
                add_task_text.getText().clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }


}
