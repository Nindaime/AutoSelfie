package app0.com.autoselfie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGoToAttendanceView(View view){
        Intent intent = new Intent(getApplicationContext(), AttendanceActivity.class);
        startActivity(intent);

    }

    public void seeListOfAttendance(View view){
        Intent intent = new Intent(getApplicationContext(), AttendanceViewActivity.class);
        startActivity(intent);

    }

    public void onGoToSignupActivity(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);

    }


}