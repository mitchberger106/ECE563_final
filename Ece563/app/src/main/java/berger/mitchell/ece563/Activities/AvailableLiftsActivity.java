package berger.mitchell.ece563.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import berger.mitchell.ece563.Adapters.AvailableWorkoutAdapter;
import berger.mitchell.ece563.R;
import berger.mitchell.ece563.Sources.AvailableWorkoutSource;

public class AvailableLiftsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AvailableWorkoutAdapter mAdapter;
    private List<AvailableWorkoutSource> WorkoutList = new ArrayList<>();
    private Context mContext;
    private String date;
    private FloatingActionButton my_fab;

    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection<Document> itemsCollection;

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

    private void prepareLiftData(){
            //TODO: Get values from database
        /*stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        RemoteMongoCollection<Document> itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        RemoteFindIterable findResults = itemsCollection.find();
        findResults.forEach(item -> {
            Log.d("app", String.format("successfully found:  %s", item.toString()));
        });*/


            AvailableWorkoutSource lift1 = new AvailableWorkoutSource("Bench");
            WorkoutList.add(lift1);
            AvailableWorkoutSource lift2 = new AvailableWorkoutSource("Squat");
            WorkoutList.add(lift2);
            AvailableWorkoutSource lift3 = new AvailableWorkoutSource("Deadlift");
            WorkoutList.add(lift3);
            mAdapter.notifyDataSetChanged();
    }
}
