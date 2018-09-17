package co.project.cosy;

import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.Add_VehicleWS;
import co.project.cosy.WebService.VihicaleModelWS;
import co.project.cosy.WebService.model_makeWS;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class add_vehicle extends AppCompatActivity {

    TextView entermodel,entermake,enteryear,entercolor;
    ArrayList MainArray = new ArrayList();
    private Dialog Location;

    Dialog Loader;

    String VihicleID,MakeID;

    Button save;
    LinearLayout one,two,three,four;
    String carSteus = "1";
    ImageView sedan,hatchback, suv,exotic;


    TextView sedantext,hatchbacktext,suvtext,exotictext;

    int stetus = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);


        sedan=findViewById(R.id.sedan);
        hatchback=findViewById(R.id.hatchback);
        suv=findViewById(R.id.suv);
        exotic=findViewById(R.id.exotic);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("     ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getApplicationContext(),ManageVihical.class));

                onBackPressed();
            }
        });



        entermodel = findViewById(R.id.entermodel);
        entermake = findViewById(R.id.entermake);

        enteryear = findViewById(R.id.enteryear);
        entercolor = findViewById(R.id.entercolor);

        save = findViewById(R.id.save);

        VehecalModelWebServiceCall();


        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);


        sedantext = findViewById(R.id.sedantext);
        hatchbacktext = findViewById(R.id.hatchbacktext);
        suvtext = findViewById(R.id.suvtext);
        exotictext = findViewById(R.id.exotictext);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sedantext.setTextColor(Color.parseColor("#f31541"));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle_sel));

                hatchback.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                exotic.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));

                hatchbacktext.setTextColor(Color.parseColor("#000000"));
                suvtext.setTextColor(Color.parseColor("#000000"));
                exotictext.setTextColor(Color.parseColor("#000000"));
                carSteus = "1";

            }
        });

        two.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                hatchbacktext.setTextColor(Color.parseColor("#f31541"));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));

                hatchback.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle_sel));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                exotic.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                sedantext.setTextColor(Color.parseColor("#000000"));
                suvtext.setTextColor(Color.parseColor("#000000"));
                exotictext.setTextColor(Color.parseColor("#000000"));

                carSteus = "2";

            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suvtext.setTextColor(Color.parseColor("#f31541"));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                hatchback.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle_sel));
                exotic.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                sedantext.setTextColor(Color.parseColor("#000000"));
                hatchbacktext.setTextColor(Color.parseColor("#000000"));
                exotictext.setTextColor(Color.parseColor("#000000"));

                carSteus = "3";

            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exotictext.setTextColor(Color.parseColor("#f31541"));
                sedan.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                hatchback.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                suv.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle));
                exotic.setImageDrawable(getResources().getDrawable(R.drawable.manage_vehicle_sel));

                sedantext.setTextColor(Color.parseColor("#000000"));
                hatchbacktext.setTextColor(Color.parseColor("#000000"));
                suvtext.setTextColor(Color.parseColor("#000000"));
                carSteus = "4";

            }
        });

        entermodel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainArray.clear();
                stetus = 1;
                MainArray.addAll(Constant.brand_name_Model);

                locationpopup();

            }
        });

        entermake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VehecalMakeWebServiceCall();

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean valid_=validate();

                if (valid_ == true) {


                    AddVehecalWebServiceCall();

                }

            }
        });


    }

    private void locationpopup() {
        Location = new Dialog(add_vehicle.this, android.R.style.Theme_Translucent_NoTitleBar);
        Location.setContentView(R.layout.popuplocation);

        final ListView listview = Location.findViewById(R.id.location_list);
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,MainArray);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(stetus == 1) {

                    VihicleID = Constant.vehicleID_Model.get(position);
                    entermodel.setText(Constant.brand_name_Model.get(position));
                    for (int i=0;i<listview.getChildCount();i++){
                        ((TextView)listview.getChildAt(i)).setTextColor(getResources().getColor(R.color.all));
                    }

                }
                else if(stetus ==2)
                {
                    MakeID = Constant.vehicleID_make.get(position);
                    entermake.setText(Constant.model_name_make.get(position));
                }

                Location.dismiss();

            }
        });

        Location.show();

    }

    public void VehecalModelWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String jsonData = toJSon("1");
            new GET_DATA().execute(jsonData);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
        }

    }

    public static String toJSon(String userID) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("appID", userID);
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
            VihicaleModelWS selectArea = new VihicaleModelWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

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
    public  void  NamePopUp() {

        Loader = new Dialog(add_vehicle.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();
    }

    public boolean validate()
    {
        boolean valid=true;

        if(enteryear.getText().toString().isEmpty())
        {
            enteryear.setError("Enter valid Value");
            valid=false;
        }
        else
        {
            enteryear.setError(null);
        }
        if(entercolor.getText().toString().isEmpty())
        {
            entercolor.setError("Enter valid Value");
            valid=false;
        }
        else
        {
            entercolor.setError(null);
        }

        return valid;

    }



    public void VehecalMakeWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String jsonData = toJSon2(VihicleID,"suv");
            new GET_DATA2().execute(jsonData);

        }
        else {
            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
        }

    }

    public static String toJSon2(String Vihecleid,String VihicalType) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("vehicleID", Vihecleid);
            jsonObj.put("vehicle_type", VihicalType);
            return jsonObj.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private class GET_DATA2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            model_makeWS selectArea = new model_makeWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    MainArray.clear();
                    MainArray.addAll(Constant.model_name_make);
                    stetus = 2;
                    locationpopup();

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


    public void AddVehecalWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String userID = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_ID);

            String jsonData = toJSon3(userID,carSteus,VihicleID,MakeID,enteryear.getText().toString(),entercolor.getText().toString());
            new GET_DATA3().execute(jsonData);

        }
        else {
            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
        }

    }


    public static String toJSon3(String userid,String VihicalType,String vihicleID,String make,String year,String color) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", userid);
            jsonObj.put("vehicle_type", VihicalType);
            jsonObj.put("vehicleID", vihicleID);
            jsonObj.put("make", make);
            jsonObj.put("year", year);
            jsonObj.put("color", color);

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
            Add_VehicleWS selectArea = new Add_VehicleWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    Toast.makeText(getApplicationContext(),"Vehicle Add Successfully",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(add_vehicle.this,ManageVihical.class);
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
