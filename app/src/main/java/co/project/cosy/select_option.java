package co.project.cosy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class select_option extends AppCompatActivity {
    CircleImageView cv,dorstop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("     ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getApplicationContext(),drawer.class));
                onBackPressed();
            }
        });


        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        cv=(CircleImageView)findViewById(R.id.studio_service);
        dorstop = (CircleImageView)findViewById(R.id.dorstop);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(select_option.this,studio_list.class);
                intent.putExtra("stetus","studio");
                startActivity(intent);
            }
        });

        dorstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(select_option.this,studio_list.class);
                intent.putExtra("stetus","doorstep");
                startActivity(intent);
            }
        });


    }
}
