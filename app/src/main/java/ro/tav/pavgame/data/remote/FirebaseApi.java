package ro.tav.pavgame.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import ro.tav.pavgame.data.GameHistory;

public interface FirebaseApi {
    String BASE_URL = "https://pav-game-tav.firebaseio.com/";

    @GET("items.json")
    Call < List < GameHistory > > getItems();

    static FirebaseApi createApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory( GsonConverterFactory.create())
                .build()
                .create(FirebaseApi.class);
    }
}

