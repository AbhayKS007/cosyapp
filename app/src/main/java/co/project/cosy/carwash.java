package co.project.cosy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.WE_Service_Type;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class carwash extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static Context context;
    AdapterPassbook adapterForDownload;
    RecyclerView myList;

    String service_type;
    Dialog Loader;

    TextView titel,description;

    String price = "";
    Button next;

    SliderLayout mDemoSlider;

    HashMap<Integer, String> file_maps = new HashMap<Integer, String>();
    VideoView videoView;
    int pos;
    RelativeLayout videorelativ,silde_rel;

    TextView cross_icon;

    TextView titeltop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carwash);


        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("            ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),drawer.class));
                onBackPressed();
            }
        });

        videoView = findViewById(R.id.video_play);
        mDemoSlider = findViewById(R.id.slider);
        silde_rel = findViewById(R.id.silde_rel);
        cross_icon = findViewById(R.id.cross_icon);
        titeltop = findViewById(R.id.titeltop);

        videorelativ = findViewById(R.id.videorelativ);
        service_type = getIntent().getExtras().getString("carwash","defaultKey");

        if(service_type.equalsIgnoreCase("car_wash"))
        {
            titeltop.setText("CAR WASH");

        }
        else if(service_type.equalsIgnoreCase("car_detailing"))
        {
            titeltop.setText("CAR DETALING");
        }
        else if(service_type.equalsIgnoreCase("regular_service"))
        {
            titeltop.setText("REGULAR SERVICE");
        }


        description = findViewById(R.id.description);
        titel = findViewById(R.id.titel);


        myList=(RecyclerView)findViewById(R.id.carwash_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);


        cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoView.stopPlayback();
                silde_rel.setVisibility(View.VISIBLE);
                videorelativ.setVisibility(View.GONE);

            }
        });


        WebService();


    }
    public void WebService()
    {
        if (Configuration.isInternetConnection(getApplicationContext())) {

            Constant.Home_Baner.clear();

            String user_id = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID);
            //here we call toTson methed for convert java object to json object
            String jsonData = toJSon(user_id,service_type);
            //here call webservice signup
            new Home_Function().execute(jsonData);
        }

        else {
            Intent intent = new Intent(carwash.this,ErrorScreen.class);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(), Constant.NO_INTERNET_MSG, Toast.LENGTH_LONG).show();
        }

    }

    public static String toJSon(String uid,String service_type) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", uid);// Set the first name/pair
            jsonObj.put("service_type",service_type);// Set the first name/pair
            return jsonObj.toString();
        } catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        pos = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static Intent newIntent(drawer drawer, String title) {

        Intent intent = new Intent(context, drawer.class);
        intent.putExtra("title", title);
        return intent;
    }


    private class Home_Function extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            WE_Service_Type loginBL = new WE_Service_Type();
            //  String result = loginBL.sendDetail(getApplicationContext(), params[0]);
            String result = loginBL.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    titel.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.title));
                    description.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.description));

                    adapterForDownload=new AdapterPassbook(getApplicationContext());
                    myList.setAdapter(adapterForDownload);


                    for (int i = 0; i < Constant.media_name.size(); i++)
                    {
                        System.out.println("banner===>" + i + "   " + Constant.media_name.get(i));
                        file_maps.put(i, Constant.media_name.get(i));
                    }

                    for (Integer name : file_maps.keySet()) {
                        TextSliderView textSliderView = new TextSliderView(carwash.this);
                        // initialize a SliderLayout
                        textSliderView
                                .image(file_maps.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit);

                        mDemoSlider.addSlider(textSliderView);

                        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {

                                MediaController mediaController= new MediaController(carwash.this);
                                mediaController.setAnchorView(videoView);

                                if(Constant.media_type.get(pos).toString().equalsIgnoreCase("video"))
                                {

                                    silde_rel.setVisibility(View.GONE);
                                    videorelativ.setVisibility(View.VISIBLE);
                                Uri vidUri = Uri.parse(Constant.video.get(pos));
                                videoView.setMediaController(mediaController);
                                videoView.setVideoURI(vidUri);

                                videoView.requestFocus();
                                videoView.start();

                                }

                             //   Toast.makeText(getApplicationContext(),"working "+pos,Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                    mDemoSlider.setDuration(4000);
                    mDemoSlider.addOnPageChangeListener(carwash.this);


                }
                else {

                }
                // startActivity(new Intent(getApplicationContext(),OtpVatificationActivity.class).putExtra("mobile",emailedit.getText().toString()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                Loader.dismiss();
            }
        }

    }
    public  void  NamePopUp() {

        Loader = new Dialog(carwash.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();
    }


    public class AdapterPassbook extends RecyclerView.Adapter<AdapterPassbook.ViewHolder> {

        Context mContext;
        String cityName,activityName;

        int stetus = 0;


        public AdapterPassbook(Context context) {
            this.mContext = context;

        }

        // 3
        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageblanck,imagered;
            TextView name,price;
            LinearLayout main_layout;




            public ViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.name);
                price = itemView.findViewById(R.id.price);

                imageblanck = itemView.findViewById(R.id.imageblanck);
                imagered = itemView.findViewById(R.id.imagered);

                main_layout = itemView.findViewById(R.id.main_layout);


            }
        }
        @Override
        public int getItemCount() {

            int i=0;
            try{

                    i= Constant.service_name.size();
            }
            catch (Exception e)
            {
                i=0;
                e.printStackTrace();
            }

            return  i;
        }

        // 2
        @Override
        public AdapterPassbook.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_detaling_tetra_cost, parent, false);
            return new AdapterPassbook.ViewHolder(view);

        }

        @Override
        public long getItemId(int position) {

            return position;

        }
        @Override
        public int getItemViewType(int position) {

            return position;

        }

        @Override
        public void onBindViewHolder(final AdapterPassbook.ViewHolder holder, final int position) {
            try {
                    holder.name.setText(Constant.service_name.get(position));
                    holder.price.setText("₹"+Constant.min_price.get(position));

                    holder.main_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //System.out.println("possition "+ position);

                            Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.srv_type,holder.name.getText().toString());
                            Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.svID,String.valueOf(Constant.SERVICE_ID.get(position)));
                            Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.POSSITION,String.valueOf(position));
                            Intent intent = new Intent(carwash.this,PlatinamWash.class);
                            //intent.putExtra("possition",String.valueOf(possition));
                            startActivity(intent);

                        }
                    });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
