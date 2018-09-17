package co.project.cosy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.WE_AddBooking;
import co.project.cosy.WebService.WE_GetTimeSloat;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class studio_list extends AppCompatActivity implements OnMapReadyCallback {


    AdapterPassbook adapterForDownload;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    RecyclerView myList;

    String stetus;
    Dialog Loader;

    String studioID;


    String studioType;
    JSONObject jsonnn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_list);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

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

        stetus = getIntent().getExtras().getString("stetus","defaultKey");

        SendDateWebServiceCall();

        myList=(RecyclerView)findViewById(R.id.studio_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);


    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


         MarkerOptions options = new MarkerOptions();
         ArrayList<LatLng> latlngs = new ArrayList<>();


//         for(int i =0; i<Constant.latitude.size();i++)
//         {
//             latlngs.add(new LatLng(Double.parseDouble(Constant.latitude.get(i)),Double.parseDouble(Constant.longitude.get(i))));
//
//             createMarker(Double.parseDouble(Constant.latitude.get(i)),Double.parseDouble(Constant.longitude.get(i)));
//
//         }
         //some latitude and logitude value
//
//        for (LatLng point : latlngs) {
//            options.position(point);
//            options.title("Studio Location");
//            mMap.addMarker(options);
//        }


        LatLng newdelhi = new LatLng(28.489638,77.055884);
        mMap.addMarker(new MarkerOptions().position(newdelhi).title("Studio Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.489638, 77.055884), 14.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newdelhi));
    }



    protected Marker createMarker(double latitude, double longitude) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title("Studio List")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));
    }

    public void SendDateWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            //String userID = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID);

            String jsonData = toJSon3(stetus);
            new GET_DATA3().execute(jsonData);

        }
        else {
            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
        }

    }

    public static String toJSon3(String userid) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("type", userid);
            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private class GET_DATA3 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            WE_GetTimeSloat selectArea = new WE_GetTimeSloat();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.studio);
                    mapFragment.getMapAsync(studio_list.this);

                    adapterForDownload=new AdapterPassbook(getApplicationContext());
                    myList.setAdapter(adapterForDownload);

                }
                else
                {


                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                Loader.dismiss();
            }
        }
    }
    public  void  NamePopUp() {

        Loader = new Dialog(studio_list.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();
    }



    public class AdapterPassbook extends RecyclerView.Adapter<AdapterPassbook.ViewHolder> {

        Context mContext;
        String cityName,activityName;


        public AdapterPassbook(Context context) {

            this.mContext = context;
        }

        // 3
        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageblanck,imagered;
            TextView name,address,phone;
            LinearLayout main_layout;

            RatingBar simpleRatingBar;

            public ViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.name);
                address = itemView.findViewById(R.id.address);
                phone = itemView.findViewById(R.id.phone);
                simpleRatingBar = itemView.findViewById(R.id.simpleRatingBar);
                main_layout = itemView.findViewById(R.id.main_layout);
            }
        }
        @Override
        public int getItemCount() {

            int i=0;
            try{

                i= Constant.studioID_list.size();
            }
            catch (Exception e)
            {
                i=0;
                e.printStackTrace();
            }

            return  i;
        }

        @Override
        public AdapterPassbook.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
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
                holder.name.setText(Constant.name_list.get(position));
                holder.address.setText(Constant.address_list.get(position));
                holder.phone.setText(Constant.contact.get(position));
                holder.simpleRatingBar.setRating(Float.parseFloat(Constant.review_list.get(position)));

                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        studioID = Constant.studioID_list.get(position);
                        studioType = Constant.studio_type_list.get(position);

                        SendDateStudioListWebServiceCall();

                    }
                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void SendDateStudioListWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String userID = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID);
            String slotId = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.finalstot);
            String date = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.finaldate);
            String location = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.finaladdress);
            String addressID = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.finaladdressId);
            String finalAmount = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.finalamount);
            String finalVihicalID = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.finalvihicalID);
            String finalModelId = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.finalModelID);
            String jsondata = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.jsondata);

            System.out.println("jsondata "+ Constant.passArray.toString());


            // String niceFormattedJson = JsonWriter.formatJson(jsonString);
            String jsonData = toJSon4(userID,date,slotId,location,studioID,addressID,studioType,finalAmount,finalVihicalID,finalModelId,"cod");
            new GET_DATA4().execute(jsonData);
        }

        else {

            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);

        }
    }

    public static String toJSon4(String userID,String booking_date,String slotID,String location,String studioID,String addressID,
                                 String studio_type,String amount,String vehicleID,String modelID,String payment_mode) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", userID);
            jsonObj.put("booking_date", booking_date);
            jsonObj.put("slotID", slotID);
            jsonObj.put("location", location);
            jsonObj.put("studioID", studioID);
            jsonObj.put("addressID", addressID);
            jsonObj.put("studio_type", studio_type);
            jsonObj.put("amount", amount);
            jsonObj.put("vehicleID", vehicleID);
            jsonObj.put("modelID", modelID);
            jsonObj.put("payment_mode", payment_mode);
            jsonObj.put("services",Constant.passArray.toString());

            return jsonObj.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private class GET_DATA4 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            WE_AddBooking selectArea = new WE_AddBooking();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    Intent intent = new Intent(studio_list.this,thankyou.class);
                    startActivity(intent);

                }
                else
                {


                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                Loader.dismiss();
            }
        }
    }



}
