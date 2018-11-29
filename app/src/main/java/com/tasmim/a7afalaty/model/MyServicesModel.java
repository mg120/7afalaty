package com.tasmim.a7afalaty.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ma7MouD on 10/25/2018.
 */

public class MyServicesModel {
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

    // --------------------------------------
    public static class Datum implements Parcelable{

        @SerializedName("id")
        @Expose
        private Integer id;
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
        @SerializedName("disc")
        @Expose
        private String disc;
        @SerializedName("stars")
        @Expose
        private Integer stars;
        @SerializedName("views")
        @Expose
        private Integer views;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_phone")
        @Expose
        private String userPhone;
        @SerializedName("user_email")
        @Expose
        private String userEmail;
        @SerializedName("user_avatar")
        @Expose
        private String userAvatar;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("comments")
        @Expose
        private List<Comment> comments = null;

        protected Datum(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            title = in.readString();
            lat = in.readString();
            lng = in.readString();
            image = in.readString();
            disc = in.readString();
            if (in.readByte() == 0) {
                stars = null;
            } else {
                stars = in.readInt();
            }
            if (in.readByte() == 0) {
                views = null;
            } else {
                views = in.readInt();
            }
            if (in.readByte() == 0) {
                userId = null;
            } else {
                userId = in.readInt();
            }
            userName = in.readString();
            userPhone = in.readString();
            userEmail = in.readString();
            userAvatar = in.readString();
            if (in.readByte() == 0) {
                categoryId = null;
            } else {
                categoryId = in.readInt();
            }
            categoryName = in.readString();
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

        public String getDisc() {
            return disc;
        }

        public void setDisc(String disc) {
            this.disc = disc;
        }

        public Integer getStars() {
            return stars;
        }

        public void setStars(Integer stars) {
            this.stars = stars;
        }

        public Integer getViews() {
            return views;
        }

        public void setViews(Integer views) {
            this.views = views;
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

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<Comment> getComments() {
            return comments;
        }

        public void setComments(List<Comment> comments) {
            this.comments = comments;
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
            parcel.writeString(title);
            parcel.writeString(lat);
            parcel.writeString(lng);
            parcel.writeString(image);
            parcel.writeString(disc);
            if (stars == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(stars);
            }
            if (views == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(views);
            }
            if (userId == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(userId);
            }
            parcel.writeString(userName);
            parcel.writeString(userPhone);
            parcel.writeString(userEmail);
            parcel.writeString(userAvatar);
            if (categoryId == null) {
                parcel.writeByte((byte) 0);
            } else {
                parcel.writeByte((byte) 1);
                parcel.writeInt(categoryId);
            }
            parcel.writeString(categoryName);
        }
    }

    // -------------------------------------------

    public class Comment {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_avatar")
        @Expose
        private String userAvatar;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("rate")
        @Expose
        private String rate;
        @SerializedName("date")
        @Expose
        private String date;

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

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }
}