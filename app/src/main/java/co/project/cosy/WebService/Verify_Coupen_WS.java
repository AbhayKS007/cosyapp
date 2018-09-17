package co.project.cosy.WebService;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import co.project.cosy.Constant.Constant;


/**
 * Created by Appslure on 11/28/2017.
 */

public class Verify_Coupen_WS {
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

            String URL = Constant.WS_URL+""+Constant.WS_Coupen_Appled;
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


            }

        }
        catch (Exception e) {

            e.printStackTrace();

        }
        return status;
    }


    public void cropping_demonstration(String data)
    {
        System.out.println("data===>>"+data);
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(data);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.COUPEN_CODE.add(jsonObject.get("offer_code").toString());
                Constant.COUPEN_TYPE.add(jsonObject.get("offer_type").toString());
                Constant.COUPEN_AMOUNT.add(jsonObject.get("value").toString());

                System.out.println("COUPEN_CODE "+Constant.COUPEN_CODE.toString());

            }

        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }

    }

}
