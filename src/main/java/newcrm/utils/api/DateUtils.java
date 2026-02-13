package newcrm.utils.api;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 根据指定的年龄+天数获取出生日期
     * @param age
     * @param days
     * @return
     */
    public static String getDateFromAge(int age,int days){
        LocalDate current = LocalDate.now();
        return current.minusYears(age).minusDays(days).toString();
    }


    /**
     * 指定日期类型，获取当天的日期或者时间
     * @return
     */
    public static String getDate_yyyyMMDD(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(date);
        return  currentDate;
    }

    /**
     * 指定日期类型，获取当天的日期或者时间
     * @return
     */
    public static String getDateTime_HHMMSS(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = sdf.format(date);
        return  currentDate;
    }

    /**
     * 指定日期类型，获取当天的日期或者时间
     * @return
     */
    public static String getDateTimeStr(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmssSSS");
        String currentDate = sdf.format(date);
        return  currentDate;
    }


    /**
     * 获取当天的日期，返回 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateTimeBeforeMonth(int month){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH,month);
        Date before = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateBefore = sdf.format(before);
        return dateBefore;
    }

    public static void main(String[] args) {
//        LocalDate current = LocalDate.now();
//        LocalDate birthday = current.minusYears(18).minusDays(-5);
//        System.out.println(getDate_yyyyMMDD());
//        System.out.println(getDateTimeBeforeMonth(-3));
        System.out.println(getDateTimeStr());
    }
}
