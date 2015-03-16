package apps.denux.mayorga.objetos;

/**
 * Created by dexter on 14/03/15.
 */
public class SyncItem {

    public String Url;
    public Objeto Objeto;
    public long LastUpdate;

    public SyncItem(String url, Objeto objeto) {
        Url = url;
        Objeto = objeto;
    }
}
