package berger.mitchell.ece563.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.model.Filters;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.SyncFindIterable;
import com.mongodb.stitch.core.services.mongodb.remote.sync.SyncInsertOneResult;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Array;
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

   // private StitchAppClient stitchClient;
    //private RemoteMongoClient mongoClient;
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
                StitchAppClient stitchClient = Stitch.getDefaultAppClient();
               RemoteMongoClient mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                RemoteMongoCollection<Document> itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
                //PASS IN ID

                String date=SharedPref.read("Date","");
                Log.d("app",String.format("date: %s",date));
                Bson filter=Filters.eq("date",date);
                itemsCollection.sync().deleteOne(filter);
                SyncFindIterable findResults = itemsCollection.sync().find(filter);
                itemsCollection.sync().find().forEach(item-> {

                    Log.d("app",String.format("%s" ,Boolean.toString(((Document)item).get("date").equals(date))));
                });
                Document updated= new Document();

                Task<Long> t= itemsCollection.sync().count(filter);
                t.addOnCompleteListener(new OnCompleteListener<Long>() {
                    @Override
                    public void onComplete(@NonNull Task<Long> task) {
                        Log.d("app",String.format("count: %s",task.getResult().toString()));
                        if(task.getResult()==0){
                            Document brandnew=new Document();

                            brandnew.append("date",date);
                            ArrayList<Document> brandnewlifts;
                            brandnewlifts = new ArrayList<Document>();
                            Document brandnewlift=new Document();
                            String name=SharedPref.read("Workout","");
                            brandnewlift.append("name",name);
                            ArrayList<Document>brandnewsets= new ArrayList<Document>();
                            Document brandnewset=new Document();
                            brandnewset.append("reps",Integer.parseInt(repText.getText().toString()));
                            brandnewset.append("weight",Integer.parseInt(weightText.getText().toString()));
                            brandnewsets.add(brandnewset);
                            brandnewlift.append("sets",brandnewsets);
                            brandnewlifts.add(brandnewlift);
                            brandnew.append("lift",brandnewlifts);
                            Log.d("app",brandnew.toString());

                            itemsCollection.sync().insertOne(brandnew).addOnCompleteListener(new OnCompleteListener<SyncInsertOneResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SyncInsertOneResult> task) {
                                    itemsCollection.sync().find().forEach(item-> Log.d("app",String.format("after insert %s",item.toString())));
                                }
                            });


                        }
                        else{
                            findResults.forEach(item -> {

                                updated.append("date", ((Document) item).get("date").toString());
                                ArrayList<Document> updatedlifts = new ArrayList<Document>();
                                ArrayList<Document> lifts = (ArrayList<Document>) ((Document) item).get("lift");
                                boolean seen = false;

                                for (Document lift : lifts) {

                                    //TODO replace this with the actual lift name
                                    if (lift.get("name").toString().equals("Bench Press")) {
                                        ArrayList<Document> sets = (ArrayList<Document>) lift.get("sets");
                                        Document d = new Document();
                                        d.append("reps", repText.getText().toString());
                                        d.append("weight", weightText.getText().toString());
                                        sets.add(d);
                                        Document updatedlift = new Document();
                                        updatedlift.append("name", SharedPref.read("Workout",""));
                                        updatedlift.append("sets", sets);
                                        updatedlifts.add(updatedlift);
                                        seen = true;
                                    } else {
                                        updatedlifts.add(lift);
                                    }

                                }
                                if (!seen) {
                                    Document newlift = new Document();
                                    newlift.append("name",SharedPref.read("Workout",""));
                                    ArrayList<Document> newsets = new ArrayList<Document>();
                                    Document newset = new Document();
                                    newset.append("reps", repText.getText().toString());
                                    newset.append("weight", weightText.getText().toString());
                                    newsets.add(newset);
                                    newlift.append("sets", newsets);
                                    updatedlifts.add(newlift);
                                }
                                updated.append("lift", updatedlifts);
                            });
                            itemsCollection.sync().updateOne(filter, updated);

                        }
                    }
                });










                SetSource set = new SetSource(weightText.getText().toString(), repText.getText().toString());
                SetList.add(set);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void prepareSetData() {
        //TODO: Get existing values from database if aplicable
        StitchAppClient stitchClient = Stitch.getDefaultAppClient();
        RemoteMongoClient mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        //itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        //Log.d("Lifts", String.valueOf(itemsCollection.count()));
        RemoteMongoCollection<Document> itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        String id="";
        Bson filter=Filters.eq("date",SharedPref.read("Date",""));
        SyncFindIterable findResults = itemsCollection.sync().find(filter);
        findResults.forEach(item -> {

            ArrayList<Document> lifts=(ArrayList<Document>) ((Document)item).get("lift");
            for(Document d:lifts){
                if(d.get("name").toString().equals(SharedPref.read("Workout",""))){
                    ArrayList<Document>sets=(ArrayList)d.get("sets");
                    for(Document set:sets){
                        SetSource n= new SetSource(set.get("reps").toString(),set.get("weight").toString());
                        SetList.add(n);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }
}
