package picklenostra.picklebankapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LoginActivity extends ActionBarActivity {

    private ImageView imgBrandingLogo;
    private EditText etEmail, etPassword;
    private ProgressBar pbProgressBar;
    private Button btnLogin;

    private String email,password;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getApplicationContext());

        imgBrandingLogo = (ImageView) findViewById(R.id.imgBrandingLogo);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if(validate(email,password)){
                    session.createUserLogin(email,password);
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean validate(String email, String password){
        boolean isValid = true;
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Enter a valid email address");
            isValid = false;
        }
        else if(password.isEmpty() || password.length() < 8){
            etPassword.setError("Password must be 8 characters or more");
            isValid = false;
        }
        else{
            etEmail.setError(null);
            etPassword.setError(null);
        }
        return isValid;
    }

}
