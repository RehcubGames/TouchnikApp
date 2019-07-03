package de.rehcub.touchnik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class OptionsActivity extends AppCompatActivity
{
    Switch surroundDetectionSwitch;
    Switch fourDirectionsSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        surroundDetectionSwitch = findViewById(R.id.surroundDetactionSwitch);
        surroundDetectionSwitch.setChecked(Settings.SURROUND_DETECTION);
        surroundDetectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.SURROUND_DETECTION = isChecked;
            }
        });
        fourDirectionsSwitch = findViewById(R.id.fourDirectionsSwitch);
        fourDirectionsSwitch.setChecked(Settings.FOUR_DIRECTIONS);
        fourDirectionsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.FOUR_DIRECTIONS = isChecked;
            }
        });
    }
}
