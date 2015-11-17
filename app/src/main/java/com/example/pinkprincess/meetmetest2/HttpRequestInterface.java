package com.example.pinkprincess.meetmetest2;

import android.content.Context;

/**
 * Created by pinkprincess on 25.10.15.
 */
public interface HttpRequestInterface {

    void doGetOtherUsers(Context context);
    void doGetUserMeeting(Context context, String otherUserName, String verificationCode);
    void doGetTeamRanking(Context context);
    void doGetUserRanking(Context context);
    void doGetFriends(Context context);
    void doVerifyLogin(Context context);
}
