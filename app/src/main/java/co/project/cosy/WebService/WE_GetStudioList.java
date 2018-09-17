package co.project.cosy.WebService;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.project.cosy.Constant.Constant;

/**
 * Created by Saurabh Saini on 3/9/2018.
 */

public class WE_GetStudioList {

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
            String URL = Constant.WS_URL+""+Constant.slot_WS;
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
            {Constant.Home_Baner.clear();

            Constant.period.clear();
            Constant.slotID.clear();
               // Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.bookings, String.valueOf(jsonObject.get("bookings").toString()));

                String plans= String.valueOf(jsonObject.get("slot"));
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
                Constant.period.add(String.valueOf(jsonObject.get("period").toString()));
                Constant.slotID.add(String.valueOf(jsonObject.get("slotID").toString()));

            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }
}
