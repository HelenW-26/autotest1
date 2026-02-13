package tools;

import java.util.*;

import org.apache.commons.codec.binary.Base64;

public class helentest {

    public static void main(String[] args) {


       //star

        HashMap<Long,String> userIdMap = new HashMap<>();
        userIdMap.put(2712643L,"199214");//Star
        userIdMap.put(211943L,"");//AU
        userIdMap.put(830429L,"830665");//UM
        userIdMap.put(2666174L,"");//VJP
        userIdMap.put(110140L,"");//PU

        Long userIdLong = 110140L;
        //MO
       /* Long userIdLong = 830429L;*/

        byte[] userIdBytes = userIdLong.toString().getBytes();
        String currentUserIdEncoded = new String(new Base64().encode(userIdBytes));
        String parentID = userIdMap.get(110140L);

        //String affid = "NB" + currentUserIdEncoded + "&am=" + parentID;
        String affid = "NB" + currentUserIdEncoded;

        System.out.println("currentUserIdEncoded: " + currentUserIdEncoded);
        System.out.println("affid: " + affid);




    /*    String notification = "Order filled\n" +
                "Buy 0.01 lots AUDUSD, at 0.65044.";
        String[] details = notification.split("\n")[1].split("[\\s,]");
        for(String str: details)
        {
            System.out.println("str" + str);
        }

        List<String> list = new ArrayList<>();

        list.add("postion");
        list.add("history");

        List<String> brand = Arrays.asList("vt","pu","au");

        System.out.println("brand1:" + brand.get(1));

        System.out.println("binary:" + Collections.binarySearch(brand, "au"));

        Collections.sort(brand);

        for(String s:brand)
        {
            System.out.println("s:" + s);
        }*/


    }
}
