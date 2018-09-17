package co.project.cosy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.login.Login;

import co.project.cosy.Constant.Constant;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

import static android.os.Binder.getCallingUid;

public class splashscreen extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();


        boolean hasAndroidPermissions = hasPermissions(getApplicationContext(), new String[]{
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WAKE_LOCK,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.CAPTURE_AUDIO_OUTPUT,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.GET_ACCOUNTS,


        });

        if (hasAndroidPermissions) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID)==null )
                    {
                        startActivity(new Intent(getApplicationContext(),WelcomeScreen.class));
                    }
                    else if (Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID)!=null ){
                        startActivity(new Intent(getApplicationContext(),drawer.class));
                    }
                }
            }, 3000);


        } else {

            ActivityCompat.requestPermissions(splashscreen.this,
                    new String[]{
                            android.Manifest.permission.INTERNET,
                            android.Manifest.permission.ACCESS_NETWORK_STATE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CALL_PHONE,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WAKE_LOCK,
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.CAPTURE_AUDIO_OUTPUT,
                            android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                            android.Manifest.permission.READ_CONTACTS,
                            android.Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.GET_ACCOUNTS,

                    },
                    1);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        boolean hasAllPermissions = true;

        System.out.println("getting the insid tjhe method");
        for (String permission : permissions) {

            System.out.println("getting all the perission" + permission);

            //return false instead of assigning, but with this you can log all permission values
            if (!hasPermission(context, permission)) {
                System.out.println("getting inside the false condidition");
                hasAllPermissions = false;
            }
        }

        return hasAllPermissions;

    }

    public static boolean hasPermission(Context context, String permission) {

        PackageManager pm = context.getPackageManager();
        boolean permissionaaa = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permission, pm.getNameForUid(getCallingUid())));
        System.out.println("permissio i granted" + permission + " and firs res alues" + " and  " + permissionaaa);
        return permissionaaa;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID)==null )
                            {
                                startActivity(new Intent(getApplicationContext(),WelcomeScreen.class));
                            }
                            else if (Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID)!=null )
                            {
                                startActivity(new Intent(getApplicationContext(),drawer.class));
                            }

                        }
                    }, 4000);
                }
                else {

                    Toast.makeText(splashscreen.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private void initView() {
        // linLay = (RelativeLayout) findViewById(R.id.lin_lay);
    }

}
