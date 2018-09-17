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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.WE_Booking_History;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class booking_history extends AppCompatActivity {

    AdapterPassbook adapterForDownload;
    RecyclerView myList;
    Dialog Loader;

    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("          ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),drawer.class));
                onBackPressed();
            }
        });


        myList=(RecyclerView)findViewById(R.id.booking_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);


        nodata = findViewById(R.id.nodata);

        WebService();

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
            Intent intent = new Intent(booking_history.this,ErrorScreen.class);
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

            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            WE_Booking_History loginBL = new WE_Booking_History();
            //  String result = loginBL.sendDetail(getApplicationContext(), params[0]);
            String result = loginBL.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {


                    if(Constant.bookingID_history.size()==0)
                    {
                        nodata.setVisibility(View.VISIBLE);
                        myList.setVisibility(View.GONE);
                    }
                    else
                    {
                        adapterForDownload = new AdapterPassbook(getApplicationContext());
                        myList.setAdapter(adapterForDownload);

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                Loader.dismiss();
            }
        }

    }
    public  void  NamePopUp() {

        Loader = new Dialog(booking_history.this, android.R.style.Theme_Translucent_NoTitleBar);
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

            TextView car_name,date,time,location,finalAmount,otp;

            LinearLayout main_layout;


            public ViewHolder(View itemView) {
                super(itemView);

                car_name = itemView.findViewById(R.id.car_name);
                date = itemView.findViewById(R.id.date);
                time = itemView.findViewById(R.id.time);
                location = itemView.findViewById(R.id.location);
                finalAmount = itemView.findViewById(R.id.finalAmount);
                otp = itemView.findViewById(R.id.otp);
                main_layout = itemView.findViewById(R.id.main_layout);
            }
        }
        @Override
        public int getItemCount() {

            int i=0;
            try{

                i= Constant.bookingID_history.size();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_card, parent, false);
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
                holder.car_name.setText(Constant.vehicle_name_history.get(position));
                holder.date.setText(Constant.date_history.get(position));
                holder.time.setText(Constant.slot_history.get(position));
                holder.location.setText(Constant.address_history.get(position));
                holder.finalAmount.setText("₹"+Constant.amount_history.get(position));
                holder.otp.setText(Constant.passcode_history.get(position));

                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(booking_history.this,Booking_Details.class);
                        intent.putExtra("position",String.valueOf(position));
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
