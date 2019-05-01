package berger.mitchell.ece563.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import berger.mitchell.ece563.Adapters.AvailableWorkoutAdapter;
import berger.mitchell.ece563.R;
import berger.mitchell.ece563.Sources.AvailableWorkoutSource;
import berger.mitchell.ece563.Sources.DailyWorkoutSource;

public class AvailableLiftsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AvailableWorkoutAdapter mAdapter;
    private List<AvailableWorkoutSource> WorkoutList = new ArrayList<>();
    private Context mContext;
    private String date;
    private FloatingActionButton my_fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_lifts);

        getSupportActionBar().setTitle("Available Workouts");

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new AvailableWorkoutAdapter(WorkoutList, this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        my_fab = findViewById(R.id.fab);
        my_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvailableLiftsActivity.this, NewLiftActivity.class);
                startActivity(intent);
            }
        });

        prepareLiftData();
    }

<<<<<<< HEAD
    private void prepareLiftData(){
            //TODO: Get values from database
        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("LiftNames");
        //RemoteMongoCollection<Document> itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("LiftNames");
        itemsCollection.sync().configure(DefaultSyncConflictResolvers.remoteWins(),null,null);
        SyncFindIterable findResults = itemsCollection.sync().find();
        findResults.forEach(item -> {

            String name=((Document)(item)).get("name").toString();
            String bodypart=((Document)(item)).get("bodypart").toString();
            //BERGS THIS IS WHAT YOU WOULD USE TO SHOW THE BODY PART

            WorkoutList.add(new AvailableWorkoutSource(name, bodypart));
        });

            mAdapter.notifyDataSetChanged();
=======
    private void prepareLiftData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LiftNames");
        final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                WorkoutList.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                        AvailableWorkoutSource temp = new AvailableWorkoutSource(Snapshot.getKey(), Snapshot.child("BodyType").getValue(String.class));
                        WorkoutList.add(temp);
                        mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("PartyListActivity", "Failed to read value.", error.toException());
            }
        });
>>>>>>> a5cb84eef6b67516770eff24cf8ae4a5729d0a0e
    }
}
