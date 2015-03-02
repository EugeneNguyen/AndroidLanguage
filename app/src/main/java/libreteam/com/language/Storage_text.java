package libreteam.com.language;

/**
 * Created by thanhhaitran on 2/28/15.
 */
public class Storage_text {

    //int id;
    String text;
    String screen;
    String language;
    String tranlatedtext;

    public Storage_text(){

    }
    public Storage_text(/*int _id,*/ String _text, String _screen, String _language, String _tranlatedtext)
    {
        //this.id = _id;
        this.text = _text;
        this.screen = _screen;
        this.language = _language;
        this.tranlatedtext = _tranlatedtext;
    }


//    public int getId(){
//        return this.id;
//    }
//
//    public void setId(int _id){
//        this.id = _id;
//    }

    public String getText(){
        return this.text;
    }

    public void setText(String _text){
        this.text = _text;
    }

    public String getScreen(){
        return this.screen;
    }

    public void setScreen(String _screen){
        this.screen = _screen;
    }

    public String getLanguage(){
        return this.language;
    }

    public void setLanguage(String _language){
        this.language = _language;
    }

    public String getTranlatedtext(){
        return this.tranlatedtext;
    }

    public void setTranlatedtext(String _translatedtext){
        this.tranlatedtext = _translatedtext;
    }
}
