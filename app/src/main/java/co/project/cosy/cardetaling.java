package co.project.cosy;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class cardetaling extends AppCompatActivity {



    private static ViewPager nViewPager;
    private static int currentPage = 0;
    private int[] sliderImageIda;
    private static final Integer[] image= {R.drawable.car_wash,R.drawable.car_wash,R.drawable.car_wash};


    private ArrayList<Integer> IMAGEArray = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardetaling);
        init();
    }


    private void init() {
        for(int i=0;i<3;i++)
            IMAGEArray.add(image[i]);

        nViewPager = (ViewPager) findViewById(R.id.cars);
        nViewPager.setAdapter(new ImageAdepter2(cardetaling.this,image));
        CircleIndicator indicatorse = (CircleIndicator) findViewById(R.id.indicators);
        indicatorse.setViewPager(nViewPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == image.length) {
                    currentPage=0;
                }
                nViewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }
}
