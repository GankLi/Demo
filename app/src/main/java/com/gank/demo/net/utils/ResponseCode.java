package com.gank.demo.net.utils;

/**
 * Created by GankLi on 2016/7/25.
 */

public class ResponseCode {
    public static final int UPLOAD_DATA_SUCCESS = 0;
    public static final int UPLOAD_DATA_FAILED = 1;

    public static final int QUERY_NO_USER = 100;
    public static final int QUERY_FAILED = 200;
    public static final int QUERY_NO_DEVICE = 201;

    public static final int QRCODE_PRINT_SUCCESS = 300;
    public static final int QRCODE_PRINT_FALIED = 301;

    public static final int QUERY_ORDER_FAILED = 401;

    public static final int NO_OP = 900;
    public static final int OP_FAILED = 911;

    public static final int AUTH_FAILED = -2;
    public static final int USER_DEVICE_NO_MATCH = -3;
    public static final int PRINT_OP_ERROR = -4; //
    public static final int USER_INVALID = -5;
    public static final int NOT_FIND_DEVICE = -1;
    public static final int DEVICE_NO = -7;
    public static final int PARAM_ERROR = -8;
    public static final int BARCODE_ERROR = -102;
    public static final int MUTIL_PRINT_NO_PRIFIX = -103;
}
