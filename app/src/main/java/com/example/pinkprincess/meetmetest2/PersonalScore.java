package com.example.pinkprincess.meetmetest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mahandru on 22.10.2015.
 */
public class PersonalScore extends Activity {

    Button aktualisierenbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalscore);
        aktualisierenbtn=(Button)findViewById(R.id.bAktualisieren);

        TextView scoreText = (TextView)findViewById(R.id.score_field);
        scoreText.setText(OwnUser.score.toString());
        aktualisierenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Aktualisieren Clicked", Toast.LENGTH_SHORT).show();
                TextView scoreText = (TextView)findViewById(R.id.score_field);
                scoreText.setText(OwnUser.score.toString());
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
                startActivity(new Intent(PersonalScore.this,MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

            case R.id.action_homep:
                startActivity(new Intent(PersonalScore.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"Settings Clicked",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(PersonalScore.this, PersonalStatistics.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;

            case R.id.action_score:
                startActivity(new Intent(PersonalScore.this, PersonalScore.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;

            case R.id.action_ranking:
                startActivity(new Intent(PersonalScore.this, TeamRanking.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
}
