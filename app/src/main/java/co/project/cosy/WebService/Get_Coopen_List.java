package co.project.cosy.WebService;

import android.content.Context;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import co.project.cosy.Constant.Constant;

public class Get_Coopen_List {
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

            String URL = Constant.WS_URL+""+Constant.WS_Coupen;
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

                Constant.COUPEN_CODE.clear();
                Constant.COUPEN_TYPE.clear();
                Constant.COUPEN_AMOUNT.clear();
                Constant.COUPAN_DESCRIPTION.clear();
                String cropping_demonstration= String.valueOf(jsonObject.get("coupon"));
                cropping_demonstration(cropping_demonstration);

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

                Constant.COUPEN_CODE.add(jsonObject.get("coupon_code").toString());
                Constant.COUPEN_TYPE.add(jsonObject.get("coupon_type").toString());
                Constant.COUPEN_AMOUNT.add(jsonObject.get("value").toString());
                Constant.COUPAN_DESCRIPTION.add(jsonObject.get("description").toString());
                System.out.println("COUPEN_CODE "+Constant.COUPAN_DESCRIPTION.toString());

            }

        }
        catch (Exception e) {
            System.out.println("in second catch block");
            e.printStackTrace();
        }

    }

}
