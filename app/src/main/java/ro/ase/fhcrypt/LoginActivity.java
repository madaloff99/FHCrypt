package ro.ase.fhcrypt;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private Button registerButton;
    private EditText username;
    private EditText password;
    private String userName;
    private String userPass;
    public static final String PREFS_GAME = "Preferences";
    private boolean result = false;
    private AppDatabase appDatabase;
    private UserDao userDao;


    private boolean loginNewUser(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (username.equals("")) {
            Toast.makeText(this, "Please enter username!", Toast.LENGTH_SHORT).show();
            return result;
        }
        if (password.equals("")) {
            Toast.makeText(this, "Please enter password!", Toast.LENGTH_SHORT).show();
            return result;
        }
        String hashedPass = Helper.hashPassword(password);
        User dbUser = userDao.login(username, hashedPass);
        if(dbUser!=null)
            result=true;

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.pass);
        loginButton = (Button) findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        appDatabase=AppDatabase.getAppDatabase(getApplicationContext());
        userDao=appDatabase.userDao();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = username.getText().toString();
                userPass = password.getText().toString();
                try {
                    boolean result = loginNewUser(userName, userPass);
                    if (result) {

                        String hashedPass = Helper.hashPassword(userPass);
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("LoggedUserName", userName);
                        editor.putString("LoggedUserPassword", hashedPass);
                        editor.commit();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });
    }
}