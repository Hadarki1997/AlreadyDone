package com.example.alreadydone.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/register")
    Call<ApiResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("/login")
    Call<ApiResponse> loginUser(@Body LoginRequest loginRequest);
}
