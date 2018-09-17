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

public class WE_Service_Type {

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
            String URL = Constant.WS_URL+""+Constant.service_type_detailWS;
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

                Constant.media_name.clear();

                Constant.media_type.clear();
                Constant.SERVICE_ID.clear();
                Constant.service_name.clear();
                Constant.service_description.clear();
                Constant.min_price.clear();
                Constant.addon_title.clear();
                Constant.addon_description.clear();

                String plans= String.valueOf(jsonObject.get("detail"));
                homeMethod("["+plans+"]");
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
           //   Constant.Home_Baner.add(String.valueOf(jsonObject.get("banner").toString()));
                Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.title, String.valueOf(jsonObject.get("title").toString()));
                Configuration.setSharedPrefrenceValue(context, Constant.PREFS_NAME,Constant.description, String.valueOf(jsonObject.get("description").toString()));


                String plans= String.valueOf(jsonObject.get("media"));
                media(plans);



                String serv= String.valueOf(jsonObject.get("services"));
                services(serv);


            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }



    public void media(String data)
    {
        System.out.println("data===>>"+data);
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(data);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                   Constant.media_name.add(String.valueOf(jsonObject.get("media_name").toString()));
                Constant.media_type.add(String.valueOf(jsonObject.get("media_type").toString()));

                Constant.video.add(String.valueOf(jsonObject.get("video").toString()));

            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }



    public void services(String data)
    {
        System.out.println("data===>>"+data);
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(data);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                String reId = String.valueOf(jsonObject.get("serviceID").toString());

                Constant.SERVICE_ID.add(String.valueOf(jsonObject.get("serviceID").toString()));
                Constant.service_name.add(String.valueOf(jsonObject.get("service_name").toString()));
                Constant.service_description.add(String.valueOf(jsonObject.get("description").toString()));
                Constant.min_price.add(String.valueOf(jsonObject.get("min_price").toString()));

                Constant.SERVICE_MEDIA.put(reId,String.valueOf(jsonObject.get("service_media").toString()));
                Constant.SERVICE_PRICE.put(reId,String.valueOf(jsonObject.get("prices").toString()));
                Constant.SERVICE_ADDON.put(reId,String.valueOf(jsonObject.get("addon").toString()));

                Constant.addon_title.add(String.valueOf(jsonObject.get("addon_title").toString()));
                Constant.addon_description.add(String.valueOf(jsonObject.get("addon_description").toString()));

            }
        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }
    }


}
