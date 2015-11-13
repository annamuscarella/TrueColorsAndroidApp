package com.example.pinkprincess.meetmetest2;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_call:
                Intent dialer= new Intent(Intent.ACTION_DIAL);
                startActivity(dialer);
                return true;

            case R.id.action_homepic:
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
                return true;


            case R.id.action_homep:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                return true;


            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(MainActivity.this, PersonalStatistics.class));
                return true;

            case R.id.action_score:
                startActivity(new Intent(MainActivity.this, PersonalScore.class));
                return true;

            case R.id.action_ranking:
                startActivity(new Intent(MainActivity.this, TeamRanking.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(getApplicationContext(),voice_text,Toast.LENGTH_LONG).show();

        }
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
