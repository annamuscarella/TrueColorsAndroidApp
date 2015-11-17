package com.example.pinkprincess.meetmetest2;

import java.util.ArrayList;

/**
 * Created by pinkprincess on 16.10.15.
 */
public interface HttpResponseInterface {
    void displayOtherUser(ArrayList<OtherUser> userArray);
    void userMeetingValidation(String otherUserName, Boolean userMeeting);
    void displayBestUserRanking(String[][] bestUserArray);
    void displayTeamRanking(String[][] teamRankingArray);
    void displayFriends(String[][] friendArray);
    void verificationCompleted(Boolean result);

}
