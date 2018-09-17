package co.project.cosy;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.WEProfile;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ProfileActivity extends AppCompatActivity {
    ImageView edit_profile,userimage;

    RelativeLayout back,changepassword;
    TextView username,mobile_below,mobile,email;
    Dialog Loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        edit_profile = findViewById(R.id.edit_profile);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        back = findViewById(R.id.back);
        changepassword = findViewById(R.id.changepassword);
        username = findViewById(R.id.username);
        mobile_below = findViewById(R.id.mobile_below);
        mobile = findViewById(R.id.mobile);
        TextView email = findViewById(R.id.email);
        userimage = findViewById(R.id.userimage);


        email.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_EMAIL));
        username.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_NAME));
        mobile.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_MOBILE));
        mobile_below.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_MOBILE));



        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this,EditProfile.class);
                startActivity(intent);

            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this,ChangePassword.class);
                startActivity(intent);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this,drawer.class);
                startActivity(intent);

            }
        });

        init();
    }

    public void init()
    {
        if (Configuration.isInternetConnection(getApplicationContext())) {

         String userID = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID);
            // String chapter_id = Constant.CHAPTER_ID.get(chap);
            String jsonData = toJSon(userID);
            new VocabluryFuction().execute(jsonData);

        }
        else {

            Intent intent = new Intent(ProfileActivity.this,ErrorScreen.class);
            startActivity(intent);
        }

    }
    public static String toJSon(String userID) {

        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID",userID);  // Set the first name/pair
            return jsonObj.toString();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private class VocabluryFuction extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            NamePopUp();
        }
        @Override
        protected String doInBackground(String... params) {
            WEProfile selectArea=new WEProfile();
            String result = selectArea.sendDetail(getApplicationContext(),params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success") ) {


                    Glide.with(ProfileActivity.this)
                            .load(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.Image_URL)+Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_IMAGE))
                            .placeholder(R.drawable.man)
                            .crossFade()
                            .into(userimage);
                }
                else  {
                    Toast.makeText(getApplicationContext(),"No History Found",Toast.LENGTH_LONG).show();
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
        Loader = new Dialog(ProfileActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();
    }


}
