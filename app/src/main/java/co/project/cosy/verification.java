package co.project.cosy;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.RegistreSendDataWS;
import co.project.cosy.WebService.RegistreWS;
import co.project.cosy.configuration.Configuration;
import co.project.cosy.utility.SmsReceiver;
import me.anwarshahriar.calligrapher.Calligrapher;

public class verification extends AppCompatActivity {

    TextView mobile_textview;
    EditText otp;
    boolean valid_;
    Button submit;
    private String deviceID="",deviceToken,otpgenerated="";
    String mob_rec,first_name,last_name="",email,password;

    String auth = "Normal";
    String authID = "";
    String otpp;
    Dialog Loader;

    String sms2;

    String ref="";

    TextView resendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("          ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),registrationpage.class));
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FirebaseApp.initializeApp(getApplicationContext());
        System.out.println("getting the fcm registratin id--->"+" and another tokess"+ FirebaseInstanceId.getInstance().getToken());
        deviceToken= FirebaseInstanceId.getInstance().getToken();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);setContentView(R.layout.activity_verification);

        mobile_textview=findViewById(R.id.mobile);
        otp=findViewById(R.id.otp);
        submit=findViewById(R.id.submit);

        resendOTP = findViewById(R.id.resendOTP);

        mob_rec=getIntent().getExtras().getString("mobile");
        first_name=getIntent().getExtras().getString("first_name");
        email=getIntent().getExtras().getString("email");
        password=getIntent().getExtras().getString("password");
        otpp=getIntent().getExtras().getString("otp");
        ref=getIntent().getExtras().getString("reffral");

        if(ref==null)
        {
            ref = "";
        }


        mobile_textview.setText(mob_rec);

        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                valid_=validate();
                if(otp.getText().length()==4)
                {
                    valid_=validate();
                    if(otpp.equalsIgnoreCase(otp.getText().toString()))
                    {
                        if(Configuration.isInternetConnection(getApplicationContext())) {

                            String jsonData = toJSon(first_name,email,mob_rec,password,auth,authID,ref,deviceID,deviceToken,"android");
                            new REGISTRAION_FUN().execute(jsonData);
                        }
                        else
                        {
                            Intent intent = new Intent(verification.this,ErrorScreen.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Enter A Valid OTP",Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid_=validate();
                if(otpp.equalsIgnoreCase(otp.getText().toString()))
                {
                    if(Configuration.isInternetConnection(getApplicationContext())) {

                        String jsonData = toJSon(first_name,email,mob_rec,password,auth,ref,authID,deviceID,deviceToken,"android");
                        new REGISTRAION_FUN().execute(jsonData);
                    }
                    else
                    {
                        Intent intent = new Intent(verification.this,ErrorScreen.class);
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Enter A Valid OTP",Toast.LENGTH_LONG).show();
                }

            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid_=validate();
                if(Configuration.isInternetConnection(getApplicationContext())) {

                    int value=(int)(Math.random()*9000)+1000;
                        otpgenerated= String.valueOf(value);

                        String jsonData = toJSon2(email,mob_rec,otpgenerated);
                        new GET_DATA1().execute(jsonData);

                }
                else
                {
                    Intent intent = new Intent(verification.this,ErrorScreen.class);
                    startActivity(intent);
                }


            }
        });



    }


    public static String toJSon2(String email,String mobile,String otp) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("email", email);
            jsonObj.put("mobile", mobile);
            jsonObj.put("otp", otp);

            return jsonObj.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public  boolean validate()
    {
        boolean valid=true;
        if(otp.getText().toString().isEmpty())
        {
            valid=false;
            otp.setError("Please enter the OTP");
        }
        else
        {
            otp.setError(null);
        }
        return  valid;
    }

    public static String toJSon(String first_name,String email,String mobile,String password,String auth,String ref,String authID,String deviceID,String deviceToken,String deviceType) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("name", first_name);
            jsonObj.put("email", email);
            jsonObj.put("mobile", mobile);
            jsonObj.put("password", password);
            jsonObj.put("auth", auth);
            jsonObj.put("code", ref);
            jsonObj.put("authID", authID);
            jsonObj.put("deviceID", deviceID);
            jsonObj.put("deviceToken", deviceToken);
            jsonObj.put("deviceType", deviceType);
            return jsonObj.toString();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private class REGISTRAION_FUN extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            RegistreSendDataWS selectArea = new RegistreSendDataWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {
                    Intent  add_child_intent2= new Intent(getApplicationContext(),drawer.class);
                    startActivity(add_child_intent2);
                    //Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),""+Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.MESSAGE),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Loader.dismiss();
            }
        }
    }

    public  void  NamePopUp() {

        Loader = new Dialog(verification.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();
    }



    public void updateList(String sms)
    {
        sms2 =  sms.replaceAll("[^0-9]","");
        otp.setText(sms2);
        System.out.println("messageText "+sms2);
    }
    private BroadcastReceiver mUnreadSmsBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String smsBody = intent.getStringExtra(SmsReceiver.EXTRA_SMS_BODY);
            String smsAddress = intent.getStringExtra(SmsReceiver.EXTRA_SMS_ADDRESS);
            updateList(smsBody);
            if (SmsReceiver.PRE_ADDRESS.equals(smsAddress)) {
                // notify your unread message
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mUnreadSmsBroadCastReceiver, new IntentFilter(SmsReceiver.ACTION_UNREAD_SMS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mUnreadSmsBroadCastReceiver);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
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


    private class GET_DATA1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            RegistreWS selectArea = new RegistreWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    otpp = Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.OTP);
                    Toast.makeText(getApplicationContext(),"OTP Send",Toast.LENGTH_LONG).show();

                }
                else {

                    Toast.makeText(getApplicationContext(),""+Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.MESSAGE),Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Loader.dismiss();
            }
        }
    }


}
