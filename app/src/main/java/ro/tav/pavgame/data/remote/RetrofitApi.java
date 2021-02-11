package ro.tav.pavgame.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ro.tav.pavgame.data.GameEntity;

public interface RetrofitApi {
    String BASE_URL = "https://pav-game-tav.firebaseio.com/";

    //    static final ContentResolver contentResolver = PavGameApplication.getContext().getContentResolver();
//    static final String androidId = Settings.Secure.ANDROID_ID;
//    static final String json = Settings.Secure.getString( contentResolver,androidId);
    @GET( "games/1.json" )
    Call < List < GameEntity > > getAllGames();

    @GET( "games/1.json" )
    Call < GameEntity > getGame();

    @POST( "games.json" )
    Call < GameEntity > insertGame( GameEntity game );

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

