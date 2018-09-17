package co.project.cosy;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

import co.project.CupenCodeActivity;
import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.LogoutWS;
import co.project.cosy.WebService.WE_HomePage;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    SliderLayout mDemoSlider;
    ProgressDialog progressDialog;
    private TextView mTextMessage;

    TextView booking_tv;
    private Menu nav_menu;

    RelativeLayout carwashh,cardenting,regularservice,denting,caraccess,carrep;

    LinearLayout booking_btn;

    HashMap<Integer, String> file_maps = new HashMap<Integer, String>();
    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.offer:
                    Intent faqs=new Intent(drawer.this,CupenCodeActivity.class);
                    startActivity(faqs);
                    return true;

                case R.id.faq:
//                    Intent faqs=new Intent(drawer.this,thankyou.class);
//                    startActivity(faqs);
                    return true;
                case R.id.navigation_history:
                    Intent his=new Intent(drawer.this,booking_history.class);
                    startActivity(his);
                    return true;

                case R.id.navigation_Outlets:
                    Intent out=new Intent(drawer.this,StudioListHome.class);
                    startActivity(out);
                    return true;

            }
            return false;
        }
    };



    private void init() {

        mDemoSlider = findViewById(R.id.slider);
        progressDialog = new ProgressDialog(drawer.this);
        mTextMessage = (TextView) findViewById(R.id.message);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        setTitle(getIntent().getStringExtra("title"));

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        WebService();

        RelativeLayout cv= findViewById(R.id.cardenting);

        booking_tv = findViewById(R.id.booking_tv);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ckd=new Intent(drawer.this,cardetaling.class);
                startActivity(ckd);

            }
        });

        carwashh = findViewById(R.id.carwash);
        cardenting = findViewById(R.id.cardenting);
        regularservice = findViewById(R.id.regularservice);
        denting = findViewById(R.id.denting);
        caraccess = findViewById(R.id.caraccess);
        carrep = findViewById(R.id.carrep);

        booking_btn = findViewById(R.id.booking_btn);

        booking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(drawer.this,UpcomingBookingList.class);
                startActivity(intent);

            }
        });


        carwashh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(drawer.this,carwash.class);
                intent.putExtra("carwash","car_wash");
                startActivity(intent);

            }
        });


        cardenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(drawer.this,carwash.class);
                intent.putExtra("carwash","car_detailing");
                startActivity(intent);

            }
        });


        regularservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(drawer.this,carwash.class);
                intent.putExtra("carwash","regular_service");
                startActivity(intent);

            }
        });


        denting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(drawer.this,carwash.class);
                intent.putExtra("carwash","dent_paint");
                startActivity(intent);

            }
        });


        caraccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(drawer.this,carwash.class);
                intent.putExtra("carwash","car_accessory");
                startActivity(intent);

            }
        });

        carrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(drawer.this,carwash.class);
                intent.putExtra("carwash","car_repair");
                startActivity(intent);

            }
        }
        );

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        drawer.BottomNavigationViewHelper.disableShiftMode(navigation);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nav_menu = navigationView.getMenu();
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

        ImageView imageView = header.findViewById(R.id.imageVieww);
        TextView header_name = header.findViewById(R.id.header_name);
        TextView headeremail = header.findViewById(R.id.headeremail);

        header_name.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_NAME));
        headeremail.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_EMAIL));

        Glide.with(this)
                .load(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_IMAGE))
                .placeholder(R.drawable.man)
                .crossFade()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(drawer.this,ProfileActivity.class);
                startActivity(intent);

            }
        });

    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            try{
                if (doubleBackToExitPressedOnce) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press Again To Close App", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent ho=new Intent(drawer.this,drawer.class);
            startActivity(ho);
            // Handle the camera action
        } else if (id == R.id.service) {
//            Intent se=new Intent(drawer.this,appointment.class);
//            startActivity(se);


        } else if (id == R.id.location) {

            Intent ho=new Intent(drawer.this,ManageAddress.class);
            startActivity(ho);

        } else if (id == R.id.vehicle) {
            Intent ve=new Intent(drawer.this,ManageVihical.class);
            startActivity(ve);

        } else if (id == R.id.history) {
            Intent ve=new Intent(drawer.this,booking_history.class);
            startActivity(ve);

        } else if (id == R.id.rate) {

            reviewOnApp();

        } else if (id == R.id.notification) {
            Intent ve=new Intent(drawer.this,Notification.class);
            startActivity(ve);

        } else if (id == R.id.settings){

        } else if (id == R.id.logout) {

            LogOutFunction();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void WebService()
    {
        if (Configuration.isInternetConnection(getApplicationContext())) {

            Constant.Home_Baner.clear();

            String user_id = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID);
            //here we call toTson methed for convert java object to json object
            String jsonData = toJSon(user_id);
            //here call webservice signup
            new Home_Function().execute(jsonData);

        }

        else {
            Intent intent = new Intent(drawer.this,ErrorScreen.class);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(), Constant.NO_INTERNET_MSG, Toast.LENGTH_LONG).show();
        }

    }

    public static String toJSon(String uid) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", uid);// Set the first name/pair
            return jsonObj.toString();
        } catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



    private class Home_Function extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            WE_HomePage loginBL = new WE_HomePage();
            //  String result = loginBL.sendDetail(getApplicationContext(), params[0]);
            String result = loginBL.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    booking_tv.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.bookings)+" upcoming booking");

                    for (int i = 0; i < Constant.Home_Baner.size(); i++)
                    {
                        System.out.println("banner===>" + i + "   " + Constant.Home_Baner.get(i));
                        file_maps.put(i, Constant.Home_Baner.get(i));
                    }

                    for (Integer name : file_maps.keySet()) {
                        TextSliderView textSliderView = new TextSliderView(drawer.this);
                        // initialize a SliderLayout
                        textSliderView
                                .image(file_maps.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(drawer.this);

                        mDemoSlider.addSlider(textSliderView);

                    }

                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider.setDuration(4000);
                    mDemoSlider.addOnPageChangeListener(drawer.this);
                }
                else {

                }
                // startActivity(new Intent(getApplicationContext(),OtpVatificationActivity.class).putExtra("mobile",emailedit.getText().toString()));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressDialog.dismiss();
            }
        }

    }

    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void LogOutFunction()
    {

        if (Configuration.isInternetConnection(getApplicationContext())) {
            //here we call toTson methed for convert java object to json object
            String jsonData = toJSon2(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID));
            new LoginFunction().execute(jsonData);
        }
        else {
            Intent intent = new Intent(drawer.this,ErrorScreen.class);
            startActivity(intent);
        }

    }


    public static String toJSon2(String userid) {

        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", userid); // Set the first name/pair
            return jsonObj.toString();
        }

        catch (JSONException ex) {

            ex.printStackTrace();

        }
        return null;
    }


    private class LoginFunction extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            LogoutWS loginBL=new LogoutWS();
            String result = loginBL.sendDetail(getApplicationContext(),params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(s.equalsIgnoreCase("success"))
                {

                    Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.USER_ID,null);
                    //Toast.makeText(getApplicationContext(),"Support Submit",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),signin.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong ",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {

                progressDialog.dismiss();

            }
        }
    }



}


