package com.gwtf.flow;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gwtf.flow.Utilites.Constants;

public class ProfileSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        EditText txtFullName = findViewById(R.id.txtFullName);
        EditText txtMobile = findViewById(R.id.txtMobile);
        EditText txtEmail = findViewById(R.id.txtEmail);
        TextView txt_AddEmail = findViewById(R.id.txt_AddEmail);
        TextView txtUpdateName = findViewById(R.id.txtUpdateName);
        txtFullName.setText(Constants.UserName);
        txtMobile.setText(Constants.PhoneNumber);
        txtEmail.setText(Constants.Email);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Constants.Email.isEmpty())
            txt_AddEmail.setVisibility(View.VISIBLE);
        else
            txt_AddEmail.setVisibility(View.GONE);

        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txt_AddEmail.setVisibility(View.VISIBLE);
            }
        });

        txt_AddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Email", txtEmail.getText().toString());
                editor.apply();
                Toast.makeText(ProfileSettingsActivity.this, "Email Attached Successfully", LENGTH_SHORT).show();
            }
        });

        txtUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserName", txtFullName.getText().toString());
                editor.apply();
                Toast.makeText(ProfileSettingsActivity.this, "User Name Updated Successfully", LENGTH_SHORT).show();
            }
        });

        txtFullName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txtFullName.getText().equals(Constants.UserName))
                    txtUpdateName.setVisibility(View.GONE);
                else
                    txtUpdateName.setVisibility(View.VISIBLE);
            }
        });
    }
}