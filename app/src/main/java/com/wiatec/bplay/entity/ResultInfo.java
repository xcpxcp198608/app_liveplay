package com.wiatec.bplay.entity;

import java.util.List;

/**
 * the result info from server
 */

public class ResultInfo<T> {

    /**
     * request success (GET method)
     */
    public static final int CODE_OK = 200;

    /**
     * create or update success (POST(insert) / PUT (update) method)
     */
    public static final int CODE_CREATED = 201;

    /**
     * delete data success (DELETE method)
     */
    public static final int CODE_DELETED = 204;

    /**
     * request operate failure (GET , POST , PUT , DELETE method)
     */
    public static final int CODE_INVALID = 400;

    /**
     * user permission error (UserName , Password , Token error)
     */
    public static final int CODE_UNAUTHORIZED = 401;

    /**
     * request resource not exists (GET)
     */
    public static final int CODE_NO_FOUND = 404;

    /**
     * server error
     */
    public static final int CODE_SERVER_ERROR = 500;

    /**
     * request success (GET method)
     */
    public static final String STATUS_OK = "Request Success";

    /**
     * create or update success (POST(insert) / PUT (update) method)
     */
    public static final String STATUS_CREATED = "Request Success";

    /**
     * delete data success (DELETE method)
     */
    public static final String STATUS_DELETED = "Deleted Success";

    /**
     * request operate failure (GET , POST , PUT , DELETE method)
     */
    public static final String STATUS_INVALID = "Request Invalid";

    /**
     * user permission error (UserName , Password , Token error)
     */
    public static final String STATUS_UNAUTHORIZED = "Authorization Error";

    /**
     * request resource not exists (GET)
     */
    public static final String STATUS_NO_FOUND = "Resource no found";

    /**
     * server error
     */
    public static final String STATUS_SERVER_ERROR = "Server error, please try again later";

    /**
     *  status code
     */
    private int code;

    /**
     * status description
     */
    private String status;

    /**
     * custom message
     */
    private String message;

    /**
     * data
     */
    private List<T> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
