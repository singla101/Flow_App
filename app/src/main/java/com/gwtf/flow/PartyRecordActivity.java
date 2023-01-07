package com.gwtf.flow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwtf.flow.Database.SqlDatabase;
import com.gwtf.flow.Utilites.AmountCalculator;
import com.gwtf.flow.adapter.DataAdapter;
import com.gwtf.flow.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class PartyRecordActivity extends AppCompatActivity {

    private String _id;
    private EditText searchBox;
    private TextView netTotalTxt, totalEntriesTxt, cashOutTxt, cashInTxt;
    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private SqlDatabase database;
    private List<DataModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_record);
        _id = getIntent().getStringExtra("id");
        database = new SqlDatabase(this);
        recyclerView = findViewById(R.id.listReports);
        netTotalTxt = findViewById(R.id.totalAmount);
        cashOutTxt = findViewById(R.id.totalOut);
        totalEntriesTxt = findViewById(R.id.totalEnteries);
        cashInTxt =  findViewById(R.id.totalIn);
        netTotalTxt.setText("" + (AmountCalculator.getPartyIn(this, _id) - AmountCalculator.getPartyOut(this, _id)));
        cashOutTxt.setText("" + AmountCalculator.getPartyOut(this, _id));
        cashInTxt.setText("" + AmountCalculator.getPartyIn(this, _id));

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData();
    }

    private void getData() {
        list = database.getDataParty(_id);
        totalEntriesTxt.setText(list.size() + " Entries");
        adapter = new DataAdapter(list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

}