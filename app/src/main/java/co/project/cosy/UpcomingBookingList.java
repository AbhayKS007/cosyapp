package co.project.cosy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.project.cosy.Constant.Constant;

public class UpcomingBookingList extends AppCompatActivity {
    AdapterPassbook adapterForDownload;
    RecyclerView myList;

    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_booking_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("          ");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),drawer.class));
                onBackPressed();
            }
        });
        myList=(RecyclerView)findViewById(R.id.booking_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);

        nodata = findViewById(R.id.nodata);


        if(Constant.bookingID_upcoming.size()==0)
        {
            nodata.setVisibility(View.VISIBLE);
            myList.setVisibility(View.GONE);
        }
        else {
            adapterForDownload = new AdapterPassbook(getApplicationContext());
            myList.setAdapter(adapterForDownload);
        }

    }

    public class AdapterPassbook extends RecyclerView.Adapter<AdapterPassbook.ViewHolder> {

        Context mContext;
        String cityName,activityName;

        int stetus = 0;


        public AdapterPassbook(Context context) {
            this.mContext = context;

        }

        // 3
        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageblanck,imagered;

            TextView car_name,date,time,location,finalAmount,otp;

            LinearLayout main_layout;


            public ViewHolder(View itemView) {
                super(itemView);

                car_name = itemView.findViewById(R.id.car_name);
                date = itemView.findViewById(R.id.date);
                time = itemView.findViewById(R.id.time);
                location = itemView.findViewById(R.id.location);
                finalAmount = itemView.findViewById(R.id.finalAmount);
                otp = itemView.findViewById(R.id.otp);
                main_layout = itemView.findViewById(R.id.main_layout);
            }
        }
        @Override
        public int getItemCount() {

            int i=0;
            try{

                i= Constant.bookingID_upcoming.size();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_card, parent, false);
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
                holder.car_name.setText(Constant.vehicle_name_upcoming.get(position));
                holder.date.setText(Constant.date_upcoming.get(position));
                holder.time.setText(Constant.slot_upcoming.get(position));
                holder.location.setText(Constant.address_upcoming.get(position));
                holder.finalAmount.setText("₹"+Constant.amount_upcoming.get(position));
                holder.otp.setText(Constant.passcode_upcoming.get(position));

                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(UpcomingBookingList.this,UpcomingBookingDetails.class);
                        intent.putExtra("position",String.valueOf(position));
                        startActivity(intent);
                    }
                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
