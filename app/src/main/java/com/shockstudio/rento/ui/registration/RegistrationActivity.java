package com.shockstudio.rento.ui.registration;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shockstudio.rento.R;
import com.shockstudio.rento.appconfig.AppConstants;
import com.shockstudio.rento.network.HttpParse;
import com.shockstudio.rento.ui.login.LoginActivity;

import java.util.HashMap;

/**
 * @author Nishant Thapa [24/12/2019]
 */

public class RegistrationActivity extends AppCompatActivity
{

    Button registerBT, loginBT;
    EditText fnameET, lnameET, emailET, passwordET, phoneET;
    String fnameStr, lnameStr, phoneStr, emailStr, passwordStr;
//    String F_Name_Holder, L_Name_Holder, EmailHolder, PasswordHolder;
    String reason = "", finalResult ;
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Assign Id'S
        fnameET = (EditText)findViewById(R.id.first_name);
        lnameET = (EditText)findViewById(R.id.last_name);
        phoneET = (EditText)findViewById(R.id.phone);
        emailET = (EditText)findViewById(R.id.email);
        passwordET = (EditText)findViewById(R.id.password);

        registerBT = (Button)findViewById(R.id.register);
        loginBT = (Button)findViewById(R.id.login);

        //Adding Click Listener on button.
        registerBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fnameStr = fnameET.getText().toString();
                lnameStr = lnameET.getText().toString();
                emailStr = emailET.getText().toString();
                phoneStr = phoneET.getText().toString();
                passwordStr = passwordET.getText().toString();
                if(isFirstNameValid(fnameStr) && isPhoneValid(phoneStr) && isPasswordValid(passwordStr))
                {

                    register(fnameStr, lnameStr, phoneStr, emailStr, passwordStr);
                }
                else
                {
                    Toast.makeText(RegistrationActivity.this, reason, Toast.LENGTH_LONG).show();
                }
            }
        });

        loginBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }


    private boolean isPhoneValid(String phone)
    {
        if(phone == null)
        {
            reason = reason + "\nPhone Number cannot be empty";
            return false;
        }
        if (!phone.equals(""))
        {
            if(phone.length() == 10)
            {
                return Patterns.PHONE.matcher(phone).matches();
            }
            else if(phone.length() == 13)
            {
                if(phone.charAt(0) == '+')
                {
                    if(Patterns.PHONE.matcher(phone).matches())
                    {
                        return true;
                    }
                    else
                    {
                        reason = reason + "\nInvalid Phone Number";
                        return false;
                    }
                }
                else
                {
                    reason = reason + "\nInvalid Phone Number";
                    return false;
                }
            }
            else
            {
                reason = reason + "\nInvalid Phone Number";
                return  false;
            }
        }
        else
        {
            reason = reason + "\nPhone Number cannot be empty";
            return !phone.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password)
    {
        if(password != null && password.trim().length() > 5)
        {
            return true;
        }
        else
        {
            reason = reason + "\nInvalid Password";
            return false;
        }
    }

    private boolean isFirstNameValid(String firstName)
    {
        if(firstName.length() == 0 || firstName == null)
        {
            reason = reason + "\nFirst Name cannot be empty";
            return false;
        }
        else
        {
            return true;
        }
    }


    public void register(final String first_Name, final String last_Name, final String phone, final String email, final String password)
    {
        class UserRegisterFunctionClass extends AsyncTask<String,Void,String>
        {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(RegistrationActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(RegistrationActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params)
            {
                hashMap.put("first_name",params[0]);
                hashMap.put("last_name",params[1]);
                hashMap.put("phone",params[2]);
                hashMap.put("email",params[3]);
                hashMap.put("password",params[4]);
                finalResult = httpParse.postRequest(hashMap, AppConstants.SERVER_REGISTRATION_URL);
                return finalResult;
            }
        }
        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
        userRegisterFunctionClass.execute(first_Name, last_Name, phone, email, password);
    }
}