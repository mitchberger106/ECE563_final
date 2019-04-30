package berger.mitchell.ece563.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;

import berger.mitchell.ece563.R;
import berger.mitchell.ece563.SharedPref;

public class HistoryFragment extends Fragment {
    private static final String TAG = "History Fragment";
    private Context mContext;

    private CalendarView mCalendar;
    private Button select;

    private String date;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        rootView.setTag(TAG);

        mCalendar = rootView.findViewById(R.id.my_calendar);
        mCalendar.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                date = String.valueOf(month+1)+String.valueOf(dayOfMonth)+String.valueOf(year);
            }
        });

        select = rootView.findViewById(R.id.dateSelect);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.write("Date",date);
                Log.d("app",String.format("date: %s",date));
                Fragment newFragment = new WorkoutFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.commit();
            }
        });
        //Intent intent = new Intent(this, AnotherActivity.class);
        //intent.putExtra("long date", date.getTime());
        //startActivity(intent);
        return rootView;
    }
}
