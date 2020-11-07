package com.transporteruser.api;


import com.transporteruser.bean.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class UserService {

  public static final String BASE_URL = "http://192.168.43.121:8080";
  public static UserApi userApi;
  public static UserApi getUserApiInstance(){
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    if(userApi == null)
      userApi = retrofit.create(UserApi.class);
    return userApi;
  }
  public interface UserApi{
    @Multipart
    @POST("/user/")
    public Call<User> saveProfile(@Part MultipartBody.Part file,
                                  @Part("name") RequestBody name,
                                  @Part("address") RequestBody address,
                                  @Part("contactNumber") RequestBody contactNumber,
                                  @Part("token") RequestBody token);
  }
}
