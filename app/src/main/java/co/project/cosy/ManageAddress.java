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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.Delet_AddressWS;
import co.project.cosy.WebService.WE_Address_List;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ManageAddress extends AppCompatActivity {

    AdapterPassbook adapterForDownload;
    RecyclerView myList;
    Dialog Loader;

    String vehicleUserId;

    Button add_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("       ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //startActivity(new Intent(getApplicationContext(),appointment.class));

            }
        });

        myList=(RecyclerView)findViewById(R.id.address_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);

        WebService();

        add_address = findViewById(R.id.add_address);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ManageAddress.this,address.class);
                startActivity(intent);

            }
        });

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
            Intent intent = new Intent(ManageAddress.this,ErrorScreen.class);
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
            WE_Address_List loginBL = new WE_Address_List();
            //  String result = loginBL.sendDetail(getApplicationContext(), params[0]);
            String result = loginBL.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    adapterForDownload=new AdapterPassbook(getApplicationContext());
                    myList.setAdapter(adapterForDownload);
                }
                else {

                }

                // startActivity(new Intent(getApplicationContext(),OtpVatificationActivity.class).putExtra("mobile",emailedit.getText().toString()));

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

        Loader = new Dialog(ManageAddress.this, android.R.style.Theme_Translucent_NoTitleBar);
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

            ImageView imagemain;
            TextView address_name,address,flat_number;

            TextView delet_btn;

            LinearLayout main_layout;


            public ViewHolder(View itemView) {
                super(itemView);

                address_name = itemView.findViewById(R.id.address_name);
                address = itemView.findViewById(R.id.address);
                flat_number = itemView.findViewById(R.id.flat_number);

                imagemain = itemView.findViewById(R.id.imagemain);

                delet_btn = itemView.findViewById(R.id.delet_btn);

                main_layout = itemView.findViewById(R.id.main_layout);

            }
        }
        @Override
        public int getItemCount() {

            int i=0;
            try{

                i= Constant.addressID.size();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_address, parent, false);
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
                holder.address_name.setText(Constant.address_type.get(position));
                holder.flat_number.setText(Constant.location.get(position));
                holder.address.setText( Constant.flat_no.get(position));


                if(Constant.address_type.get(position).toString().equalsIgnoreCase("resident"))
                {
                    holder.imagemain.setBackgroundResource(R.drawable.home_icon);
                }
                else
                {
                    holder.imagemain.setBackgroundResource(R.drawable.bussiness_icon);

                }

                holder.delet_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vehicleUserId =  Constant.addressID.get(position);
                        DeletVehecalWebServiceCall();
                    }
                });

                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.address,holder.flat_number.getText().toString());
                        Configuration.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.finaladdressId,Constant.addressID.get(position));

                        Intent intent = new Intent(ManageAddress.this,appointment.class);
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


    public void DeletVehecalWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String jsonData = toJSon2(vehicleUserId);
            new GET_DATA().execute(jsonData);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
        }

    }

    public static String toJSon2(String userID) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("addressID", userID);
            return jsonObj.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private class GET_DATA extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            Delet_AddressWS selectArea = new Delet_AddressWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    Intent intent = new Intent(ManageAddress.this,ManageAddress.class);
                    startActivity(intent);
                }
                else
                {

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Loader.dismiss();
            }
        }
    }



}
