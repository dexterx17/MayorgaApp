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
public class Pedido extends Objeto{
    //variables
    public String DISPOSITIVO;
    public int COD_PEDIDO;
    public Timestamp FECHA;
    public String RUCCI;
    public String VENDEDOR;
    public String RUTA;
    public double LONG;
    public double LAT;
    public String LOCK;
    public Timestamp F_UPDATE;

    public  double Total;
    public String Empresa;

    /**
     * Constructor, crea una instancia vacia de Pedido
     */
    public Pedido() {
    }

    /**
     * Crea un objeto de Pedido con los parametros recibidos
     * @param COD_PEDIDO
     * @param DISPOSITIVO
     * @param FECHA
     * @param RUCCI
     * @param VENDEDOR
     * @param RUTA
     * @param LONG
     * @param LAT
     * @param LOCK
     * @param f_UPDATE
     */
    public Pedido(String DISPOSITIVO, int COD_PEDIDO, Timestamp FECHA, String RUCCI, String VENDEDOR, String RUTA, double LONG, double LAT, String LOCK, Timestamp f_UPDATE) {
        this.COD_PEDIDO = COD_PEDIDO;
        this.DISPOSITIVO = DISPOSITIVO;
        this.FECHA = FECHA;
        this.RUCCI = RUCCI;
        this.VENDEDOR = VENDEDOR;
        this.RUTA = RUTA;
        this.LONG = LONG;
        this.LAT = LAT;
        this.LOCK = LOCK;
        this.F_UPDATE = f_UPDATE;
    }

    /**
     * Crea un objeto Pedido a partir de un Cursor
     * @param c Cursor obtenido de SQLITE3
     */
    public Pedido(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    this.DISPOSITIVO = c.getString(c.getColumnIndex("DIS_CODIGO"));
                    this.COD_PEDIDO = c.getInt(c.getColumnIndex("PED_CODIGO"));
                    this.FECHA = Timestamp.valueOf(c.getString(c.getColumnIndex("FECHA")));
                    this.RUCCI = c.getString(c.getColumnIndex("RUCCI"));
                    this.VENDEDOR = c.getString(c.getColumnIndex("VEN_CODIGO"));
                    this.RUTA = c.getString(c.getColumnIndex("RUTA"));
                    this.LONG = c.getDouble(c.getColumnIndex("LONG"));
                    this.LAT = c.getDouble(c.getColumnIndex("LAT"));
                    this.LOCK = c.getString(c.getColumnIndex("LOCK"));
                    this.F_UPDATE = Timestamp.valueOf(c.getString(c.getColumnIndex("F_UPDATE")));
                } while (c.moveToNext());
            }
        }
    }

    /**
     * Crea un objeto Pedido a partir de un objeto JSONObject
     * @param object
     */
    public Pedido(JSONObject object){
        try{
            this.DISPOSITIVO = object.getString("DIS_CODIGO");
            this.COD_PEDIDO = object.getInt("TRP_CODIGO");
            this.FECHA = Timestamp.valueOf(object.getString("TRP_FECHA"));
            this.RUCCI = object.getString("EMO_RUCCI");
            this.VENDEDOR = object.getString("VEN_CODIGO");
            this.RUTA = object.getString("TRP_RUTA");
            this.LONG = object.getDouble("TRP_LONG");
            this.LAT = object.getDouble("TRP_LAT");
            this.LOCK = object.getString("TRP_LOCK");
            this.F_UPDATE = Timestamp.valueOf(object.getString("TRP_UPDATE"));

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Crea un ArrayList de Pedidos desde un Cursor
     * @param c Cursor obtenido de SQLITE3
     * @return ArrayList
     */
    public static ArrayList<Pedido> fromCursor(Cursor c) {
        ArrayList<Pedido> pedido = new ArrayList<Pedido>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    Pedido pedido1 = new Pedido();
                    pedido1.DISPOSITIVO= c.getString(c.getColumnIndex("DIS_CODIGO"));
                    pedido1.COD_PEDIDO= c.getInt(c.getColumnIndex("PED_CODIGO"));
                    pedido1.FECHA= Timestamp.valueOf(c.getString(c.getColumnIndex("FECHA")));
                    pedido1.RUCCI= c.getString(c.getColumnIndex("RUCCI"));
                    pedido1.VENDEDOR= c.getString(c.getColumnIndex("VEN_CODIGO"));
                    pedido1.RUTA= c.getString(c.getColumnIndex("RUTA"));
                    pedido1.LONG= c.getDouble(c.getColumnIndex("LONG"));
                    pedido1.LAT = c.getDouble(c.getColumnIndex("LAT"));
                    pedido1.LOCK = c.getString(c.getColumnIndex("LOCK"));
                    pedido1.F_UPDATE = Timestamp.valueOf(c.getString(c.getColumnIndex("F_UPDATE")));
                    pedido.add(pedido1);
                }while(c.moveToNext());
            }
        }
        if(c!=null && !c.isClosed()){
            c.close();
        }
        return pedido;
    }

    public static ArrayList<Pedido> fromCursorJoin(Cursor c) {
        ArrayList<Pedido> pedido = new ArrayList<Pedido>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    Pedido pedido1 = new Pedido();
                    pedido1.Empresa = c.getString(c.getColumnIndex("EMPRESA"));
                    pedido1.DISPOSITIVO= c.getString(c.getColumnIndex("DIS_CODIGO"));
                    pedido1.COD_PEDIDO= c.getInt(c.getColumnIndex("PED_CODIGO"));
                    pedido1.FECHA= Timestamp.valueOf(c.getString(c.getColumnIndex("FECHA")));
                    pedido1.RUCCI= c.getString(c.getColumnIndex("RUCCI"));
                    pedido1.Total = c.getDouble(c.getColumnIndex("TOTALXPRODUCTO"));
                    pedido.add(pedido1);
                }while(c.moveToNext());
            }
        }
        if(c!=null && !c.isClosed()){
            c.close();
        }
        return pedido;
    }

    /**
     * Crea un ArrayList de Pedidos de un Objeto JSON
     * @param jsonObjects
     * @return ArrayList
     */
    public static ArrayList<Pedido> fromJson(JSONArray jsonObjects) {
        ArrayList<Pedido> pedido = new ArrayList<Pedido>();
        for (int i = 0; i < jsonObjects.length(); i++)
            try {
                pedido.add(new Pedido(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return pedido;
    }

}
