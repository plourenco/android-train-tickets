package feup.cm.traintickets.runnables;

import org.json.JSONException;
import org.json.JSONObject;

import feup.cm.traintickets.controllers.ServiceHandler;
import feup.cm.traintickets.controllers.UserController;
import feup.cm.traintickets.models.TokenModel;

public class TokenRefreshTask implements Runnable {

    private final String refresh;
    private TokenModel token;

    public TokenRefreshTask(String refresh) {
        this.refresh = refresh;
    }

    @Override
    public void run() {
        UserController userController = new UserController();
        String res = userController.refresh(refresh);

        if(res != null) {
            try {
                JSONObject object = new JSONObject(res);
                if(object.getString("token") != null &&
                        object.getLong("expires") != 0L) {
                    token = new TokenModel(object.getString("token"), refresh,
                            object.getLong("expires"));
                }
            }
            catch(JSONException ignored) { }
        }
    }

    public TokenModel getToken() {
        return token;
    }
}
