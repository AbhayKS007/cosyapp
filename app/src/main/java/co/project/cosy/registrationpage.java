package co.project.cosy;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.RefferCode;
import co.project.cosy.WebService.RegistreWS;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class registrationpage extends AppCompatActivity {

    Dialog Loader;

    EditText usernamee,email,mobileno,passworde;
    CheckBox check;
    Button button;
    private String deviceID="",deviceToken,otpgenerated="";

    TextView login;

    TextView refral_code;
    Dialog Reffer;
    ProgressDialog progressDialog;
    String reffer_code;

    int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationpage);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        progressDialog=new ProgressDialog(registrationpage.this);

          usernamee=(EditText)findViewById(R.id.usernamee);
          email=(EditText)findViewById(R.id.email);
          mobileno=(EditText)findViewById(R.id.mobileno);
          passworde=(EditText)findViewById(R.id.passworde);
          button=(Button)findViewById(R.id.button2);
          check=(CheckBox)findViewById(R.id.checkbox);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid_=validate();
                if(Configuration.isInternetConnection(getApplicationContext())) {
                    if (valid_ == true) {

                        int value=(int)(Math.random()*9000)+1000;
                        otpgenerated= String.valueOf(value);

                        String jsonData = toJSon2(email.getText().toString(),mobileno.getText().toString(),otpgenerated);
                        new GET_DATA1().execute(jsonData);

                    }
                }
                else
                {
                    Intent intent = new Intent(registrationpage.this,ErrorScreen.class);
                    startActivity(intent);
                }


            }
        });


        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(registrationpage.this,signin.class);
                startActivity(intent);

            }
        });

        refral_code = findViewById(R.id.refral_code);

        refral_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RefferPopUp();
            }
        });


//        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
//        startActivityForResult(googlePicker, REQUEST_CODE);
    }

//    protected void onActivityResult(final int requestCode, final int resultCode,
//                                    final Intent data) {
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
//            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//
//            usernamee.setText(accountName);
//
//        }
//    }


  //  private boolean isValidUser(String uname){

    //    String User_type= "^[A-Za-z0-9_-]{3,15}$";
    //    Pattern pattern=Pattern.compile(User_type);
    //    Matcher matcher=pattern.matcher(uname);
    //    return matcher.matches();
  //  }


    private boolean isValidEmail(String uemail){

        String Email_Pattern="^[A-Za-z0-9]+(\\.[a-za-z0-9]+)*@+"
                +"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-za-z]{2,})$";
        Pattern patternee= Pattern.compile(Email_Pattern);
        Matcher matcherrr=patternee.matcher(uemail);
        return matcherrr.matches();
    }

    private boolean isValidMobile(String umobile){
        String mobile_type= "^[0-9]{10}";
        Pattern pattern= Pattern.compile(mobile_type);
        Matcher matcher= pattern.matcher(umobile);
        return  matcher.matches();
    }

    private boolean isValidPassword(String upassword){


        if (upassword != null && upassword.length()>5){
            return true;
        }
        return false;
    }

    private void DisplayToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public boolean validate() {
        boolean valid = true;

        final String ucheck=check.getText().toString();
        if(check.isChecked()) {
            //DisplayToast("T&C Accepted");
        }
        else {
            valid=false;
            DisplayToast("Accept Term and Condition");
        }

        if(usernamee.getText().toString().isEmpty()) {
            usernamee.setError("Enter valid User Name");
            valid = false;
        } else
        {
            usernamee.setError(null);
        }

        final  String uemail =email.getText().toString();
        if (!isValidEmail(uemail)){
            valid=false;
            email.setError("Invalid Email");
        }

        final  String umobile = mobileno.getText().toString();
        if (!isValidMobile(umobile)){
            valid=false;
            mobileno.setError("Invalid Mobile");
        }

        final  String upassword = passworde.getText().toString();
        if (!isValidPassword(upassword)){
            valid=false;
            passworde.setError("Password Must Be Of Six Digit");
        }

        return valid;
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

                    Intent intent = new Intent(registrationpage.this,verification.class);
                    intent.putExtra("first_name",usernamee.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("mobile",mobileno.getText().toString());
                    intent.putExtra("password",passworde.getText().toString());
                    intent.putExtra("otp",otpgenerated);
                    intent.putExtra("reffral",reffer_code);
                    startActivity(intent);

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

        Loader = new Dialog(registrationpage.this, android.R.style.Theme_Translucent_NoTitleBar);
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

    public  void  RefferPopUp() {

        Reffer = new Dialog(registrationpage.this, android.R.style.Theme_Translucent_NoTitleBar);
        Reffer.setContentView(R.layout.reffer_popup);

        ImageView cross_icon = Reffer.findViewById(R.id.cross_icon);
        Button submit_btn = Reffer.findViewById(R.id.submit_btn);
        final EditText name_edit = Reffer.findViewById(R.id.name_edit);

        System.out.println("reffer_code "+reffer_code);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reffer_code = name_edit.getText().toString();

                progressDialog=new ProgressDialog(registrationpage.this);
                if(Configuration.isInternetConnection(getApplicationContext())) {

                    String jsonData = toJSon(name_edit.getText().toString().trim());
                    new GET_DATA().execute(jsonData);
                }
                else
                {
                    Intent intent = new Intent(registrationpage.this,ErrorScreen.class);
                    startActivity(intent);
                }

            }
        });

        cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Reffer.dismiss();
            }
        });

        Reffer.show();
    }

    public static String toJSon(String code) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("code", code);

            return jsonObj.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private class GET_DATA extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            RefferCode selectArea = new RefferCode();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    Toast.makeText(getApplicationContext(),"Referral Code Applied",Toast.LENGTH_LONG).show();
                    refral_code.setFocusable(false);
                    Reffer.dismiss();

                }

                else {
                    Toast.makeText(getApplicationContext(),"Referral Code Not Vailed",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressDialog.dismiss();
            }
        }
    }


}
