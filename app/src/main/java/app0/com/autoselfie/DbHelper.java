package app0.com.autoselfie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.LocaleData;
import android.os.Build;
import android.util.Log;


import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    private static final String STUDENT_TABLE = "student";
    private static final String ID_COLUMN = "id";
    private static final String STUDENT_IMG_TABLE = "images";
    private static final String STUDENT_ATTENDANCE_TABLE = "attendance";
    private static final String FIRST_NAME_COLUMN = "firstName";
    private static final String LAST_NAME_COLUMN = "lastName";
    private static final String IMAGE_LOCATION_COLUMN = "imageLocation";
    private static final String TIME_COLUMN = "time";
    private static final String IS_ONLINE_COLUMN = "isOnline";
    private static final String EMAIL_COLUMN = "email";
    private static final String PASSWORD_COLUMN = "password";
    private static final String TAG = "DBHelper";


    public DbHelper(Context context) {
        super(context, STUDENT_TABLE + ".db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Creating Tables If tables do not exist");

        String createStudentTable = "CREATE TABLE " + STUDENT_TABLE + " ( " + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL_COLUMN + " TEXT ," + PASSWORD_COLUMN + " TEXT," + FIRST_NAME_COLUMN + " TEXT, " + LAST_NAME_COLUMN + " TEXT, " + IS_ONLINE_COLUMN + " BOOLEAN DEFAULT 0, UNIQUE (" + EMAIL_COLUMN + ") )";
        String createStudentImageTable = "CREATE TABLE " + STUDENT_IMG_TABLE + " ( " + ID_COLUMN + " INTEGER , " + IMAGE_LOCATION_COLUMN + " TEXT, FOREIGN KEY (" + ID_COLUMN + ") REFERENCES " + STUDENT_TABLE + "(" + ID_COLUMN + ") )";
        String createStudentAttendanceTable = "CREATE TABLE " + STUDENT_ATTENDANCE_TABLE + " ( " + ID_COLUMN + " INTEGER , " + TIME_COLUMN + " DATETIME DEFAULT CURRENT_TIMESTAMP , FOREIGN KEY (" + ID_COLUMN + ") REFERENCES " + STUDENT_TABLE + "(" + ID_COLUMN + ") )";

        sqLiteDatabase.execSQL(createStudentTable);
        sqLiteDatabase.execSQL(createStudentImageTable);
        sqLiteDatabase.execSQL(createStudentAttendanceTable);

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


        return (int) insert;
    }

    public long onAddUserImage(int id, String userImage) {

        Log.i(TAG, "adding user images");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ID_COLUMN, id);
        cv.put(IMAGE_LOCATION_COLUMN, userImage);

        long insert = sqLiteDatabase.insert(STUDENT_IMG_TABLE, null, cv);
        Log.i(TAG, "added user images");
//        sqLiteDatabase.close();
        return insert;
    }

    public boolean onAddUserToAttendanceRegister(int id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ID_COLUMN, id);

        long insert = sqLiteDatabase.insert(STUDENT_ATTENDANCE_TABLE, TIME_COLUMN, cv);
        Log.i(TAG, "added user to attendance");

        return insert != -1;
    }

    public boolean setStudentStatusToOnline(int id) {

        Log.i(TAG, "adding user images");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(IS_ONLINE_COLUMN, true);

        long insert = sqLiteDatabase.update(STUDENT_TABLE, cv, ID_COLUMN + "=?", new String[]{String.valueOf(id)});
        Log.i(TAG, "added user to attendance");

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
        Log.i(TAG, "gotten users");

        return output;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<UserModel> onGetUsersImages() {
        Log.i(TAG, "getting user images");

        ArrayList<UserModel> users;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        users = onGetUsers();

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

        return users;
    }

    public ArrayList<AttendanceModel> getAttendanceList() {

        ArrayList<AttendanceModel> attendanceList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "SELECT * FROM " + STUDENT_ATTENDANCE_TABLE + " INNER JOIN " + STUDENT_TABLE + " ON " + STUDENT_TABLE + "." + ID_COLUMN + " = " + STUDENT_ATTENDANCE_TABLE + "." + ID_COLUMN + " ORDER BY " + TIME_COLUMN + " DESC";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{});
        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(FIRST_NAME_COLUMN));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(LAST_NAME_COLUMN));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COLUMN));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME_COLUMN));
                UserModel user = new UserModel(email, firstName, lastName, id);

                attendanceList.add(new AttendanceModel(user, time));

            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return attendanceList;
    }

    public void deletePictureFromImageTable(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "DELETE FROM " + STUDENT_IMG_TABLE + " WHERE " + ID_COLUMN + " = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(id)});


    }


}
