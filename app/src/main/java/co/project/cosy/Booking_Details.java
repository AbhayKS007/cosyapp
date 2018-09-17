package co.project.cosy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.project.cosy.Constant.Constant;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Booking_Details extends AppCompatActivity {

    TextView studio_name;
    TextView car_name,date,time,location,finalAmount,otp;

    AdapterPassbook adapterForDownload;
    RecyclerView myList;

    TextView totalamout,paymentmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("        ");
        setSupportActionBar(toolbar);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplicationContext(),drawer.class));
                onBackPressed();

            }
        });

        Constant.bsID_history.clear();
        Constant.serviceID_history.clear();
        Constant.service_name_history.clear();
        Constant.price_history.clear();

        car_name = findViewById(R.id.car_name);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        location = findViewById(R.id.location);
        finalAmount = findViewById(R.id.finalAmount);
        otp = findViewById(R.id.otp);
        studio_name = findViewById(R.id.studio_name);
        totalamout = findViewById(R.id.totalamout);
        paymentmode = findViewById(R.id.paymentmode);

        String position = getIntent().getExtras().getString("position","defaultKey");

        car_name.setText(Constant.vehicle_name_history.get(Integer.parseInt(position)));
        date.setText(Constant.date_history.get(Integer.parseInt(position)));
        time.setText(Constant.slot_history.get(Integer.parseInt(position)));
        location.setText(Constant.address_history.get(Integer.parseInt(position)));
        finalAmount.setText("₹"+Constant.amount_history.get(Integer.parseInt(position)));
        otp.setText(Constant.passcode_history.get(Integer.parseInt(position)));
        studio_name.setText(Constant.studio_name_history.get(Integer.parseInt(position)));
        totalamout.setText("₹"+Constant.amount_history.get(Integer.parseInt(position)));
        paymentmode.setText(Constant.payment_history.get(Integer.parseInt(position)));

        myList=(RecyclerView)findViewById(R.id.bookingdetails_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);


        String post = Constant.BOOKING_HISTORY_SERVICE.get(Constant.bookingID_history.get(Integer.parseInt(position)));
        ServiceMedia(post);


    }


    public void ServiceMedia(String data)
    {
        System.out.println("data===>>"+data);
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(data);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.bsID_history.add(String.valueOf(jsonObject.get("bsID").toString()));
                Constant.serviceID_history.add(String.valueOf(jsonObject.get("serviceID").toString()));
                Constant.service_name_history.add(String.valueOf(jsonObject.get("service_name").toString()));
                Constant.price_history.add(String.valueOf(jsonObject.get("price").toString()));

            }


            adapterForDownload=new AdapterPassbook(getApplicationContext());
            myList.setAdapter(adapterForDownload);
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
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

                i= Constant.bsID_history.size();

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
                holder.service_name.setText(Constant.service_name_history.get(position));
                holder.service_price.setText("₹ "+Constant.price_history.get(position));

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
