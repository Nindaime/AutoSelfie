package app0.com.autoselfie;

//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// The adapter class which
// extends RecyclerView Adapter
public class AttendanceListAdapter
        extends RecyclerView.Adapter<AttendanceListAdapter.MyView> {

    // List with String type
    private ArrayList<AttendanceModel> list;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView name, time;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            // initialise TextView with id
            name = (TextView)view
                    .findViewById(R.id.name);

            time = (TextView)view
                    .findViewById(R.id.time);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public AttendanceListAdapter(ArrayList<AttendanceModel> list)
    {
        this.list = list;
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
                .inflate(R.layout.attendance_item,
                        parent,
                        false);

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
        System.out.println(list.get(position).getNameOfUser());

        holder.name.setText(list.get(position).getNameOfUser());
        holder.time.setText(list.get(position).getTime());
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {

        Log.d("Attendance Adapter","this is the size of the list: "+list.size());
        return list.size();
    }
}
