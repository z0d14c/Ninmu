// Main Menu activity
// Handles access to session activity, settings activity
// and provides a pretty splash screen :)
package tgrice.utdallas.edu.ninmu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class MainActivity extends Activity {

    // grab these variables in onResume so they can be passed to Session activity
    Integer sessionLength;
    Integer breakLength;
    // bind event listeners in onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind listeners to buttons
        Button sessionButton = (Button) findViewById(R.id.sessionButton);
        sessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to session activity
                launchSession(v);
            }
        });
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                // go to session activity
                launchSettings(v);
            }
        });
    }

    // get settings from onResume
    @Override
    protected void onResume() {
        super.onResume();
        NinmuDB db = new NinmuDB(this);
        SQLiteDatabase qdb = db.getReadableDatabase();
        String sessionLengthVar = "'" + getString(R.string.session_length_var) + "'";
        String breakLengthVar = "'" + getString(R.string.break_length_var) + "'";
        Cursor sessionLengthCursor = qdb.rawQuery("SELECT * FROM settings where setting_name=" +
                sessionLengthVar, null);
        Cursor breakLengthCursor = qdb.rawQuery("SELECT * FROM settings where setting_name=" +
                breakLengthVar, null);
        if (sessionLengthCursor.moveToFirst()) { // move to first (and only) item
            this.sessionLength = sessionLengthCursor.getInt(2);
        }
        if (breakLengthCursor.moveToFirst()) { // move to first (and only) item
            this.breakLength = breakLengthCursor.getInt(2);
        }
        // close after use to free up resources.
        sessionLengthCursor.close();
        breakLengthCursor.close();
    }

    // launch session activity, pass relevant info via intent
    public void launchSession(View view) {
        Intent intent = new Intent(MainActivity.this, ActivitySession.class);
        intent.putExtra("session_length", this.sessionLength);
        intent.putExtra("break_length", this.breakLength);
        startActivity(intent);
    }

     // launch settings activity
    public void launchSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.putExtra("session_length", this.sessionLength);
        intent.putExtra("break_length", this.breakLength);
        startActivity(intent);
    }
}
