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
public class Producto extends Objeto{

    public String CODIGO;
    public String NOMBRE;
    public String MARCA;
    public double IVA;
    public double EXISTENCIA;
    public String BARRA;
    public double PRECIO1;
    public double PRECIO2;
    public double PRECIO3;
    public double PRECIO4;
    public String UNIDAD;
    public double PESO;
    public boolean DESTACADO;
    public Timestamp ACTUALIZACION;

    /**
     * Constructor para inicializar variables
     * @param CODIGO
     * @param NOMBRE
     * @param MARCA
     * @param IVA
     * @param EXISTENCIA
     * @param BARRA
     * @param PRECIO1
     * @param PRECIO2
     * @param PRECIO3
     * @param PRECIO4
     * @param UNIDAD
     * @param PESO
     */
    public Producto(String CODIGO, String NOMBRE, String MARCA, double IVA, double EXISTENCIA, String BARRA, double PRECIO1, double PRECIO2, double PRECIO3, double PRECIO4, String UNIDAD, double PESO, Boolean destacado, Timestamp ACTUALIZACION) {
        this.CODIGO = CODIGO;
        this.NOMBRE = NOMBRE;
        this.MARCA = MARCA;
        this.IVA = IVA;
        this.EXISTENCIA = EXISTENCIA;
        this.BARRA = BARRA;
        this.PRECIO1 = PRECIO1;
        this.PRECIO2 = PRECIO2;
        this.PRECIO3 = PRECIO3;
        this.PRECIO4 = PRECIO4;
        this.UNIDAD = UNIDAD;
        this.PESO = PESO;
        this.DESTACADO = destacado;
        this.ACTUALIZACION=ACTUALIZACION;
    }
    public Producto(Cursor c){
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    this.CODIGO= c.getString(c.getColumnIndex("CODIGO"));
                    this.NOMBRE= c.getString(c.getColumnIndex("NOMBRE"));
                    this.MARCA= c.getString(c.getColumnIndex("MARCA"));
                    this.IVA= c.getInt(c.getColumnIndex("IVA"));
                    this.EXISTENCIA = c.getInt(c.getColumnIndex("EXISTENCIA"));
                    this.BARRA = c.getString(c.getColumnIndex("BARRA"));
                    this.PRECIO1 = c.getDouble(c.getColumnIndex("PRECIO1"));
                    this.PRECIO2 = c.getDouble(c.getColumnIndex("PRECIO2"));
                    this.PRECIO3 = c.getDouble(c.getColumnIndex("PRECIO3"));
                    this.PRECIO4= c.getDouble(c.getColumnIndex("PRECIO4"));
                    this.UNIDAD = c.getString(c.getColumnIndex("UNIDAD"));
                    this.PESO = c.getDouble(c.getColumnIndex("PESO"));
                    this.DESTACADO = Boolean.parseBoolean(c.getString(c.getColumnIndex("DESTACADO")));
                    this.ACTUALIZACION =Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                    Log.i("Producto", this.CODIGO + "" + this.NOMBRE + ':' + this.PRECIO1 + ":" + this.PESO);
                }while(c.moveToNext());
            }
        }
    }

    public Producto(JSONObject object){
        try{
            this.CODIGO = object.getString("PRD_CODIGO");
            this.NOMBRE = object.getString("PRD_NOMBRE");
            this.MARCA = object.getString("PRD_MARCA");
            this.IVA = object.getDouble("PRD_IVA");
            this.EXISTENCIA = object.getDouble("PRD_EXI");
            this.BARRA = object.getString("PRD_BARRA");
            this.PRECIO1 = object.getDouble("PRD_PRECIO1");
            this.PRECIO2 = (object.get("PRD_PRECIO2").equals(JSONObject.NULL)|| object.get("PRD_PRECIO2").equals(""))? 0 :object.getDouble("PRD_PRECIO2");
            this.PRECIO3 = (object.get("PRD_PRECIO3").equals(JSONObject.NULL)|| object.get("PRD_PRECIO3").equals(""))? 0 :object.getDouble("PRD_PRECIO3");
            this.PRECIO4 = (object.get("PRD_PRECIO4").equals(JSONObject.NULL)|| object.get("PRD_PRECIO4").equals(""))? 0 :object.getDouble("PRD_PRECIO4");
            this.UNIDAD = object.getString("UND_CODIGO");
            this.PESO = (object.get("PRD_PESO").equals(JSONObject.NULL)|| object.get("PRD_PESO").equals(""))? 0 :object.getDouble("PRD_PESO");
            this.ACTUALIZACION = Timestamp.valueOf(object.getString("PRD_UPDATE"));
            Log.i("Producto", this.CODIGO + "" + this.NOMBRE + ':' + this.PRECIO1);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public Producto() {

    }

    public static ArrayList<Producto> fromCursor(Cursor c) {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    Producto Producto2 = new Producto();
                    Producto2.CODIGO= c.getString(c.getColumnIndex("CODIGO"));
                    Producto2.NOMBRE= c.getString(c.getColumnIndex("NOMBRE"));
                    Producto2.MARCA= c.getString(c.getColumnIndex("MARCA"));
                    Producto2.IVA= c.getDouble(c.getColumnIndex("IVA"));
                    Producto2.EXISTENCIA= c.getDouble(c.getColumnIndex("EXISTENCIA"));
                    Producto2.BARRA= c.getString(c.getColumnIndex("BARRA"));
                    Producto2.PRECIO1 = c.getDouble(c.getColumnIndex("PRECIO1"));
                    Producto2.PRECIO2 = c.getDouble(c.getColumnIndex("PRECIO2"));
                    Producto2.PRECIO3 = c.getDouble(c.getColumnIndex("PRECIO3"));
                    Producto2.PRECIO4 = c.getDouble(c.getColumnIndex("PRECIO4"));
                    Producto2.UNIDAD = c.getString(c.getColumnIndex("UNIDAD"));
                    Producto2.PESO = c.getDouble(c.getColumnIndex("PESO"));
                    Producto2.DESTACADO = Boolean.parseBoolean(c.getString(c.getColumnIndex("DESTACADO")));
                    Producto2.ACTUALIZACION =Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                    productos.add(Producto2);
                }while(c.moveToNext());
            }
        }
        if(c!=null && !c.isClosed()){
            c.close();
        }
        return productos;
    }

    public static ArrayList<Producto> fromJson(JSONArray jsonObjects) {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        for (int i = 0; i < jsonObjects.length(); i++)
            try {
                productos.add(new Producto(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return productos;
    }
}
