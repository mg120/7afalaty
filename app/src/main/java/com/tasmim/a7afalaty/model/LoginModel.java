package com.tasmim.a7afalaty.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ma7MouD on 10/10/2018.
 */

public class LoginModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    ///////////////////////////////////
    public static class Data implements Parcelable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("code")
        @Expose
        private Object code;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("active")
        @Expose
        private Integer active;
        @SerializedName("checked")
        @Expose
        private Integer checked;
        @SerializedName("banned")
        @Expose
        private Integer banned;
        @SerializedName("type")
        @Expose
        private Integer type;
        @SerializedName("role")
        @Expose
        private Integer role;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("long")
        @Expose
        private String _long;
        @SerializedName("remember_token")
        @Expose
        private String rememberToken;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        protected Data(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            name = in.readString();
            email = in.readString();
            phone = in.readString();
            avatar = in.readString();
            if (in.readByte() == 0) {
                active = null;
            } else {
                active = in.readInt();
            }
            if (in.readByte() == 0) {
                checked = null;
            } else {
                checked = in.readInt();
            }
            if (in.readByte() == 0) {
                banned = null;
            } else {
                banned = in.readInt();
            }
            if (in.readByte() == 0) {
                type = null;
            } else {
                type = in.readInt();
            }
            if (in.readByte() == 0) {
                role = null;
            } else {
                role = in.readInt();
            }
            lat = in.readString();
            _long = in.readString();
            rememberToken = in.readString();
            createdAt = in.readString();
            updatedAt = in.readString();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Integer getActive() {
            return active;
        }

        public void setActive(Integer active) {
            this.active = active;
        }

        public Integer getChecked() {
            return checked;
        }

        public void setChecked(Integer checked) {
            this.checked = checked;
        }

        public Integer getBanned() {
            return banned;
        }

        public void setBanned(Integer banned) {
            this.banned = banned;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getRole() {
            return role;
        }

        public void setRole(Integer role) {
            this.role = role;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLong() {
            return _long;
        }

        public void setLong(String _long) {
            this._long = _long;
        }

        public String getRememberToken() {
            return rememberToken;
        }

        public void setRememberToken(String rememberToken) {
            this.rememberToken = rememberToken;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            if (id == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(id);
            }
            parcel.writeString(name);
            parcel.writeString(email);
            parcel.writeString(phone);
            parcel.writeString(avatar);
            if (active == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(active);
            }
            if (checked == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(checked);
            }
            if (banned == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(banned);
            }
            if (type == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(type);
            }
            if (role == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(role);
            }
            parcel.writeString(lat);
            parcel.writeString(_long);
            parcel.writeString(rememberToken);
            parcel.writeString(createdAt);
            parcel.writeString(updatedAt);
        }
    }
}
