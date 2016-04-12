package tgrice.utdallas.edu.ninmu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    // bind event listeners in onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // bind listeners to buttons
        Button sessionButton = (Button) findViewById(R.id.sessionButton);
        sessionButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick (View v){
            // go to session activity
            launchSession(v);
        }
        });
    }

    // launch session activity
    public void launchSession(View view) {
        Intent intent = new Intent(MainActivity.this, ActivitySession.class);
        startActivity(intent);
    }
}
