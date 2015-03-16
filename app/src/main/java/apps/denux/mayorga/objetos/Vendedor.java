package apps.denux.mayorga.objetos;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by dexter on 13/03/15.
 */
public class Vendedor extends Objeto{
    //variables
    public String CEDULA;
    public String APELLIDOS;
    public String NOMBRES;
    public String TELEFONO;
    public Timestamp F_INGRESO;
    public String PASSWORD;
    public String COMISION;
    public Timestamp F_UPDATE;


    /**
     *
     * @param ci
     * @param ape
     * @param nomb
     * @param telf
     * @param f_ingreso
     * @param pass
     * @param comision
     * @param f_update
     */
    public Vendedor (String ci, String ape, String nomb, String telf, Timestamp f_ingreso, String pass, String comision, Timestamp f_update){
        this.CEDULA = ci;
        this.APELLIDOS = ape;
        this.NOMBRES = nomb;
        this.TELEFONO = telf;
        this.F_INGRESO = f_ingreso;
        this.PASSWORD = pass;
        this.COMISION = comision;
        this.F_UPDATE = f_update;
    }

    /**
     *
     * @param c
     */
    public Vendedor(Cursor c){
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    this.CEDULA= c.getString(c.getColumnIndex("CEDULA"));
                    this.APELLIDOS= c.getString(c.getColumnIndex("APELLIDOS"));
                    this.NOMBRES= c.getString(c.getColumnIndex("NOMBRES"));
                    this.TELEFONO= c.getString(c.getColumnIndex("TELEFONO"));
                    this.F_INGRESO= Timestamp.valueOf(c.getString(c.getColumnIndex("F_INGRESO")));
                    this.PASSWORD= c.getString(c.getColumnIndex("PASS"));
                    this.COMISION= c.getString(c.getColumnIndex("COMISION"));
                    this.F_UPDATE = Timestamp.valueOf(c.getString(c.getColumnIndex("F_UPDATE")));
                }while(c.moveToNext());
            }
        }
    }

    /**
     *
     * @param object
     */
    public Vendedor (JSONObject object){
        try{
            this.CEDULA= object.getString("EMPL_CI");
            this.APELLIDOS= object.getString("EMPL_APELLIDOS");
            this.NOMBRES= object.getString("EMPL_NOMBRE");
            this.TELEFONO= object.getString("EMPL_TELEFONO");
            this.F_INGRESO= Timestamp.valueOf(object.getString("EMP_FINGRESO"));
            this.PASSWORD= object.getString("EMPL_PASS");
            this.COMISION= object.getString("EMPL_COMISION");
            this.F_UPDATE = Timestamp.valueOf(object.getString("EMPL_UPDATE"));
            Log.i("Vendedor", this.CEDULA + "" + this.NOMBRES + ':' + this.APELLIDOS);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public Vendedor() {

    }

    /**
     *
     */
    public static ArrayList<Vendedor> fromCursor(Cursor c) {
        ArrayList<Vendedor> vendedor = new ArrayList<Vendedor>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    Vendedor vendedor2 = new Vendedor();
                    vendedor2.CEDULA= c.getString(c.getColumnIndex("CEDULA"));
                    vendedor2.APELLIDOS= c.getString(c.getColumnIndex("APELLIDOS"));
                    vendedor2.NOMBRES= c.getString(c.getColumnIndex("NOMBRES"));
                    vendedor2.TELEFONO= c.getString(c.getColumnIndex("TELEFONO"));
                    vendedor2.F_INGRESO= Timestamp.valueOf(c.getString(c.getColumnIndex("F_INGRESO")));
                    vendedor2.PASSWORD= c.getString(c.getColumnIndex("PASS"));
                    vendedor2.COMISION= c.getString(c.getColumnIndex("COMISION"));
                    vendedor2.F_UPDATE = Timestamp.valueOf(c.getString(c.getColumnIndex("F_UPDATE")));
                    vendedor.add(vendedor2);
                }while(c.moveToNext());
            }
        }
        if(c!=null && !c.isClosed()){
            c.close();
        }
        return vendedor;
    }

    /**
     *
     * @param jsonObjects
     * @return
     */
    public static ArrayList<Vendedor> fromJson(JSONArray jsonObjects) {
        ArrayList<Vendedor> vendedor = new ArrayList<Vendedor>();
        for (int i = 0; i < jsonObjects.length(); i++)
            try {
                vendedor.add(new Vendedor(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return vendedor;
    }
}
