// Session Activity
// Handles work/task sessions and
package tgrice.utdallas.edu.ninmu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivitySession extends AppCompatActivity {
    private Button timerButton;
    private TextView timerText; // timer text
    private Handler timerHandler; // handles timer Runnable thread
    private boolean stopped = true; // is the timer paused or not
    private boolean inSession = true; // if false, means it is currently break time
    private long time = 0; //time elapsed
    private int sessionMinutes; // minutes in a session
    private int breakMinutes;

    // bind event listeners to buttons etc.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        this.timerText = (TextView) findViewById(R.id.timerText);
        this.timerButton = (Button) findViewById(R.id.sessionButton);
    }

    // get intent info and bind timerHandler
    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        this.sessionMinutes = intent.getIntExtra("session_length", 0);
        this.breakMinutes = intent.getIntExtra("break_length", 0);
        timerText.setText(String.valueOf(convertSecondsToTimeString(time)));
        this.timerHandler = new Handler();
        timerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                time = 0; // reset time in either case
                if (stopped) { // if stopped
                    stopped = false;
                    timerHandler.postDelayed(timerRunnable, 1000);
                } else { // if not stopped
                    stopped = true;
                    timerHandler.removeCallbacks(timerRunnable);

                }
                Log.v("stop status ", Boolean.toString(stopped));
                toggleTimerText();
            }
        });
    }

    // set timer button text appropriately based on stopped status
    private void toggleTimerText() {
        if (this.stopped) {
            timerButton.setText(R.string.start_timer_text);
        } else {
            timerButton.setText(R.string.stop_timer_text);
        }
    }

    // runnable to handle the repeated incrementing of timer
    // and updating the view timer
    // TODO: add additional visual element to track time aside from timer text
    private Runnable timerRunnable = new Runnable() {
        public void run(){
            time += 1;
            timerText.setText(String.valueOf(convertSecondsToTimeString(time)));
            // TODO: add logic for when time runs out.
            timerHandler.postDelayed(this, 1000);
        }
    };

    // convert time to readable string
    private String convertSecondsToTimeString(long ms) {
        Integer relevantTime;
        if( this.inSession ) {
            relevantTime = this.sessionMinutes;
        } else {
            relevantTime = this.breakMinutes;
        }
        int minutesDenominator = 60;
        Double secondsRemainder = Math.floor(ms % (minutesDenominator));
        Integer secondsInt = 59 - secondsRemainder.intValue();
        String seconds = secondsInt.toString();
        Double minutesDouble = Math.floor(ms / minutesDenominator);
        Integer minutesInt = (relevantTime - 1) - minutesDouble.intValue();
        String minutes = minutesInt.toString();
        if (secondsInt < 10) {
            seconds = "0" + seconds;
        }
        if (minutesInt < 10) {
            minutes = "0" + minutes;
        }
        return minutes + ":" + seconds;
    };

    private void timerRunsOutEvent(){

    }

}
