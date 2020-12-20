package com.inddevid.aksiberbagi_donatur.services;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;

import org.json.JSONObject;

public class ApiService {

    static String token ;
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
        return AndroidNetworking.get(endpoint)
                .addHeaders("Accept", "application/json").addHeaders("Authorization", "Bearer "+ token).setPriority(Priority.HIGH).build();
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
                .addHeaders("Accept", "application/json").addHeaders("Authorization", "Bearer "+ token).build();
    }

    /**
     * Post http request
     *
     * @return ANRequest
     */
    public static ANRequest post(String endpoint){
        return AndroidNetworking.post(endpoint)
                .addHeaders("Accept", "application/json").addHeaders("Authorization", "Bearer "+ token).build();
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
                .addHeaders("Accept", "application/json").addHeaders("Authorization", "Bearer "+ token).build();
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
                .addHeaders("Accept", "application/json").addHeaders("Authorization", "Bearer "+ token).build();
    }

    public static ANRequest getKoneksi(String data){
        token = data;
        String endpoint = baseUrl("koneksi");
        return get(endpoint);
    }

    public static ANRequest postMasuk(JSONObject data){
        String endpoint = baseUrl("autentikasi/masuk");
        return post(endpoint, data);
    }

    public static ANRequest postDaftar(JSONObject data){
        String endpoint = baseUrl("autentikasi/daftar");
        return post(endpoint, data);
    }

    public static ANRequest postPasswordReset(JSONObject data){
        String endpoint = baseUrl("autentikasi/reset-password");
        return post(endpoint, data);
    }

    public static ANRequest postLupaPassword(JSONObject data){
        String endpoint = baseUrl("autentikasi/lupa-password");
        return post(endpoint, data);
    }

    public static ANRequest postKodeOTPVerifikasi(JSONObject data){
        String endpoint = baseUrl("autentikasi/konfirmasi-kode-reset");
        return post(endpoint, data);
    }

    public static ANRequest postRefreshToken(String data){
        token = data ;
        String endpoint = baseUrl("autentikasi/refresh-token");
        return post(endpoint);
    }

    public static ANRequest postLogout(String data){
        token = data ;
        String endpoint = baseUrl("autentikasi/keluar");
        return post(endpoint);
    }

    public static ANRequest getLelang(String data){
        token = data;
        String endpoint = baseUrl("flashsale/");
        return get(endpoint);
    }

    public static ANRequest getLelangDetail(String data, String Id){
        token = data;
        String endpoint = baseUrl("flashsale/"+ Id);
        return get(endpoint);
    }

    public static ANRequest getLelangNominal(String data, String Id){
        token = data;
        String endpoint = baseUrl("flashsale/nominal/"+ Id);
        return get(endpoint);
    }

    public static ANRequest getRekomendasi(String data){
        token = data ;
        String endpoint = baseUrl("slider/rekomendasi");
        return get(endpoint);
    }

    public static ANRequest getLaporan(String data){
        token = data;
        String endpoint = baseUrl("slider/laporan");
        return get(endpoint);
    }

    public static ANRequest getProgramTerbaru(String data){
        token = data;
        String endpoint = baseUrl("slider/program");
        return get(endpoint);
    }

    public static ANRequest getBanner(String data){
        token = data;
        String endpoint = baseUrl("pengaturan/banner");
        return get(endpoint);
    }

    public static ANRequest getProgramFavorit(String data){
        token = data;
        String endpoint = baseUrl("program/favorit/saya");
        return get(endpoint);
    }

    public static ANRequest getProgramDetail(String data, String id){
        token = data;
        String endpoint = baseUrl("program/"+ id);
        return get(endpoint);
    }

    public static ANRequest getProgramWord(String data, String word){
        token = data;
        String endpoint = baseUrl("program/judul/"+ word);
        return get(endpoint);
    }

    public static ANRequest getDonasiDetail(String data, String id){
        token = data;
        String endpoint = baseUrl("donasi/detail/"+ id);
        return get(endpoint);
    }

    public static ANRequest getDonatur(String data, String id){
        token = data;
        String endpoint = baseUrl("program/200-donasi-terbaru/"+ id);
        return get(endpoint);
    }

    public static ANRequest getPilihanNominal(String data, String id){
        token = data;
        String endpoint = baseUrl("pilihan-nominal/"+ id);
        return get(endpoint);
    }

    public static ANRequest getJudulProgram(String data){
        token = data;
        String endpoint = baseUrl("program/all/judul/");
        return get(endpoint);
    }

    public static ANRequest getAllProgram(String data){
        token = data;
        String endpoint = baseUrl("program");
        return get(endpoint);
    }

    public static ANRequest getPembayaran(String data){
        token = data;
        String endpoint = baseUrl("pilihan-nominal/metode-pembayaran");
        return get(endpoint);
    }

    public static ANRequest postDonasi(String auth,  JSONObject data){
        token = auth ;
        String endpoint = baseUrl("donasi");
        return post(endpoint, data);
    }

    public static ANRequest postFavorit(String auth,  JSONObject data){
        token = auth ;
        String endpoint = baseUrl("program/favorit");
        return post(endpoint, data);
    }

    public static ANRequest deleteFavorit(String auth,  String data){
        token = auth ;
        String endpoint = baseUrl("program/favorit/"+ data);
        return delete(endpoint);
    }

    public static ANRequest getDonasiSaya(String data){
        token = data;
        String endpoint = baseUrl("donasi/saya");
        return get(endpoint);
    }

    public static ANRequest getDonasiRutin(String data){
        token = data;
        String endpoint = baseUrl("donasi-rutin");
        return get(endpoint);
    }

    public static ANRequest postDonasiRutin(String auth,  JSONObject data){
        token = auth ;
        String endpoint = baseUrl("donasi-rutin/simpan");
        return post(endpoint, data);
    }

    public static ANRequest deleteDonasiRutin(String auth,  String data){
        token = auth ;
        String endpoint = baseUrl("donasi-rutin/delete/"+ data);
        return delete(endpoint);
    }

    public static ANRequest putProfil(String auth,  JSONObject data){
        token = auth ;
        String endpoint = baseUrl("pengguna/saya");
        return put(endpoint, data);
    }



}
