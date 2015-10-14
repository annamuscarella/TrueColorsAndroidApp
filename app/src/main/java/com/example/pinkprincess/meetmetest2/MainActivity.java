package com.example.pinkprincess.meetmetest2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button bLogout;
    Button bBegin;
    EditText etName, etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    etName = (EditText) findViewById(R.id.etName);
    etUsername = (EditText) findViewById(R.id.etUsername);

    bLogout =(Button) findViewById(R.id.bRegister);
        bBegin =(Button) findViewById(R.id.bBegin);
    bLogout.setOnClickListener(this);
        bBegin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.bBegin:

                break;
        }

    }
}
