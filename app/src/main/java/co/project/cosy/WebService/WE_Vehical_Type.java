package co.project.cosy.WebService;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.project.cosy.Constant.Constant;

/**
 * Created by Saurabh Saini on 3/9/2018.
 */

public class WE_Vehical_Type {

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
            String URL = Constant.WS_URL+""+Constant.list_vehicleWS;
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
                Constant.vehicleID.clear();
                Constant.vehicle_type.clear();
                Constant.year.clear();
                Constant.color.clear();
                Constant.brand_name.clear();
                Constant.model_name.clear();
                Constant.user_vehicleID.clear();
                Constant.modelID_manage.clear();

                String plans= String.valueOf(jsonObject.get("vehicles"));
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

               Constant.vehicleID.add(String.valueOf(jsonObject.get("vehicleID").toString()));
                Constant.vehicle_type.add(String.valueOf(jsonObject.get("vehicle_type").toString()));
                Constant.year.add(String.valueOf(jsonObject.get("year").toString()));
                Constant.color.add(String.valueOf(jsonObject.get("color").toString()));
                Constant.brand_name.add(String.valueOf(jsonObject.get("brand_name").toString()));
                Constant.model_name.add(String.valueOf(jsonObject.get("model_name").toString()));
                Constant.user_vehicleID.add(String.valueOf(jsonObject.get("user_vehicleID").toString()));
                Constant.modelID_manage.add(String.valueOf(jsonObject.get("modelID").toString()));


            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }


}
