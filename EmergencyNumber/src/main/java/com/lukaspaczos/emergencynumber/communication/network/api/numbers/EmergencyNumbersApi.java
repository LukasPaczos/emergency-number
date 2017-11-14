package com.lukaspaczos.emergencynumber.communication.network.api.numbers;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lukas on 14.11.17
 */

public class EmergencyNumbersApi {

    public static EmergencyNumbersApi instance;

    public static EmergencyNumbersApi getInstance() {
        if (instance == null)
            instance = new EmergencyNumbersApi();

        return instance;
    }

    public interface EmergencyNumbersService {
        @GET("country/{countryCode}")
        Call<EmergencyNumbersPOJO> getNumbers(@Path("countryCode") String countryCode);
    }

    private EmergencyNumbersService service = new Retrofit.Builder()
            .baseUrl("http://emergencynumberapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(EmergencyNumbersService.class);

    public EmergencyNumbersService getService() {
        return service;
    }
}
