package co.project.cosy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;

import co.project.cosy.Constant.Constant;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class PlatinamWash extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    String posstion;
    AdapterPassbook adapterForDownload;
    RecyclerView myList;

    TextView titel,description;

    ImageView carimage;
    TextView cross_icon;
    RelativeLayout videorelativ,silde_rel;
    VideoView videoView;
    SliderLayout mDemoSlider;

    int row_index=-1;
    HashMap<Integer, String> file_maps = new HashMap<Integer, String>();
    int pos;

    Button next;
    String svname = "",seprice="",serviceId="";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platinam_wash);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("          ");
        setSupportActionBar(toolbar);


        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(),drawer.class));
                onBackPressed();

            }
        });

        videoView = findViewById(R.id.video_play);
        mDemoSlider = findViewById(R.id.slider);

        carimage=findViewById(R.id.carimage);
        silde_rel = findViewById(R.id.silde_rel);

        cross_icon = findViewById(R.id.cross_icon);

        videorelativ = findViewById(R.id.videorelativ);

        titel = findViewById(R.id.titel);
        description = findViewById(R.id.description);
        posstion = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.POSSITION); ///getIntent().getExtras().getString("possition","defaultKey");

        myList=(RecyclerView)findViewById(R.id.platinum_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        myList.setLayoutManager(llm);

        titel.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.title));
        description.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.description));

        System.out.println("sasasassas "+Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.POSSITION));

        Constant.media_name_service.clear();
        Constant.media_type_service.clear();
        Constant.service_typeID_PRICE.clear();
        Constant.vehicle_type_PRICE.clear();
        Constant.price_PRICE.clear();
        Constant.video_service.clear();


        String post = Constant.SERVICE_MEDIA.get(Constant.SERVICE_ID.get(Integer.parseInt(posstion)));
        ServiceMedia(post);

        String pric = Constant.SERVICE_PRICE.get(Constant.SERVICE_ID.get(Integer.parseInt(posstion)));
        price(pric);


        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.srvicename,svname);
//                Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.serviceamount,seprice);
//                Intent intent = new Intent(PlatinamWash.this,appointment.class);
//                intent.putExtra("stetus","1");
//                startActivity(intent);

                Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.srvicename,svname);
                Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.serviceamount,seprice);
                Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.serviceid,serviceId);

                 Intent intent = new Intent(PlatinamWash.this,AddOnActivity.class);
                intent.putExtra("stetus","1");
                startActivity(intent);


            }
        });

        cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoView.stopPlayback();

                silde_rel.setVisibility(View.VISIBLE);
                videorelativ.setVisibility(View.GONE);

            }
        });


    }

    public void ServiceMedia(String data)
    {
        System.out.println("data===>>"+data);
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(data);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.media_name_service.add(String.valueOf(jsonObject.get("media_name").toString()));
                Constant.media_type_service.add(String.valueOf(jsonObject.get("media_type").toString()));
                Constant.video_service.add(String.valueOf(jsonObject.get("video").toString()));
            }

            for (int i = 0; i < Constant.media_name_service.size(); i++)
            {
                System.out.println("banner===>" + i + "   " + Constant.media_name_service.get(i));
                file_maps.put(i, Constant.media_name_service.get(i));
            }

            for (Integer name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(PlatinamWash.this);
                // initialize a SliderLayout
                textSliderView
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                mDemoSlider.addSlider(textSliderView);

                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {

                        MediaController mediaController= new MediaController(PlatinamWash.this);
                        mediaController.setAnchorView(videoView);

                        if(Constant.media_type_service.get(pos).toString().equalsIgnoreCase("video"))
                        {

                            silde_rel.setVisibility(View.GONE);
                            videorelativ.setVisibility(View.VISIBLE);
                            Uri vidUri = Uri.parse(Constant.video_service.get(pos));
                            videoView.setMediaController(mediaController);
                            videoView.setVideoURI(vidUri);

                            videoView.requestFocus();
                            videoView.start();

                        }

                        //Toast.makeText(getApplicationContext(),"working "+pos,Toast.LENGTH_LONG).show();

                    }
                });
            }

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(PlatinamWash.this);


            System.out.println("media_name_service "+Constant.media_name_service.toString());

        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }


    public void price(String data)
    {
        System.out.println("data===>>"+data);
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(data);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.service_typeID_PRICE.add(String.valueOf(jsonObject.get("type_name").toString()));
                Constant.vehicle_type_PRICE.add(String.valueOf(jsonObject.get("vehicle_type").toString()));
                Constant.price_PRICE.add(String.valueOf(jsonObject.get("price").toString()));
                Constant.price_ID.add(String.valueOf(jsonObject.get("serviceID").toString()));
            }

            adapterForDownload=new AdapterPassbook(getApplicationContext());
            myList.setAdapter(adapterForDownload);

        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
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

    public class AdapterPassbook extends RecyclerView.Adapter<AdapterPassbook.ViewHolder> {

        Context mContext;

        int stetus = 0;


        public AdapterPassbook(Context context) {
            this.mContext = context;

        }

        // 3
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView car_name,carprice;
            LinearLayout main_layout;


            public ViewHolder(View itemView) {
                super(itemView);

                car_name = itemView.findViewById(R.id.car_name);
                carprice = itemView.findViewById(R.id.carprice);

                main_layout = itemView.findViewById(R.id.main_layout);

            }
        }
        @Override
        public int getItemCount() {

            int i=0;
            try{

                i= Constant.price_PRICE.size();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_service, parent, false);
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

                holder.car_name.setText(Constant.service_typeID_PRICE.get(position));
                holder.carprice.setText("₹"+Constant.price_PRICE.get(position));
                if(row_index==position){
                    holder.main_layout.setBackgroundColor(Color.parseColor("#00000000"));
                    holder.car_name.setTextColor(Color.parseColor("#f31541"));
                    holder.carprice.setTextColor(Color.parseColor("#f31541"));
                }
                else
                {
                    holder.main_layout.setBackgroundColor(Color.parseColor("#00000000"));
                    holder.car_name.setTextColor(Color.parseColor("#d9d9d9"));
                    holder.carprice.setTextColor(Color.parseColor("#d9d9d9"));

                }
                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        svname  = Constant.service_typeID_PRICE.get(position);
                        seprice  = Constant.price_PRICE.get(position);
                        serviceId = Constant.price_ID.get(position);


                        row_index=position;
                        notifyDataSetChanged();

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
