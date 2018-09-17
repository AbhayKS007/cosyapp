package co.project.cosy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.project.cosy.WebService.ChangePasswordWS;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ChangePassword extends AppCompatActivity {
    EditText old_password,new_password,confirm_password;
    Button submit;
    LinearLayout main_layout;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        progressDialog = new ProgressDialog(ChangePassword.this);

        old_password=findViewById(R.id.old_password);
        new_password=findViewById(R.id.new_password);
        confirm_password=findViewById(R.id.confirm_password);
        submit=findViewById(R.id.submit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("       ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        main_layout=findViewById(R.id.main_layout);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean valid_=validate();
//                if(valid_==true){
//                    Snackbar snackbar= Snackbar.make(main_layout,"Your Password has been changed",Snackbar.LENGTH_LONG);
//                    snackbar.show();
//                }

                if (Configuration.isInternetConnection(getApplicationContext())) {

                    if (validate()) {
                        //here we call toTson methed for convert java object to json object
                        String jsonData = toJSon("10",old_password.getText().toString(),new_password.getText().toString(),confirm_password.getText().toString());
                        new LoginFunction().execute(jsonData);
                    }
                }
                else {

//                    Intent intent = new Intent(Support.this,ErrorScreen.class);
//                    startActivity(intent);

                }

            }
        });

    }
    public boolean validate()
    {
        boolean valid=true;
        if(old_password.getText().toString().isEmpty())
        {
            old_password.setError("Invalid Password");
            valid=false;
        }
        else
        {
            old_password.setError(null);
        }
        if(new_password.getText().toString().isEmpty()||new_password.getText().toString().length()<6)
        {
            new_password.setError("Password must contain atleast 6 characters");
            valid=false;
        }
        else
        {
            new_password.setError(null);
            if (confirm_password.getText().toString().equals(new_password.getText().toString()))
            {
                confirm_password.setError(null);
            }
            else
            {
                confirm_password.setError("Please re-enter your correct password");
                valid=false;
            }
        }
        return valid;
    }

    public static String toJSon(String userid,String name,String email,String meg) {

        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", userid); // Set the first name/pair
            jsonObj.put("old_password", name); // Set the first name/pair
            jsonObj.put("password", email);
            return jsonObj.toString();
        }

        catch (JSONException ex) {

            ex.printStackTrace();

        }
        return null;
    }



    private class LoginFunction extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            ChangePasswordWS loginBL=new ChangePasswordWS();
            String result = loginBL.sendDetail(getApplicationContext(),params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(s.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getApplicationContext(),"Password Change",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ChangePassword.this,Home.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong ",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            finally {
                progressDialog.dismiss();
            }
        }
    }



}
