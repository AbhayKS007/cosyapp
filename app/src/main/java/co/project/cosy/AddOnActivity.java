package co.project.cosy;

import android.content.Context;
import android.content.Intent;
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
import android.widget.VideoView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

import co.project.cosy.Constant.Constant;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class AddOnActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    String posstion;
    SliderLayout mDemoSlider;
    VideoView videoView;
    HashMap<Integer, String> file_maps = new HashMap<Integer, String>();
    int pos;
    TextView cross_icon;
    RelativeLayout videorelativ,silde_rel;
    RecyclerView myList;

    AdapterPassbook adapterForDownload;

    int row_index;
    Button next;
    ArrayList<String> testing =new ArrayList<String>();
    ArrayList<String> price =new ArrayList<String>();
    ArrayList<String> type =new ArrayList<String>();


    TextView titel,description;
    String val = "",val2;
    StringBuilder stringBuilder = new StringBuilder();

    int poss;
    String vall;

    int stetus = 0;

   // JSONArray passArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_on);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("          ");
        setSupportActionBar(toolbar);

        StringBuilder stringBuilder;

        testing.clear();
        price.clear();
        type.clear();

        Constant.passArray.clear();

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

        silde_rel = findViewById(R.id.silde_rel);

        cross_icon = findViewById(R.id.cross_icon);

        titel = findViewById(R.id.titel);
        description = findViewById(R.id.description);
        posstion = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.POSSITION); ///getIntent().getExtras().getString("possition","defaultKey");


        myList=(RecyclerView)findViewById(R.id.add_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);


        Constant.ADDONID.clear();
        Constant.ADDONPRICE.clear();
        Constant.ADDONNAME.clear();


        String post = Constant.SERVICE_MEDIA.get(Constant.SERVICE_ID.get(Integer.parseInt(posstion)));
        ServiceMedia(post);
        String pric = Constant.SERVICE_ADDON.get(Constant.SERVICE_ID.get(Integer.parseInt(posstion)));
        price(pric);

        titel = findViewById(R.id.titel);
        description = findViewById(R.id.description);

        titel.setText(Constant.addon_title.get(Integer.parseInt(posstion)));
        description.setText(Constant.addon_description.get(Integer.parseInt(posstion)));




        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addon = "";
                if(testing.size()==0)
                {

                }
                else
                {
                    testing.add(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.serviceid));
                    price.add(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.serviceamount));
                    type.add("service");

                   // Constant.passArray = new JSONArray();
                    for (int i = 0;i<testing.size();i++)
                    {
                        JSONObject jObjP = new JSONObject();
                        try {
                            jObjP.put("serviceID", testing.get(i));
                            jObjP.put("price", price.get(i));
                            jObjP.put("service_type", type.get(i));
                            Constant.passArray.add(jObjP);

                            System.out.println("jsontesting inside "+Constant.passArray.toString());

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.jsondata,passArray.toString());
                    System.out.println("jsontesting "+Constant.passArray.toJSONString());

                }

                Intent intent = new Intent(AddOnActivity.this,appointment.class);
                intent.putExtra("stetus","1");
                startActivity(intent);

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
                TextSliderView textSliderView = new TextSliderView(AddOnActivity.this);
                // initialize a SliderLayout
                textSliderView
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                mDemoSlider.addSlider(textSliderView);

                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {

                        MediaController mediaController= new MediaController(AddOnActivity.this);
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
            mDemoSlider.addOnPageChangeListener(AddOnActivity.this);


            System.out.println("media_name_service "+Constant.media_name_service.toString());

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

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
                Constant.ADDONID.add(String.valueOf(jsonObject.get("addonID").toString()));
                Constant.ADDONPRICE.add(String.valueOf(jsonObject.get("price").toString()));
                Constant.ADDONNAME.add(String.valueOf(jsonObject.get("addon_name").toString()));
            }

            adapterForDownload=new AdapterPassbook(getApplicationContext());
            myList.setAdapter(adapterForDownload);

        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }

    public class AdapterPassbook extends RecyclerView.Adapter<AdapterPassbook.ViewHolder> {

        Context mContext;

        public AdapterPassbook(Context context) {

            this.mContext = context;

        }

        // 3
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name,price;

            ImageView imageblanck,imagered;

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

                i= Constant.ADDONID.size();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addoncard, parent, false);
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

                holder.name.setText(Constant.ADDONNAME.get(position));
                holder.price.setText("₹"+Constant.ADDONPRICE.get(position));

//                if(row_index==position){
//                    holder.imageblanck.setVisibility(View.VISIBLE);
//                    holder.imagered.setVisibility(View.GONE);
//
//                    testing.add(String.valueOf(position));
//
//                }
//                else
//                {
//                    //holder.main_layout.setBackgroundColor(Color.parseColor("#5e5e5e"));
//                }


                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        holder.imageblanck.setVisibility(View.GONE);
//                        holder.imagered.setVisibility(View.VISIBLE);

                           if(testing.contains(Constant.ADDONID.get(position)))
                            {
                                holder.imageblanck.setVisibility(View.VISIBLE);
                                holder.imagered.setVisibility(View.GONE);

                                vall = Constant.ADDONID.get(position);
                                poss =  testing.indexOf(vall);

                                testing.remove(poss);
                                price.remove(poss);
                                type.remove(poss);

                                System.out.println("testing remove "+testing.toString());
                            }
                            else
                           {
                               testing.add(Constant.ADDONID.get(position));
                               price.add(Constant.ADDONPRICE.get(position));
                               type.add("addon");
                               holder.imageblanck.setVisibility(View.GONE);
                               holder.imagered.setVisibility(View.VISIBLE);

                               System.out.println("testing add "+testing.toString());
                           }

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

    @Override
    protected void onResume() {
        super.onResume();

//        Intent intent = new Intent(AddOnActivity.this,AddOnActivity.class);
//        startActivity(intent);

//        testing.clear();
//        price.clear();
//        type.clear();
    }
}
