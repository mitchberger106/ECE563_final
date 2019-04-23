package berger.mitchell.ece563.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import berger.mitchell.ece563.Adapters.SetAdapter;
import berger.mitchell.ece563.R;
import berger.mitchell.ece563.SharedPref;
import berger.mitchell.ece563.Sources.DailyWorkoutSource;
import berger.mitchell.ece563.Sources.SetSource;

public class LiftInfoActivity extends AppCompatActivity {

    private String workout;
    private Button repPlus, repMinus, weightPlus, weightMinus, addSet;
    private TextView weightText, repText;

    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection<Document> itemsCollection;

    private List<SetSource> SetList = new ArrayList<>();
    private SetAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lift_info);

        workout = SharedPref.read("Workout", "");

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(workout);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mAdapter = new SetAdapter(SetList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        repPlus = findViewById(R.id.repPlus);
        repMinus = findViewById(R.id.repMinus);
        weightPlus = findViewById(R.id.weightPlus);
        weightMinus = findViewById(R.id.weightMinus);
        addSet = findViewById(R.id.addSet);
        weightText = findViewById(R.id.weightText);
        repText = findViewById(R.id.repText);

        //prepareSetData();

        repPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                repText.setText(String.valueOf(Integer.parseInt(repText.getText().toString())+1));
            }
        });
        repMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(repText.getText().toString()) > 0)
                    repText.setText(String.valueOf(Integer.parseInt(repText.getText().toString())-1));
            }
        });

        weightPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                weightText.setText(String.valueOf(Integer.parseInt(weightText.getText().toString())+1));
            }
        });
        weightMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(weightText.getText().toString()) > 0)
                    weightText.setText(String.valueOf(Integer.parseInt(weightText.getText().toString())-1));
            }
        });

        addSet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("add","add");
                SetSource set = new SetSource(weightText.getText().toString(), repText.getText().toString());
                SetList.add(set);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void prepareSetData() {
        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        //itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        //Log.d("Lifts", String.valueOf(itemsCollection.count()));
        RemoteMongoCollection<Document> itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        RemoteFindIterable findResults = itemsCollection.find();
        findResults.forEach(item -> {
            Log.d("app", String.format("successfully found:  %s", item.toString()));
        });


    }
}
