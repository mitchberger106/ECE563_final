package berger.mitchell.ece563.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import berger.mitchell.ece563.Activities.AvailableLiftsActivity;
import berger.mitchell.ece563.Adapters.DailyWorkoutAdapter;
import berger.mitchell.ece563.Adapters.MaxAdapter;
import berger.mitchell.ece563.SharedPref;
import berger.mitchell.ece563.Sources.DailyWorkoutSource;
import berger.mitchell.ece563.Sources.MaxSource;
import berger.mitchell.ece563.R;

public class MaxFragment extends Fragment {
    private Context mContext;
    private static final String TAG = "MaxFragment";
    private RecyclerView mRecyclerView;
    private MaxAdapter mAdapter;
    private ArrayList<MaxSource> MaxList = new ArrayList<>();
    private FloatingActionButton my_fab;


    public MaxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        //Refresh your stuff here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        rootView.setTag(TAG);





        mRecyclerView = rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MaxAdapter(MaxList, mContext);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        my_fab = rootView.findViewById(R.id.fab);
        my_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AvailableLiftsActivity.class);
                startActivity(i);
            }
        });

        prepareMaxData();

        return rootView;
    }
    private void prepareMaxData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Lifts");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               MaxList.clear();
               HashMap<String,Integer>maxMap=new HashMap<String,Integer>();
                for (DataSnapshot Snapshot0 : dataSnapshot.getChildren()) {


                        for (DataSnapshot Snapshot1 : Snapshot0.getChildren()) {
                            String workout_name = Snapshot1.getKey();

                            for (DataSnapshot Snapshot2 : Snapshot1.getChildren()) {
                                if (!Snapshot2.getKey().equals("Count")) {
                                    int reps= Integer.parseInt(Snapshot2.child("Reps").getValue(String.class));

                                    int weight= Integer.parseInt(Snapshot2.child("Weight").getValue(String.class));
                                    if(!maxMap.containsKey(workout_name)||calcMax(weight,reps)>maxMap.get(workout_name)){
                                    maxMap.put(workout_name,calcMax(weight,reps));
                                    }

                                }
                            }


                        }

                }
                for(String workout_name:maxMap.keySet()) {
                    MaxSource temp = new MaxSource(workout_name, maxMap.get(workout_name));
                    MaxList.add(temp);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("PartyListActivity", "Failed to read value.", error.toException());
            }
        });
    }
    private int calcMax(int weight,int reps){
        double factor=0;
        switch(reps){
            case 0:
                factor=0;
                break;
            case 1:
                factor=1;
                break;
            case 2:
                factor=1.05;
                break;
            case 3:
                factor=1.08;
                break;
            case 4:
                factor=1.11;
                break;
            case 5:
                factor=1.15;
                break;
            case 6:
                factor=1.18;
                break;
            case 7:
                factor=1.2;
                break;
            case 8:
                factor=1.25;
                break;
            case 9:
                factor=1.3;
                break;
            case 10:
                factor=1.33;
                break;
            case 11:
                factor=1.43;
                break;
            case 12:
                factor=1.49;
                break;
            case 13:
                factor=1.51;
                break;
            case 14:
                factor=1.53;
                break;
            case 15:
                factor=1.54;
                break;
            default:
                factor=1.56;


        }



        return (int)(factor*weight);
    }

}