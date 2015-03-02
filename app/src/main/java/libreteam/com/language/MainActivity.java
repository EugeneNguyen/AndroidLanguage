package libreteam.com.language;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        Language.getInstance(this).initWithHost(getString(R.string.host));

        setContentView(R.layout.activity_main);

        test();

       Button b = (Button) findViewById(R.id.button);

       b.setText(Language.getInstance(this).stringForKeyandScreen("key","loginScreen"));

//        System.testLog("-------" + Language.getInstance(this).stringForKeyandScreen("key","loginScreen"));

        b.setOnClickListener(this);
    }

    public void test()
    {
        for(Storage_text lang : Language.getInstance(this).getAllText())
        {
           // System.testLog(lang.getText() + "----" + lang.getScreen() + "-----" + lang.getLanguage() + "------" + lang.getTranlatedtext());
        }
    }

    @Override
    public void onClick(View v) {
        Language.getInstance(this).getVersion();
    }
}
