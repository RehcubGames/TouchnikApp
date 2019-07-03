package de.rehcub.touchnik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        Button circleButton = (Button)findViewById(R.id.circleButton);
        Button squareButton = (Button)findViewById(R.id.squareButton);
        Button labyrinthButton = (Button)findViewById(R.id.labyrinthButton);

        final Intent intent1 = new Intent(this, HapticView.class);
        intent1.putExtra("background", "kreis");
        circleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(intent1);
            }
        });

        final Intent intent2 = new Intent(this, HapticView.class);
        intent2.putExtra("background", "quadrat");
        squareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(intent2);
            }
        });

        final Intent intent3 = new Intent(this, HapticView.class);
        intent3.putExtra("background", "labyrinth");
        labyrinthButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(intent3);
            }
        });
    }
}
