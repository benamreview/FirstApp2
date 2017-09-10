package com.example.duyho.firstapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Display;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static int StrikeNum=0; //for outside methods to access
    public static int BallNum=0;
    public static int OutNum=0;
    private ActionMode mActionMode; //for contextual menu
    TextToSpeech toSpeech; //for Text-To-Speech Announcement (extra credit)
    int result;
    boolean SwitchStatus;//for Text-To-Speech Announcement (extra credit)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.relativeLayout); //contextual menu on background (extra credit)

        toSpeech = new TextToSpeech (MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS){
                    result = toSpeech.setLanguage(Locale.US);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Feature not supported here! Sorry.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rl.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v) {
                mActionMode = startActionMode(callback);
                mActionMode.setTitle(R.string.app_name);//changeable in strings.xml
                return true;
            }

        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button btn = (Button) findViewById(R.id.StrikeBtn);
        Button btn2 = (Button) findViewById(R.id.BallBtn);
        final TextView StrikeNumText = (TextView) findViewById(R.id.StrikeTextView);
        final TextView BallNumText = (TextView) findViewById(R.id.BallTextView);
        final TextView OutNumText = (TextView) findViewById(R.id.OutTextView);
        StrikeNumText.setText("Strike: " + Integer.toString(StrikeNum));
        BallNumText.setText("Ball: " + Integer.toString(BallNum));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                if (StrikeNum<2) {
                    StrikeNum++;
                    StrikeNumText.setText("Strike: " + Integer.toString(StrikeNum));
                }
                else{
                    //builder.setCancelable(true);
                    builder.setTitle("Alert!");
                    builder.setMessage("Out!");
                    if (SwitchStatus==true){
                        Fragment fragment = new MainActivity.SettingsScreen(); //Initialize to speak in the Main_Activity instead of only initializing in Main2Activity
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){ //check compatibility from each device whether it has the language
                            Toast.makeText(getApplicationContext(), "Feature not supported here! Sorry.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            toSpeech.speak("Out", TextToSpeech.QUEUE_FLUSH,null);
                        }
                    }
                    /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });*/
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            StrikeNumText.setVisibility(View.VISIBLE);
                            StrikeNumText.setText("Strike: 0");
                            OutNum++;
                            OutNumText.setText("Total Outs: " + Integer.toString(OutNum));
                        }
                    });

                    StrikeNum=0;
                    BallNum=0;
                    builder.show();
                }


            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                if (BallNum<3) {
                    BallNum++;
                    BallNumText.setText("Ball: " + Integer.toString(BallNum));
                }
                else{
                    //builder.setCancelable(true);
                    builder.setTitle("Alert!");
                    builder.setMessage("Walk!");
                    /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });*/
                    if (SwitchStatus==true){
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){ //check compatibility from each device whether it has the language
                            Toast.makeText(getApplicationContext(), "Feature not supported here! Sorry.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            toSpeech.speak("Walk", TextToSpeech.QUEUE_FLUSH,null);
                        }
                    }
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BallNumText.setVisibility(View.VISIBLE);
                            BallNumText.setText("Ball: 0");
                        }
                    });
                    BallNum=0;
                    StrikeNum=0;
                    builder.show();
                }


            }
        });
    }
    protected void onPause(){
        super.onPause();
        //Toast.makeText(MainActivity.this, "You have loaded  ", Toast.LENGTH_SHORT).show();
        SaveInfo(null);
    }
    protected void onResume(){
        super.onResume();
        //Toast.makeText(MainActivity.this, "You have loaded  ", Toast.LENGTH_SHORT).show();
        DisplayInfo(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset_settings) {
            Toast.makeText(MainActivity.this, "You have clicked reset button ", Toast.LENGTH_SHORT).show();
            MainActivity.BallNum=0;
            MainActivity.StrikeNum=0;
            final TextView StrikeNumText = (TextView) findViewById(R.id.StrikeTextView);
            final TextView BallNumText = (TextView) findViewById(R.id.BallTextView);
            StrikeNumText.setText("Strike: 0");
            BallNumText.setText("Ball: 0");
            return true;
        }
        else if (id == R.id.about_settings) {
            startActivity(new Intent(MainActivity.this, About.class));
            return true;
        }
        else if (id == R.id.general_settings) {
            startActivity(new Intent(MainActivity.this, Main2Activity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void SaveInfo(View view){
        SharedPreferences sharedPref = getSharedPreferences("AppInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("ballnum", BallNum);
        editor.putInt("strikenum", StrikeNum);
        editor.putInt("outnum", OutNum);
        editor.putBoolean("switchon", SettingsScreen.SwitchOn); //for saving TextToSpeech.
        editor.apply();
        Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
    }
    public void DisplayInfo(View view){
        SharedPreferences sharedPref = getSharedPreferences("AppInfo", Context.MODE_PRIVATE);
        Toast.makeText(MainActivity.this, "Loaded!", Toast.LENGTH_SHORT).show();
        int SavedOuts = sharedPref.getInt("outnum", -1);
        boolean SavedSwitch = sharedPref.getBoolean("switchon",false);
        SwitchStatus=SavedSwitch;
        OutNum = SavedOuts;
        final TextView OutNumText = (TextView) findViewById(R.id.OutTextView);
        OutNumText.setText("Total Outs: " + Integer.toString(SavedOuts));

    }

private ActionMode.Callback callback = new ActionMode.Callback() { //For contextual Menu
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
        int id = item.getItemId();
        final TextView BallNumText = (TextView) findViewById(R.id.BallTextView);
        if (id == R.id.ball_settings) {
            Toast.makeText(MainActivity.this, "You have clicked contextual ball button ", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            if (BallNum<3) {
                BallNum++;
                BallNumText.setText("Ball: " + Integer.toString(BallNum));
            }
            else{
                //builder.setCancelable(true);
                builder.setTitle("Alert!");
                builder.setMessage("Walk!");
                if (SwitchStatus==true){
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){ //check compatibility from each device whether it has the language
                        Toast.makeText(getApplicationContext(), "Feature not supported here! Sorry.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        toSpeech.speak("Walk", TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
                    /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });*/
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BallNumText.setVisibility(View.VISIBLE);
                        BallNumText.setText("Ball: 0");
                    }
                });
                BallNum=0;
                StrikeNum=0;
                builder.show();
            }
            return true;
        }
        else if (id == R.id.strike_settings) {
            Toast.makeText(MainActivity.this, "You have clicked contextual ball button ", Toast.LENGTH_SHORT).show();
            final TextView StrikeNumText = (TextView) findViewById(R.id.StrikeTextView);
            final TextView OutNumText = (TextView) findViewById(R.id.OutTextView);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            if (StrikeNum<2) {
                StrikeNum++;
                StrikeNumText.setText("Strike: " + Integer.toString(StrikeNum));
            }
            else{
                //builder.setCancelable(true);
                builder.setTitle("Alert!");
                builder.setMessage("Out!");
                if (SwitchStatus==true){
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){ //check compatibility from each device whether it has the language
                        Toast.makeText(getApplicationContext(), "Feature not supported here! Sorry.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        toSpeech.speak("Out", TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
                    /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });*/
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StrikeNumText.setVisibility(View.VISIBLE);
                        StrikeNumText.setText("Strike: 0");
                        OutNum++;
                        OutNumText.setText("Total Outs: " + Integer.toString(OutNum));
                    }
                });

                StrikeNum=0;
                BallNum=0;
                builder.show();
            }
            return true;}
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
    }
};
public static class SettingsScreen extends PreferenceFragment { //for Text-to-Speech announcement using PreferenceFragment
    public static boolean SwitchOn = false;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);
        SwitchPreference switchPreference = (SwitchPreference) findPreference("switch"); //checked if Text to Speech function is checked on
        if (switchPreference.isChecked() == true)
        {
            SwitchOn=true;
        }
        else
        {
            SwitchOn=false;
        }
    }
}
}