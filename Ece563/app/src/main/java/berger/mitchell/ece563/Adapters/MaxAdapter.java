package berger.mitchell.ece563.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import berger.mitchell.ece563.R;

import java.util.List;

import berger.mitchell.ece563.Sources.MaxSource;
import berger.mitchell.ece563.Sources.SetSource;

public class MaxAdapter extends RecyclerView.Adapter<MaxAdapter.MyViewHolder>{
    private List<MaxSource> maxList;
    private Context mContext;

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

    public MaxAdapter(List<MaxSource> MaxList, Context context) {
        this.maxList = MaxList;
        this.mContext = context;
    }
    public MaxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.set_row, parent, false);

        return new MaxAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MaxAdapter.MyViewHolder holder, int position) {
        final MaxSource availableSet = maxList.get(position);
        holder.reps.setText("Name: " + availableSet.getName());
        holder.weight.setText("Estimated 1 Rep Max: "+ availableSet.getMax());

    }
    @Override
    public int getItemCount() {
        return maxList.size();
    }

}
