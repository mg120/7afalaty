package com.tasmim.a7afalaty.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ma7MouD on 10/28/2018.
 */

public class ContactModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    //////////////////////////////////
    public class Data {

        @SerializedName("site_email")
        @Expose
        private String siteEmail;
        @SerializedName("site_phone")
        @Expose
        private String sitePhone;
        @SerializedName("site_fax")
        @Expose
        private String siteFax;

        public String getSiteEmail() {
            return siteEmail;
        }

        public void setSiteEmail(String siteEmail) {
            this.siteEmail = siteEmail;
        }

        public String getSitePhone() {
            return sitePhone;
        }

        public void setSitePhone(String sitePhone) {
            this.sitePhone = sitePhone;
        }

        public String getSiteFax() {
            return siteFax;
        }

        public void setSiteFax(String siteFax) {
            this.siteFax = siteFax;
        }
    }
}