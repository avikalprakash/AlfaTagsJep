package com.organization.jeptagsalpha.Temperature;

import java.util.regex.Pattern;

/**
 * Created by lue on 23/1/18.
 */

public class Help {
    public static String uname="";
    public static String email="";
    public static String id="";
    public static String resultsText="";
public static final String DEVELOPER_KEY=" AIzaSyCOa3QtNJXq7vyfPt-8q_fnpxrIrdVNw3A ";
public static String Youtube_Code="";
    public static int ZINGSCANNERVIEW=0;
    public static int CAMERA_STATUS=0;
    public static String DATA="";
    public static boolean validEmail(String name){


        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(name).matches();
    }
}
