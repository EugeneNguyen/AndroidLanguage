package libreteam.com.language;

/**
 * Created by thanhhaitran on 2/28/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class System {

    private static SharedPreferences preferences;
    public static void printHashKey(Activity v)
    {
        try {
            PackageInfo info = v.getPackageManager().getPackageInfo("com.libreteam.taxi", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String myhash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("TEMPTAGHASH KEY:", myhash);
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

//	private boolean canToggleGPS(Context context) {
//	    PackageManager pacman = context.getPackageManager();
//	    PackageInfo pacInfo = null;
//
//	    try {
//	        pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
//	    } catch (NameNotFoundException e) {
//	        return false; //package not found
//	    }
//
//	    if(pacInfo != null){
//	        for(ActivityInfo actInfo : pacInfo.receivers){
//	            //test if recevier is exported. if so, we can toggle GPS.
//	            if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported){
//	                return true;
//	            }
//	        }
//	    }
//
//	    return false; //default
//	}

    @SuppressWarnings("static-access")
    public static Boolean checkGPS(Context context,final Activity activity)
    {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            return false;
        }
        else
            return true;
    }

    /* Set GPS or Network on device by Ying */
    @SuppressWarnings("static-access")
    public static Boolean changeGPS(Context context,final Activity activity)
    {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setTitle("Turn off Location Services");
            builder.setMessage("Would you turn off Location Services and GPS?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i)	{
                }
            });

            Dialog alertDialog = builder.create();

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            return true;
        }
        else
            return true;
    }
    /****************************************/

    public static void addValueForKey(Context v,String name, String value)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(v);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name,value);
        editor.commit();
    }

    public static String getValueForKey(Context v,String name)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(v);
        String r = preferences.getString(name, "");
        return r;
    }

    public static String jsonString(String[] name,String[] value,
                                    String[] hashName, HashMap<String,JSONObject> hashMap,
                                    String[] hashInfo, HashMap<String,JSONObject> mapInfor
    )
    {
        JSONObject json = new JSONObject();
        for(int i = 0; i< name.length; i++)
        {
            try {
                json.put(name[i], value[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject temp = new JSONObject();
        for(int i = 0; i< hashName.length; i++)
        {
            try
            {
                temp.put(hashName[i],hashMap.get(hashName[i]).getString(hashName[i]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject temp1 = new JSONObject();
        if(hashInfo != null && mapInfor != null)
        {
            for(int i = 0; i< hashInfo.length; i++)
            {
                try
                {
                    temp1.put(hashInfo[i],mapInfor.get(hashInfo[i]).getString(hashInfo[i]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        try
        {
            json.put("info", temp);
            if(hashInfo != null && mapInfor != null)
                json.put("userinfo", temp1);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return json.toString();
    }

//    static boolean checkPlayServices(Activity activity)
//    {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getApplicationContext());
//        if (resultCode != ConnectionResult.SUCCESS)
//        {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 9000).show();
//            }
//            else
//            {
//                Log.i("", "This device is not supported.");
//                activity.finish();
//            }
//            return false;
//        }
//        return true;
//    }

    static float getTextSize(Activity activity)
    {
        return	(16 * activity.getResources().getDisplayMetrics().density);
    }

    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ProgressDialog pd;
    public static void showDialog(Activity v, String title, String message)
    {
        if(pd == null)
            pd = new ProgressDialog(v);
        pd.setTitle(title);
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                hideDialog();
            }
        }, 1000 * 45);
    }

    public static void hideDialog()
    {
        if(pd != null && pd.isShowing())
        {
            pd.dismiss();
            pd = null;
        }
    }

    public static Boolean connectionStatus(Context v)
    {
        ConnectivityManager conMgr =(ConnectivityManager)v.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();

        if(info != null && info.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void showToast(Context v , String content)
    {
        Toast.makeText(v.getApplicationContext(), content , Toast.LENGTH_LONG).show();
    }

    public static void testLog( Object t)
    {
        Log.e("testLog","" + t);
    }

    public static DisplayMetrics checkScreen(Context v)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)
                v.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static int getHeight(Context v)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)
                v.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getWidth(Context v)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)
                v.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static void setContent(View v,Context context,float w,float h)
    {
        v.getLayoutParams().height = (int) (System.getHeight(context) * h);
        v.getLayoutParams().width = (int) (System.getWidth(context) * w);
    }

//    public static void didAddFragment(Fragment fr,Fragment frag,String var,String[] list,boolean isSave)
//    {
//        final FragmentTransaction ft = fr.getFragmentManager().beginTransaction();
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        Bundle data = new Bundle();
//        data.putStringArray(var,list);
//        frag.setArguments(data);
//        ft.replace(R.id.activity_main_content_fragment, frag , "NewFragmentTag");
//        if(isSave)
//        {
//            ft.addToBackStack(null);
//        }
//        ft.commit();
//    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    static void setFontFamily(ViewGroup container, Activity activity)
    {
        for(int i = 0; i< container.getChildCount(); i++)
        {
            if(container.getChildAt(i) instanceof TextView)
            {
                Typeface font = Typeface.createFromAsset(activity.getAssets(), "pt_sans.ttf");
                ((TextView)container.getChildAt(i)).setTypeface(font);
            }
        }
    }

    /**
     * Get list of address by latitude and longitude
     * @return null or List<Address>
     */
    private static List<Address> getGeocoderAddress(Context context, double lat, double lng)
    {
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 5);
            return addresses;
        }
        catch (IOException e)
        {
            Log.e("Error : Geocoder", "Impossible to connect to Geocoder", e);
        }

        return null;
    }

    public static String getAddressLine(Context context, double lat, double lng)
    {
        List<Address> addresses = getGeocoderAddress(context, lat, lng);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            int n = address.getMaxAddressLineIndex();
            String addressLine = "";
            if(n>1) {
                for (int i = 0; i < n-1; i++)
                    addressLine += address.getAddressLine(i)+ ", ";
            }
            return addressLine;
        }
        else
            return "";
    }

    static void setFontFamily_normal(ViewGroup container, Activity activity)
    {
        for(int i = 0; i< container.getChildCount(); i++)
        {
            if(container.getChildAt(i) instanceof TextView)
            {
                Typeface font = Typeface.createFromAsset(activity.getAssets(), "pt_sans_normal.ttf");
                ((TextView)container.getChildAt(i)).setTypeface(font);
            }
        }
    }

    public static void applyFonts(final View v, Typeface fontToSet)
    {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    applyFonts(child, fontToSet);
                }
            } else if (v instanceof TextView) {
                ((TextView)v).setTypeface(fontToSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Typeface faceType(Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), "pt_sans.ttf");
    }

    static Typeface faceType_normal(Activity activity)
    {
        return Typeface.createFromAsset(activity.getAssets(), "pt_sans_normal.ttf");
    }

    static String sendRequest(String...strings)
    {
        String result = null;
        String url = strings[0];
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            InputStream inputStream = httpResponse.getEntity().getContent();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String bufferedStrChunk = null;
            while((bufferedStrChunk = bufferedReader.readLine()) != null){
                stringBuilder.append(bufferedStrChunk);
            }
            result = stringBuilder.toString();
        } catch (ClientProtocolException cpe) {
            cpe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;
    }

    public static String sendRequest(String url, ArrayList<String> nameReq, ArrayList<String> valueReq){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        BasicNameValuePair basicNameValuePair = null;
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        for(int i=0; i<nameReq.size();i++){
            basicNameValuePair = new BasicNameValuePair(nameReq.get(i), valueReq.get(i));
            nameValuePairList.add(basicNameValuePair);
        }
       try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
            httpPost.setEntity(urlEncodedFormEntity);
            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);/////
                InputStream inputStream = httpResponse.getEntity().getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String bufferedStrChunk = null;
                while((bufferedStrChunk = bufferedReader.readLine()) != null){
                    stringBuilder.append(bufferedStrChunk);
                }
                return stringBuilder.toString();
            } catch (ClientProtocolException cpe) {
                Log.i("", "cpe--"+cpe.toString());
                cpe.printStackTrace();
            } catch (IOException ioe) {
                Log.i("", "ioe--"+ioe.toString());
                ioe.printStackTrace();
            }
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }
        return null;
    }

//   public class Request extends AsyncTask<HashMap<String,String>, Void, String>
//    {
//        @Override
//        protected String doInBackground(HashMap<String, String>... params) {
//
//            ArrayList<String> nameReq =  new ArrayList<String>();
//
//            ArrayList<String> valueReq =  new ArrayList<String>();
//
//            for(Map.Entry<String, String> entry : params[0].entrySet())
//            {
//                if(!entry.getKey().equalsIgnoreCase("url")) {
//                    nameReq.add(entry.getKey());
//                    valueReq.add(entry.getValue());
//                }
//            }
//            String url = params[0].get("url");
//            String data = nameReq.size() == 0 ? sendRequest(url) : sendRequest(url, nameReq, valueReq);
//
//            if(data!=null) {
//                try
//                {
//                    JSONObject jsObj = new JSONObject(data);
//                    String code = jsObj.getString("code");
//                    return data;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute(){
//           // System.showDialog(activity, context.getString(R.string.title), "Proccessing ...");
//        }
//
//        @Override
//        protected void onPostExecute(String result){
//            super.onPostExecute(result);
//           // System.hideDialog();
//            System.testLog(result);
////            try {
////                JSONObject jsObj = new JSONObject(result);
////                String code = jsObj.getString("code");
//////                if(code!=null && code.equals("200")) {
//////                    callBack.didSuccessAuthenticated(result);
//////                }
//////                else {
//////                    callBack.didFailAuthenticated(result);
//////                }
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//        }
//    }

    public static void showAlert(Activity v,String info) {
        new AlertDialog.Builder(v)
                .setTitle(v.getApplicationContext().getString(R.string.title))
                .setMessage(info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public static HashMap<String, String> build(String... data){
        HashMap<String, String> result = new HashMap<String, String>();

        if(data.length % 2 != 0)
            throw new IllegalArgumentException("Odd number of arguments");

        String key = null;
        Integer step = -1;

        for(String value : data){
            step++;
            switch(step % 2){
                case 0:
                    if(value == null)
                        throw new IllegalArgumentException("Null key value");
                    key = value;
                    continue;
                case 1:
                    result.put(key, value);
                    break;
            }
        }

        return result;
    }
}
