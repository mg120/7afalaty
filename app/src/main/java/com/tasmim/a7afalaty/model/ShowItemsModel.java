package com.tasmim.a7afalaty.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ma7MouD on 10/11/2018.
 */

public class ShowItemsModel {

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

    //-------------------------------------------------------

    public class Datum implements Parcelable{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("section_id")
        @Expose
        private Integer sectionId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("details")
        @Expose
        private String details;
        @SerializedName("stars")
        @Expose
        private Integer stars;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("views")
        @Expose
        private Integer views;

        protected Datum(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            if (in.readByte() == 0) {
                sectionId = null;
            } else {
                sectionId = in.readInt();
            }
            if (in.readByte() == 0) {
                userId = null;
            } else {
                userId = in.readInt();
            }
            title = in.readString();
            lat = in.readString();
            lng = in.readString();
            image = in.readString();
            details = in.readString();
            if (in.readByte() == 0) {
                stars = null;
            } else {
                stars = in.readInt();
            }
            status = in.readString();
            createdAt = in.readString();
            updatedAt = in.readString();
            if (in.readByte() == 0) {
                views = null;
            } else {
                views = in.readInt();
            }
        }

        public final Creator<Datum> CREATOR = new Creator<Datum>() {
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

        public Integer getSectionId() {
            return sectionId;
        }

        public void setSectionId(Integer sectionId) {
            this.sectionId = sectionId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Integer getStars() {
            return stars;
        }

        public void setStars(Integer stars) {
            this.stars = stars;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public Integer getViews() {
            return views;
        }

        public void setViews(Integer views) {
            this.views = views;
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
            if (sectionId == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(sectionId);
            }
            if (userId == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(userId);
            }
            parcel.writeString(title);
            parcel.writeString(lat);
            parcel.writeString(lng);
            parcel.writeString(image);
            parcel.writeString(details);
            if (stars == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(stars);
            }
            parcel.writeString(status);
            parcel.writeString(createdAt);
            parcel.writeString(updatedAt);
            if (views == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(views);
            }
        }
    }
}
