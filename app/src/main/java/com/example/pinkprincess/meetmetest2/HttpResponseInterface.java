package com.example.pinkprincess.meetmetest2;

import java.util.ArrayList;

/**
 * Created by pinkprincess on 16.10.15.
 */
public interface HttpResponseInterface {
    public void displayOtherUser(ArrayList<OtherUser> userArray);
    public void userMeetingValidation(String otherUserName, Boolean userMeeting);
    public void displayBestUserRanking(ArrayList bestUserArray);
    public void displayTeamRanking(String[][] teamRankingArray);

}
