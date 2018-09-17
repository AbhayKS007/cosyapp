package co.project.cosy;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.project.cosy.Constant.Constant;
import co.project.cosy.WebService.FinalOrderWS;
import co.project.cosy.WebService.Get_Coopen_List;
import co.project.cosy.WebService.Verify_Coupen_WS;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class thankyou extends AppCompatActivity {


    TextView name,date,time,address,service_name,amount,finalAmount;
    Button cashondelevery;

    AdapterPassbook adapterForDownload;
    RecyclerView myList;

    LinearLayout coupen_btn;
    ProgressDialog progressDialog;

     Dialog Location;

    ArrayList MainArray = new ArrayList();
    ArrayList CoupanDescription=new ArrayList();
    ArrayList<String>Sum=new ArrayList<>();

    String cupenCode = "",cupenValue,cupenType;
    LinearLayout discount_relativ;

    TextView discount;
    String finalDiscount="0";
    String bookingID;

    Double Total_amount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        progressDialog=new ProgressDialog(thankyou.this);
        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        address = findViewById(R.id.address);
        //service_name = findViewById(R.id.service_name);
       // amount = findViewById(R.id.amount);
        finalAmount = findViewById(R.id.finalAmount);
        cashondelevery = findViewById(R.id.cashondelevery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("                ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getApplicationContext(),drawer.class));
                onBackPressed();
            }
        });


        cupenWebServiceCall();

        coupen_btn = findViewById(R.id.coupen_btn);
        discount_relativ = findViewById(R.id.discount_relativ);

        discount = findViewById(R.id.discount);

        name.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.vehicle_name));
        date.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.date));
        time.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.slot));
        address.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.address_book));
       // service_name.setText(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.services));
//        amount.setText("₹"+Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.amount));
        finalAmount.setText("₹"+Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.amount));


        bookingID = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.bookingID);

        cashondelevery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getApplicationContext(),"Order Placed",Toast.LENGTH_LONG).show();
//
//                Intent intent = new Intent(thankyou.this,drawer.class);
//                startActivity(intent);

                ChashOrderWsCalling();


            }
        });


        myList=(RecyclerView)findViewById(R.id.service_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);

        adapterForDownload=new AdapterPassbook(getApplicationContext());
        myList.setAdapter(adapterForDownload);



        coupen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                locationpopup();
            }
        });



    }


    public class AdapterPassbook extends RecyclerView.Adapter<AdapterPassbook.ViewHolder> {

        Context mContext;

        public AdapterPassbook(Context context) {
            this.mContext = context;

        }

        // 3
        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView service_name,service_price;
            LinearLayout main_layout;


            public ViewHolder(View itemView) {
                super(itemView);

                service_name = itemView.findViewById(R.id.service_name);
                service_price = itemView.findViewById(R.id.service_price);

               // main_layout = itemView.findViewById(R.id.main_layout);

            }
        }
        @Override
        public int getItemCount() {

            int i=0;
            try{

                i= Constant.bsID.size();

            }
            catch (Exception e)
            {
                i=0;
                e.printStackTrace();
            }

            return  i;
        }

        // 2
        @Override
        public AdapterPassbook.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finalprice_card, parent, false);
            return new AdapterPassbook.ViewHolder(view);

        }

        @Override
        public long getItemId(int position) {

            return position;

        }
        @Override
        public int getItemViewType(int position) {

            return position;

        }

        @Override
        public void onBindViewHolder(final AdapterPassbook.ViewHolder holder, final int position) {
            try {
                holder.service_name.setText(Constant.thanks_service_name.get(position));
                holder.service_price.setText("₹ "+Constant.price.get(position));

//                holder.main_layout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                    }
//                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void cupenWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String jsonData = toJSon("1");
            new GET_DATA().execute(jsonData);
        }
        else {
            Intent intent = new Intent(thankyou.this, ErrorScreen.class);
            startActivity(intent);
        }

    }

    public static String toJSon(String userID) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("app_id", userID);
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
            Get_Coopen_List selectArea = new Get_Coopen_List();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success"))
                {

                }
                else
                {

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally
            {
                progressDialog.dismiss();
            }
        }
    }

    private void locationpopup() {
        Location = new Dialog(thankyou.this, android.R.style.Theme_Translucent_NoTitleBar);
        Location.setContentView(R.layout.popuplocation);
        MainArray.clear();
        CoupanDescription.clear();
        Sum.clear();
        CoupanDescription.add(Constant.COUPAN_DESCRIPTION);
        MainArray.addAll(Constant.COUPEN_CODE);
        for (int i=0; i<MainArray.size();i++)
        {
            Sum.add(Constant.COUPEN_CODE.get(i)+"\n"+Constant.COUPAN_DESCRIPTION.get(i));
        }

        System.out.println("ksjxksdx"+Sum);
        ListView listview = Location.findViewById(R.id.location_list);
        TextView textView=Location.findViewById(R.id.norecords);
        textView.setVisibility(View.GONE);

        if (!MainArray.isEmpty()){
            listview.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);

        }
        else {
            listview.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.rec_coupan,Sum);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cupenCode = Constant.COUPEN_CODE.get(position);
                cupenValue = Constant.COUPEN_AMOUNT.get(position);
                cupenType = Constant.COUPEN_TYPE.get(position);
                verifyAppalyCuopone();
                Location.dismiss();

            }
        });

        Location.show();
    }

    public void verifyAppalyCuopone()
    {
        if (Configuration.isInternetConnection(getApplicationContext())) {

            String jsonData = toJSon2(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_ID),cupenCode);
            new GET_DATA1().execute(jsonData);

        }
        else {

            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);

        }
    }
    public static String toJSon2(String userID,String cupenCode) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", userID);
            jsonObj.put("coupon_code", cupenCode);
            return jsonObj.toString();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private class GET_DATA1 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            Verify_Coupen_WS selectArea = new Verify_Coupen_WS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {
                    Location.dismiss();
                    discount_relativ.setVisibility(View.VISIBLE);
                    ApplayCoupenCodeFun();

                    Toast.makeText(getApplicationContext(),"Coupon Applied Successfully",Toast.LENGTH_LONG).show();

                }
                else
                {

                    Toast.makeText(getApplicationContext(),"Not a Vailed Coupon Code",Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressDialog.dismiss();
            }
        }
    }


    public void ApplayCoupenCodeFun() {
        if (cupenType.equalsIgnoreCase("FIXED")) {

            if(Double.parseDouble(cupenValue)<Double.parseDouble(Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.amount))) {

                String amount, deleviry;


                finalDiscount = cupenValue;

                discount.setText(cupenValue);

                amount = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.amount);
                //deleviry = delivery_amount.getText().toString();

                Total_amount = Double.parseDouble(amount) - Double.parseDouble(cupenValue);
                finalAmount.setText("- ₹"+String.valueOf(Total_amount));

                System.out.println("amount " + Total_amount);

            }
            else
            {
                Toast.makeText(getApplicationContext(),"You Can't Use This Coupon",Toast.LENGTH_LONG).show();
            }


        }
        else if (cupenType.equalsIgnoreCase("percentage"))
        {
            String amount, deleviry;
            Double total_amountt;


            Double discount_amount;

            //discount.setText(cupenValue);
            amount = Configuration.getSharedPrefrenceValue(getApplicationContext(), Constant.amount);
           // deleviry = delivery_amount.getText().toString();

            total_amountt = Double.parseDouble(amount);

            discount_amount = total_amountt*Double.parseDouble(cupenValue)/100;
            Total_amount = total_amountt - discount_amount;

            finalDiscount = String.valueOf(discount_amount);

            discount.setText("- ₹"+String.valueOf(discount_amount));
            finalAmount.setText("₹"+String.valueOf(Total_amount));
            System.out.println("amount " + Total_amount);
        }

    }

    public void ChashOrderWsCalling()
    {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String jsonData = toJSon4(Configuration.getSharedPrefrenceValue(getApplicationContext(),Constant.USER_ID),bookingID,cupenCode,finalDiscount,Total_amount.toString(),"cod","","Success");
            new GET_DATA4().execute(jsonData);

        }
        else {

            Intent intent = new Intent(getApplicationContext(), ErrorScreen.class);
            startActivity(intent);
        }
    }

    public static String toJSon4(String userID,String bookingID,String coupon_code,String discount_amount,String total_amount,String payment_mode,String transactionID, String transaction_status) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userID", userID);
            jsonObj.put("bookingID", bookingID);
            jsonObj.put("coupon_code", coupon_code);
            jsonObj.put("discount_amount", discount_amount);
            jsonObj.put("total_amount", total_amount);
            jsonObj.put("payment_mode", payment_mode);
            jsonObj.put("transactionID", transactionID);
            jsonObj.put("transaction_status", transaction_status);
            return jsonObj.toString();
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private class GET_DATA4 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            FinalOrderWS selectArea = new FinalOrderWS();
            String result = selectArea.sendDetail(getApplicationContext(), params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s.equalsIgnoreCase("success")) {

                    Toast.makeText(getApplicationContext(),"Order Done",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(thankyou.this,drawer.class);
                    startActivity(intent);
                }
                else
                {

                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressDialog.dismiss();
            }
        }
    }



}
