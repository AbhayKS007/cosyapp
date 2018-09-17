package co.project.cosy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.Add_AddressWS;
import co.project.cosy.configuration.Configuration;
import co.project.cosy.utility.GPSTracker;
import me.anwarshahriar.calligrapher.Calligrapher;


public class address extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private TextView bus;
    private TextView res;



    GPSTracker gpsTracker;

    String addresss,city,state,country,postalCode,knownName;
    LocationManager locationManager;

    EditText main_address;

    EditText house_number,ladmark;
    LinearLayout resident,bussnessaddress;
    RelativeLayout resi;


    String addresstype = "resident";
    Button add_address;
    Dialog Loader;
    TextView residence,business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

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
              //  startActivity(new Intent(getApplicationContext(),ManageAddress.class));
            }
        });


        gpsTracker = new GPSTracker(getApplicationContext());
        gpsTracker.getLatitude();
        gpsTracker.getLongitude();

        System.out.println("latlong "+gpsTracker.getLatitude()+" "+gpsTracker.getLongitude());


        residence = findViewById(R.id.residence);
        business = findViewById(R.id.business);

        resi=findViewById(R.id.resi);

        res=(TextView)findViewById(R.id.residence);
        bus=(TextView)findViewById(R.id.business);


        add_address = findViewById(R.id.add_address);

        main_address = findViewById(R.id.main_address);

        house_number = findViewById(R.id.house_number);
        ladmark = findViewById(R.id.ladmark);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);


        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if(isLocationEnabled())
        {
            addresss = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName(); // Onl
            System.out.println("location 2 " + addresss + " " + city + " " + state + " " + country + " " + postalCode);

            main_address.setText(addresss + " " + city + " " + state + " " + country + " " + postalCode);

        }

        resident = findViewById(R.id.resident);
        bussnessaddress = findViewById(R.id.bussnessaddress);

        resi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                residence.setTextColor(Color.parseColor("#ffffff"));
                business.setTextColor(Color.parseColor("#000000"));
                addresstype = "resident";
                resi.setBackgroundColor(Color.RED);
                bussnessaddress.setBackgroundColor(Color.WHITE);
            }
        });

        bussnessaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                business.setTextColor(Color.parseColor("#ffffff"));
                residence.setTextColor(Color.parseColor("#000000"));
                addresstype = "business";
                resi.setBackgroundColor(Color.WHITE);
                bussnessaddress.setBackgroundColor(Color.RED);

            }
        });


        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddAdressWebServiceCall();

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng newdelhi = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        mMap.addMarker(new MarkerOptions().position(newdelhi).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 14.0f));

    }


    protected boolean isLocationEnabled(){
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getApplicationContext().getSystemService(le);
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

            System.out.println("falseeeeeeeeee");
            return false;

        }
        else {

            System.out.println("trueeeeeeeee");
            return true;
        }
    }


       public void AddAdressWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String userID = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID);

            String jsonData = toJSon3(userID,addresstype,main_address.getText().toString(),house_number.getText().toString(),ladmark.getText().toString(),String.valueOf(gpsTracker.getLatitude()),String.valueOf(gpsTracker.getLongitude()));
            new GET_DATA3().execute(jsonData);

        }
        else {
            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
        }

    }

    public static String toJSon3(String userid,String addtype,String mainadd,String flat,String ladmar,String lat,String longi) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", userid);
            jsonObj.put("address_type", addtype);
            jsonObj.put("location", mainadd);
            jsonObj.put("flat_no", flat);
            jsonObj.put("landmark", ladmar);
            jsonObj.put("latitude", lat);
            jsonObj.put("longitude", longi);

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
            Add_AddressWS selectArea = new Add_AddressWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    Toast.makeText(getApplicationContext(),"Address Add Successfully",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(address.this,ManageAddress.class);
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
    public  void  NamePopUp() {

        Loader = new Dialog(address.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();
    }

}
