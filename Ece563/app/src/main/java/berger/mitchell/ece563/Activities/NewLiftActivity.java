package berger.mitchell.ece563.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.SyncFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.sync.SyncInsertOneResult;

import org.bson.Document;

import berger.mitchell.ece563.R;

public class NewLiftActivity extends AppCompatActivity {

    private AppCompatSpinner my_spinner;
    private EditText lift_name;
    private Button submit;
    private String spinner_val, workout_name;
    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection<Document> itemsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lift);

        getSupportActionBar().setTitle("Add New Lift");

        my_spinner = findViewById(R.id.spinner);
        lift_name = findViewById(R.id.input_name);
        submit = findViewById(R.id.btn_enter);

        my_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_val = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workout_name = lift_name.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("LiftNames");
                final DatabaseReference myRef0 = myRef.child(workout_name);
                final DatabaseReference myRef1 = myRef0.child("BodyType");
                myRef1.setValue(spinner_val);
                Intent intent = new Intent(NewLiftActivity.this, AvailableLiftsActivity.class);
                startActivity(intent);
            }
        });
    }
}
