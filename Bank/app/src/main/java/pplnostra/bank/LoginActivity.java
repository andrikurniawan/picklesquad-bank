package pplnostra.bank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.OnActivityResult;
//import org.androidannotations.annotations.ViewById;

/**
 * Created by andrikurniawan.id@gmail.com
 */

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    public static final int REQ_LOGIN = 201;
    private static final int SIGN_UP = 101;

    @ViewById(R.id.mETemail)
    protected EditText mETemail;

    @ViewById(R.id.mETpassword)
    protected EditText mETpassword;

    @ViewById(R.id.mBtnLogin)
    protected Button mBtnLogin;

    @ViewById(R.id.mPBprogress)
    protected ProgressBar mPBprogress;

    /**
     * LOGIN REQUEST TO SERVER;
     * IF SUCCESS GO TO MAIN MENU, ELSE REPORT ERROR ON CREDENTIALS
     */
    private void doLogin(){
        VolleyClient volleyClient = new VolleyClient();
        RequestParams params = new RequestParams();
        params.put(ParamKey.EMAIL, mETemail.getText().toString());
        params.put(ParamKey.PASSWORD, mETpassword.getText().toString());

        volleyClient.post(RestUri.User.USER_LOGIN, null, params, null, new Response.Listener<KapilerResponse>() {
            @Override
            public void onResponse(KapilerResponse response) {
                mPBprogress.setVisibility(View.GONE);
                mBtnLogin.setVisibility(View.VISIBLE);

                switch (response.getStatus()){
                    case VolleyClient.STATUS_OK:
                        setResult(RESULT_OK);
                        finish();
                        User user = GsonUtil.getInstance().fromJson(response.getMessage(), User.class);
                        SavedUser savedUser = new SavedUser(LoginActivity.this);
                        savedUser.saveData(user);
                        break;
                    case VolleyClient.STATUS_NOT_FOUND:
                        Toast.makeText(LoginActivity.this, "Email atau password salah", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    /**
     * Check if form is valid or not
     * @return form validity
     */
    private boolean isValid() {
        return FormValidation.isEmailAddress(mETemail, true) && FormValidation.isPassword(mETpassword, true);
    }

    @Click(R.id.mBtnLogin)
    protected void onLoginClicked() {
        if (isValid()) {
            mBtnLogin.setVisibility(View.GONE);
            mPBprogress.setVisibility(View.VISIBLE);
            doLogin();
        }
    }

    @Click(R.id.mBtnSignup)
    protected void onSignupClicked() {
        SignupActivity_.intent(this).startForResult(SIGN_UP);
    }

    @OnActivityResult(SIGN_UP)
    protected void onSignUpResult(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
