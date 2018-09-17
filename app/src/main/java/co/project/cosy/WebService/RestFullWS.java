package co.project.cosy.WebService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RestFullWS {

    private final static String USER_AGENT = "Mozilla/5.0";

    public static String serverRequest(String _url, String jsonData) {

        StringBuilder params = new StringBuilder("");
        String result = "";
        try {
            String url = _url;
            URL obj = new URL(_url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            //add request header
            //con.setRequestProperty("User-Agent", USER_AGENT);
            //con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; text/html; charset=UTF-8");
            con.setDoOutput(true);
            con.setUseCaches(false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
            outputStreamWriter.write(jsonData);
            //outputStreamWriter.write(URLEncoder.encode(jsonData.toString(),"UTF-8"));
            outputStreamWriter.flush();


            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + jsonData);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            result = response.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }


    }
}
