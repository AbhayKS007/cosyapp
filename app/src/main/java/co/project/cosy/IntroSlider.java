package co.project.cosy;

import android.content.Intent;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.dev.sacot41.scviewpager.DotsView;
import com.dev.sacot41.scviewpager.SCPositionAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimationUtil;
import com.dev.sacot41.scviewpager.SCViewPager;
import com.dev.sacot41.scviewpager.SCViewPagerAdapter;
import com.facebook.login.Login;

public class IntroSlider extends AppCompatActivity {
    private static final int NUM_PAGES = 6;
    private SCViewPager mViewPager;
    private SCViewPagerAdapter mPageAdapter;
    private DotsView mDotsView;
    private PreferenceManager preferenceManager;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);


        Button start=findViewById(R.id.textview_main_linkedin_link);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(IntroSlider.this, signin.class);
                startActivity(in);
            }
        });




        mViewPager = findViewById(R.id.viewpager_main_activity);
        mDotsView = findViewById(R.id.dotsview_main);
        mDotsView.setDotRessource(R.drawable.sel_dot, R.drawable.non_sel_dot);
        mDotsView.setNumberOfPage(NUM_PAGES);

        mPageAdapter = new SCViewPagerAdapter(getSupportFragmentManager());
        mPageAdapter.setNumberOfPage(NUM_PAGES);
        mPageAdapter.setFragmentBackgroundColor(R.color.theme_100);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setBackgroundResource(R.drawable.wel_final);






        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mDotsView.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final Point size = SCViewAnimationUtil.getDisplaySize(this);

        View nameTag = findViewById(R.id.imageview_main_activity_name_tag);
        SCViewAnimation nameTagAnimation = new SCViewAnimation(nameTag);
        nameTagAnimation.addPageAnimation(new SCPositionAnimation(this, 0,0,0));
        mViewPager.addAnimation(nameTagAnimation);

        View currentlyWork = findViewById(R.id.imageview_main_activity_currently_work);
        SCViewAnimation currentlyWorkAnimation = new SCViewAnimation(currentlyWork);
        currentlyWorkAnimation.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        mViewPager.addAnimation(currentlyWorkAnimation);

        View atSkex = findViewById(R.id.imageview_main_activity_at_skex);
        SCViewAnimationUtil.prepareViewToGetSize(atSkex);
        SCViewAnimation atSkexAnimation = new SCViewAnimation(atSkex);
        atSkexAnimation.addPageAnimation(new SCPositionAnimation(getApplicationContext(), 0, 0,-size.y));
        atSkexAnimation.addPageAnimation(new SCPositionAnimation(getApplicationContext(), 1, -size.x, 0));
        mViewPager.addAnimation(atSkexAnimation);

        View mobileView = findViewById(R.id.imageview_main_activity_mobile);
        SCViewAnimation mobileAnimation = new SCViewAnimation(mobileView);
        mobileAnimation.startToPosition((int)(size.x*1.5), null);
        mobileAnimation.addPageAnimation(new SCPositionAnimation(this, 0, -(int)(size.x*1.5), 0));
        mobileAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -(int)(size.x*1.5), 0));
        mViewPager.addAnimation(mobileAnimation);


        View djangoView = findViewById(R.id.imageview_main_activity_django_python);
        SCViewAnimation djangoAnimation = new SCViewAnimation(djangoView);
        djangoAnimation.startToPosition(null,-size.y);
        djangoAnimation.addPageAnimation(new SCPositionAnimation(this, 0, 0, size.y));
        djangoAnimation.addPageAnimation(new SCPositionAnimation(this, 1, 0, size.y));
        mViewPager.addAnimation(djangoAnimation);




        View diplomeView = findViewById(R.id.imageview_main_activity_diploma);
        SCViewAnimation diplomeAnimation = new SCViewAnimation(diplomeView);
        diplomeAnimation.startToPosition((size.x *2), null);
        diplomeAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x*2,0));
        diplomeAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x*2 ,0));
        mViewPager.addAnimation(diplomeAnimation);

        View whyView = findViewById(R.id.imageview_main_activity_why);
        SCViewAnimation whyAnimation = new SCViewAnimation(whyView);
        whyAnimation.startToPosition(size.x, null);
        whyAnimation.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        whyAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        mViewPager.addAnimation(whyAnimation);





        View raspberryView = findViewById(R.id.imageview_main_raspberry_pi);
        SCViewAnimation raspberryAnimation = new SCViewAnimation(raspberryView);
        raspberryAnimation.startToPosition(null, -size.y);
        raspberryAnimation.addPageAnimation(new SCPositionAnimation(this, 2, 0, size.y));
        raspberryAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, size.y));
        mViewPager.addAnimation(raspberryAnimation);

        View connectedDeviceView = findViewById(R.id.imageview_main_connected_device);
        SCViewAnimation connectedDeviceAnimation = new SCViewAnimation(connectedDeviceView);
        connectedDeviceAnimation.startToPosition((int)(size.x *1.5), null);
        connectedDeviceAnimation.addPageAnimation(new SCPositionAnimation(this, 2, -(int) (size.x * 1.5), 0));
        connectedDeviceAnimation.addPageAnimation(new SCPositionAnimation(this, 3,  - size.x, 0));
        mViewPager.addAnimation(connectedDeviceAnimation);

        View checkOutView = findViewById(R.id.imageview_main_check_out);
        SCViewAnimation checkOutAnimation = new SCViewAnimation(checkOutView);
        checkOutAnimation.startToPosition(size.x, null);
        checkOutAnimation.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        checkOutAnimation.addPageAnimation(new SCPositionAnimation(this, 4, -size.x, 0));
        mViewPager.addAnimation(checkOutAnimation);

        View linkedinView = findViewById(R.id.textview_main_linkedin_link);
        SCViewAnimation linkedinAnimation = new SCViewAnimation(linkedinView);
        linkedinAnimation.startToPosition(size.x, null);
        linkedinAnimation.addPageAnimation(new SCPositionAnimation(this, 4, -size.x, 0));
        mViewPager.addAnimation(linkedinAnimation);

        View githubView = findViewById(R.id.textview_main_github_link);
        SCViewAnimation githubAnimation = new SCViewAnimation(githubView);
        githubAnimation.startToPosition(((int)(size.x*1.5)), null);
        githubAnimation.addPageAnimation(new SCPositionAnimation(this, 3, (int) (-size.x*1.5), 0));
        githubAnimation.addPageAnimation(new SCPositionAnimation(this, 4, -size.x, 0));
        mViewPager.addAnimation(githubAnimation);

        View checkView = findViewById(R.id.imageview_main_check);
        SCViewAnimation checkAnimation = new SCViewAnimation(checkView);
        checkAnimation.startToPosition(null, -size.y);
        checkAnimation.addPageAnimation(new SCPositionAnimation(this, 4, 0, size.y));
        mViewPager.addAnimation(checkAnimation);

        View githubsixView = findViewById(R.id.textview_main_github);
        SCViewAnimation githubsixAnimation = new SCViewAnimation(githubsixView);
        githubsixAnimation.startToPosition(null, ((int)(size.y*1.5)));
        githubsixAnimation.addPageAnimation(new SCPositionAnimation(this, 4,0, ((int)(-size.y*1.5))));
        mViewPager.addAnimation(githubsixAnimation);
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroSlider.this, signin.class));
        finish();
    }


}
