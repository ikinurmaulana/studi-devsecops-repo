package com.example.jujojazbase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import json.JSONArray;
import json.JSONObject;
import kotlin.Unit;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private FloatingActionButton fabHome;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public List<ModelData> data = new ArrayList<>();
    public List<ModelData> new_data = new ArrayList<>();
    public static List<List<Integer>> id = new ArrayList<>();

    public Unit onDone(JSONObject json){
        new_data = JujojazLib.Companion.convertJSONToModelData(json);
        Auth.datas = new_data;
        List<String> checkTipe = new ArrayList<>();
        id = new ArrayList<>();
        for (ModelData o : this.new_data) {
            if (!checkTipe.contains(o.getTipe())) {
                Log.d("HomeActivityTipe", o.getTipe());
                StringBuilder merk = new StringBuilder();
                List<Integer> idMerk = new ArrayList<>();
                for (ModelData i : this.new_data) {
                    if (i.getTipe().equals(o.getTipe()) && !checkTipe.contains(i.getTipe())){
                        merk.append(i.getCar_name()).append("\n");
                        idMerk.add(i.getId());
                    }
                }
                checkTipe.add(o.getTipe());
                data.add(new ModelData(o.getId(), merk.toString(), o.getTipe()));
                id.add(idMerk);
            }
        }

        Log.d("HomeActivityID", id.toString());
        Log.d("HomeActivity", String.valueOf(Auth.datas.size()));
        Log.d("HomeActivity", String.valueOf(data.size()));

        recyclerView = findViewById(R.id.recyclerViewHome);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterHomeRecycler(this, data);
        recyclerView.setAdapter(adapter);
        return Unit.INSTANCE;
    }
    public Unit onError(String msg){
        return Unit.INSTANCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.homeToolBar);
        setSupportActionBar(toolbar);

        fabHome = findViewById(R.id.fabHome);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                Log.d("Home", v.toString());
                Intent intent = new Intent(getApplicationContext(), AddVehicle.class);
                startActivity(intent);
            }
        });

        JujojazLib.Companion.hitAPI("/api/allvehicles/", Auth.authJson, this, this::onDone, this::onError, true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        MenuItem logOut = menu.findItem(R.id.menuLogOut);
        logOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                File file = new File(getApplicationContext().getFilesDir(), "Auth");
                new File(file, "Login").delete();
                Intent intent = new Intent(getApplicationContext(), Auth.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Intent broadcastNotification = new Intent(HomeActivity.this, BroadcastNotification.class);
                Notification.restart = false;
                broadcastNotification.putExtra("STARTSERVICE", false);
                sendBroadcast(broadcastNotification);

                Intent alarmManagerReceiver = new Intent(HomeActivity.this, AlarmManagerReceiver.class);
                alarmManagerReceiver.putExtra("STARTALARM", false);
                sendBroadcast(alarmManagerReceiver);

                finish();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput =  newText.toLowerCase();
        List<ModelData> newData = new ArrayList<>();
        for (ModelData dataset : data) {
            if (dataset.getTipe().toLowerCase().contains(userInput)) {
                newData.addAll(Collections.singleton(dataset));
            }
        }

        AdapterHomeRecycler.data = new ArrayList<>();
        AdapterHomeRecycler.data.addAll(newData);
        Log.d("Home", AdapterHomeRecycler.data.toString());
        Log.d("Home", data.toString());
        adapter.notifyDataSetChanged();
        return true;
    }

}
