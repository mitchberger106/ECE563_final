package berger.mitchell.ece563.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.SyncFindIterable;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import berger.mitchell.ece563.Adapters.SetAdapter;
import berger.mitchell.ece563.R;
import berger.mitchell.ece563.SharedPref;
import berger.mitchell.ece563.Sources.AvailableWorkoutSource;
import berger.mitchell.ece563.Sources.DailyWorkoutSource;
import berger.mitchell.ece563.Sources.SetSource;

public class LiftInfoActivity extends AppCompatActivity {

    private String workout;
    private Button repPlus, repMinus, weightPlus, weightMinus, addSet;
    private TextView weightText, repText;

   // private StitchAppClient stitchClient;
    //private RemoteMongoClient mongoClient;
    private RemoteMongoCollection<Document> itemsCollection;

    private List<SetSource> SetList = new ArrayList<>();
    private SetAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Integer count;
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

        prepareSetData();

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
                //TODO: Add to database


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference r1= database.getReference("Lifts");
                final DatabaseReference r2= r1.child(SharedPref.read("Date",""));
                final DatabaseReference r3= r2.child(SharedPref.read("Workout",""));

                final DatabaseReference r4= r3.child(Integer.toString(count+1));
                r3.child("Count").setValue(count+1);
                r4.child("Reps").setValue(repText.getText().toString());
                r4.child("Weight").setValue(weightText.getText().toString());



                SetSource set = new SetSource(weightText.getText().toString(), repText.getText().toString());
                SetList.add(set);
                count++;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void prepareSetData() {
        //TODO: Get existing values from database if aplicable
        FirebaseDatabase myRef=FirebaseDatabase.getInstance();
        DatabaseReference r1=myRef.getReference("Lifts");
        r1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Log.d("app",SharedPref.read("Date",""));
                        Log.d("app",String.format("d: %s",dataSnapshot.child(SharedPref.read("Date","")).toString()));
                        if(dataSnapshot.hasChild(SharedPref.read("Date",""))) {
                            DataSnapshot dat=dataSnapshot.child(SharedPref.read("Date",""));
                            if (dat.hasChild(SharedPref.read("Workout", ""))) {
                                DataSnapshot SnapShot = dat.child(SharedPref.read("Workout", ""));
                                Log.d("app",SnapShot.toString());
                                count = SnapShot.child("Count").getValue(Integer.class);
                                for (DataSnapshot s : SnapShot.getChildren()) {
                                    Log.d("app", s.toString());
                                    if (!s.getKey().equals("Count")) {
                                        SetList.add(new SetSource(s.child("Weight").getValue(String.class),s.child("Reps").getValue(String.class)));
                                    }
                                }


                            } else {
                                count = 0;

                            }

                        }
                        else{
                            count=0;
                        }
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("PartyListActivity", "Failed to read value.", error.toException());
                    }
                });



    }
}
