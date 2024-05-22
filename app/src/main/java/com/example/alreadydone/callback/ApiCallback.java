package com.example.alreadydone.callback;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.alreadydone.api.ApiResponse;
import com.example.alreadydone.LoginActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallback implements Callback<ApiResponse> {
    private final OnApiResponse onApiResponse;
    private final Context context;
    private final String errorMessage;

    public ApiCallback(OnApiResponse onApiResponse, Context context, String errorMessage) {
        this.onApiResponse = onApiResponse;
        this.context = context;
        this.errorMessage = errorMessage;
    }

    @Override
    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            onApiResponse.handleResponse(response.body());
        } else {
            handleErrorResponse(response);
        }
    }

    @Override
    public void onFailure(Call<ApiResponse> call, Throwable t) {
        Log.e("ApiCallback", "onFailure: " + t.getMessage(), t);
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void handleErrorResponse(Response<ApiResponse> response) {
        String errorBody = "Unknown error";
        try {
            errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
        } catch (IOException e) {
            Log.e("ApiCallback", "onResponse: error parsing error body", e);
        }

        int errorCode = response.code();
        switch (errorCode) {
            case 400:
                Log.e("ApiCallback", "Bad Request: " + errorBody);
                Toast.makeText(context, "בקשה לא תקינה", Toast.LENGTH_SHORT).show();
                break;
            case 401:
                Log.e("ApiCallback", "Unauthorized: " + errorBody);
                Toast.makeText(context, "סיסמה או אימייל לא נכונים", Toast.LENGTH_SHORT).show();
                break;
            case 403:
                Log.e("ApiCallback", "Forbidden: " + errorBody);
                Toast.makeText(context, "אין לך הרשאות", Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Log.e("ApiCallback", "Not Found: " + errorBody);
                Toast.makeText(context, "הכתובת לא נמצאה", Toast.LENGTH_SHORT).show();
                break;
            case 500:
                Log.e("ApiCallback", "Internal Server Error: " + errorBody);
                Toast.makeText(context, "שגיאה בשרת", Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.e("ApiCallback", "Unknown error: " + errorBody);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
