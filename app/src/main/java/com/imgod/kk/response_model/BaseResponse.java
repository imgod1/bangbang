package com.imgod.kk.response_model;

/**
 * BaseResponse.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/7/16 10:49
 * @update imgod1 2018/7/16 10:49
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class BaseResponse {

    /**
     * status : Failure
     * msg : 该面值已被领完,请尝试其它面值！
     */

    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
