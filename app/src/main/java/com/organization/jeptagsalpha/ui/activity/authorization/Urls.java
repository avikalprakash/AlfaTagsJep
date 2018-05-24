package com.organization.jeptagsalpha.ui.activity.authorization;

/**
 * Created by lue on 30-08-2017.
 */

public class Urls {

    public static String serverAddress = "http://54.67.107.248/jeptags";

    public static String get_brands = serverAddress + "/apirest/get_brands";
    public static String get_conditions = serverAddress + "/apirest/get_conditions";
    public static String get_subcategories = serverAddress + "/apirest/get_subcategories";
    public static String get_seller_categories = serverAddress + "/apirest/get_seller_categories";
    public static String get_seller_store_address = serverAddress + "/apirest/get_seller_store_address";
    public static String add_products = serverAddress + "/apirest/add_products";
    public static String get_status = serverAddress + "/apirest/get_status";
    public static String seller_products_list = serverAddress + "/apirest/seller_products";
    public static String login_seller = serverAddress + "/apirest/login_seller";
    public static String check_seller_default_address = serverAddress + "/apirest/check_seller_default_address";
    public static String update_store_address = serverAddress + "/apirest/update_store_address";
    public static String get_seller_store = serverAddress + "/apirest/get_seller_store";
    public static String register_seller = serverAddress + "/apirest/register_seller";
    public static String forgot_password_seller = serverAddress + "/apirest/forgot_password_seller";
    public static String update_password_seller = serverAddress + "/apirest/update_password_seller";
    public static String delete_products = serverAddress + "/apirest/delete_products";
    public static String encoding_verify = serverAddress + "/apirest/encoding/verify";
    public static String encoding_get_product_tags = serverAddress + "/apirest/encoding/get_product_tags";

    public static String pending_product_list = serverAddress + "/apirest/seller_products_pending";
    public static String approved_product_list = serverAddress + "/apirest/seller_products_approved";
    public static String temp_list = serverAddress + "/apirest/encoding/temp_record_list";
    public static String report = serverAddress + "/apirest/report";

}
