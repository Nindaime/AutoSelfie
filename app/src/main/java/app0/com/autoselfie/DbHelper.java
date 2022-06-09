package app0.com.autoselfie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.LocaleData;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app0.com.autoselfie.Model.Course;
import app0.com.autoselfie.Model.ScheduleEntry;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String STUDENT_TABLE = "student";
    private static final String ID_COLUMN = "id";
    private static final String STUDENT_IMG_TABLE = "images";
    private static final String STUDENT_ATTENDANCE_TABLE = "attendance";
    private static final String FIRST_NAME_COLUMN = "firstName";
    private static final String LAST_NAME_COLUMN = "lastName";
    private static final String IMAGE_LOCATION_COLUMN = "imageLocation";
    private static final String TIME_COLUMN = "currentTime";
    private static final String DATE_COLUMN = "currentDate";
    private static final String SCHEDULE_VENUE_COLUMN = "venue";
    private static final String IS_ONLINE_COLUMN = "isOnline";
    private static final String EMAIL_COLUMN = "email";
    private static final String PASSWORD_COLUMN = "password";
    private static final String CONFIDENCE_LEVEL_COLUMN = "confidenceLevel";
    private static final String SCHEDULE_ID_COLUMN = "scheduleId";
    private static final String COURSE_TABLE = "course";
    private static final String COURSE_CODE_COLUMN = "courseCode";
    private static final String COURSE_NAME_COLUMN = "courseName";
    private static final String SCHEDULE_TABLE = "schedule";
    private static final String SCHEDULE_DAY_COLUMN = "day";
    private static final String SCHEDULE_START_TIME_COLUMN = "startTime";
    private static final String SCHEDULE_END_TIME_COLUMN = "endTime";

    private static final String TAG = "DBHelper";


    public DbHelper(Context context) {
        super(context, STUDENT_TABLE + ".db", null, 1);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Creating Tables If tables do not exist");

        String createStudentTable = "CREATE TABLE " + STUDENT_TABLE + " ( " +
                "" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL_COLUMN + " TEXT ,"
                + PASSWORD_COLUMN + " TEXT," + FIRST_NAME_COLUMN + " TEXT, " + LAST_NAME_COLUMN
                + " TEXT, " + IS_ONLINE_COLUMN + " BOOLEAN DEFAULT 0, UNIQUE (" + EMAIL_COLUMN + ") )";

        String createStudentImageTable = "CREATE TABLE " + STUDENT_IMG_TABLE
                + " ( " + ID_COLUMN + " INTEGER , " + IMAGE_LOCATION_COLUMN
                + " TEXT, FOREIGN KEY (" + ID_COLUMN + ") REFERENCES " + STUDENT_TABLE
                + "(" + ID_COLUMN + ") )";


        String createCourseTable = "CREATE TABLE " + COURSE_TABLE + " ( " +
                "" + COURSE_CODE_COLUMN + " TEXT PRIMARY KEY, " + COURSE_NAME_COLUMN + " TEXT)";

        String createScheduleTable = "CREATE TABLE " + SCHEDULE_TABLE + " ( "
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SCHEDULE_DAY_COLUMN + " TEXT, "
                + COURSE_CODE_COLUMN + " TEXT, "
                + SCHEDULE_START_TIME_COLUMN + " TIME, "
                + SCHEDULE_END_TIME_COLUMN + " TIME, "
                + SCHEDULE_VENUE_COLUMN + " TEXT, "
                + "FOREIGN KEY (" + COURSE_CODE_COLUMN + ") "
                + "REFERENCES " + COURSE_TABLE + " (" + COURSE_CODE_COLUMN + ") ) ";

        String createStudentAttendanceTable = "CREATE TABLE " + STUDENT_ATTENDANCE_TABLE
                + " ( " + ID_COLUMN + " INTEGER , "
                + TIME_COLUMN
                + " TIME, "
                + " " + DATE_COLUMN + " DATE, "
                + SCHEDULE_ID_COLUMN + " INTEGER,"
                + CONFIDENCE_LEVEL_COLUMN + " FLOAT, " +
                " PRIMARY KEY (" + SCHEDULE_ID_COLUMN  + ", " + DATE_COLUMN + ")," +
                "FOREIGN KEY (" + ID_COLUMN + ") " +
                "REFERENCES " + STUDENT_TABLE + "(" + ID_COLUMN + "), " +
                "FOREIGN KEY (" + SCHEDULE_ID_COLUMN + ") REFERENCES " + SCHEDULE_TABLE + " (" + ID_COLUMN + ") )";

        sqLiteDatabase.execSQL(createStudentTable);
        sqLiteDatabase.execSQL(createStudentImageTable);
        sqLiteDatabase.execSQL(createCourseTable);
        sqLiteDatabase.execSQL(createScheduleTable);
        sqLiteDatabase.execSQL(createStudentAttendanceTable);


        try {
            List<Course> courses = Course.getCourses();
            List<ScheduleEntry> scheduleEntries = ScheduleEntry.getSchedule(courses);

            onAddCourses(sqLiteDatabase, courses);
            onAddSchedules(sqLiteDatabase, scheduleEntries);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }

        Log.i(TAG, "Tables query ran successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int onAddUser(String email, String firstName, String lastName, String password) {

        Log.i(TAG, "Creating user with email: " + email);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(FIRST_NAME_COLUMN, firstName);
        cv.put(LAST_NAME_COLUMN, lastName);
        cv.put(EMAIL_COLUMN, email);
        cv.put(PASSWORD_COLUMN, password);
//        cv.put(IS_ONLINE_COLUMN, 0);

        long insert = sqLiteDatabase.insert(STUDENT_TABLE, IS_ONLINE_COLUMN, cv);

        Log.i(TAG, "Created user with email: " + email);

        sqLiteDatabase.close();
        return (int) insert;
    }

    public void onAddCourses(SQLiteDatabase sqLiteDatabase, List<Course> courses) {

        Log.i(TAG, "Creating courses");


        courses.forEach(course -> {

            ContentValues cv = new ContentValues();
            /**
             * TODO
             * Use transactions if possible
             * */
            Log.i(TAG, "Creating course: " + course.getCourseCode());
            cv.put(COURSE_NAME_COLUMN, course.getCourseName());
            cv.put(COURSE_CODE_COLUMN, course.getCourseCode());

            long insert = sqLiteDatabase.insert(COURSE_TABLE, null, cv);
            Log.i(TAG, "Created course: " + course.getCourseCode());


        });

//        sqLiteDatabase.close();


        Log.i(TAG, "Done creating courses");

    }

    public void onAddSchedules(SQLiteDatabase sqLiteDatabase, List<ScheduleEntry> schedules) {

        Log.i(TAG, "Creating Schedule: ");


        schedules.forEach(scheduleEntry -> {
//            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            /**
             * TODO
             * Use transactions if possible
             * */
            cv.put(SCHEDULE_DAY_COLUMN, scheduleEntry.getDay());
            cv.put(COURSE_CODE_COLUMN, scheduleEntry.getCourseCode());
            cv.put(SCHEDULE_START_TIME_COLUMN, scheduleEntry.getStartTime());
            cv.put(SCHEDULE_END_TIME_COLUMN, scheduleEntry.getEndTime());
            cv.put(SCHEDULE_VENUE_COLUMN, scheduleEntry.getVenue());


            long insert = sqLiteDatabase.insert(SCHEDULE_TABLE, null, cv);

//            sqLiteDatabase.close();

        });
        Log.i(TAG, "Done Creating Schedules");

    }

    public long onAddUserImage(int id, String userImage) {

        Log.i(TAG, "adding user images");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ID_COLUMN, id);
        cv.put(IMAGE_LOCATION_COLUMN, userImage);

        long insert = sqLiteDatabase.insert(STUDENT_IMG_TABLE, null, cv);
        Log.i(TAG, "added user images");
        sqLiteDatabase.close();
        return insert;
    }

    public boolean onAddUserToAttendanceRegister(int userId, int scheduleId, double confidenceLevel) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        Log.d(TAG, "On Add User: " + scheduleId);

        Date date = new Date();

        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm a");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-M-yyyy ");

        cv.put(ID_COLUMN, userId);
        cv.put(SCHEDULE_ID_COLUMN, scheduleId);
        cv.put(TIME_COLUMN, timeFormatter.format(date));
        cv.put(DATE_COLUMN, dateFormatter.format(date));
        cv.put(CONFIDENCE_LEVEL_COLUMN, confidenceLevel);

        // TODO this query should only occur if user has not been submitted for the same day

//        long insert = sqLiteDatabase.insert(STUDENT_ATTENDANCE_TABLE, TIME_COLUMN, cv);
        long insert = sqLiteDatabase.insertWithOnConflict(STUDENT_ATTENDANCE_TABLE, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        Log.i(TAG, "added user to attendance: inserId "+insert);
        sqLiteDatabase.close();

        return insert != -1;
    }

    public boolean setStudentStatusToOnline(int id) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(IS_ONLINE_COLUMN, true);

        long insert = sqLiteDatabase.update(STUDENT_TABLE, cv, ID_COLUMN + "=?", new String[]{String.valueOf(id)});

        sqLiteDatabase.close();
        return insert != -1;
    }


    public boolean resetStudentStatusToOffline() {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(IS_ONLINE_COLUMN, false);

        long insert = sqLiteDatabase.update(STUDENT_TABLE, cv, null, null);

        sqLiteDatabase.close();
        return insert != -1;
    }


    public boolean onGetUserStatus(int id) {
        Log.i(TAG, "getting user by id: ");

        boolean status = false;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();


        Cursor cursor = sqLiteDatabase.query(true, STUDENT_TABLE, new String[]{
                IS_ONLINE_COLUMN
        }, ID_COLUMN + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor.moveToFirst()) {

            status = cursor.getInt(cursor.getColumnIndexOrThrow(IS_ONLINE_COLUMN)) == 1 ? true : false;

        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        Log.i(TAG, "gotten user");
        sqLiteDatabase.close();

        return status;
    }


    public ArrayList<UserModel> onGetUsers() {
        Log.i(TAG, "getting users");

        ArrayList<UserModel> output = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();


        Cursor cursor = sqLiteDatabase.query(true, STUDENT_TABLE, new String[]{
                ID_COLUMN, EMAIL_COLUMN, FIRST_NAME_COLUMN, LAST_NAME_COLUMN
        }, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COLUMN));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(FIRST_NAME_COLUMN));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(LAST_NAME_COLUMN));

                UserModel user = new UserModel(email, firstName, lastName, id);

                output.add(user);

            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        sqLiteDatabase.close();
        Log.i(TAG, "gotten users");

        return output;
    }


    public ArrayList<ScheduleEntry> onGetSchedule() {
        Log.i(TAG, "getting schedule from database");

        ArrayList<ScheduleEntry> output = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(true, SCHEDULE_TABLE, new String[]{
                ID_COLUMN, SCHEDULE_DAY_COLUMN, COURSE_CODE_COLUMN, SCHEDULE_START_TIME_COLUMN, SCHEDULE_END_TIME_COLUMN, SCHEDULE_VENUE_COLUMN
        }, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(SCHEDULE_DAY_COLUMN));
                String courseCode = cursor.getString(cursor.getColumnIndexOrThrow(COURSE_CODE_COLUMN));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(SCHEDULE_START_TIME_COLUMN));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow(SCHEDULE_END_TIME_COLUMN));
                String venue = cursor.getString(cursor.getColumnIndexOrThrow(SCHEDULE_VENUE_COLUMN));

                ScheduleEntry entry = new ScheduleEntry(day, courseCode, startTime, endTime,venue).setId(id);

                output.add(entry);

            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }
        Log.i(TAG, "gotten users");
        sqLiteDatabase.close();

        return output;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<UserModel> onGetUsersImages() {
        Log.i(TAG, "getting user images");

        ArrayList<UserModel> users;


        users = onGetUsers();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        users.forEach(user -> {

            System.out.println(user.getFirstName());
            Cursor cursor = sqLiteDatabase.query(true, STUDENT_IMG_TABLE, new String[]{
                    IMAGE_LOCATION_COLUMN,
            }, ID_COLUMN + "= ?", new String[]{String.valueOf(user.getId())}, null, null, null, null);

            if (cursor.moveToFirst()) {

                do {

                    String imageLocation = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_LOCATION_COLUMN));
                    user.addToImages(imageLocation);

                } while (cursor.moveToNext());
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }


            Log.i(TAG, "gotten user images");

        });

        sqLiteDatabase.close();

        return users;
    }

    public ArrayList<AttendanceModel> getAttendanceList(int scheduleId) {

        ArrayList<AttendanceModel> attendanceList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Log.d(TAG, "schedule id; " + scheduleId);

        String query = "SELECT " + STUDENT_ATTENDANCE_TABLE + ".*, " + STUDENT_TABLE + ".* " +
                ", " + SCHEDULE_TABLE + "." + SCHEDULE_DAY_COLUMN +
                " FROM " + STUDENT_ATTENDANCE_TABLE
                + " INNER JOIN " + STUDENT_TABLE
                + " ON " + STUDENT_TABLE + "." + ID_COLUMN + " = " + STUDENT_ATTENDANCE_TABLE + "." + ID_COLUMN
                + " INNER JOIN " + SCHEDULE_TABLE
                + " ON " + STUDENT_ATTENDANCE_TABLE + "." + SCHEDULE_ID_COLUMN + " = " + SCHEDULE_TABLE + "." + ID_COLUMN
                + " WHERE " + STUDENT_ATTENDANCE_TABLE + "." + SCHEDULE_ID_COLUMN + " = ?"
                + " ORDER BY " + TIME_COLUMN + " DESC";

//        String query = "SELECT * FROM student INNER JOIN attendance ON student.id= attendance.id inner join schedule on attendance.scheduleId = schedule.id";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{ String.valueOf(scheduleId)});
        if (cursor.moveToFirst()) {

            Log.d(TAG, "Returned result " + cursor.getColumnIndexOrThrow(SCHEDULE_ID_COLUMN));

            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(FIRST_NAME_COLUMN));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(LAST_NAME_COLUMN));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COLUMN));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME_COLUMN));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE_COLUMN));
                UserModel user = new UserModel(email, firstName, lastName, id);

                attendanceList.add(new AttendanceModel(user, date+ " "+time));

            } while (cursor.moveToNext());
        }

        Log.d(TAG, "done");

        if (!cursor.isClosed()) {
            cursor.close();
        }

        sqLiteDatabase.close();

        return attendanceList;
    }

    public void deletePictureFromImageTable(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "DELETE FROM " + STUDENT_IMG_TABLE + " WHERE " + ID_COLUMN + " = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(id)});

        if (!cursor.isClosed()) {
            cursor.close();
        }

        sqLiteDatabase.close();


    }


    public void deleteUserFromRegistration(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "DELETE FROM " + STUDENT_TABLE + " WHERE " + ID_COLUMN + " = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(id)});
        if (!cursor.isClosed()) {
            cursor.close();
        }

        sqLiteDatabase.close();

    }


}
