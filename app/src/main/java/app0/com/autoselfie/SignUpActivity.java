package app0.com.autoselfie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class SignUpActivity extends AppCompatActivity {

    private EditText email, password, firstName, lastName;

    private DbHelper dbHelper;
    private final String TAG = "SignUpActivity";

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);


        dbHelper = new DbHelper(getApplicationContext());

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);



    }

    private void handleIfFieldsAreEmpty(String emailText, String firstNameText, String lastNameText, String passwordText) throws Exception {
        if (emailText.isEmpty()) {
            throw new Exception("Email field cannot be empty ");
        }

        if (passwordText.isEmpty()) {
            throw new Exception("Password field cannot be empty ");
        }

        if (firstNameText.isEmpty()) {
            throw new Exception("First name field cannot be empty ");
        }

        if (lastNameText.isEmpty()) {
            throw new Exception("Last name field cannot be empty ");
        }
    }


    public void onSubmitHandler(View view) {
        Log.i(TAG, "Submit button triggered");

        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        String firstNameText = firstName.getText().toString();
        String lastNameText = lastName.getText().toString();

        try {

            handleIfFieldsAreEmpty(emailText, firstNameText, lastNameText, passwordText);
            int userId = dbHelper.onAddUser(emailText, firstNameText, lastNameText, passwordText);
            if (userId == -1) {
                throw new Exception("User account already exists");
            }
            Log.i(TAG, "User account created");
            Intent intent = new Intent(getApplicationContext(), UserView.class);

            Bundle bundle = new Bundle();
            bundle.putInt("id", userId);
            bundle.putString("email", emailText);
            bundle.putString("firstName", firstNameText);
            bundle.putString("lastName", lastNameText);




            intent.putExtras(bundle);

            startActivity(intent);

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}