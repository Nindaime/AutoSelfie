package app0.com.autoselfie;

//import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app0.com.autoselfie.Model.ScheduleEntry;

// The adapter class which
// extends RecyclerView Adapter
public class ScheduleAdapter
        extends RecyclerView.Adapter<ScheduleAdapter.MyView> {

    // List with String type
    private ArrayList<ScheduleEntry> list;
    private Context context;
    private final String TAG = "ScheduleAdapter";
    private final ArrayList<String> daysAlreadyIncludedInTheView = new ArrayList<>();

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView day,courseCode, time, venue;

        Button startClassButton, viewAttendanceButton;
        LinearLayout top, secondFromTop;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);


            day = (TextView)view
                    .findViewById(R.id.day);

            courseCode = (TextView)view
                    .findViewById(R.id.courseCode);

            time = (TextView)view
                    .findViewById(R.id.time);

            venue = (TextView)view
                    .findViewById(R.id.venue);

            startClassButton = (Button) view
                    .findViewById(R.id.startClass);

            viewAttendanceButton = (Button) view
                    .findViewById(R.id.viewAttendance);

            top = (LinearLayout) view.findViewById(R.id.top);
            secondFromTop = (LinearLayout) view.findViewById(R.id.secondFromTop);

        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public ScheduleAdapter(ArrayList<ScheduleEntry> list, Context context)
    {

        this.list = list;
        this.context = context;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {

        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.schedule_item,
                        parent,
                        false);

        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items

        ScheduleEntry entry = list.get(position);

        String day = entry.getDay();

//        int shownPositions [] = new int []{0,4,7, 11};

        ArrayList<Integer> shownPositions = new ArrayList<>();
        shownPositions.add(0);
        shownPositions.add(4);
        shownPositions.add(7);
        shownPositions.add(11);

//        if(shownPositions.contains(position)){
            holder.day.setText(entry.getDay());

//            daysAlreadyIncludedInTheView.add(day);
//        }
//        else{
//            holder.top.setVisibility(View.INVISIBLE);


//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(0, 0, 0, 0);
//
//            holder.secondFromTop.setLayoutParams(params);
//        }

        holder.courseCode.setText(entry.getCourseCode());
        holder.time.setText(entry.getTime());
        holder.venue.setText(entry.getVenue());

        holder.startClassButton.setOnClickListener(v ->{
            Intent intent = new Intent(context, AttendanceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("scheduleEntryId", entry.getId());
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.viewAttendanceButton.setOnClickListener(v ->{

            Intent intent = new Intent(context, AttendanceViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("scheduleEntryId", entry.getId());
            bundle.putString("courseCode", entry.getCourseCode());
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {

        return list.size();
    }
}
