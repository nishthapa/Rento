package com.shockstudio.rento.ui.dashboard;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shockstudio.rento.ui.login.LoginActivity;
import com.shockstudio.rento.R;

/**
 * @author Nishant Thapa [24/12/2019]
 */

public class DashboardActivity extends AppCompatActivity
{

    Button LogOut;
    TextView EmailShow;
    String EmailHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        LogOut = (Button)findViewById(R.id.button);
        EmailShow = (TextView)findViewById(R.id.EmailShow);

        Intent intent = getIntent();
        EmailHolder = intent.getStringExtra(LoginActivity.userPhone);
        EmailShow.setText(EmailHolder);

        LogOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(DashboardActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
            }
        });
    }
}