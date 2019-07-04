package de.rehcub.touchnik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StartMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        final ListView hapticViewList = findViewById(R.id.hapticViewList);

        final String[] hapticNameArray =
                {
                  "kreis", "quadrat", "diamond", "kreis_no_fill", "quadrat_no_fill", "diamond_no_fill", "labyrinth", "relief", "relief_big"
                };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1 , hapticNameArray);


        hapticViewList.setAdapter(arrayAdapter);

        final Intent intent1 = new Intent(this, HapticView.class);
        hapticViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                intent1.putExtra("background",hapticNameArray[position]);
                startActivity(intent1);
            }
        });
    }
}
