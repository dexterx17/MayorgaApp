package apps.denux.mayorga.objetos;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by dexter on 13/03/15.
 */
public class TipoCliente extends Objeto{
    public int CODIGO;
    public CharSequence DESCRIPCION;
    public Timestamp ACTUALIZACION;

    /**
     * Crea un objeto de TipoCliente
     * @param CODIGO
     * @param DESCRIPCION
     */
    public TipoCliente(int CODIGO, CharSequence DESCRIPCION) {
        this.CODIGO = CODIGO;
        this.DESCRIPCION = DESCRIPCION;
    }

    /**
     * Crea un objeto de TipoCliente
     * @param CODIGO
     * @param DESCRIPCION
     * @param ACTUALIZACION
     */
    public TipoCliente(int CODIGO, CharSequence DESCRIPCION, Timestamp ACTUALIZACION) {
        this.CODIGO = CODIGO;
        this.DESCRIPCION = DESCRIPCION;
        this.ACTUALIZACION = ACTUALIZACION;
    }

    /**
     * Crea un objeto TipooCliente en base un objeto JSONObject
     * @param object JSONObject con la información de un cliente
     */
    public TipoCliente(JSONObject object){
        try{
            this.CODIGO = object.getInt("TEM_CODIGO");
            this.DESCRIPCION = object.getString("TEM_DESCRIPCION");
            this.ACTUALIZACION = Timestamp.valueOf(object.getString("TEM_UPDATE"));
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Crea un Objeti TipoCliente vacio
     */
    public TipoCliente() {}

    /**
     * Devuelve un ArrayList de TipoCliente
     * @param jsonObjects
     * @return ArrayList
     */
    public static ArrayList<TipoCliente> fromJson(JSONArray jsonObjects) {
        ArrayList<TipoCliente> tipos = new ArrayList<TipoCliente>();
        for (int i = 0; i < jsonObjects.length(); i++)
            try {
                tipos.add(new TipoCliente(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return tipos;
    }

    /**
     * Crea un ArrayList de Cliente desde un Cursor
     * @param c Cursor obtenido de SQLITE3
     * @return ArrayList
     */
    public static ArrayList<TipoCliente> fromCursor(Cursor c) {
        ArrayList<TipoCliente> tipos = new ArrayList<TipoCliente>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    TipoCliente tipo = new TipoCliente();
                    tipo.CODIGO= c.getInt(c.getColumnIndex("CODIGO"));
                    tipo.DESCRIPCION= c.getString(c.getColumnIndex("DESCRIPCION"));
                    tipo.ACTUALIZACION=Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                    tipos.add(tipo);
                }while(c.moveToNext());
            }
        }
        if(c!=null && !c.isClosed()){
            c.close();
        }
        return tipos;
    }
}
