package com.example.duyho.firstapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Fragment fragment = new MainActivity.SettingsScreen(); // for Text-to-speech Announcements (extra credit)
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null){
            //created for the first time
            fragmentTransaction.add(R.id.relativeLayout2, fragment, "settings_fragment"); //Layout of the main 2 activity
            fragmentTransaction.commit();
        }
        else{
            fragment = getFragmentManager().findFragmentByTag("settings_fragment");
        }
    }
}
