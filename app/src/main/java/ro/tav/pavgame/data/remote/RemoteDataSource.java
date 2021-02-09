package ro.tav.pavgame.data.remote;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import ro.tav.pavgame.data.GameHistory;
import timber.log.Timber;

public class RemoteDataSource {
    private static final String TAG = "remote-source";
    private final FirebaseApi api;

    public RemoteDataSource(FirebaseApi api) {
        this.api = api;
    }

    public List < GameHistory > getItems() {
        try {
            Response <List<GameHistory>> response = api.getItems().execute();
            return response.body();
        } catch ( IOException e) {
            Timber.tag(TAG).w(e, "Something went wrong");
            return Collections.emptyList();
        }
    }
}
