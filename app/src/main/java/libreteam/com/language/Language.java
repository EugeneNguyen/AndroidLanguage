package libreteam.com.language;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by thanhhaitran on 2/28/15.
 */
public class Language extends SQLiteOpenHelper {

    private static Language share;

    private Activity activity;

    private  Context context;

    private String host;

    private String language;

    public static Language getInstance(Activity act)
    {
        if (share == null)
        {
            share = new Language(act);
            share.language = "en";
        }
        return share;
    }
//////////////////////////////////////////////////////////////////

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Language";

        private static final String Storage_text = "Info";

        private static final String TEXT_ID = "id";
        private static final String TEXT_INFO = "text";
        private static final String TEXT_SCREEN = "screen";
        private static final String TEXT_LANGUAGE = "language";
        private static final String TEXT_TRANSLATEDTEXT = "translatedtext";


    private static final String Storage_language = "Lang";

    private static final String LANGUAGE_NAME = "name";
    private static final String LANGUAGE_SHORTNAME = "shorname";

        public Language(Activity act)
        {
            super(act.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
            context =  act.getApplicationContext();
            activity = act;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String CREATE_INFO_TABLE = "CREATE TABLE " + Storage_text + "("
                    + TEXT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TEXT_INFO + " TEXT,"
                    + TEXT_SCREEN + " TEXT," + TEXT_LANGUAGE + " TEXT," +
                    TEXT_TRANSLATEDTEXT + " TEXT" + ")";

            String CREATE_LANG_TABLE = "CREATE TABLE " + Storage_language + "("
                    + LANGUAGE_SHORTNAME + " TEXT PRIMARY KEY ," + LANGUAGE_NAME + " TEXT" + ")";

            db.execSQL(CREATE_INFO_TABLE);
            db.execSQL(CREATE_LANG_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Storage_text);
            db.execSQL("DROP TABLE IF EXISTS " + Storage_language);
            onCreate(db);
        }

        public void updateText(Storage_text info, String text, String screen, String language, String translatedtext) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TEXT_INFO, text);
            values.put(TEXT_SCREEN, screen);
            values.put(TEXT_LANGUAGE, language);
            values.put(TEXT_TRANSLATEDTEXT, translatedtext);

            db.update(Storage_text, values, TEXT_INFO + " = ? AND " + TEXT_SCREEN + " = ? AND " +TEXT_LANGUAGE + " = ? AND " + TEXT_TRANSLATEDTEXT + " = ?" ,
                    new String[] {info.getText(),info.getScreen(),info.getLanguage(),info.getTranlatedtext()}

            );
            db.close();
        }

        public void addText(Storage_text contact) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            //values.put(TEXT_ID, contact.getId());
            values.put(TEXT_INFO, contact.getText());
            values.put(TEXT_SCREEN, contact.getScreen());
            values.put(TEXT_LANGUAGE,contact.getLanguage());
            values.put(TEXT_TRANSLATEDTEXT, contact.getText());

            String text = contact.getText();

            String screen = contact.getScreen();

            String language = contact.getLanguage();

            if(getTextWithId(text,screen,language).size() != 0)
            {
                updateText(getTextWithId(text,screen,language).get(0),contact.getText(),contact.getScreen(),contact.getLanguage(),contact.getTranlatedtext());
            }
            else
            {
                db.insert(Storage_text, null, values);
            }
            db.close();
        }

        public List<Storage_text> getAllText() {
            List<Storage_text> contactList = new ArrayList<Storage_text>();
            String selectQuery = "SELECT  * FROM " + Storage_text;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Storage_text contact = new Storage_text();
                    //contact.setId(cursor.getInt(cursor.getColumnIndex(TEXT_ID)));
                    contact.setText(cursor.getString(cursor.getColumnIndex(TEXT_INFO)));
                    contact.setScreen(cursor.getString(cursor.getColumnIndex(TEXT_SCREEN)));
                    contact.setLanguage(cursor.getString(cursor.getColumnIndex(TEXT_LANGUAGE)));
                    contact.setTranlatedtext(cursor.getString(cursor.getColumnIndex(TEXT_TRANSLATEDTEXT)));

                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
            return contactList;
        }

        public List<Storage_text> getTextWithId(String text, String screen, String language) {
            List<Storage_text> texts = new ArrayList<Storage_text>();
            for(Storage_text c : getAllText())
            {
                //if(String.valueOf(c.getId()).equalsIgnoreCase(infoId))
                if(c.getText().equalsIgnoreCase(text) && c.getScreen().equalsIgnoreCase(screen) && c.getLanguage().equalsIgnoreCase(language))
                {
                    texts.add(c);
                }
            }
            return texts;
        }

        public void removeTextWithId(String infoId)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            if(getAllText().size() != 0)
            {
                for(int i = 0; i< getAllText().size(); i++)
                {
                    db.delete(Storage_text, TEXT_ID + " = ?",
                            new String[] { infoId });
                }
            }
            db.close();
        }

    public void removeAllText()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Storage_text, null, null);
        db.close();
    }

/////////////////////////////////////////////////////////////////

    public void updateLanguae(Storage_language lang, String info) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LANGUAGE_NAME, info);
        db.update(Storage_language, values, LANGUAGE_SHORTNAME + " = ?",
                new String[] {lang.getShortname()});
        db.close();
    }

    public void addLang(Storage_language lang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LANGUAGE_NAME, lang.getname());
        values.put(LANGUAGE_SHORTNAME, lang.getShortname());

        String name = lang.getShortname();

        if(getLangWithShortName(name).size() != 0)
        {
            updateLanguae(getLangWithShortName(name).get(0), lang.getname());
        }
        else
        {
            db.insert(Storage_language, null, values);
        }
        db.close();
    }

    public List<Storage_language> getAllLang() {
        List<Storage_language> languages = new ArrayList<Storage_language>();
        String selectQuery = "SELECT  * FROM " + Storage_language;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Storage_language lang = new Storage_language();
                lang.setName(cursor.getString(cursor.getColumnIndex(LANGUAGE_NAME)));
                lang.setShortname(cursor.getString(cursor.getColumnIndex(LANGUAGE_SHORTNAME)));

                languages.add(lang);
            } while (cursor.moveToNext());
        }
        return languages;
    }

    public List<Storage_language> getLangWithShortName(String shortname) {
        List<Storage_language> langs = new ArrayList<Storage_language>();
        for(Storage_language c : getAllLang())
        {
            if(c.getShortname().equalsIgnoreCase(shortname))
            {
                langs.add(c);
            }
        }
        return langs;
    }

    public void removeAllLang()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Storage_language, null, null);
        db.close();
    }

/////////////////////////////////////////////////////////////////////////////

    public void setLanguage(String language)
    {
       // language = [language ];
        suggestedLanguage(language);
        this.language = language;
        System.addValueForKey(context,"XBLanguageSelectedLanguage",language);
    }

    public void initWithHost(String host)
    {
        this.host = host;

        if(System.getValueForKey(context,"XBLanguageSelectedLanguage") != null)
        {
            this.language = System.getValueForKey(context,"XBLanguageSelectedLanguage");

            if(getLangWithShortName(this.language).size() == 0)
            {
                this.language = System.getValueForKey(context,"XBLanguagePrimaryLanguage");
            }
        }
        else
        {
            String currentLanguage = Locale.getDefault().getLanguage();
            suggestedLanguage(currentLanguage);
        }

        getVersion();
    }

    public void getVersion()
    {
        isValidate();

        HashMap<String,String> hash = new HashMap<String,String>();
        hash.put("url",String.format("%s/%s",host,"get_version"));

       HttpRequestAsync request =  new HttpRequestAsync(null);
       request.execute(hash);
       request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
               System.testLog(string);
               // updateLanguage();                                   ///////////////////////////////
                //updateText();                                      ////////////////////////////////
                JSONObject jsObj = null;
                try {

                    jsObj = new JSONObject(string);
                    String code = jsObj.getString("language_version");

                    String recentVersion = System.getValueForKey(context,"XBLanguageVersion");
                    String serverVersion = code;
                    if(!recentVersion.equalsIgnoreCase(serverVersion))
                    {
                        System.addValueForKey(context,"XBLanguageVersion",serverVersion);
                        updateText();
                        updateLanguage();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didFailAuthenticated(String string) {
                System.testLog(string);
            }
        };
    }

    public void updateLanguage()
    {
        isValidate();

        HashMap<String,String> hash = new HashMap<String,String>();
        hash.put("url",String.format("%s/%s",host,"get_language_supported"));

        HttpRequestAsync request =  new HttpRequestAsync(null);
        request.execute(hash);
        request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                System.testLog(string);
                JSONObject jsObj = null;
                JSONArray  arr = null;
                try {

                    jsObj = new JSONObject(string);
                    arr = jsObj.getJSONArray("data");
                    for(int i=0; i<arr.length(); i++) {
                        JSONObject json_data = arr.getJSONObject(i);
                        addLang(new Storage_language(json_data.getString("name"),json_data.getString("long_name")));
                    }
                    System.addValueForKey(context,"XBLanguagePrimaryLanguage",jsObj.getString("primary"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didFailAuthenticated(String string) {
                System.testLog(string);
            }
        };
    }


        public  void updateText()
        {
            isValidate();

            HashMap<String,String> hash = new HashMap<String,String>();
            hash.put("url",String.format("%s/%s",host,"get_list_text"));

            HttpRequestAsync request =  new HttpRequestAsync(null);
            request.execute(hash);
            request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
                @Override
                public void didSuccessAuthenticated(String string) {
                    System.testLog(string);
                    JSONObject jsObj = null;
                    JSONArray  arr = null;
                    try {

                        jsObj = new JSONObject(string);
                        arr = jsObj.getJSONArray("data");
                        for(int i=0; i<arr.length(); i++) {
                            JSONObject json_data = arr.getJSONObject(i);

                            Map<String,String> map = JsonHelper.toHashMap(json_data);

                            String text = json_data.getString("keyname");
                            String screen = json_data.getString("screen");

                            map.remove("keyname");
                            map.remove("screen");
                            for(String key : map.keySet())
                            {
                                addText(new Storage_text(text,screen,key,map.get(key)));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void didFailAuthenticated(String string) {
                    System.testLog(string);
                }
            };
        }


    public String stringForKeyandScreen(String key, String screen)
    {
        if(this.language == null)
        {
            return key;
        }
        if(screen == null)
        {
            return "default";
        }

        List<Storage_text> list = getTextWithId(key,screen,this.language);

        if(list.size() == 0)
        {
            isValidate();

            HashMap<String,String> hash = new HashMap<String,String>();
            hash.put("url",String.format("%s/%s",host,"add_text"));
            hash.put("text",key);
            hash.put("screen",screen);

            HttpRequestAsync request =  new HttpRequestAsync(null);
            request.execute(hash);
            request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
                @Override
                public void didSuccessAuthenticated(String string) {
                    System.testLog(string);
                }

                @Override
                public void didFailAuthenticated(String string) {
                    System.testLog(string);
                }
            };

            if(System.getValueForKey(context,"XBLanguagePrimaryLanguage") != null)
            {
                list = getTextWithId(key,screen,System.getValueForKey(context,"XBLanguagePrimaryLanguage"));

                if(list.size() != 0)
                {
                    return list.get(list.size() - 1).getTranlatedtext();
                }
            }
        }
        else
        {
           return list.get(list.size() - 1).getTranlatedtext();
        }

        return  null;
    }

    public void suggestedLanguage(String language)
    {
        isValidate();

        HashMap<String,String> hash = new HashMap<String,String>();
        hash.put("url",String.format("%s/%s",host,"language"));
        hash.put("language",language);

        HttpRequestAsync request =  new HttpRequestAsync(null);
        request.execute(hash);
        request.callBack =  new HttpRequestAsync.AuthenticationCallback() {
            @Override
            public void didSuccessAuthenticated(String string) {
                System.testLog(string);
            }

            @Override
            public void didFailAuthenticated(String string) {
                System.testLog(string);
            }
        };
    }

    private void isValidate()
    {
        if(!System.connectionStatus(context)) {
            System.showAlert(activity,"Please check your network connection.");
            return;
        }
    }

}
