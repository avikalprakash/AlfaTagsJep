package com.organization.jeptagsalpha;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import com.organization.jeptagsalpha.wordpress.rest.WpClientRetrofit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lord Ganesha on 28/09/2016.
 */

public class MyConstants {
    public static final boolean DEBUG_MODE = true;
    public static final String FONT_REG_PATH = "fonts/din_light.ttf";
    public static final String FONT_BOLD_PATH = "fonts/din_bold.ttf";
    private static final String CAMERA_FILE_NAME_PREFIX = "CAMERA_";
    private static final String CAMERA_FILE_EXT = ".jpg";
    public static final String ENC_KEY = "webstersys@gmail.com";

    public static String ROLE_EMPLOYEE = "employee";
    public static String ROLE_INTERNATIONAL = "international";
    public static String ROLE_DOMESTIC = "domestic";
    public static String ROLE_ADMIN = "admin";
    public static String NFC_TAG_CAT_ID = "6";
    public static String DEFAULT_IMAGE_ID = "Placeholder";
    public static String WordpressIP = "jeptags.com";
  //  public static String WordpressIP = "10.0.0.10";
   // public static String WordpressIP = "10.0.2.2";
    //public static String WordpressIP = "192.168.43.48"; //sagar hotspot
  //  public static String WordpressIP = "192.168.1.14"; //Airtel Primus
    //public static String WordpressIP = "192.168.0.3"; //Shraddha Home
   //  public static String WordpressIP = "192.168.0.103"; //My Home


    public static String TAG_ID = "TAG_ID";
    public static String DEFAULT_NFC_TAG_PROD_NAME = "NFC TAG";

    public static String ATTRIBUTE_CUST_ID = "1";
    public static String ATTRIBUTE_CUST_ID_NAME = "cust_id";
    public static int ATTRIBUTE_LOCATION_ID = 2;
  /*  public static String ATTRIBUTE_CUST_NAME = "";
    public static String ATTRIBUTE_CUST_EMAIL = "";
    public static String ATTRIBUTE_CUST_PHONE = "";
    public static String ATTRIBUTE_CUST_WEBSITE = "";
    public static String ATTRIBUTE_CUST_ADDRESS = "";*/

    //public static String WordpressBaseURL = "http://10.0.2.2/wordpress/wp-json/wc/v1/";

    public static String WC_BASE_URL = "http://"+WordpressIP+"/wp-json/wc/v1/";
    public static String WP_BASE_URL = "http://"+WordpressIP+"/wp-json/wp/v3/";
    public static String WP_BASE_ORIGNAL_URL = "http://"+WordpressIP+"/wp-json/wp/v2/";

/*    public static String WC_BASE_URL = "http://10.0.0.10/jeptags/api/login";
    public static String WP_BASE_URL = "http://10.0.0.10/jeptags/api/login";
    public static String WP_BASE_ORIGNAL_URL = "http://10.0.0.10/jeptags/api/login";*/


    public static final String SELLER_ID = "seller_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String NFCTAG_ID = "tag_id";
    public static final String PRODUCT_URL = "product_url";
    public static final String PRODUCT_QUANTITY = "qty";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_JEPTAG_LIST = "listCount";

    public static final String FIRSTNAME = "fname";
    public static final String LASTNAME = "lname";
    public static final String STORE_NAME = "storename";
    public static final String STORE_ID = "store_id";
    public static final String COMPANY_NAME = "company_name" ;
    public static final String CONTRACT_PERSON_NAME = "c_person_name";
    public static final String TELEPHONE = "telephone";
    public static final String FAX = "fax";
    public static final String STREET_ADDRESS = "s_address";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String POSTALCODE = "postal";
    public static final String COUNTRY = "country";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String ADDRESS = "address";
    public static final String BILLING = "is_default_billing";
    public static final String SHIPPING = "is_default_shipping";
    public static final String ADDRESS_ID = "entity_id";


    public static WpClientRetrofit WPService = null;
 ;
    public static WpClientRetrofit WOService = null;

    public static WpClientRetrofit WCService = null;


    public static String saveUriToFile(Uri uri) throws Exception {
        ParcelFileDescriptor parcelFileDescriptor = App.getInstance().getContentResolver()
                .openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        InputStream inputStream = new FileInputStream(fileDescriptor);
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        File parentDir = getAppExternalDataDirectoryFile();
        String fileName = String.valueOf(System.currentTimeMillis()) + CAMERA_FILE_EXT;
        File resultFile = new File(parentDir, fileName);

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(resultFile));

        byte[] buf = new byte[2048];
        int length;

        try {
            while ((length = bis.read(buf)) > 0) {
                bos.write(buf, 0, length);
            }
        } catch (Exception e) {
            throw new IOException("Can\'t save Storage API bitmap to a file!", e);
        } finally {
            parcelFileDescriptor.close();
            bis.close();
            bos.close();
        }

        return resultFile.getAbsolutePath();
    }
    public static String getAppExternalDataDirectoryPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory()).append(File.separator).append("Android")
                .append(File.separator).append("data").append(File.separator)
                .append(App.getInstance().getPackageName()).append(File.separator);

        return sb.toString();
    }

    public static File getAppExternalDataDirectoryFile() {
        File dataDirectoryFile = new File(getAppExternalDataDirectoryPath());
        dataDirectoryFile.mkdirs();

        return dataDirectoryFile;
    }
    public static String removeHtmlTags(String desc)
    {
        return android.text.Html.fromHtml(desc).toString();
    }

 /*   public static int convertDpToPixel(int dp){

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, App.getInstance().getResources().getDisplayMetrics());
        return px;

    }*/

    public static int convertPixelsToDp(int px){
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


}

/*{
        "username" : "ubheshital",
        "name" : "shital ubhe",
        "first_name" : "Shital",
        "last_name" : "Ubhe",
        "email" : "ubheshital@gmail.com",
        "password":"welcome123",
        "billing" :
        {
        "first_name" : "Shital",
        "last_name" : "Ubhe",
        "company": "Education"
        }
}*/
