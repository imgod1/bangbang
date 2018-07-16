package com.imgod.kk.response_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * GetTaskResponse.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/7/16 11:11
 * @update imgod1 2018/7/16 11:11
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class GetTaskResponse extends BaseResponse {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amount : 49.75
         * appCode : mobilefee
         * balanceHis : -16.0
         * clearStatus : NA
         * createTime : 20180716111000
         * discount : 0.985
         * expectInPrice : 49.25
         * expireTime : 20180716112500
         * expireTime2 : 20180717111000
         * finishType : 0
         * id : 32933101
         * inPrice : 49.25
         * mobile : 13562220638
         * oper : CHINAMOBILE
         * operName : 移动
         * orderId : 329331
         * parentUserId : 103643
         * parval : 50
         * preBalance : -16
         * province : SD
         * provinceName : 山东
         * queryCount : 0
         * status : 90
         * submitStatus : NA
         * userId : 103700
         */

        private double amount;
        private String appCode;
        private String balanceHis;
        private String clearStatus;
        private String createTime;
        private double discount;
        private double expectInPrice;
        private String expireTime;
        private String expireTime2;
        private int finishType;
        private String id;
        private double inPrice;
        private String mobile;
        private String oper;
        private String operName;
        private String orderId;
        private String parentUserId;
        private int parval;
        private double preBalance;
        private String province;
        private String provinceName;
        private int queryCount;
        @SerializedName("status")
        private String statusX;
        private String submitStatus;
        private String userId;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getAppCode() {
            return appCode;
        }

        public void setAppCode(String appCode) {
            this.appCode = appCode;
        }

        public String getBalanceHis() {
            return balanceHis;
        }

        public void setBalanceHis(String balanceHis) {
            this.balanceHis = balanceHis;
        }

        public String getClearStatus() {
            return clearStatus;
        }

        public void setClearStatus(String clearStatus) {
            this.clearStatus = clearStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getExpectInPrice() {
            return expectInPrice;
        }

        public void setExpectInPrice(double expectInPrice) {
            this.expectInPrice = expectInPrice;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public String getExpireTime2() {
            return expireTime2;
        }

        public void setExpireTime2(String expireTime2) {
            this.expireTime2 = expireTime2;
        }

        public int getFinishType() {
            return finishType;
        }

        public void setFinishType(int finishType) {
            this.finishType = finishType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getInPrice() {
            return inPrice;
        }

        public void setInPrice(double inPrice) {
            this.inPrice = inPrice;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getOper() {
            return oper;
        }

        public void setOper(String oper) {
            this.oper = oper;
        }

        public String getOperName() {
            return operName;
        }

        public void setOperName(String operName) {
            this.operName = operName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getParentUserId() {
            return parentUserId;
        }

        public void setParentUserId(String parentUserId) {
            this.parentUserId = parentUserId;
        }

        public int getParval() {
            return parval;
        }

        public void setParval(int parval) {
            this.parval = parval;
        }

        public double getPreBalance() {
            return preBalance;
        }

        public void setPreBalance(double preBalance) {
            this.preBalance = preBalance;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public int getQueryCount() {
            return queryCount;
        }

        public void setQueryCount(int queryCount) {
            this.queryCount = queryCount;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public String getSubmitStatus() {
            return submitStatus;
        }

        public void setSubmitStatus(String submitStatus) {
            this.submitStatus = submitStatus;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
