package com.columcross.warmercolder;

import android.location.Location;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;


public class Game {

    private Location goal;
    private float currentDistance;

    private Button button;
    private TextView goalBox;
    private TextView guessBox;
    private TextView distanceBox;


    public Game(Button getLocationBtn, TextView box1, TextView box2, TextView box3) {
        button = getLocationBtn;
        button.setText("Set your destination");

        goalBox = box1;
        guessBox = box2;
        distanceBox = box3;
    }

    /**
     *
     * @param guess The location of the guess you are making
     * @return Whether the
     */
    public boolean guess(Location guess) {
        if (Objects.isNull(goal)) {
            setGoal(guess);
            return false;
        } else if(Objects.isNull(currentDistance)) {
            setStartingPoint(guess);
            return false;
        } else {
            float previousDistance = currentDistance;
            currentDistance = guess.distanceTo(goal);

            guessBox.setText(guess.getLatitude()+" "+guess.getLongitude());
            distanceBox.setText(String.valueOf(currentDistance));

            if(currentDistance < 2) {
                button.setText("On Fire!");
                button.setBackgroundResource(R.drawable.button_round);
                return true;
            } else {
                if(currentDistance < previousDistance) {
                    button.setText("Warmer");
                    button.setBackgroundResource(R.drawable.warmer_background);
                } else {
                    button.setText("Colder");
                    button.setBackgroundResource(R.drawable.colder_background);
                }
                return false;
            }
        }

    }

    private void setGoal(Location location) {
        goal = location;
        button.setText("Set your starting point");
        goalBox.setText(goal.getLatitude()+" "+goal.getLongitude());
    }

    private void setStartingPoint(Location location) {
        currentDistance = location.distanceTo(goal);
        button.setText("Start Guessing");
    }
}
