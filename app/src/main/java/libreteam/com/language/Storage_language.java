package libreteam.com.language;

/**
 * Created by thanhhaitran on 3/2/15.
 */
public class Storage_language {

    String name;
    String shortname;

    public Storage_language(){

    }
    public Storage_language(String _shortname, String _name)
    {
        this.name = _name;
        this.shortname = _shortname;
    }


    public String getname(){
        return this.name;
    }

    public void setName(String _name){
        this.name = _name;
    }

    public String getShortname(){
        return this.shortname;
    }

    public void setShortname(String _shortname){
        this.shortname = _shortname;
    }


}
