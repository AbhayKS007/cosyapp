package co.project.cosy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar_loc = (Toolbar) findViewById(R.id.toolbar_nf);
        toolbar_loc.setNavigationIcon(R.drawable.miku_back);
        toolbar_loc.setTitle("          ");
        setSupportActionBar(toolbar_loc);

        toolbar_loc.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });

    }
}
