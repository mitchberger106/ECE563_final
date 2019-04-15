package berger.mitchell.ece563.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;

import org.bson.Document;

import berger.mitchell.ece563.Fragments.WorkoutFragment;
import berger.mitchell.ece563.R;
import berger.mitchell.ece563.SharedPref;

public class LoginActivity extends AppCompatActivity {

    private StitchAppClient stitchAppClient;
    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        stitchAppClient = Stitch.initializeDefaultAppClient("liftoff_stitch-dhdoc");
        stitchAppClient = Stitch.getDefaultAppClient();

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnSignup = findViewById(R.id.btn_signup);
        btnLogin = findViewById(R.id.btn_login);
        btnReset = findViewById(R.id.btn_reset_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                UserPasswordCredential credential = new UserPasswordCredential(email, password);
                Stitch.getDefaultAppClient().getAuth().loginWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                                                   @Override
                                                   public void onComplete(@NonNull final Task<StitchUser> task) {
                                                       if (task.isSuccessful()) {
                                                           Toast.makeText(LoginActivity.this, "Logged in as: "+email, Toast.LENGTH_LONG).show();
                                                           SharedPref.init(LoginActivity.this);
                                                           SharedPref.write(SharedPref.ClientID, stitchAppClient.getAuth().getUser().getId());
                                                           final RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(
                                                                   RemoteMongoClient.factory,
                                                                   "mongodb-atlas"
                                                           );
                                                           RemoteMongoCollection<Document> itemsCollection = mongoClient.getDatabase("LiftOff").getCollection("Lifts");
                                                           Log.d("Here", String.valueOf(itemsCollection.count()));
                                                           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                           startActivity(intent);
                                                           finish();
                                                       } else {
                                                           Toast.makeText(LoginActivity.this, "Error Logging In ", Toast.LENGTH_LONG).show();
                                                           progressBar.setVisibility(View.INVISIBLE);
                                                       }
                                                   }
                                               }
                        );
            }
        });
    }
}