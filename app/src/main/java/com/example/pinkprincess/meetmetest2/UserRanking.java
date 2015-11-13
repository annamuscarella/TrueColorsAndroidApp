package com.example.pinkprincess.meetmetest2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mahandru on 22.10.2015.
 */
public class UserRanking extends Activity implements HttpResponseInterface {

    Button teamrankingbtn;
    Button aktualisierenbtn;
    Context context = this;
    HttpRequestInterface httpRequest = new HttpRequestSender();
    HttpRequestInterface offlineRequest = new OfflineTester();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topuser_fragment);
        teamrankingbtn =(Button)findViewById(R.id.bTeam);
        teamrankingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRanking.this, TeamRanking.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        if (MapsActivity.connectionToServer){httpRequest.doGetUserRanking(context);}
        else {offlineRequest.doGetUserRanking(context);}

        aktualisierenbtn=(Button)findViewById(R.id.bAktualisieren);
        aktualisierenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Aktualisieren Clicked",Toast.LENGTH_SHORT).show();
                 httpRequest.doGetUserRanking(context);//SEND REQUEST HERE
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
                startActivity(new Intent(UserRanking.this,MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;
            case R.id.action_homep:
                startActivity(new Intent(UserRanking.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(UserRanking.this, PersonalStatistics.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_score:
                startActivity(new Intent(UserRanking.this, PersonalScore.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;

            case R.id.action_ranking:
                startActivity(new Intent(UserRanking.this, UserRanking.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void displayOtherUser(ArrayList<OtherUser> userArray) {

    } //not needed

    @Override
    public void userMeetingValidation(String otherUserName, Boolean userMeeting) {

    } //not needed

    @Override
    public void displayBestUserRanking(String[][] bestUserArray) {

        TextView[] scoretext = new TextView[5];
        scoretext[0] = (TextView)findViewById(R.id.user_score_1);
        scoretext[1] = (TextView)findViewById(R.id.user_score_2);
        scoretext[2] = (TextView)findViewById(R.id.user_score_3);
        scoretext[3] = (TextView)findViewById(R.id.user_score_4);
        scoretext[4] = (TextView)findViewById(R.id.user_score_5);

        ImageView[] flag = new ImageView[5];
        flag[0] = (ImageView)findViewById(R.id.imageView_user1);
        flag[1] = (ImageView)findViewById(R.id.imageView_user2);
        flag[2] = (ImageView)findViewById(R.id.imageView_user3);
        flag[3] = (ImageView)findViewById(R.id.imageView_user4);
        flag[4] = (ImageView)findViewById(R.id.imageView_user5);

        TextView[] teamText = new TextView[5];
        teamText[0] = (TextView)findViewById(R.id.user_player_1);
        teamText[1] = (TextView)findViewById(R.id.user_player_2);
        teamText[2] = (TextView)findViewById(R.id.user_player_3);
        teamText[3] = (TextView)findViewById(R.id.user_player_4);
        teamText[4] = (TextView)findViewById(R.id.user_player_5);

        for (int i = 0; i < scoretext.length; i++) {
            scoretext[i].setText(bestUserArray[i][2]);
            if (bestUserArray[i][1].equals("german"))
            {flag[i].setImageResource(R.drawable.germanflag);}
            else {
                flag[i].setImageResource(R.drawable.australianflag);}
            teamText[i].setText(bestUserArray[i][0]);
        }
    }

    @Override
    public void displayTeamRanking(String[][] teamRankingArray) {
        //not needed


    }

    @Override
    public void displayFriends(String[][] friendArray) {

    }

    @Override
    public void verificationCompleted(Boolean result) {

    }
}
