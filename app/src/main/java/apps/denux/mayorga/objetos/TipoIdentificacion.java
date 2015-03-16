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
public class TipoIdentificacion extends Objeto {
    public int CODIGO;
    public CharSequence DESCRIPCION;
    public Timestamp ACTUALIZACION;

    /**
     * Crea un objeto TipoIdentificacion
     * @param CODIGO
     * @param DESCRIPCION
     */
    public TipoIdentificacion(int CODIGO, CharSequence DESCRIPCION) {
        this.CODIGO = CODIGO;
        this.DESCRIPCION = DESCRIPCION;
    }

    /**
     * Crea un objeto TipoIdentificacion
     * @param CODIGO
     * @param DESCRIPCION
     * @param ACTUALIZACION
     */
    public TipoIdentificacion(int CODIGO, CharSequence DESCRIPCION, Timestamp ACTUALIZACION) {
        this.CODIGO = CODIGO;
        this.DESCRIPCION = DESCRIPCION;
        this.ACTUALIZACION = ACTUALIZACION;
    }

    /**
     * Crea un objeto TipoIdentificacion en base a un objeto JSONObject
     * @param object JSONObject con la información de un cliente
     */
    public TipoIdentificacion(JSONObject object){
        try{
            this.CODIGO = object.getInt("TID_CODIGO");
            this.DESCRIPCION = object.getString("TID_IDENTIFICACION");
            this.ACTUALIZACION = Timestamp.valueOf(object.getString("TID_UPDATE"));
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Crea un objeto TipoIdentificacion vacio
     */
    public TipoIdentificacion() { }

    /**
     * Devuelve un ArrayList de TipoIdentificacion
     * @param jsonObjects
     * @return ArrayList
     */
    public static ArrayList<TipoIdentificacion> fromJson(JSONArray jsonObjects) {
        ArrayList<TipoIdentificacion> tipos = new ArrayList<TipoIdentificacion>();
        for (int i = 0; i < jsonObjects.length(); i++)
            try {
                tipos.add(new TipoIdentificacion(jsonObjects.getJSONObject(i)));
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
    public static ArrayList<TipoIdentificacion> fromCursor(Cursor c) {
        ArrayList<TipoIdentificacion> tipos = new ArrayList<TipoIdentificacion>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    TipoIdentificacion tipo = new TipoIdentificacion();
                    tipo.CODIGO= c.getInt(c.getColumnIndex("CODIGO"));
                    tipo.DESCRIPCION= c.getString(c.getColumnIndex("IDENTIFICACION"));
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
