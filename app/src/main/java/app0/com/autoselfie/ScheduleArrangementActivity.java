package app0.com.autoselfie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;


import java.util.ArrayList;

import app0.com.autoselfie.Model.ScheduleEntry;

public class ScheduleArrangementActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DbHelper dbHelper;
    private final String TAG = "ScheduleArrangementActivity";
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    LinearLayoutManager verticalLayout;
    private int scheduleEntryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbHelper = new DbHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_arrangement);


        ArrayList<ScheduleEntry> list = dbHelper.onGetSchedule();

        Toast.makeText(getApplicationContext(), ""+list.size(), Toast.LENGTH_SHORT).show();


        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerView);

        verticalLayout
                = new LinearLayoutManager(
                ScheduleArrangementActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(verticalLayout);

        recyclerView.setAdapter(new ScheduleAdapter(list, getApplicationContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<ScheduleEntry> list = dbHelper.onGetSchedule();
        recyclerView.setAdapter(new ScheduleAdapter(list, getApplicationContext()));
    }
}