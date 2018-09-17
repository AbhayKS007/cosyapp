package co.project.cosy.WebService;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.project.cosy.Constant.Constant;

/**
 * Created by Saurabh Saini on 3/9/2018.
 */

public class WE_GetTimeSloat {

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
            String URL = Constant.WS_URL+""+Constant.list_studio_WS;
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
                Constant.studioID_list.clear();
                Constant.name_list.clear();
                Constant.image_list.clear();
                Constant.address_list.clear();
                Constant.latitude_list.clear();
                Constant.longitude_list.clear();
                Constant.studio_type_list.clear();
                Constant.review_list.clear();

                Constant.contact.clear();

                String plans= String.valueOf(jsonObject.get("studios"));
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
                Constant.studioID_list.add(String.valueOf(jsonObject.get("studioID").toString()));
                Constant.name_list.add(String.valueOf(jsonObject.get("name").toString()));
                Constant.image_list.add(String.valueOf(jsonObject.get("image").toString()));
                Constant.address_list.add(String.valueOf(jsonObject.get("address").toString()));
                Constant.latitude_list.add(String.valueOf(jsonObject.get("latitude").toString()));
                Constant.longitude_list.add(String.valueOf(jsonObject.get("longitude").toString()));
                Constant.studio_type_list.add(String.valueOf(jsonObject.get("studio_type").toString()));
                Constant.review_list.add(String.valueOf(jsonObject.get("review").toString()));
                Constant.contact.add(String.valueOf(jsonObject.get("contact").toString()));





            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }
}
