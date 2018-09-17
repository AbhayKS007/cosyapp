package co.project.cosy.WebService;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.project.cosy.Constant.Constant;
import co.project.cosy.configuration.Configuration;

/**
 * Created by Saurabh Saini on 3/9/2018.
 */

public class WE_HomePage {

    Context context;

    public String sendDetail(Context ctx, String userData) {

        context=ctx;
        String result = callWS(userData);
        String finalvalue = validate(result);
        return finalvalue;
    }

    private String callWS(String jsondata) {
        String text = null;
        try {

            //http://www.gotgame.in/gotgame_app/signup.php?fullname=anand&email=anandpksjkjaadndey@gmail.com&password=linux@123&referrer=123&age=56&gender=male&gcm_id=12345
            String URL = Constant.WS_URL+""+Constant.Home_Page_WS;
            System.out.println("final url---->" + URL+"  and json object--"+jsondata);
            text = RestFullWS.serverRequest(URL, jsondata);
            System.out.println("gettin the respnse---->"+text);
        } catch (Exception e) {

        }
        return text;
    }

    public String validate(String strValue) {
        JSONParser jsonP = new JSONParser();
        String status = "";
        try {
            Object obj = jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            status = jsonObject.get("result").toString();

            if(status.equalsIgnoreCase("success"))
            {
                Constant.Home_Baner.clear();

                Constant.bookingID_upcoming.clear();

                Constant.passcode_upcoming.clear();
                Constant.vehicle_name_upcoming.clear();
                Constant.model_name_upcoming.clear();
                Constant.studioID_upcoming.clear();
                Constant.studio_name_upcoming.clear();
                Constant.studio_type_upcoming.clear();
                Constant.date_upcoming.clear();
                Constant.slot_upcoming.clear();


                Constant.amount_upcoming.clear();
                Constant.is_paid_upcoming.clear();
                Constant.payment_upcoming.clear();
                Constant.booking_upcoming.clear();
                Constant.address_upcoming.clear();


                Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.bookings, String.valueOf(jsonObject.get("bookings").toString()));

                String plans= String.valueOf(jsonObject.get("banners"));
                homeMethod(plans);


                String plans2= String.valueOf(jsonObject.get("booking_data"));
                Upcoming(plans2);

            }
            else {
            }

        }
        catch (Exception e) {

            e.printStackTrace();


        }
        return status;
    }
    public void homeMethod(String data)
    {
        System.out.println("data===>>"+data);
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(data);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.Home_Baner.add(String.valueOf(jsonObject.get("banner").toString()));

            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }


    public void Upcoming(String data)
    {
        System.out.println("data===>>"+data);
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(data);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                String reId = String.valueOf(jsonObject.get("bookingID").toString());

                Constant.bookingID_upcoming.add(String.valueOf(jsonObject.get("bookingID").toString()));
                Constant.passcode_upcoming.add(String.valueOf(jsonObject.get("passcode").toString()));
                Constant.vehicle_name_upcoming.add(String.valueOf(jsonObject.get("vehicle_name").toString()));
                Constant.studioID_upcoming.add(String.valueOf(jsonObject.get("studioID").toString()));
                Constant.studio_name_upcoming.add(String.valueOf(jsonObject.get("studio_name").toString()));
                Constant.studio_type_upcoming.add(String.valueOf(jsonObject.get("studio_type").toString()));
                Constant.date_upcoming.add(String.valueOf(jsonObject.get("date").toString()));
                Constant.slot_upcoming.add(String.valueOf(jsonObject.get("slot").toString()));

                Constant.amount_upcoming.add(String.valueOf(jsonObject.get("amount").toString()));
                Constant.is_paid_upcoming.add(String.valueOf(jsonObject.get("is_paid").toString()));
                Constant.payment_upcoming.add(String.valueOf(jsonObject.get("payment_mode").toString()));
                Constant.booking_upcoming.add(String.valueOf(jsonObject.get("booking_status").toString()));
                Constant.address_upcoming.add(String.valueOf(jsonObject.get("address").toString()));


                Constant.UPCOMING_BOOKING_SERVICE.put(reId,String.valueOf(jsonObject.get("services").toString()));

            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }

}
