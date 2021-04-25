package com.columcross.warmercolder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity implements LocationListener {

    Button getLocationBtn;
    Game game;

    LocationManager locationManager;
    int PERMISSION_ID = 44;

    TextView box1;
    TextView box2;
    TextView box3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        box1 = (TextView) findViewById(R.id.goalText);
        box2 = (TextView) findViewById(R.id.currentGuessText);
        box3 = (TextView) findViewById(R.id.distanceText);

        box1.setVisibility(View.INVISIBLE);
        box2.setVisibility(View.INVISIBLE);
        box3.setVisibility(View.INVISIBLE);

        getLocationBtn = (Button) findViewById(R.id.button);
        game = new Game(getLocationBtn, box1, box2, box3);

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationBtn.setText("Checking Guess...");
                getLocation();
            }
        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
            requestPermissions();
            game = new Game(getLocationBtn, (TextView) findViewById(R.id.goalText), (TextView) findViewById(R.id.currentGuessText), (TextView) findViewById(R.id.distanceText));
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            boolean gameOver = game.guess(location);
            locationManager.removeUpdates(this);
            if (gameOver) {
                endGame();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    private void endGame() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("You Win!");
        alertDialog.setMessage("Good job, you found the initial location. You win!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        game = new Game(getLocationBtn, (TextView) findViewById(R.id.goalText), (TextView) findViewById(R.id.currentGuessText), (TextView) findViewById(R.id.distanceText));
    }

    public void showInstructions(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Instructions");
        alertDialog.setMessage("Requires 2 players. Player 1 will go to a position and press the button. Player 1 will then hand the phone to Player 2. Player 2 will try to guess the first location by clicking the button. If Player 2 is closer than the previous guess, the button will say 'warmer'. If Player 2 is farther away than the previous guess, the button will say 'colder'. Player 2 wins when a guess is made within 6 feet of the location Player 1 chose.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void toggleInfo(View view) {
        if(box1.getVisibility() == View.VISIBLE){
            box1.setVisibility(View.INVISIBLE);
            box2.setVisibility(View.INVISIBLE);
            box3.setVisibility(View.INVISIBLE);
        }
        else {
            box1.setVisibility(View.VISIBLE);
            box2.setVisibility(View.VISIBLE);
            box3.setVisibility(View.VISIBLE);
        }
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

}