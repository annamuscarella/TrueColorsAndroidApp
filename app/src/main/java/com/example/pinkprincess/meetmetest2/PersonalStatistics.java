package com.example.pinkprincess.meetmetest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by mahandru on 22.10.2015.
 */
public class PersonalStatistics extends Activity {

    Button freundschaftbtn;
    Button punktestandbtn;
    Button deinteambtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_profile);

        freundschaftbtn=(Button)findViewById(R.id.bFriends);
        freundschaftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Freundschaft Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        punktestandbtn=(Button)findViewById(R.id.bScore);
        punktestandbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Punktestand Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        deinteambtn=(Button)findViewById(R.id.b);
        deinteambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Dein Team Clicked",Toast.LENGTH_SHORT).show();
            }
        });

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
                startActivity(new Intent(PersonalStatistics.this,MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;
            case R.id.action_register:
                startActivity(new Intent(PersonalStatistics.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;
            case R.id.action_homep:
                startActivity(new Intent(PersonalStatistics.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_login:
                startActivity(new Intent(PersonalStatistics.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(PersonalStatistics.this, PersonalStatistics.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_score:
                startActivity(new Intent(PersonalStatistics.this, PersonalScore.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_ranking:
                startActivity(new Intent(PersonalStatistics.this, TeamRanking.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
