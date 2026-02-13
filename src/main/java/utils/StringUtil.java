package utils;

public class StringUtil {
    /**
     * 安全地将 Object 转换为 double
     * @param obj 要转换的对象
     * @param defaultValue 默认值
     * @return 转换后的 double 值
     */
    public static double safeConvertToDouble(Object obj, double defaultValue) {
        if (obj == null) {
            return defaultValue;
        }

        try {
            if (obj instanceof String) {
                return Double.parseDouble((String) obj);
            } else if (obj instanceof Number) {
                return ((Number) obj).doubleValue();
            } else {
                return Double.parseDouble(obj.toString());
            }
        } catch (NumberFormatException e) {
            LogUtils.info("Failed to convert to double: " + obj + ", using default value: " + defaultValue);
            return defaultValue;
        }
    }
}
