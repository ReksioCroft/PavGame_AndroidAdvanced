package ro.tav.pavgame.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ro.tav.pavgame.data.GameEntity;

public interface RetrofitApi {
    String BASE_URL = "https://pav-game-tav.firebaseio.com/";

    @GET( "games.json" )
    Call < List < JsonObject > > getAllGames();

    @POST( "games/0.json" )
    Call < GameEntity > insertGame( @Body GameEntity gameEntity );

    static RetrofitApi createApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor( new StethoInterceptor() )
                .build();

        return new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .client( okHttpClient )
                .addConverterFactory( GsonConverterFactory.create() )
                .build()
                .create( RetrofitApi.class );
    }
}
