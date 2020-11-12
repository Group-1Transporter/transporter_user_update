package com.transporteruser.api;


import com.transporteruser.bean.Lead;
import com.transporteruser.bean.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class UserService {

    public static UserApi userApi;

    public static UserApi getUserApiInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (userApi == null)
            userApi = retrofit.create(UserApi.class);
        return userApi;
    }

    public interface UserApi {
        @Multipart
        @POST("/user/")
        public Call<User> saveProfile(@Part MultipartBody.Part file,
                                      @Part("userId") RequestBody userId,
                                      @Part("name") RequestBody name,
                                      @Part("address") RequestBody address,
                                      @Part("contactNumber") RequestBody contactNumber,
                                      @Part("token") RequestBody token);

        //check user profile created or not
        @GET("/user/checkProfile/{userId}")
        public Call<User> checkProfile(@Path("userId") String userId);

        @GET("/lead/all-lead/{userId}")
        public Call<ArrayList<Lead>> getAllLeads(@Path("userId")String userId);
    }
}
