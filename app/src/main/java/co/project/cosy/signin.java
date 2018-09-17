package co.project.cosy;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.LoginSimpleWS;
import co.project.cosy.WebService.RegistreSendDataWS;
import co.project.cosy.WebService.SocialConnectWS;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class signin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView Register;
    EditText username_Ed,password;
    private String deviceID="",deviceToken,otpgenerated="";
    Button button,facebook,gmail;
    boolean valid_;
    Dialog Loader;
    String type = "Normal";
    private String strFacebook="Normal";
    private String socialID="";
    private String userProfileUrl;
    private String userName;
    private String emailAddress;
    private String firstName;
    private String lastName;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 100;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    TextView login_forgot_pass;
    Dialog Reffer;

    String solialName;
    String authID;


    String possibleEmail;


    int REQUEST_CODE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FirebaseApp.initializeApp(getApplicationContext());
        System.out.println("getting the fcm registratin id--->"+" and another tokess"+ FirebaseInstanceId.getInstance().getToken());
        deviceToken= FirebaseInstanceId.getInstance().getToken();

        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);setContentView(R.layout.activity_signin);

        hashFromSHA1("F4:12:C7:8C:39:8C:D2:12:85:A2:3A:85:E1:26:12:3F:FE:A7:37:34");

        Register=findViewById(R.id.register);
        username_Ed=findViewById(R.id.username);
        password=findViewById(R.id.password);
        facebook =findViewById(R.id.facebook);
        gmail =findViewById(R.id.gmail);

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_api_app_id))
                .requestEmail()
                .build();

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        button=findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid_=validate();
                if(validate())
                {
                    if(Configuration.isInternetConnection(getApplicationContext())) {

//                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//                        FirebaseApp.initializeApp(getApplicationContext());
//                        System.out.println("getting the fcm registratin id--->"+" and another tokess"+ FirebaseInstanceId.getInstance().getToken());
//                        deviceToken= FirebaseInstanceId.getInstance().getToken();
//                        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);setContentView(R.layout.activity_signin);

                        String jsonData = toJSon(username_Ed.getText().toString(),password.getText().toString(),deviceID,deviceToken,"android");
                        new LoginData().execute(jsonData);

                    }
                    else {
                        Intent intent = new Intent(signin.this,ErrorScreen.class);
                        startActivity(intent);
                    }
                }

            }
        });


        login_forgot_pass = findViewById(R.id.login_forgot_pass);
        login_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(signin.this,ForgetPassword.class);
                startActivity(intent);

            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken() != null) {

                    type = "Facebook";
                    LoginManager.getInstance().logOut();

                }
                // Login and Access User Date
                LoginManager.getInstance().logInWithReadPermissions(signin.this, Arrays.asList("public_profile", "email"));
            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }

        // Register Callback for Facebook LoginManager
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Profile profile = Profile.getCurrentProfile();
                        if (profile != null) {
                            Log.e("Facebook : firstName", profile.getFirstName());
                            Log.e("Facebook : lastName", profile.getLastName());
                            Log.e("Facebook : authToken", loginResult.getAccessToken().getToken());
                        }
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            final JSONObject object,
                                            GraphResponse response) {
                                        Log.e("Facebook : Graph", response.toString());
                                        try {
                                            socialID = object.getString("id");
                                            authID= object.getString("id");

                                            userProfileUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
//                                            userName = object.getString("first_name")+" "+object.getString("last_name");
                                            //userName1 = "";
                                            lastName = object.getString("last_name");
                                            firstName = object.getString("first_name");
                                            emailAddress = object.getString("email");
                                            strFacebook="Facebook";
                                            //  facebookSignIn(object);

                                           // username_Ed.setText(firstName+" "+lastName);
                                            //  et_email.setText(emailAddress);
                                            System.out.println("lastName "+lastName+" emailAddress "+emailAddress);

                                            if (Configuration.isInternetConnection(getApplicationContext())) {
                                                if (true) {

//                                                    int value = (int) (Math.random() * 9000) + 1000;
//                                                    otpgenerated = String.valueOf(value);
                                                    //   Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SIGNUP_OTP, otpgenerated);
                                                    solialName  = firstName+" "+lastName;
                                                    String deviceType = "android";
                                                    String auth_type="facebook";
                                                  //  here we call toTson methed for convert java object to json object
                                                    String jsonData = toJSonSocial(emailAddress, deviceID, deviceToken,deviceType);
                                                    if (deviceToken != null) {
                                                        new SendingUserSocian().execute(jsonData);
                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), "Please Refresh App ,We Could Not Recognize Your Device, Due to Internet Connection", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), Constant.NO_INTERNET_MSG, Toast.LENGTH_LONG).show();
                                            }

                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday,picture.width(150).height(150),first_name,last_name");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        //Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        //Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });



        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signin.this, registrationpage.class);
                startActivity(i);
            }
        });

        // Initializing Google API Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(signin.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mGoogleApiClient.isConnected()){

                    type = "Google";

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                }

                // Get the Google SignIn Intent
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

                // Start Activity with Google SignIn Intent
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });





    }



    public boolean validate() {
        boolean valid = true;

        String  userPass=username_Ed.getText().toString();
        String  userMobileText=password.getText().toString();


        if (userPass.isEmpty()) {
            username_Ed.setError("Enter Valid Password");
            valid = false;
        } else {
            username_Ed.setError(null);
        }

        if (userMobileText.isEmpty())
        {
            password.setError("Enter Valid User Name");
            valid = false;
        }
        else {
            password.setError(null);
        }
        return valid;
    }


    public static String toJSon(String mobile,String password,String deviceID,String deviceToken,String deviceType) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("mobile", mobile);
            jsonObj.put("password", password);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class LoginData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            LoginSimpleWS selectArea = new LoginSimpleWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                   Intent intent = new Intent(signin.this,drawer.class);
                    startActivity(intent);

                }
                else if(s.equalsIgnoreCase("failure"))
                    {
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

        Loader = new Dialog(signin.this, android.R.style.Theme_Translucent_NoTitleBar);
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

    public void hashFromSHA1(String sha1) {
        String[] arr = sha1.split(":");
        byte[] byteArr = new  byte[arr.length];

        for (int i = 0; i< arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.e("hash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d("Google :", "handleSignInResult:" + result.isSuccess());
        boolean isSuccess = result.isSuccess();
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            String Name = result.getSignInAccount().getDisplayName();
            String email = result.getSignInAccount().getEmail();
            String id = result.getSignInAccount().getId();
            userName = acct.getDisplayName();
            firstName = acct.getGivenName();

            solialName = acct.getDisplayName();
            //  lastName = acct.getFamilyName();
            lastName = "";
            System.out.println("userName===>" + acct + Name);
            emailAddress = acct.getEmail();
            socialID = acct.getId();
            strFacebook = "google";

            username_Ed.setText(firstName);

            authID = result.getSignInAccount().getId();
            String deviceType = "android";
            //  here we call toTson methed for convert java object to json object
            String jsonData = toJSonSocial(emailAddress, deviceID, deviceToken,deviceType);
            if (deviceToken != null) {
                new SendingUserSocian().execute(jsonData);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please Refresh App ,We Could Not Recognize Your Device, Due to Internet Connection", Toast.LENGTH_LONG).show();
            }


            //et_email.setText(emailAddress);
//            userProfileUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "";
//            if (Configuration.isInternetConnection(getApplicationContext())) {
//                if (true) {
//
//                    int value = (int) (Math.random() * 9000) + 1000;
//                    otpgenerated = String.valueOf(value);
//                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SIGNUP_OTP, otpgenerated);
//                    String name1 = firstName + " " + lastName;
//                    String deviceType = "android";
//                    String auth_type = "google";
//                    //here we call toTson methed for convert java object to json object
//                    String jsonData = toJSonSocial(name1, emailAddress, socialID, userProfileUrl, deviceID, deviceToken,
//                            deviceType, auth_type);
//                    if (deviceToken != null) {
//                        new SendingUserSocian().execute(jsonData);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Please Refresh App ,We Could Not Recognize Your Device, Due to Internet Connection", Toast.LENGTH_LONG).show();
//                    }
//                }
//            } else {
//                Toast.makeText(getApplicationContext(), Constant.NO_INTERNET_MSG, Toast.LENGTH_LONG).show();
//            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            System.out.println("result==>"+result);

            handleGoogleSignInResult(result);
        }}

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static String toJSonSocial(String password,String deviceID,String deviceToken,String deviceType) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("email", password);
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


    private class SendingUserSocian extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {
            SocialConnectWS selectArea = new SocialConnectWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    Intent intent = new Intent(signin.this,drawer.class);
                    startActivity(intent);

                }
                else {

                    RefferPopUp();

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Loader.dismiss();
            }
        }
    }

    public  void  RefferPopUp() {
        Reffer = new Dialog(signin.this, android.R.style.Theme_Translucent_NoTitleBar);
        Reffer.setContentView(R.layout.mobilenumber);

        ImageView cross_icon = Reffer.findViewById(R.id.cross_icon);
        Button submit_btn = Reffer.findViewById(R.id.submit_btn);
        final EditText name_edit = Reffer.findViewById(R.id.name_edit);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(!name_edit.getText().toString().trim().isEmpty())
               {
                if(Configuration.isInternetConnection(getApplicationContext())) {

                    String ref = "";

                    String jsonData = toJSon(solialName,emailAddress,name_edit.getText().toString(),"",type,authID,ref,deviceID,deviceToken,"android");
                    new REGISTRAION_FUN().execute(jsonData);

                }
                else
                {
                    Intent intent = new Intent(signin.this,ErrorScreen.class);
                    startActivity(intent);
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Enter A Mobile Number",Toast.LENGTH_LONG).show();
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


    public static String toJSon(String first_name,String email,String mobile,String password,String auth,String authID,String ref,String deviceID,String deviceToken,String deviceType) {
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

}
