package co.project.cosy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class WelcomeScreen extends AppCompatActivity {

    PrefManager preferenceManager;
    LinearLayout Layout_bars;
    TextView[] bottomBars;
    int[] screens;
    Button Next;
    RelativeLayout layoutskip;
    ViewPager vp;
    MyViewPagerAdapter myvpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        vp = (ViewPager) findViewById(R.id.view_pager);
        Layout_bars = (LinearLayout) findViewById(R.id.layoutBars);

//        layoutskip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent inn=new Intent(WelcomeScreen.this,signin.class);
//                startActivity(inn);
//
//            }
//        });
        Next = (Button) findViewById(R.id.next);
        myvpAdapter = new MyViewPagerAdapter();
        preferenceManager = new PrefManager(this);
        vp.addOnPageChangeListener(viewPagerPageChangeListener);
        if (!preferenceManager.FirstLaunch()) {
            launchMain();
            finish();
        }



        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(WelcomeScreen.this,signin.class);
                startActivity(in);
            }
        });


        screens = new int[]{
                R.layout.intro_screen1,
                R.layout.intro_screen2,
                R.layout.intro_screen3,
                R.layout.intro_screen4,
                R.layout.intro_screen5,
                R.layout.intro_screen6
        };

        vp.setAdapter(myvpAdapter);
        ColoredBars(0);
    }

    public void next(View v) {
        int i = getItem(+1);
        if (i < screens.length) {
            vp.setCurrentItem(i);
        } else {
            launchMain();
        }
    }

    public void skip(View view) {
        launchMain();
    }

    private void ColoredBars(int thisScreen) {
        int[] colorsInactive = getResources().getIntArray(R.array.dot_on_page_not_active);
        int[] colorsActive = getResources().getIntArray(R.array.dot_on_page_active);
        bottomBars = new TextView[screens.length];

        Layout_bars.removeAllViews();
        for (int i = 0; i < bottomBars.length; i++) {
            bottomBars[i] = new TextView(this);
            bottomBars[i].setTextSize(100);
            bottomBars[i].setText(Html.fromHtml("¯"));
            Layout_bars.addView(bottomBars[i]);
            bottomBars[i].setTextColor(colorsInactive[thisScreen]);
        }
        if (bottomBars.length > 0)
            bottomBars[thisScreen].setTextColor(colorsActive[thisScreen]);
    }

    private int getItem(int i) {
        return vp.getCurrentItem() + i;
    }

    private void launchMain() {
        preferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeScreen.this, signin.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageSelected(int position) {
            ColoredBars(position);
            if (position == screens.length - 1) {
                Next.setText("start");
                Next.setBackground(getResources().getDrawable(R.drawable.button_back_red));
                Next.setVisibility(View.VISIBLE);


            } else {
                Next.setText(getString(R.string.next));
                Next.setBackgroundColor(Color.parseColor("#00000000"));
                Next.setVisibility(View.INVISIBLE);
            }
        }

       @Override
        public void onPageScrolled(int arg0, float arg1, int arg5) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(screens[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return screens.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        @Override
        public boolean isViewFromObject(View v, Object object) {
            return v == object;
        }
    }
}
