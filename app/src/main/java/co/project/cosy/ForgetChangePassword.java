package co.project.cosy;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import co.project.cosy.WebService.ForgetPasswordChangeWS;
import co.project.cosy.configuration.Configuration;

public class ForgetChangePassword extends AppCompatActivity {

    String mobile;

    EditText password;
    Button submit;

    private String deviceID="",deviceToken,otpgenerated="";
    Dialog Loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_change_password);

        FirebaseApp.initializeApp(getApplicationContext());
        System.out.println("getting the fcm registratin id--->"+" and another tokess"+ FirebaseInstanceId.getInstance().getToken());
        deviceToken= FirebaseInstanceId.getInstance().getToken();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);setContentView(R.layout.activity_forget_change_password);


        mobile=getIntent().getExtras().getString("mobile");
        password = findViewById(R.id.passwordd);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!(password.getText().toString().isEmpty()))
                {
                    webservice();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Enter a Password First",Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public void webservice()
    {
        if(Configuration.isInternetConnection(getApplicationContext())) {

            int value=(int)(Math.random()*9000)+1000;
            otpgenerated= String.valueOf(value);

            String jsonData = toJSon2(mobile,password.getText().toString(),deviceID,deviceToken,"android");
            new Forget().execute(jsonData);

        }
        else
        {
            Intent intent = new Intent(ForgetChangePassword.this,ErrorScreen.class);
            startActivity(intent);
        }
    }

    public static String toJSon2(String mobile,String password, String deviceID ,String deviceToken,String divtype) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("mobile", mobile);
            jsonObj.put("password", password);
            jsonObj.put("deviceID", deviceID);
            jsonObj.put("deviceToken", deviceToken);
            jsonObj.put("deviceType", divtype);



            return jsonObj.toString();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private class Forget extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            ForgetPasswordChangeWS loginBL=new ForgetPasswordChangeWS();
            String result = loginBL.sendDetail(getApplicationContext(),params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(s.equalsIgnoreCase("success"))
                {

                    // Toast.makeText(getApplicationContext(),"Password Successfully Sent To Your Registered Mobile Number",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgetChangePassword.this,signin.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Mobile Number Not Register With Us",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            finally {
                Loader.dismiss();
            }
        }
    }


    public  void  NamePopUp() {
        Loader = new Dialog(ForgetChangePassword.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();

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



}
