package co.project.cosy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.WE_GetStudioList;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class appointment extends AppCompatActivity {


    int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    Calendar C = Calendar.getInstance();

    String[] month;
    private static TextView txedate,txetime,enterlocation,selectv,service,enteramount;

    String address,carname,srvicename,serviceprice,strviveID;
    Dialog Loader;

    private Dialog Location;
    TextView entertime;
    ArrayList MainArray = new ArrayList();


    String sloatID;

    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("          ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),AddOnActivity.class));
                //onBackPressed();
            }
        });

        srvicename = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.srv_type);//getIntent().getExtras().getString("srvicename","defult");
        serviceprice = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.serviceamount); //getIntent().getExtras().getString("serviceamount","defult");

        strviveID = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.svID);

        button2 = findViewById(R.id.button2);
        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);
        txedate =  findViewById(R.id.txedate);
        txetime= findViewById(R.id.txetime);


        entertime = findViewById(R.id.entertime);

        enterlocation = findViewById(R.id.enterlocation);
        selectv = findViewById(R.id.selectv);
        service = findViewById(R.id.service);
        enteramount = findViewById(R.id.enteramount);


        selectv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        service.setText(srvicename);
        enteramount.setText(serviceprice);


        if(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.address) == null)
        {
            enterlocation.setText("");
        }
        else
        {
            address = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.address);
            enterlocation.setText(address);
        }
        if(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.vname) == null)
        {
            selectv.setText("");
        }
        else
        {
            carname =  Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.vname);
            selectv.setText(carname);
        }

        txedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialogFragment datepicker=new DateDialogFragment();
//                datepicker.show(getSupportFragmentManager(), "showDate");
                datepicker.show(getSupportFragmentManager(),"show");


            }
        });

        txetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


        enterlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(appointment.this,ManageAddress.class);
                startActivity(intent);

            }
        });


        selectv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(appointment.this,ManageVihical.class);
                startActivity(intent);

            }
        });



        entertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDateWebServiceCall();


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid_=validate();

                if (valid_ == true) {
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.finaldate, txedate.getText().toString());
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.finalstot, sloatID);

                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.finaladdress, enterlocation.getText().toString());
                    Configuration.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.finalamount, enteramount.getText().toString());
                    Intent intent = new Intent(appointment.this, select_option.class);
                    startActivity(intent);
                }
            }
        });

    }

    public static class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        public DateDialogFragment()
        {
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal= Calendar.getInstance();
            int year=cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);  //date is dateSetListener as per your code in question
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            return datePickerDialog;
        }
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            showSetDate(year,monthOfYear,dayOfMonth);
        }


    }


    public static void showSetDate(int year,int month,int day) {
        if(month<10) {

            txedate.setText(day + "-" + "0"+(month+1) + "-" + year);

        }
        else
        {
            txedate.setText(day+"-"+(month+1)+"-"+year);
        }

    }







    public  void SendDateWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            //String userID = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.USER_ID);

            String jsonData = toJSon3(txedate.getText().toString());
            new GET_DATA3().execute(jsonData);

        }
        else {
            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
        }

    }

    public static String toJSon3(String userid) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("booking_date", userid);
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
            WE_GetStudioList selectArea = new WE_GetStudioList();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    //Toast.makeText(getApplicationContext(),"Address Add Successfully",Toast.LENGTH_LONG).show();

                    if(!txedate.getText().toString().isEmpty()) {
                        MainArray.clear();
                        MainArray.addAll(Constant.period);

                        locationpopup();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Select Date First",Toast.LENGTH_LONG).show();
                    }

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

        Loader = new Dialog(appointment.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();
    }


    private void locationpopup() {
        Location = new Dialog(appointment.this, android.R.style.Theme_Translucent_NoTitleBar);
        Location.setContentView(R.layout.popuplocation);

        ListView listview = Location.findViewById(R.id.location_list);
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,MainArray);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                entertime.setText(Constant.period.get(position));
                sloatID = Constant.slotID.get(position);

                Location.dismiss();
            }
        });

        Location.show();

    }


    public boolean validate()
    {
        boolean valid=true;

        if(enterlocation.getText().toString().isEmpty())
        {
            enterlocation.setError("Enter valid Value");
            valid=false;
        }
        else
        {
            enterlocation.setError(null);
        }
        if(selectv.getText().toString().isEmpty())
        {
            selectv.setError("Enter valid Value");
            valid=false;
        }
        else
        {
            selectv.setError(null);
        }


        if(service.getText().toString().isEmpty())
        {
            service.setError("Enter valid Value");
            valid=false;
        }
        else
        {
            service.setError(null);
        }

        if(enteramount.getText().toString().isEmpty())
        {
            enteramount.setError("Enter valid Value");
            valid=false;
        }
        else
        {
            enteramount.setError(null);
        }

        if(txedate.getText().toString().isEmpty())
        {
            txedate.setError("Enter valid Value");
            valid=false;
        }

        else
        {
            txedate.setError(null);
        }
        if(entertime.getText().toString().isEmpty())
        {
            entertime.setError("Enter valid Value");
            valid=false;
        }
        else
        {
            entertime.setError(null);
        }

        return valid;

    }
}