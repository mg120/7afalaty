package com.tasmim.a7afalaty.network;

import com.tasmim.a7afalaty.model.AcceptBookingModel;
import com.tasmim.a7afalaty.model.AddServiceModel;
import com.tasmim.a7afalaty.model.BookingModel;
import com.tasmim.a7afalaty.model.CancelBookingModel;
import com.tasmim.a7afalaty.model.CommentServiceModel;
import com.tasmim.a7afalaty.model.ContactModel;
import com.tasmim.a7afalaty.model.DeleteBookingModel;
import com.tasmim.a7afalaty.model.DeleteServiceModel;
import com.tasmim.a7afalaty.model.ForgetPassModel;
import com.tasmim.a7afalaty.model.HomeModel;
import com.tasmim.a7afalaty.model.ItemDetailsModel;
import com.tasmim.a7afalaty.model.LogOutModel;
import com.tasmim.a7afalaty.model.LoginModel;
import com.tasmim.a7afalaty.model.MyBookingModel;
import com.tasmim.a7afalaty.model.MyServicesModel;
import com.tasmim.a7afalaty.model.NotificationsModel;
import com.tasmim.a7afalaty.model.PolicyModel;
import com.tasmim.a7afalaty.model.ProfileModel;
import com.tasmim.a7afalaty.model.ResendCodeModel;
import com.tasmim.a7afalaty.model.SendCodeModel;
import com.tasmim.a7afalaty.model.ShowItemsModel;
import com.tasmim.a7afalaty.model.SignModel;
import com.tasmim.a7afalaty.model.UpdateServiceModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ma7MouD on 10/7/2018.
 */

public interface ApiService {

    //-------------------- Login ------------------
    @FormUrlEncoded
    @POST("/api/sign_in")
    Call<LoginModel> user_login(@Field("email") String email,
                                @Field("password") String password,
                                @Field("device_id") String device_id,
                                @Field("device_type") String device_type,
                                @Field("remember_token") String remember_token); // 1 For android devices

    //-------------------- SignUp ------------------
    @FormUrlEncoded
    @POST("/api/sign_up")
    Call<SignModel> user_sgnUp(@Field("name") String name,
                               @Field("phone") String phone,
                               @Field("email") String email,
                               @Field("password") String password,
                               @Field("type") String type,
                               @Field("lat") String lat,
                               @Field("lng") String lng,
                               @Field("remember_token") String remember_token); // 1 For android devices


    //-------------------- Get Profile Data ---------------------------//
    @GET("/api/my_profile/{input}")
    Call<ProfileModel> getProfileData(@Path("input") String pageIndex);


    // ------------------- Home Data ------------------
    @GET("/api/sections")
    Call<HomeModel> homeData();


    // ------------------- Sections --------------------
    @GET("/api/section/{input}")
    Call<ShowItemsModel> getSectionData(@Path("input") String pageIndex);


    // ------------------- Profile Data ---------------------
    @Multipart
    @POST("/api/update_profile")
    Call<ProfileModel> update_profile(@Part("id") RequestBody id,
                                      @Part("name") RequestBody name,
                                      @Part("email") RequestBody email,
                                      @Part("phone") RequestBody phone,
                                      @Part("password") RequestBody password,
                                      @Part("lat") RequestBody lat,
                                      @Part("lng") RequestBody lng,
                                      @Part MultipartBody.Part profile_img
    );

    //-------------------- All User Booking ---------------------------//
    @GET("/api/mybooking/{input}")
    Call<MyBookingModel> allUserBooking(@Path("input") String pageIndex);


    //-------------------- All User Services ---------------------------//
    @GET("/api/myservices/{input}")
    Call<MyServicesModel> userServices(@Path("input") String pageIndex);


    // ------------------- New Booking -------------------
    @FormUrlEncoded
    @POST("/api/booking")
    Call<BookingModel> booking_service(@Field("user_id") String user_id,
                                       @Field("service_id") String service_id,
                                       @Field("name") String name,
                                       @Field("phone") String phone,
                                       @Field("date") String date,
                                       @Field("disc") String disc);


    // ------------------ Update Booking --------------------//
    @FormUrlEncoded
    @POST("/api/update/booking")
    Call<BookingModel> updateBooking(@Field("user_id") String user_id,
                                     @Field("service_id") String service_id,
                                     @Field("book_id") String book_id,
                                     @Field("name") String name,
                                     @Field("phone") String phone,
                                     @Field("date") String date,
                                     @Field("disc") String disc);

    // ------------------ Delete Booking --------------------//

    @GET("/api/delete/booking/{input}")
    Call<DeleteBookingModel> deleteBooking(@Path("input") String pageIndex);


    // ------------------- Accept Booking ------------------------//
    @FormUrlEncoded
    @POST("/api/accept/booking")
    Call<AcceptBookingModel> accept_Booking(@Field("owner_id") String owner_id,
                                            @Field("book_id") String book_id);

    // ------------------- Cancel Booking ------------------------//
    @FormUrlEncoded
    @POST("/api/accept/cancelbooking")
    Call<CancelBookingModel> cancel_Booking(@Field("owner_id") String owner_id,
                                            @Field("book_id") String book_id);


    //-------------------- Send Code ---------------------------//
    @FormUrlEncoded
    @POST("/api/check_code")
    Call<SendCodeModel> send_code(@Field("id") String id,
                                  @Field("code") String code);


    //-------------------- Send mail ---------------------------//
    @FormUrlEncoded
    @POST("/api/forget_password")
    Call<ForgetPassModel> forget_pass(@Field("email") String email);


    //-------------------- ReSend Code ---------------------------//
    @FormUrlEncoded
    @POST("/api/resend_code")
    Call<ResendCodeModel> resend_Code(@Field("id") String id,
                                      @Field("code") String code);


    //-------------------- ReSet Password ---------------------------//
    @FormUrlEncoded
    @POST("/api/reset_password")
    Call<LoginModel> reset_pass(@Field("id") String id,
                                @Field("password") String password);


    //-------------------- item Details ---------------------------//
    @GET("/api/services/{input}")
    Call<ItemDetailsModel> itemDetails(@Path("input") String pageIndex);


    //-------------------- Delete Service ---------------------------//
    @DELETE("/api/services/{input}")
    Call<DeleteServiceModel> delete_service(@Path("input") String pageIndex);


    // ------------------- Update Service ------------------------//
    @Multipart
    @POST("/api/update-services/{input}")
    Call<UpdateServiceModel> updateService(@Path("input") String pageIndex,
                                           @Part("user_id") RequestBody user_id,
                                           @Part("section_id") RequestBody section_id,
                                           @Part("title") RequestBody title,
                                           @Part("details") RequestBody details,
                                           @Part("lat") RequestBody lat,
                                           @Part("lng") RequestBody lng,

                                           @Part MultipartBody.Part profile_img);


    // ------------------- My Notifications ------------------------//
    @GET("/api/my-notify/{input}")
    Call<NotificationsModel> my_notifies(@Path("input") String pageIndex);


    // ------------------- comment Service ------------------------//
    @FormUrlEncoded
    @POST("/api/comment")
    Call<CommentServiceModel> commentService(@Field("user_id") String user_id,
                                             @Field("service_id") String service_id,
                                             @Field("comment") String comment,
                                             @Field("rate") String rate);


    // ------------------- Add Service -------------------------//
    @Multipart
    @POST("/api/services")
    Call<AddServiceModel> add_Service(@Part("user_id") RequestBody user_id,
                                      @Part("section_id") RequestBody section_id,
                                      @Part("title") RequestBody title,
                                      @Part("details") RequestBody details,
                                      @Part("lat") RequestBody lat,
                                      @Part("lng") RequestBody lng,

                                      @Part MultipartBody.Part profile_img);


    // ------------------- LogOut -------------------------//
    @FormUrlEncoded
    @POST("/api/logout")
    Call<LogOutModel> logout(@Field("id") String user_id,
                             @Field("device_id") String device_id);


    // ----------------- Policy Data ------------------------------//
    @GET("/api/policy")
    Call<PolicyModel> policyData();


    // ----------------- Contact US ------------------------------//
    @GET("/api/appinfo")
    Call<ContactModel> contactData();


}