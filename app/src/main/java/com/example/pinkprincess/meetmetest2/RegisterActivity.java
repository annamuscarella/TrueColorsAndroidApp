package com.example.pinkprincess.meetmetest2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by mahandru on 18.10.2015.
 */
public class RegisterActivity extends Activity {

    Button registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerbtn=(Button)findViewById(R.id.bRegister);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Register Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }






}
