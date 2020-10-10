package com.inddevid.aksiberbagi_donatur.services;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;

import org.json.JSONObject;

public class ApiService {

    /**
     * Get base url (full url)
     *
     * @param uri Ini adalah uri dari api
     * @return String
     */
    public static String baseUrl(String uri){
        String BASE_URL = "https://api.aksiberbagi.com/v1/";
        return BASE_URL + uri;
    }

    /**
     * Get http request
     *
     * @param endpoint ini adalah full url endpoint api
     * @return String
     */
    public static ANRequest get(String endpoint){
        return AndroidNetworking.get(endpoint).addHeaders("Accept", "application/json").build();
    }

    /**
     * Post http request
     *
     * @param endpoint Ini adalah full url endpoint api
     * @return ANRequest
     */
    public static ANRequest post(String endpoint, JSONObject data){
        return AndroidNetworking.post(endpoint)
                .addJSONObjectBody(data)
                .addHeaders("Accept", "application/json").build();
    }

    /**
     * Put http request
     *
     * @param endpoint Ini adalah full url endpoint api
     * @return ANRequest
     */
    public static ANRequest put(String endpoint, JSONObject data){
        try{
            data.put("_method", "PUT");
        }catch (Exception e){
            Log.d("ADD_METHOD", e.getMessage());
        }
        return AndroidNetworking.post(endpoint)
                .addJSONObjectBody(data)
                .addHeaders("Accept", "application/json").build();
    }

    /**
     * Delete http request
     *
     * @param endpoint Ini adalah full url endpoint api
     * @return ANRequest
     */
    public static ANRequest delete(String endpoint){
        JSONObject data = new JSONObject();
        try{
            data.put("_method", "DELETE");
        }catch (Exception e){
            Log.d("ADD_METHOD", e.getMessage());
        }
        return AndroidNetworking.post(endpoint)
                .addJSONObjectBody(data)
                .addHeaders("Accept", "application/json").build();
    }

    /**
     * Post masuk
     *
     * @param data Ini adalah form data masuk
     * @return ANRequest
     */
    public static ANRequest postMasuk(JSONObject data){
        String endpoint = baseUrl("autentikasi/masuk");
        return post(endpoint, data);
    }

}
