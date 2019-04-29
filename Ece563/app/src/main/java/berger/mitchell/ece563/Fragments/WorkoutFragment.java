package berger.mitchell.ece563.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        rootView.setTag(TAG);

        if(SharedPref.read("Date","") != ""){
            date = SharedPref.read("Date","");
            SharedPref.write("Date","");
        }
        else{
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy");
            date = df.format(c);
        }
        Toast.makeText(mContext, date, Toast.LENGTH_LONG).show();

        mRecyclerView = rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new DailyWorkoutAdapter(WorkoutList, mContext);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        prepareWorkoutData();

        return rootView;
    }

    private void prepareWorkoutData() {
        //TODO: Get values from database
        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
        itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        itemsCollection.sync();
        Document m=new Document("date","2019-04-10T04:00:00.000+00:00");
        itemsCollection.insertOne(m);
        Log.d("app",String.format("size of collection: %s",itemsCollection.count()));
        /*RemoteMongoCollection<Document> itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
        */RemoteFindIterable findResults = itemsCollection.find();
        findResults.forEach(item -> {

        });


        DailyWorkoutSource lift1 = new DailyWorkoutSource("Bench");
        WorkoutList.add(lift1);
        DailyWorkoutSource lift2 = new DailyWorkoutSource("Squat");
        WorkoutList.add(lift2);
        DailyWorkoutSource lift3 = new DailyWorkoutSource("Deadlift");
        WorkoutList.add(lift3);
        WorkoutList.add(lift1);
        WorkoutList.add(lift1);
        WorkoutList.add(lift1);
        WorkoutList.add(lift1);
        WorkoutList.add(lift1);
        WorkoutList.add(lift1);
        WorkoutList.add(lift1);
        WorkoutList.add(lift1);
        WorkoutList.add(lift1);
        mAdapter.notifyDataSetChanged();
    }
}
