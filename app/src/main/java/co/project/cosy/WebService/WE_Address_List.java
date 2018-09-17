package co.project.cosy.WebService;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.project.cosy.Constant.Constant;

/**
 * Created by Saurabh Saini on 3/9/2018.
 */

public class WE_Address_List {

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
            String URL = Constant.WS_URL+""+Constant.list_addressWS;
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
                Constant.addressID.clear();
                Constant.address_type.clear();
                Constant.location.clear();
                Constant.flat_no.clear();
                Constant.landmark.clear();
                Constant.latitude.clear();
                Constant.longitude.clear();

                String plans= String.valueOf(jsonObject.get("addresses"));
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

                Constant.addressID.add(String.valueOf(jsonObject.get("addressID").toString()));
                Constant.address_type.add(String.valueOf(jsonObject.get("address_type").toString()));
                Constant.location.add(String.valueOf(jsonObject.get("location").toString()));
                Constant.flat_no.add(String.valueOf(jsonObject.get("flat_no").toString()));
                Constant.landmark.add(String.valueOf(jsonObject.get("landmark").toString()));
                Constant.latitude.add(String.valueOf(jsonObject.get("latitude").toString()));
                Constant.longitude.add(String.valueOf(jsonObject.get("longitude").toString()));

            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }


}
