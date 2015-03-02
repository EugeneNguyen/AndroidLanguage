package libreteam.com.language;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thanhhaitran on 3/2/15.
 */
public class HttpRequestAsync extends AsyncTask<HashMap<String,String>, Void, String> {

    public  AuthenticationCallback callBack;

    public Activity activity;

    public HttpRequestAsync(Activity act)
    {
        this.activity = act;
    }

    public interface AuthenticationCallback
    {
        public void didSuccessAuthenticated(String string);

        public void didFailAuthenticated(String string);
    }

    @Override
    protected String doInBackground(HashMap<String, String>... params) {

        ArrayList<String> nameReq =  new ArrayList<String>();

        ArrayList<String> valueReq =  new ArrayList<String>();

        for(Map.Entry<String, String> entry : params[0].entrySet())
        {
            if(!entry.getKey().equalsIgnoreCase("url")) {
                nameReq.add(entry.getKey());
                valueReq.add(entry.getValue());
            }
        }
        String url = params[0].get("url");
        String data = nameReq.size() == 0 ? System.sendRequest(url) : System.sendRequest(url, nameReq, valueReq);

        if(data!=null) {
            try
            {
                JSONObject jsObj = new JSONObject(data);
                String code = jsObj.getString("code");
                return data;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute(){
        if(this.activity != null)
           System.showDialog(activity, activity.getApplication().getString(R.string.title), "Proccessing ...");
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        System.hideDialog();
            try {
                JSONObject jsObj = new JSONObject(result);
                String code = jsObj.getString("code");
                if(code!=null && code.equals("200")) {
                    callBack.didSuccessAuthenticated(result);
                }
                else {
                    callBack.didFailAuthenticated(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}

