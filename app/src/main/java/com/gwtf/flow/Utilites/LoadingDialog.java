package com.gwtf.flow.Utilites;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.gwtf.flow.R;


public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;
    public LoadingDialog (Activity activity1) {
        activity = activity1;

    }

    public void start() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loadingdialog, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

}
