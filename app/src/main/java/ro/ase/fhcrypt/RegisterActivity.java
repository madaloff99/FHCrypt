package ro.ase.fhcrypt;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class RegisterActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    String[] genders = {"Female", "Male"};
    private Button registerButton;
    private EditText username;
    private Spinner genderSpin;
    private EditText password;
    private EditText passwordConf;
    private String userName;
    private String userPass;
    private String userPassConf;
    private String userGender = "";
    private boolean result = false;


    private boolean registerUser(String nameUser, String passUser, String genderUser) throws NoSuchAlgorithmException {
        if (nameUser.equals("")) {
            Toast.makeText(this, "You have to enter username!", Toast.LENGTH_SHORT).show();
            return result;
        }
        if (passUser.equals("")) {
            Toast.makeText(this, "You have to enter password!", Toast.LENGTH_SHORT).show();
            return result;
        }
        if (genderUser.equals("")) {
            Toast.makeText(getApplicationContext(), "You have to select a gender!", Toast.LENGTH_LONG).show();
            return result;
        }


        String hashedPass = Helper.hashPassword(passUser);
        User newUser = new User(nameUser, hashedPass, genderUser);

        AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        UserDao userDao = appDatabase.userDao();
        userDao.insert(newUser);
        result = true;

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.pass);
        passwordConf = (EditText) findViewById(R.id.passConf);
        genderSpin = (Spinner) findViewById(R.id.gender_spin);

        genderSpin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genders);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpin.setAdapter(aa);

        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = username.getText().toString();
                userPass = password.getText().toString();
                userPassConf = passwordConf.getText().toString();

                if (userPassConf.equals("") || !userPassConf.equals(userPass)) {
                    Toast.makeText(getApplicationContext(), "Your passwords don't match!", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    boolean result = registerUser(userName, userPass, userGender);
                    if (result) {
                        Toast.makeText(getApplicationContext(), "You are registered now!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to register user!", Toast.LENGTH_LONG).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        userGender = genders[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}