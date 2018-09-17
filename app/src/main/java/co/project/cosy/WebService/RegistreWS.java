package co.project.cosy.WebService;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import co.project.cosy.Constant.Constant;
import co.project.cosy.configuration.Configuration;

/**
 * Created by Appslure on 11/28/2017.
 */
public class RegistreWS {
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

            String URL = Constant.WS_URL +""+Constant.WS_CHECK_USER_SINGUP;
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
                Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.OTP, String.valueOf(jsonObject.get("otp").toString()));
                Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.MESSAGE, String.valueOf(jsonObject.get("message").toString()));
            }

           else if(status.equalsIgnoreCase("email_exit"))
            {
               // Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.OTP, String.valueOf(jsonObject.get("otp").toString()));
                Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.MESSAGE, String.valueOf(jsonObject.get("message").toString()));

            }

           else if(status.equalsIgnoreCase("mobile_exit"))
            {
                // Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.OTP, String.valueOf(jsonObject.get("otp").toString()));
                Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.MESSAGE, String.valueOf(jsonObject.get("message").toString()));

            }
        }
        catch (Exception e) {

            e.printStackTrace();

        }
        return status;
    }

}
