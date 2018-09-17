package co.project.cosy.WebService;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.project.cosy.Constant.Constant;

/**
 * Created by Saurabh Saini on 3/9/2018.
 */

public class WE_Booking_History {

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
            String URL = Constant.WS_URL+""+Constant.booking_historyWS;
            System.out.println("final url---->" + URL+"  and json object--"+jsondata);
            text = RestFullWS.serverRequest(URL, jsondata);
            System.out.println("gettin the respnse---->"+text);
        }
        catch (Exception e) {
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

                Constant.bookingID_history.clear();

                Constant.passcode_history.clear();
                Constant.vehicle_name_history.clear();
                Constant.model_name_history.clear();
                Constant.studioID_history.clear();
                Constant.studio_name_history.clear();
                Constant.studio_type_history.clear();
                Constant.date_history.clear();
                Constant.slot_history.clear();


                Constant.amount_history.clear();
                Constant.is_paid_history.clear();
                Constant.payment_history.clear();
                Constant.booking_history.clear();
                Constant.address_history.clear();


                String plans= String.valueOf(jsonObject.get("bookings"));
                homeMethod(plans);
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

                String reId = String.valueOf(jsonObject.get("bookingID").toString());

                Constant.bookingID_history.add(String.valueOf(jsonObject.get("bookingID").toString()));
                Constant.passcode_history.add(String.valueOf(jsonObject.get("passcode").toString()));
                Constant.vehicle_name_history.add(String.valueOf(jsonObject.get("vehicle_name").toString()));
                Constant.studioID_history.add(String.valueOf(jsonObject.get("studioID").toString()));
                Constant.studio_name_history.add(String.valueOf(jsonObject.get("studio_name").toString()));
                Constant.studio_type_history.add(String.valueOf(jsonObject.get("studio_type").toString()));
                Constant.date_history.add(String.valueOf(jsonObject.get("date").toString()));
                Constant.slot_history.add(String.valueOf(jsonObject.get("slot").toString()));


                Constant.amount_history.add(String.valueOf(jsonObject.get("amount").toString()));
                Constant.is_paid_history.add(String.valueOf(jsonObject.get("is_paid").toString()));
                Constant.payment_history.add(String.valueOf(jsonObject.get("payment_mode").toString()));
                Constant.booking_history.add(String.valueOf(jsonObject.get("booking_status").toString()));
                Constant.address_history.add(String.valueOf(jsonObject.get("address").toString()));


                Constant.BOOKING_HISTORY_SERVICE.put(reId,String.valueOf(jsonObject.get("services").toString()));


            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }


}
