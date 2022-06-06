package app0.com.autoselfie;

//import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView day,courseCode, time;

        Button startClassButton, viewAttendanceButton;

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

            startClassButton = (Button) view
                    .findViewById(R.id.startClass);

            viewAttendanceButton = (Button) view
                    .findViewById(R.id.viewAttendance);

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

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.schedule_item,
                        parent,
                        false);

        Log.d(TAG, "View inflated");

        // return itemView
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

        Log.d(TAG, "Setting items");
        holder.day.setText(entry.getDay());
        holder.courseCode.setText(entry.getCourseCode());
        holder.time.setText(entry.getTime());

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
