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
import berger.mitchell.ece563.Sources.DailyWorkoutSource;

public class DailyWorkoutAdapter extends RecyclerView.Adapter<DailyWorkoutAdapter.MyViewHolder> {
        private List<DailyWorkoutSource> WorkoutList;
        private Context mContext;
        public String WorkoutName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView workoutName, totalReps, totalWeight;
        public View mWorkoutRow;

        public MyViewHolder(View view) {
            super(view);
            workoutName = view.findViewById(R.id.workoutName);
            totalReps = view.findViewById(R.id.totalReps);
            totalWeight = view.findViewById(R.id.totalWeight);
            mWorkoutRow = view.findViewById(R.id.guestlist_row_layout);
        }
    }

    public DailyWorkoutAdapter(List<DailyWorkoutSource> WorkoutList, Context context) {
        this.WorkoutList = WorkoutList;
        this.mContext = context;
    }
    public DailyWorkoutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_row, parent, false);

        return new DailyWorkoutAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(DailyWorkoutAdapter.MyViewHolder holder, int position) {
        final DailyWorkoutSource availableWorkout = WorkoutList.get(position);
        holder.workoutName.setText(availableWorkout.getName());
        holder.totalReps.setText("Total reps: " + availableWorkout.getReps());
        holder.totalWeight.setText("Total weight: " + availableWorkout.getWeight());
        holder.mWorkoutRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want to remove " + availableWorkout.getName() + "?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return false;
            }
        });
        holder.mWorkoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.init(mContext);
                SharedPref.write(SharedPref.Workout, availableWorkout.getName());
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


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(mContext, "Removing ", Toast.LENGTH_LONG).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}
