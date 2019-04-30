package berger.mitchell.ece563.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import berger.mitchell.ece563.Activities.LiftInfoActivity;
import berger.mitchell.ece563.R;
import berger.mitchell.ece563.SharedPref;
import berger.mitchell.ece563.Sources.AvailableWorkoutSource;

import static berger.mitchell.ece563.SharedPref.Workout;

public class AvailableWorkoutAdapter extends RecyclerView.Adapter<AvailableWorkoutAdapter.MyViewHolder> {
    private List<AvailableWorkoutSource> WorkoutList;
    private Context mContext;
    public String WorkoutName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView workoutName;
        public View mWorkoutRow;

        public MyViewHolder(View view) {
            super(view);
            workoutName = view.findViewById(R.id.workoutName);
            mWorkoutRow = view.findViewById(R.id.guestlist_row_layout);
        }
    }

    public AvailableWorkoutAdapter(List<AvailableWorkoutSource> WorkoutList, Context context) {
        this.WorkoutList = WorkoutList;
        this.mContext = context;
    }
    public AvailableWorkoutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_row, parent, false);

        return new AvailableWorkoutAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(AvailableWorkoutAdapter.MyViewHolder holder, int position) {
        final AvailableWorkoutSource availableWorkout = WorkoutList.get(position);
        holder.workoutName.setText(availableWorkout.getName());
        holder.mWorkoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.write("Workout", availableWorkout.getName());
                Intent intent = new Intent(mContext, LiftInfoActivity.class);
                mContext.startActivity(intent);
                Toast.makeText(mContext,"You Clicked: " + availableWorkout.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return WorkoutList.size();
    }
}
