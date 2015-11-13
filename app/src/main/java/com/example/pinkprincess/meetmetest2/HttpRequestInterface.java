package com.example.pinkprincess.meetmetest2;

import android.content.Context;

/**
 * Created by pinkprincess on 25.10.15.
 */
public interface HttpRequestInterface {

    public void doGetOtherUsers(Context context);
    public void doGetUserMeeting(Context context, String otherUserName, String verificationCode);
    public void doGetTeamRanking(Context context);
    public void doGetUserRanking(Context context);
    public void doGetFriends(Context context);
    public void doVerifyLogin(Context context);
}
