package com.example.pinkprincess.meetmetest2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by pinkprincess on 13.11.15.
 */
public class LoginWaitDialog extends DialogFragment {

    /*static LoginWaitDialog newInstance(int num) {
        LoginWaitDialog f = new LoginWaitDialog();
        return f;
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder test = new AlertDialog.Builder(getActivity())
                .setMessage("We are trying to verify your credentials. This might take a few seconds.")
                .setTitle("Please Wait");
        //test.setView(R.id.login_wait_window);
        return test.create();
    }

}
