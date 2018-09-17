package co.project.cosy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.EditProfileWE;
import co.project.cosy.configuration.Configuration;
import co.project.cosy.utility.FilePath;
import co.project.cosy.utility.Utility;
import me.anwarshahriar.calligrapher.Calligrapher;

public class EditProfile extends AppCompatActivity {
    EditText firstname,lastname,email,mobile;
    Button update;
    LinearLayout main_layout;

    RelativeLayout change_image;

    Dialog Loader;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private String selectedFilePath="";

    private static final String TAG = EditProfile.class.getSimpleName();
    PowerManager.WakeLock wakeLock;

    URL url;
    private String SERVER_URLUser  = "http://35.197.110.254/cozicar/webservice/edit_profile";

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        firstname=findViewById(R.id.edit_firstname);
        lastname=findViewById(R.id.edit_lastname);
        email=findViewById(R.id.edit_email);
        mobile=findViewById(R.id.edit_mobile);
        update=findViewById(R.id.update);
         main_layout=findViewById(R.id.main_layout);
        change_image = findViewById(R.id.change_image);
        imageView = findViewById(R.id.imageView);

        System.out.println("tsting "+Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_NAME));
        Glide.with(EditProfile.this)
                .load(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.Image_URL)+Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_IMAGE))
                .placeholder(R.drawable.man)
                .crossFade()
                .into(imageView);

        firstname.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_NAME));
        mobile.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_MOBILE));
        email.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_EMAIL));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("          ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid_=validate();
                if(valid_==true)
                {
                    System.out.println("aasss===>"+selectedFilePath);
                    PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                    wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
                    wakeLock.acquire();

                    if (selectedFilePath != null && !selectedFilePath.isEmpty() && !selectedFilePath.equals("")) {
                        // progressDialog = ProgressDialog.show(AddChild.this, "", "Uploading File...", true);
                        NamePopUp();
                        System.out.println("a5===>"+selectedFilePath);

                        valid_=validate();
                        if(valid_==true) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        uploadFile(selectedFilePath);
                                    } catch (OutOfMemoryError e) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(EditProfile.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Loader.dismiss();

                                    }


                                }
                            }).start();

                        }

                    }
                    else {
                        valid_=validate();
                        if(valid_==true) {
                            if (Configuration.isInternetConnection(getApplicationContext())) {
                                String uid = String.valueOf(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID));
                                String jsonData1 = "userID=" + uid + "&first_name=" + firstname.getText().toString() + "&last_name=" + lastname.getText().toString() + "&email=" + email.getText().toString() + "&flag=" + "N";
                                new UploadImageTask().execute(jsonData1);
                            } else {
                                Intent intent = new Intent(EditProfile.this, ErrorScreen.class);
                                startActivity(intent);
                            }

                        }

                    }
                }
            }
        });



        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectImage();



            }
        });



    }
    public boolean validate()
    {
        boolean valid=true;
        if(firstname.getText().toString().isEmpty())
        {
            valid=false;
            firstname.setError("Invalid name");
        }
        else
        {
            firstname.setError(null);
        }
        if(email.getText().toString().isEmpty())
        {
            valid=false;
            email.setError("Invalid email address");
        }
        else
        {
            email.setError(null);
        }
        if(mobile.getText().toString().isEmpty()||mobile.getText().toString().length()<10||mobile.getText().toString().length()>10)
        {
            mobile.setError("Enter valid Mobile Number");
            valid=false;
        }
        else
        {
            mobile.setError(null);
        }
        return  valid;

    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(EditProfile.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == EditProfile.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedFilePath= destination.getAbsolutePath();
        imageView.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data == null) {
            //no data present
            return;
        }

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        wakeLock.acquire();

        Uri selectedFileUri = data.getData();
        selectedFilePath = FilePath.getPath(this, selectedFileUri);
        Log.i(TAG, "Selected File Path:" + selectedFilePath);

        if (selectedFilePath != null && !selectedFilePath.equals("")) {

            imageView.setImageBitmap(BitmapFactory
                    .decodeFile(selectedFilePath));
        }
        else {
            Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
        }
    }


    public int uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;
        Map<String, String> params = new HashMap<String, String>(10);

        params.put("userID",String.valueOf(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID)));
        params.put("first_name",firstname.getText().toString());
        params.put("last_name",lastname.getText().toString());
        params.put("email",email.getText().toString());
        params.put("flag","Y");


        //        intentCityName= String.valueOf(intent.getStringExtra("cityName"));
//        intentAreaName= String.valueOf(intent.getStringExtra("areaName"));
//        intentAreaID= String.valueOf(intent.getStringExtra("AREA_ID"));
//        intentAreaCityID= String.valueOf(intent.getStringExtra("AREA_CITY_ID"));
//        intentAREA_PINCOCE= String.valueOf(intent.getStringExtra("AREA_PINCOCE"));


        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            Loader.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);

                url = new URL(SERVER_URLUser);

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("image", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                //  bufferSize = Math.max(bytesAvailable, maxBufferSize);
                bufferSize = 1*1024*1024;
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {

                    try {

                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(EditProfile.this, "Insufficient Memory!", Toast.LENGTH_SHORT).show();

                    }
                    bytesAvailable = fileInputStream.available();
                    //  bufferSize = Math.max(bytesAvailable, maxBufferSize);
                    bufferSize = 1*1024*1024;
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);


                Iterator<String> keys = params.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = params.get(key);

                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                    dataOutputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(value);
                    dataOutputStream.writeBytes(lineEnd);

                    System.out.println("value===>"+value);
                }


                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                try {
                    serverResponseCode = connection.getResponseCode();
                } catch (OutOfMemoryError e) {
                    Toast.makeText(EditProfile.this, "Memory Insufficient!", Toast.LENGTH_SHORT).show();
                }
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode==200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // fileNameText.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "" + fileName);
//
                            Intent intent = new Intent(EditProfile.this,Home.class);
                            startActivity(intent);

                        }
                    });
                }
                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

                if (wakeLock.isHeld()) {
                    wakeLock.release();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditProfile.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditProfile.this, "URL Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditProfile.this, "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            Loader.dismiss();
            return serverResponseCode;
        }
    }


    private class UploadImageTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            NamePopUp();
        }

        @Override
        protected String doInBackground(String... params) {

            EditProfileWE uploadProfifeWE = new EditProfileWE();
            String result = uploadProfifeWE.sendDetail(getApplicationContext(),params[0]);
            return result;

        }

        @Override
        protected void onPostExecute(String s) {

            try {
                if (s.equalsIgnoreCase("success")) {

                    Toast.makeText(getApplicationContext(),"Profile Update",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                }
                else {
                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Loader.dismiss();
            }
        }

    }

    public  void  NamePopUp() {
        Loader = new Dialog(EditProfile.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();
    }




}
