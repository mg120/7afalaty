package com.tasmim.a7afalaty.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ma7MouD on 10/24/2018.
 */

public class MyBookingModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    // ---------------------------------
    public static class Datum implements Parcelable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("service_id")
        @Expose
        private Integer serviceId;
        @SerializedName("service_name")
        @Expose
        private Integer serviceName;
        @SerializedName("service_image")
        @Expose
        private String serviceImage;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("disc")
        @Expose
        private String disc;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        protected Datum(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            if (in.readByte() == 0) {
                userId = null;
            } else {
                userId = in.readInt();
            }
            userName = in.readString();
            if (in.readByte() == 0) {
                serviceId = null;
            } else {
                serviceId = in.readInt();
            }
            if (in.readByte() == 0) {
                serviceName = null;
            } else {
                serviceName = in.readInt();
            }
            serviceImage = in.readString();
            if (in.readByte() == 0) {
                status = null;
            } else {
                status = in.readInt();
            }
            date = in.readString();
            name = in.readString();
            phone = in.readString();
            disc = in.readString();
            createdAt = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(id);
            }
            if (userId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(userId);
            }
            dest.writeString(userName);
            if (serviceId == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(serviceId);
            }
            if (serviceName == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(serviceName);
            }
            dest.writeString(serviceImage);
            if (status == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(status);
            }
            dest.writeString(date);
            dest.writeString(name);
            dest.writeString(phone);
            dest.writeString(disc);
            dest.writeString(createdAt);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Datum> CREATOR = new Creator<Datum>() {
            @Override
            public Datum createFromParcel(Parcel in) {
                return new Datum(in);
            }

            @Override
            public Datum[] newArray(int size) {
                return new Datum[size];
            }
        };

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
        }

        public Integer getServiceName() {
            return serviceName;
        }

        public void setServiceName(Integer serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(String serviceImage) {
            this.serviceImage = serviceImage;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDisc() {
            return disc;
        }

        public void setDisc(String disc) {
            this.disc = disc;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }
}
