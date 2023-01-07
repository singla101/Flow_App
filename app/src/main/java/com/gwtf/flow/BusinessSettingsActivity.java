package com.gwtf.flow;

import static android.widget.Toast.LENGTH_SHORT;
import static com.gwtf.flow.Utilites.Constants.Address;
import static com.gwtf.flow.Utilites.Constants.Business_Name;
import static com.gwtf.flow.Utilites.Constants.Business_Selected;
import static com.gwtf.flow.Utilites.Constants.UserName;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gwtf.flow.Database.SqlDatabase;
import com.gwtf.flow.model.BusinessModel;

import java.util.ArrayList;
import java.util.List;

public class BusinessSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_settings);

        EditText txtBusinessName = findViewById(R.id.txtBusinessName);
        EditText txtBuisnessType = findViewById(R.id.txtBuisnessType);
        EditText txtOwnerName = findViewById(R.id.txtOwnerName);
        EditText txtAddress = findViewById(R.id.txtAddress);
        Button saveBtn = findViewById(R.id.saveBtn);
        SqlDatabase database = new SqlDatabase(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<BusinessModel> models = new ArrayList<>();
        models = database.getBusines(Business_Selected);
        txtBusinessName.setText(Business_Name);
        txtBuisnessType.setText(models.get(0).getType());
        txtBuisnessType.setFocusable(false);
        txtOwnerName.setText(UserName);
        txtAddress.setText(Address);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.updateBusinessName(Business_Selected, txtBusinessName.getText().toString());
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserName", txtOwnerName.getText().toString());
                editor.putString("Address", txtAddress.getText().toString());
                editor.apply();
                Toast.makeText(BusinessSettingsActivity.this, "Data Updated Successfully", LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }
}