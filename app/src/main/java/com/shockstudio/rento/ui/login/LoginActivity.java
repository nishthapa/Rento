package com.shockstudio.rento.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shockstudio.rento.R;
import com.shockstudio.rento.appconfig.AppConstants;
import com.shockstudio.rento.network.HttpParse;
import com.shockstudio.rento.ui.dashboard.DashboardActivity;

import java.util.HashMap;

/**
 * @author Nishant Thapa [24/12/2019]
 */

public class LoginActivity extends AppCompatActivity
{

    EditText Email, passwordET, phoneET;
    Button loginBT ;
    String passwordStr, EmailHolder, phoneStr;
    String finalResult ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
//    public static final String UserEmail = "";
    public static final String userPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Email = (EditText)findViewById(R.id.email);
        phoneET = (EditText)findViewById(R.id.phone);
        passwordET = (EditText)findViewById(R.id.password);
        loginBT = (Button)findViewById(R.id.login);

        loginBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                phoneStr = phoneET.getText().toString();
                passwordStr = passwordET.getText().toString();
                if(isPhoneValid(phoneStr) && isPasswordValid(passwordStr))
                {
                    login(phoneStr, passwordStr);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Invalid Login Credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isPhoneValid(String phone)
    {
        if(phone == null)
        {
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
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return  false;
            }
        }
        else
        {
            return !phone.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password)
    {
        return (password != null && password.trim().length() > 5);
    }

    public void login(final String phone, final String password)
    {
        class UserLoginClass extends AsyncTask<String,Void,String>
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LoginActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg)
            {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                if(httpResponseMsg.equalsIgnoreCase(AppConstants.LOGIN_CREDENTIALS_MATCH))
                {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra(userPhone,phone);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params)
            {
                hashMap.put("phone",params[0]);
                hashMap.put("password",params[1]);
                finalResult = httpParse.postRequest(hashMap, AppConstants.SERVER_LOGIN_URL);
                return finalResult;
            }
        }
        UserLoginClass userLoginClass = new UserLoginClass();
        userLoginClass.execute(phone,password);
    }

}