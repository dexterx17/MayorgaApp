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
public class Cliente extends Objeto{
    public int CODIGO;
    public CharSequence RUCCI;
    public int TIPO;
    public int IDENTIFICACION;
    public int SUCURSAL;
    public CharSequence EMPRESA;
    public CharSequence REPRESENTANTE;
    public CharSequence CIUDAD;
    public CharSequence DIRECCION;
    public CharSequence DIRECCION2;
    public CharSequence TELFAX;
    public CharSequence TELFAX2;
    public CharSequence PROPIETARIO;
    public Timestamp FINGRESO;
    public boolean EMP_PUBLICA;
    public CharSequence ZONA;
    public int VENDEDOR;
    public Timestamp ACTUALIZACION;
    public Timestamp ACTUALIZACION_LOCAL;


    /**
     * Constructor, crea una instancia vacia de Cliente
     */
    public Cliente(){

    }

    /**
     * Crear un objeto cliente solo con el ID
     * @param RUCCI
     */
    public Cliente(CharSequence RUCCI){
        this.RUCCI=RUCCI;
    }
    /**
     * Crear un objeto Cliente con los parametros recibidos
     * @param CODIGO
     * @param RUCCI
     * @param TIPO
     * @param IDENTIFICACION
     * @param SUCURSAL
     * @param EMPRESA
     * @param REPRESENTANTE
     * @param CIUDAD
     * @param DIRECCION
     * @param DIRECCION2
     * @param TELFAX
     * @param TELFAX2
     * @param PROPIETARIO
     * @param EMP_PUBLICA
     * @param VENDEDOR
     * @param ACTUALIZACION
     *
     */
    public Cliente(int CODIGO, CharSequence RUCCI, int TIPO, int IDENTIFICACION,int SUCURSAL, CharSequence EMPRESA, CharSequence REPRESENTANTE, CharSequence CIUDAD, CharSequence DIRECCION, CharSequence DIRECCION2, CharSequence TELFAX, CharSequence TELFAX2, CharSequence PROPIETARIO, boolean EMP_PUBLICA, int VENDEDOR,Timestamp ACTUALIZACION,Timestamp ACTUALIZACION_LOCAL) {
        this.CODIGO = CODIGO;
        this.RUCCI = RUCCI;
        this.TIPO = TIPO;
        this.IDENTIFICACION = IDENTIFICACION;
        this.SUCURSAL=SUCURSAL;
        this.EMPRESA = EMPRESA;
        this.REPRESENTANTE = REPRESENTANTE;
        this.CIUDAD = CIUDAD;
        this.DIRECCION = DIRECCION;
        this.DIRECCION2 = DIRECCION2;
        this.TELFAX = TELFAX;
        this.TELFAX2 = TELFAX2;
        this.PROPIETARIO = PROPIETARIO;
        this.EMP_PUBLICA = EMP_PUBLICA;
        this.VENDEDOR = VENDEDOR;
        this.ACTUALIZACION=ACTUALIZACION;
        this.ACTUALIZACION_LOCAL=ACTUALIZACION_LOCAL;
    }

    /**
     * Crea un objeto Cliente desde un Cursor
     * @param c Cursor obtenido de SQLITE3
     */
    public Cliente(Cursor c){
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    this.CODIGO= c.getInt(c.getColumnIndex("CODIGO"));
                    this.RUCCI= c.getString(c.getColumnIndex("RUCCI"));
                    this.TIPO= c.getInt(c.getColumnIndex("TIPO"));
                    this.IDENTIFICACION= c.getInt(c.getColumnIndex("IDENTIFICACION"));
                    this.SUCURSAL = c.getInt(c.getColumnIndex("SUCURSAL"));
                    this.EMPRESA = c.getString(c.getColumnIndex("EMPRESA"));
                    this.FINGRESO = Timestamp.valueOf(c.getString(c.getColumnIndex("FINGRESO")));
                    this.REPRESENTANTE = c.getString(c.getColumnIndex("REPRESENTANTE"));
                    this.CIUDAD = c.getString(c.getColumnIndex("CIUDAD"));
                    this.DIRECCION = c.getString(c.getColumnIndex("DIRECCION"));
                    this.DIRECCION2 = c.getString(c.getColumnIndex("DIRECCION2"));
                    this.TELFAX = c.getString(c.getColumnIndex("TELFAX"));
                    this.TELFAX2= c.getString(c.getColumnIndex("TELFAX2"));
                    this.PROPIETARIO = c.getString(c.getColumnIndex("PROPIETARIO"));
                    String ep=c.getString(c.getColumnIndex("EMP_PUBLICA"));
                    this.EMP_PUBLICA = (ep=="N" || ep=="0")?false:true;
                    this.VENDEDOR = c.getInt(c.getColumnIndex("VENDEDOR"));
                    this.ACTUALIZACION =Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                    this.ACTUALIZACION_LOCAL =Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                    this.ZONA= c.getString(c.getColumnIndex("ZONA"));
                    Log.i("Cliente", this.RUCCI + "" + this.EMPRESA + ':' + this.FINGRESO + "/" + this.ACTUALIZACION);
                }while(c.moveToNext());
            }
        }
        return;
    }

    /**
     * Crea un objeto Cliente en base un objeto JSONObject
     * @param object JSONObject con la información de un cliente
     */
    public Cliente(JSONObject object){
        try{
            this.CODIGO = object.getInt("EMP_CODIGO");
            this.RUCCI = object.getString("EMP_RUCCI");
            this.TIPO = object.getInt("TEM_CODIGO");
            this.IDENTIFICACION = object.getInt("TID_CODIGO");
            this.EMPRESA = object.getString("EMP_EMPRESA");
            this.REPRESENTANTE = object.getString("EMP_REPRESENTANTE");
            this.CIUDAD = object.getString("EMP_CIUDAD");
            this.DIRECCION = object.getString("EMP_DIRECCION");
            this.DIRECCION2 = object.getString("EMP_DIR2");
            this.FINGRESO = Timestamp.valueOf(object.getString("EMP_FINGRESO"));
            this.TELFAX = object.getString("EMP_TELFAX");
            this.TELFAX2 = object.getString("EMP_TELFAX2");
            this.PROPIETARIO = object.getString("EMP_PROPIETARIO");
            this.EMP_PUBLICA = object.getString("EMP_PUBLICA")=="N"?false:true;
            this.VENDEDOR = (object.get("VEN_CODIGO").equals(JSONObject.NULL)|| object.get("VEN_CODIGO").equals(""))? 0 : object.getInt("VEN_CODIGO");
            this.ACTUALIZACION = Timestamp.valueOf(object.getString("EMP_UPDATE"));
            this.ZONA=  object.getString("EMP_ZONA");
            //this.ACTUALIZACION_LOCAL = Timestamp.valueOf(object.getString("EMP_REMOTE_UPDATE"));
            Log.i("Cliente", this.RUCCI + "" + this.EMPRESA+':'+this.VENDEDOR);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Crea un ArrayList de Cliente desde un Cursor
     * @param c Cursor obtenido de SQLITE3
     * @return ArrayList
     */
    public static ArrayList<Cliente> fromCursor(Cursor c) {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    Cliente client = new Cliente();
                    client.CODIGO= c.getInt(c.getColumnIndex("CODIGO"));
                    client.RUCCI= c.getString(c.getColumnIndex("RUCCI"));
                    client.TIPO= c.getInt(c.getColumnIndex("TIPO"));
                    client.IDENTIFICACION= c.getInt(c.getColumnIndex("IDENTIFICACION"));
                    client.SUCURSAL = c.getInt(c.getColumnIndex("SUCURSAL"));
                    client.EMPRESA = c.getString(c.getColumnIndex("EMPRESA"));
                    client.REPRESENTANTE = c.getString(c.getColumnIndex("REPRESENTANTE"));
                    client.CIUDAD = c.getString(c.getColumnIndex("CIUDAD"));
                    client.DIRECCION = c.getString(c.getColumnIndex("DIRECCION"));
                    client.DIRECCION2 = c.getString(c.getColumnIndex("DIRECCION2"));
                    client.FINGRESO =Timestamp.valueOf(c.getString(c.getColumnIndex("FINGRESO")));
                    client.TELFAX = c.getString(c.getColumnIndex("TELFAX"));
                    client.TELFAX2= c.getString(c.getColumnIndex("TELFAX2"));
                    client.PROPIETARIO = c.getString(c.getColumnIndex("PROPIETARIO"));
                    String ep=c.getString(c.getColumnIndex("EMP_PUBLICA"));
                    client.EMP_PUBLICA = (ep=="N" || ep=="0")?false:true;
                    client.VENDEDOR = c.getInt(c.getColumnIndex("VENDEDOR"));
                    client.ACTUALIZACION =Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                    client.ACTUALIZACION_LOCAL = Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                    client.ZONA= c.getString(c.getColumnIndex("ZONA"));
                    clientes.add(client);
                }while(c.moveToNext());
            }
        }
        if(c!=null && !c.isClosed()){
            c.close();
        }
        return clientes;
    }

    /**
     * Crear un ArrayList de Cliente desde un Objeto JSON
     * @param jsonObjects
     * @return ArrayList
     */
    public static ArrayList<Cliente> fromJson(JSONArray jsonObjects) {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        for (int i = 0; i < jsonObjects.length(); i++)
            try {
                Log.i("Cliente", ":"+i);
                clientes.add(new Cliente(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return clientes;
    }

    public JSONObject getJSON(){
        JSONObject object = new JSONObject();
        try {
            object.put("CODIGO",this.CODIGO);
            object.put("RUCCI", this.RUCCI.toString().trim());
            object.put("TIPO", this.TIPO);
            object.put("IDENTIFICACION", this.IDENTIFICACION);
            object.put("SUCURSAL", this.SUCURSAL);
            object.put("EMPRESA", this.EMPRESA.toString().trim());
            object.put("REPRESENTANTE", this.REPRESENTANTE.toString().trim());
            object.put("CIUDAD", this.CIUDAD.toString().trim());
            object.put("DIRECCION", this.DIRECCION.toString().trim());
            object.put("DIRECCION2", this.DIRECCION2.toString().trim());
            object.put("TELFAX", this.TELFAX.toString().trim());
            object.put("TELFAX2", this.TELFAX2.toString().trim());
            object.put("FINGRESO",this.FINGRESO.toString());
            object.put("PROPIETARIO", this.PROPIETARIO).toString().trim();
            object.put("EMP_PUBLICA", this.EMP_PUBLICA);
            object.put("VENDEDOR", this.VENDEDOR);
            object.put("ZONA", this.ZONA);
            object.put("ACTUALIZACION", this.ACTUALIZACION.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
