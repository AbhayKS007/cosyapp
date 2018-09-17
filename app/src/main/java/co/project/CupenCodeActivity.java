package co.project;

import android.app.Dialog;
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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import co.project.cosy.Constant.Constant;
import co.project.cosy.ErrorScreen;
import co.project.cosy.R;
import co.project.cosy.WebService.Get_Coopen_List;
import co.project.cosy.configuration.Configuration;
import me.anwarshahriar.calligrapher.Calligrapher;

public class CupenCodeActivity extends AppCompatActivity {
    RecyclerView myList;
    Dialog Loader;
    AdapterPassbook adapterForDownload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupen_code);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.miku_back);
        toolbar.setTitle("              ");
        setSupportActionBar(toolbar);

        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"Ubuntu-L.ttf",true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),drawer.class));
                onBackPressed();
            }
        });


        myList=(RecyclerView)findViewById(R.id.offer_page_recyclerview);
        myList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myList.setLayoutManager(llm);

        cupenWebServiceCall();

    }

    public void cupenWebServiceCall() {

        if (Configuration.isInternetConnection(getApplicationContext())) {

            String jsonData = toJSon("1");
            new GET_DATA().execute(jsonData);
        }
        else {
            Intent intent = new Intent(CupenCodeActivity.this, ErrorScreen.class);
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
            NamePopUp();
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
                    adapterForDownload=new AdapterPassbook(getApplicationContext());
                    myList.setAdapter(adapterForDownload);
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
                Loader.dismiss();
            }
        }
    }


    public  void  NamePopUp() {

        Loader = new Dialog(CupenCodeActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        Loader.setContentView(R.layout.loder);
        Loader.show();

    }


    public class AdapterPassbook extends RecyclerView.Adapter<AdapterPassbook.ViewHolder> {

        Context mContext;

        public AdapterPassbook(Context context) {
            this.mContext = context;

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView code,description;

            public ViewHolder(View itemView) {
                super(itemView);

                code = itemView.findViewById(R.id.code);
                description = itemView.findViewById(R.id.description);

            }
        }
        @Override
        public int getItemCount() {

            int i=0;
            try{

                i= Constant.COUPEN_CODE.size();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cupen_card, parent, false);
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

                holder.code.setText(Constant.COUPEN_CODE.get(position));
                holder.description.setText(Constant.COUPAN_DESCRIPTION.get(position));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
