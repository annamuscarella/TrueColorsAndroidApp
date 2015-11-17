package com.example.pinkprincess.meetmetest2;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;


/**
 * Created by mahandru on 18.10.2015.
 */
public class LoginActivity extends Activity implements HttpResponseInterface{

    Button loginbtn;
    private Context context;
    EditText username;
    EditText password;
    Thread thread;
    Thread current;
    Handler mHandler;
    TextView login_failed;
    DialogFragment waiting_dialog;
    final int LOGIN_SUCCESS = 1;
    final int LOGIN_FAILED = 0;
    HttpRequestInterface httpRequests = new HttpRequestSender();
    HttpRequestInterface offlineRequest = new OfflineTester();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        current = Thread.currentThread();
        context = this;
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
        public void handleMessage(Message inputMessage){
                switch (inputMessage.what){
                    case LOGIN_FAILED:
                        waiting_dialog.dismiss();
                        login_failed.setVisibility(View.VISIBLE);
                        break;
                    case LOGIN_SUCCESS:
                        waiting_dialog.dismiss();
                        OwnUser.loggedIn = true;
                        startActivity(new Intent(LoginActivity.this, MapsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        LoginActivity.this.finish();
                        break;
                    default:
                        waiting_dialog.dismiss();
                        break;

                }
            }
        };

        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);

        login_failed = (TextView) findViewById(R.id.tv_login_failed);
        TextView registerlink = (TextView) findViewById(R.id.tvRegisterLink);
        registerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
        loginbtn=(Button)findViewById(R.id.bLogin);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),getMD5(password.getText().toString()),Toast.LENGTH_LONG).show();
                OwnUser.userName = username.getText().toString();
                OwnUser.base64String = new String(Base64.encode(("" + username.getText() + ":" + getMD5(password.getText().toString())).getBytes(), Base64.DEFAULT));
                username.setText("");
                password.setText("");
                //Toast.makeText(getApplicationContext(), OwnUser.base64String, Toast.LENGTH_LONG).show();

                waiting_dialog = new LoginWaitDialog();
                waiting_dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Login_Wait);
                android.app.FragmentManager fm = getFragmentManager();
                waiting_dialog.show(fm, "id");



                thread = new Thread() {
                    @Override
                public void run() {
                        if (MapsActivity.connectionToServer) {
                            httpRequests.doVerifyLogin(context);
                        }
                        else {
                            offlineRequest.doVerifyLogin(context);
                        }
                    }
                };
                thread.start();



               /* if (MapsActivity.connectionToServer) {
                    httpRequests.doVerifyLogin(context);
                }
                else {
                    offlineRequest.doVerifyLogin(context);
                }*/

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(getApplicationContext(),voice_text,Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void displayOtherUser(ArrayList<OtherUser> userArray) {

    }

    @Override
    public void userMeetingValidation(String otherUserName, Boolean userMeeting) {

    }

    @Override
    public void displayBestUserRanking(String[][] bestUserArray) {

    }

    @Override
    public void displayTeamRanking(String[][] teamRankingArray) {

    }

    @Override
    public void displayFriends(String[][] friendArray) {

    }

    @Override
    public void verificationCompleted(Boolean result) {

        thread.interrupt();

        if (result){
            Message login_success_message = mHandler.obtainMessage(LOGIN_SUCCESS);
            login_success_message.sendToTarget();
        }
        else{
            Message login_failed_message = mHandler.obtainMessage(LOGIN_FAILED);
            login_failed_message.sendToTarget();
            //Toast.makeText(getApplicationContext(), "Login not successful", Toast.LENGTH_LONG).show();
        }
    }

    public static String getMD5(String input) {
        byte[] source;
        try {
            //Get byte according by specified coding.
            source = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            source = input.getBytes();
        }
        String result = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            //The result should be one 128 integer
            byte temp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = temp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            result = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
