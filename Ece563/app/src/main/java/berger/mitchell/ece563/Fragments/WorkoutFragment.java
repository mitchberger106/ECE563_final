package berger.mitchell.ece563.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import berger.mitchell.ece563.Activities.AvailableLiftsActivity;
import berger.mitchell.ece563.Adapters.DailyWorkoutAdapter;
import berger.mitchell.ece563.R;
import berger.mitchell.ece563.SharedPref;
import berger.mitchell.ece563.Sources.DailyWorkoutSource;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.SyncFindIterable;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.BasicDBObject;
import org.bson.Document;

public class WorkoutFragment extends Fragment {

    private static final String TAG = "WorkoutFragment";
    private RecyclerView mRecyclerView;
    private DailyWorkoutAdapter mAdapter;
    private List<DailyWorkoutSource> WorkoutList = new ArrayList<>();
    private Context mContext;
    private String date;
    private FloatingActionButton my_fab;

    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection<Document> itemsCollection;


    public WorkoutFragment() {
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

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        prepareWorkoutData();
        //Refresh your stuff here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        rootView.setTag(TAG);

        date = SharedPref.read("Date", "");

        Toast.makeText(mContext, date, Toast.LENGTH_LONG).show();

        mRecyclerView = rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new DailyWorkoutAdapter(WorkoutList, mContext);

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

        //prepareWorkoutData();

        return rootView;
    }

    private void prepareWorkoutData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Lifts");
        final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                WorkoutList.clear();
                for (DataSnapshot Snapshot0 : dataSnapshot.getChildren()) {
                    Log.d("app", date);
                    if (Snapshot0.getKey().equals(date)) {
                        for (DataSnapshot Snapshot1 : Snapshot0.getChildren()) {
                            String workout_name = Snapshot1.getKey();
                            int total_reps = 0;
                            int max_weight = 0;
                            for (DataSnapshot Snapshot2 : Snapshot1.getChildren()) {
                                total_reps += Integer.parseInt(Snapshot2.child("Reps").getValue(String.class));
                                if (Integer.parseInt(Snapshot2.child("Weight").getValue(String.class)) > max_weight){
                                    max_weight = Integer.parseInt(Snapshot2.child("Weight").getValue(String.class));
                                }
                            }
                            DailyWorkoutSource temp = new DailyWorkoutSource(workout_name, String.valueOf(total_reps), String.valueOf(max_weight));
                            WorkoutList.add(temp);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("PartyListActivity", "Failed to read value.", error.toException());
            }
        });
    }
}

