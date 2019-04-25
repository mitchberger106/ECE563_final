package berger.mitchell.ece563.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import berger.mitchell.ece563.R;

public class NewLiftActivity extends AppCompatActivity {

    private AppCompatSpinner my_spinner;
    private EditText lift_name;
    private Button submit;
    private String spinner_val, workout_name;

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

                //TODO: save to database

                Intent intent = new Intent(NewLiftActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
