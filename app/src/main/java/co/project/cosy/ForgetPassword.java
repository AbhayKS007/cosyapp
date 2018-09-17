package co.project.cosy;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.project.cosy.WebService.ForgetPasswordWS;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ForgetPassword extends AppCompatActivity {

    Button send;
    EditText email;
    Dialog Loader;

    private String deviceID="",deviceToken,otpgenerated="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        send=findViewById(R.id.forgot_send);
        email=findViewById(R.id.email);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("          ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),signin.class));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid_=validate();
                if(valid_==true)
                {

                    webservice();

                }
            }
        });
    }

    public boolean validate()
    {
        boolean valid=true;
        if(email.getText().toString().isEmpty())
        {
            email.setError("Invalid Mobile");
            valid=false;
        }
        else
        {
            email.setError(null);

        }
        return  valid;
    }


    public void webservice()
    {
        if(Configuration.isInternetConnection(getApplicationContext())) {

            int value=(int)(Math.random()*9000)+1000;
            otpgenerated= String.valueOf(value);

            String jsonData = toJSon2(email.getText().toString(),otpgenerated);
            new Forget().execute(jsonData);

        }
        else
        {
            Intent intent = new Intent(ForgetPassword.this,ErrorScreen.class);
            startActivity(intent);
        }
    }
    public static String toJSon2(String userID,String otp) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("mobile", userID);
            jsonObj.put("otp", otp);


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
            ForgetPasswordWS loginBL=new ForgetPasswordWS();
            String result = loginBL.sendDetail(getApplicationContext(),params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(s.equalsIgnoreCase("success"))
                {

                   // Toast.makeText(getApplicationContext(),"Password Successfully Sent To Your Registered Mobile Number",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgetPassword.this,ForgetPasswordMobileVerification.class);
                    intent.putExtra("otp",otpgenerated);
                    intent.putExtra("mobile",email.getText().toString());
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
        Loader = new Dialog(ForgetPassword.this, android.R.style.Theme_Translucent_NoTitleBar);
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
