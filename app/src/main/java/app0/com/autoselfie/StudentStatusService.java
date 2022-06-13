package app0.com.autoselfie;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;

public class StudentStatusService  extends IntentService {

    private static final String TAG = StudentStatusService.class.getSimpleName();
    private DbHelper dbHelper;
    private static Context context ;

    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, StudentStatusService.class);
        StudentStatusService.context = context;
        action.setData(uri);

        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public StudentStatusService() {

        super(TAG);



    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        //TODO: make your job here
//        dbHelper = new DbHelper(StudentStatusService.context);

    }
}
