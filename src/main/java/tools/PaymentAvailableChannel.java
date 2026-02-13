package tools;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import newcrm.cpapi.CPAPIBase;

import java.io.FileWriter;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentAvailableChannel {

    public static void main(String[] args) {

        //Provide all the brand info
        HashMap<String, List<String>> brands = new HashMap<>();
        brands.put("VFX", Arrays.asList(
                "https://secure.vantagemarkets.com/",
                "helentestcrmvantage@mailinator.com",
                "1234Qwer!"));

        //Provide all the countries
        List<String> countries = Arrays.asList("MY", "CN");

        //Output the result to file
        String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\newcrm\\data\\PaymentChannelInfo"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss-SSS")) + ".txt";

        for (Map.Entry<String, List<String>> entry : brands.entrySet()) {
            List<String> v = entry.getValue();
            getChannelInfo(entry.getKey(), v.get(0), v.get(1), v.get(2),countries,filePath);
        }
    }

    public static void getChannelInfo(String brand,String traderURL,String user,String password,List<String> countries,String filePath)
    {
        //login
        CPAPIBase api = new CPAPIBase(traderURL, user, password);

        try {
            //create file
            FileWriter writer = new FileWriter(filePath,true);
            writer.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss-SSS"))+"\n");

            //print channel info for each country
            for (String country : countries) {

                String path = "/web-api/cp/api/deposit/cps/availableChannel?country=" + country;

                //get actual data from api response
                JSONObject apiResult = api.sendCPAPIGETrequest(path);
                JsonArray ConvertedArray = JsonParser.parseReader(new StringReader(
                        loadJsonFromString(apiResult.getJSONObject("data").toString()))).getAsJsonObject().getAsJsonArray("channels").getAsJsonArray();

                writer.append(String.format("\n\n\n\n\nStart Processing for country %s\n",country));
                writer.append(String.format("Brand: %s TraderURL %s \n", brand, traderURL));
                writer.append(String.format("\nThere are %d channels for %s \n", ConvertedArray.size(), country));

                for (JsonElement element : ConvertedArray) {

                    writer.append(String.format("Payment method: %s Name: %s \n", element.getAsJsonObject().get("payment_method").toString(), element.getAsJsonObject().get("name").toString()));

                    JsonArray limit = element.getAsJsonObject().get("merchant_variable").getAsJsonObject().get("limit").getAsJsonArray();

                    for (JsonElement limitEle : limit) {
                        writer.append(String.format("account_currency: %s min: %s max: %s \n", limitEle.getAsJsonObject().get("account_currency"),
                                limitEle.getAsJsonObject().get("min"), limitEle.getAsJsonObject().get("max")));
                    }
                    writer.append("---------------------------------------------\n");
                }

            }
            writer.append("<br><br><br>");
            writer.flush();
        }
        catch(Exception e)
        {
            System.out.println("Write file exception: " + e);
        }
    }

    //Convert to proper json
    public static String loadJsonFromString(String strJson) {
            String sJson = strJson;
            sJson = sJson.replaceAll("\\\\\"", "\"");   // \"  => "
            sJson = sJson.replaceAll("\"\\{", "{");     // "{  => {
            sJson = sJson.replaceAll("}\"", "}");       // }"  => }
            sJson = sJson.replaceAll("} \"", "}"); // special case for "processing_time":"onehour"} ", } "  => }

            return sJson;
    }
}
