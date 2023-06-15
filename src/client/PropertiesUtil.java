package client;

import java.util.ResourceBundle;

/**
 * Created by xgzhang on 2023/6/15.
 */
public class PropertiesUtil {
    private PropertiesUtil() {
        throw new AssertionError();
    }

    public static String getProperty(String key) {
        String val = getResourceBundle().getString(key);
        return val;
    }

    private static ResourceBundle getResourceBundle() {
        //config为属性文件名，放在包com.test.config下，如果是放在src下，直接用config即可
        ResourceBundle resource = ResourceBundle.getBundle("config");
        return resource;
    }

//    public static void main(String[] args) {
//        String property = PropertiesUtil.getProperty("date");
//        System.out.println("property = " + property);
//    }
}
