package app0.com.autoselfie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

public class AttendanceViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DbHelper dbHelper;
    private final String TAG = "AttendanceViewActivity";
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    LinearLayoutManager verticalLayout;
    private int scheduleEntryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbHelper = new DbHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_view);

        Bundle bundle = getIntent().getExtras();
        scheduleEntryId = bundle.getInt("scheduleEntryId");

        ArrayList<AttendanceModel> list = dbHelper.getAttendanceList(scheduleEntryId);



        recyclerView
                = (RecyclerView) findViewById(
                R.id.recyclerView);

        verticalLayout
                = new LinearLayoutManager(
                AttendanceViewActivity.this,
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(verticalLayout);

        recyclerView.setAdapter( new AttendanceListAdapter(list));
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: Needs to pass it the scheduleEntryId
        ArrayList<AttendanceModel> list = dbHelper.getAttendanceList(scheduleEntryId);
        recyclerView.setAdapter( new AttendanceListAdapter(list));
    }
}