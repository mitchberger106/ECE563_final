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
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //prepareWorkoutData();
        //Refresh your stuff here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        rootView.setTag(TAG);

        date=SharedPref.read("Date","");

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
                Intent i= new Intent(getActivity(),AvailableLiftsActivity.class);
                startActivity(i);
            }
        });

        //prepareWorkoutData();

        return rootView;
    }

    private void prepareWorkoutData() {
        //TODO: Get values from database
        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        //itemsCollection.sync();
        /*RemoteMongoCollection<Document> itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        */
        date=SharedPref.read("Date","");
        //SyncFindIterable findResults = itemsCollection.sync().find();
        //String date=/*SharedPref.read("Date","");*/"4/30/2019";
        WorkoutList.clear();
        SyncFindIterable findResults=itemsCollection.sync().find();
        ArrayList<Document> doclist=new ArrayList<Document>();
        findResults.forEach(item -> {
            Log.d("app",String.format("before display: %s",item.toString()));
            if(((Document)item).get("date").equals(date)){
                doclist.add(((Document)item));

                ArrayList<Document>l=(ArrayList)((Document)item).get("lift");
                for(Document d:l){
                    int sets=0;
                    int weight=0;


                    ArrayList<Document>s=(ArrayList)(d.get("sets"));

                    for(Document set:s){
                        sets+=(Integer)set.get("reps");
                        if(weight<(Integer)set.get("weight")){
                            weight=(Integer)set.get("weight");
                        }
                    }


                    DailyWorkoutSource temp=new DailyWorkoutSource(d.get("name").toString(),Integer.toString(sets),Integer.toString(weight));
                    WorkoutList.add(temp);
                    Log.d("app","changed");
                    mAdapter.notifyDataSetChanged();
                }

            }

        });





    }
}
