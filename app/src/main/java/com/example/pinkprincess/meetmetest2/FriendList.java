package com.example.pinkprincess.meetmetest2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TODO: document your custom view class.
 */
public class FriendList extends Activity {

    public static String[][] friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlist);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams textParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(20, 20, 20, 40);
        TableRow.LayoutParams picParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        picParams.setMargins(20, 20, 20, 20);
        TableLayout mTableLayout = (TableLayout) findViewById(R.id.friend_tableLayout);
        mTableLayout.removeAllViews();
        for (int i=0; i<friends.length; i++){
            TableRow row =  new TableRow(this);
            row.setLayoutParams(rowParams);
            if(i%2==0){
                row.setBackgroundResource(R.color.hellgruen);
            }
            else {row.setBackgroundResource(R.color.roworange);}
            TextView name = new TextView(this);
            name.setLayoutParams(textParams);
            name.setTextSize(17);
            name.setTextColor(Color.rgb(255, 255, 255));
            //TextView nation = new TextView(this);
            ImageView nation = new ImageView(this);
            nation.setLayoutParams(textParams);
            nation.setLayoutParams(picParams);
            nation.setAdjustViewBounds(true);
            nation.setMaxHeight(30);
            //nation.setTextSize(20);
            //nation.setTextColor(Color.rgb(255, 255, 255));
            ImageView picture = new ImageView(this);
            picture.setLayoutParams(picParams);
            picture.setAdjustViewBounds(true);
            picture.setMaxHeight(80);
            name.setText(friends[i][0]);
            if(friends[i][1].equals("german")){
                nation.setImageResource(R.drawable.germanflag);
            }
            else {nation.setImageResource(R.drawable.australianflag);}
            //nation.setText(friends[i][1]);
            picture.setImageResource(R.drawable.profilepic);


            row.addView(picture);
            row.addView(name);
            row.addView(nation);

            TableRow row2 = new TableRow(this);
            row2.setLayoutParams(rowParams);
            View green = new View(this);
            green.setBackgroundResource(R.color.rowwhite);
            green.setMinimumHeight(10);
            View green2 = new View(this);
            green2.setBackgroundResource(R.color.rowwhite);
            green2.setMinimumHeight(10);
            View green3 = new View(this);
            green3.setBackgroundResource(R.color.rowwhite);
            green3.setMinimumHeight(10);
            row2.addView(green);
            row2.addView(green2);
            row2.addView(green3);

            mTableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            mTableLayout.addView(row2, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }





/* Add Button to row.
        row.addView(picture);
        row.addView(name);
        row.addView(nation);
/* Add row to TableLayout. */
//tr.setBackgroundResource(R.drawable.sf_gradient_03);
      //  mTableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));*/

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
                startActivity(new Intent(FriendList.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case R.id.action_homep:
                startActivity(new Intent(FriendList.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"Settings Clicked",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(FriendList.this, PersonalStatistics.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;

            case R.id.action_score:
                startActivity(new Intent(FriendList.this, PersonalScore.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;

            case R.id.action_ranking:
                startActivity(new Intent(FriendList.this, TeamRanking.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
