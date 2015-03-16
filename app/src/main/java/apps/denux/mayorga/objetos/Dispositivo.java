package apps.denux.mayorga.objetos;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dexter on 13/03/15.
 */
public class Dispositivo {
    public String MAC;
    public String RegID;
    public int Serie;

    /**
     * Crea un objeto dispositivo solo con su RegID de GCM
     * @param regID
     */
    public Dispositivo(String regID) {
        RegID = regID;
    }

    /**
     * Crea un objeto de tipo dispositivo
     * @param MAC
     * @param regID
     * @param serie
     */
    public Dispositivo(String MAC, String regID, int serie) {
        this.MAC = MAC;
        RegID = regID;
        Serie = serie;
    }

    /**
     * Retorn un dipositivo como objeto JSON
     * @return
     */
    public JSONObject getJSON(){
        JSONObject object = new JSONObject();
        try {
            object.put("MAC",this.MAC);
            object.put("regId", this.RegID);
            object.put("Serie", this.Serie);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
