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
import berger.mitchell.ece563.Sources.SetSource;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.MyViewHolder> {
    private List<SetSource> SetList;
    private Context mContext;
    public String WorkoutName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reps, weight;
        public View mSetRow;

        public MyViewHolder(View view) {
            super(view);
            reps = view.findViewById(R.id.reps);
            weight = view.findViewById(R.id.weight);
            mSetRow = view.findViewById(R.id.set_row_layout);
        }
    }

    public SetAdapter(List<SetSource> SetList, Context context) {
        this.SetList = SetList;
        this.mContext = context;
    }
    public SetAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.set_row, parent, false);

        return new SetAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(SetAdapter.MyViewHolder holder, int position) {
        final SetSource availableSet = SetList.get(position);
        holder.reps.setText(availableSet.getReps());
        holder.weight.setText(availableSet.getWeight());
        holder.mSetRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure you want to remove set?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return SetList.size();
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
