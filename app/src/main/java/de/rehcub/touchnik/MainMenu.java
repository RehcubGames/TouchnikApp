package de.rehcub.touchnik;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainMenu extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        try {init();} catch (Exception e) {}
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() throws IOException
    {
        Button startButton = (Button)findViewById(R.id.startButton);
        Button optionsButton = (Button)findViewById(R.id.optionsButton);

        final Intent intent1 = new Intent(this, StartMenu.class);
        startButton.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            startActivity(intent1);
        }
    });

        final Intent intent2 = new Intent(this, OptionsActivity.class);
        optionsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(intent2);
            }
        });

    }
}
