package tgrice.utdallas.edu.ninmu;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String minuteOptions[] = {"1", "5", "10", "15", "20"};
    int sessionMinutes;
    int breakMinutes;
    Spinner sessionSpinner;
    Spinner breakSpinner;
    Button saveButton;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //bind spinners to adapters
        this.context = getApplicationContext();
        this.sessionSpinner = (Spinner)findViewById(R.id.sessionLengthSpinner);
        this.breakSpinner = (Spinner)findViewById(R.id.breakLengthSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context,
                android.R.layout.simple_spinner_item, minuteOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        sessionSpinner.setAdapter(adapter);
        sessionSpinner.setOnItemSelectedListener(this);
        breakSpinner.setAdapter(adapter);
        breakSpinner.setOnItemSelectedListener(this);

        //bind button event listener
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save
                save();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        this.sessionMinutes = intent.getIntExtra("session_length", 0);
        this.breakMinutes = intent.getIntExtra("break_length", 0);
    }

    // need to set variable upon selection
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Integer intValueOfSelection = Integer.valueOf(parent.getItemAtPosition(position).toString());
        if (parent.getId() == R.id.sessionLengthSpinner) {
            // set session length
            this.sessionMinutes = intValueOfSelection;
        } else {
            // set break length
            this.breakMinutes = intValueOfSelection;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // nothing needs to happen
    }

    // save settings changes
    public void save() {
        NinmuDB db = new NinmuDB(this);
        SQLiteDatabase qdb = db.getWritableDatabase();
        String sessionLengthVar = "'" + getString(R.string.session_length_var) + "'";
        String breakLengthVar = "'" + getString(R.string.break_length_var) + "'";
        Cursor sessionLengthCursor = qdb.rawQuery("SELECT * FROM settings where setting_name=" +
                sessionLengthVar, null);
        Cursor breakLengthCursor = qdb.rawQuery("SELECT * FROM settings where setting_name=" +
                breakLengthVar, null);
        // get ids
        int sessionLengthDBId = 0;
        int breakLengthDBId = 0;
        if (sessionLengthCursor.moveToFirst()) { // move to first (and only) item
            sessionLengthDBId = sessionLengthCursor.getInt(0);
        }
        if (breakLengthCursor.moveToFirst()) { // move to first (and only) item
            breakLengthDBId = breakLengthCursor.getInt(0);
        }
        // get args and update
        ContentValues sessionTimeArgs = new ContentValues();
        sessionTimeArgs.put("setting_value", this.sessionMinutes);
        ContentValues breakTimeArgs = new ContentValues();
        breakTimeArgs.put("setting_value", this.breakMinutes);
        qdb.update("settings", sessionTimeArgs, "_id=" + sessionLengthDBId, null);
        qdb.update("settings", breakTimeArgs, "_id=" + sessionLengthDBId,  null);
        // close after use to free up resources.
        sessionLengthCursor.close();
        breakLengthCursor.close();
        //done!
    }

}
