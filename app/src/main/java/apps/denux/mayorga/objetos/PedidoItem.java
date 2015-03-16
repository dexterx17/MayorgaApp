package apps.denux.mayorga.objetos;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by dexter on 15/03/15.
 */
public class PedidoItem extends Objeto  {
    //variables
    public String COD_DISPOSITIVO;
    public int COD_PEDIDO;
    //public int CODIGO3;
    public String COD_PRODUCTO;
    public Double CANTIDAD;
    public Double PRECIO;
    public Double DESCUENTO;
    public Double IVA;
    public Timestamp F_UPDATE;

    public PedidoItem(String COD_DICPOSITIVO, int COD_PEDIDO, String COD_PRODUCTO, Double CANTIDAD, Double PRECIO, Double DESCUENTO, Double IVA, Timestamp f_UPDATE) {
        this.COD_DISPOSITIVO = COD_DICPOSITIVO;
        this.COD_PEDIDO = COD_PEDIDO;
        //this.CODIGO3 = CODIGO3;
        this.COD_PRODUCTO = COD_PRODUCTO;
        this.CANTIDAD = CANTIDAD;
        this.PRECIO = PRECIO;
        this.DESCUENTO = DESCUENTO;
        this.IVA = IVA;
        this.F_UPDATE = f_UPDATE;
    }

    public PedidoItem(Cursor c){
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    this.COD_DISPOSITIVO= c.getString(c.getColumnIndex("DIS_CODIGO"));
                    this.COD_PEDIDO= c.getInt(c.getColumnIndex("PED_CODIGO"));
                    //this.CODIGO3= c.getInt(c.getColumnIndex("TRP_CODIGO"));
                    this.COD_PRODUCTO= c.getString(c.getColumnIndex("PRD_CODIGO"));
                    this.CANTIDAD= c.getDouble(c.getColumnIndex("CANTIDAD"));
                    this.PRECIO= c.getDouble(c.getColumnIndex("PRECIO"));
                    this.DESCUENTO= c.getDouble(c.getColumnIndex("DESCUENTO"));
                    this.IVA = c.getDouble(c.getColumnIndex("IVA"));
                    this.F_UPDATE = Timestamp.valueOf(c.getString(c.getColumnIndex("F_UPDATE")));
                }while(c.moveToNext());
            }
        }
    }

    /**
     *
     * @param object
     */
    public PedidoItem (JSONObject object){
        try{
            this.COD_DISPOSITIVO= object.getString("DIS_CODIGO");
            this.COD_PEDIDO= object.getInt("PED_CODIGO");
            //this.CODIGO3= object.getInt("TRP_CODIGO");
            this.COD_PRODUCTO= object.getString("PRD_CODIGO");
            this.CANTIDAD= object.getDouble("PED_CANT");
            this.PRECIO= object.getDouble("PED_PRECIO");
            this.DESCUENTO= object.getDouble("PED_DSCTO");
            this.IVA= object.getDouble("PED_IVA");
            this.F_UPDATE= Timestamp.valueOf(object.getString("PED_UPDATE"));

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public PedidoItem() {

    }

    /**
     *
     */
    public static ArrayList<PedidoItem> fromCursor(Cursor c) {
        ArrayList<PedidoItem> pedidoItems = new ArrayList<PedidoItem>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    PedidoItem vendedor2 = new PedidoItem();
                    vendedor2.COD_DISPOSITIVO= c.getString(c.getColumnIndex("DIS_CODIGO"));
                    vendedor2.COD_PEDIDO= c.getInt(c.getColumnIndex("PED_CODIGO"));
                    //vendedor2.CODIGO3= c.getInt(c.getColumnIndex("TRP_CODIGO"));
                    vendedor2.COD_PRODUCTO= c.getString(c.getColumnIndex("PRD_CODIGO"));
                    vendedor2.CANTIDAD= c.getDouble(c.getColumnIndex("CANTIDAD"));
                    vendedor2.PRECIO= c.getDouble(c.getColumnIndex("PRECIO"));
                    vendedor2.DESCUENTO= c.getDouble(c.getColumnIndex("DESCUENTO"));
                    vendedor2.IVA= c.getDouble(c.getColumnIndex("IVA"));
                    vendedor2.F_UPDATE = Timestamp.valueOf(c.getString(c.getColumnIndex("F_UPDATE")));
                    pedidoItems.add(vendedor2);
                }while(c.moveToNext());
            }
        }
        if(c!=null && !c.isClosed()){
            c.close();
        }
        return pedidoItems;
    }

    /**
     *
     * @param jsonObjects
     * @return
     */
    public static ArrayList<PedidoItem> fromJson(JSONArray jsonObjects) {
        ArrayList<PedidoItem> pedidoItems = new ArrayList<PedidoItem>();
        for (int i = 0; i < jsonObjects.length(); i++)
            try {
                pedidoItems.add(new PedidoItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return pedidoItems;
    }

    public JSONObject getJSON(Cursor cursor){
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            while (cursor.moveToNext()){
                jsonArray.put(cursor.getString(0));
                jsonArray.put(cursor.getString(1));
                jsonArray.put(cursor.getString(2));
                jsonArray.put(cursor.getString(3));
                jsonArray.put(cursor.getString(4));
                jsonArray.put(cursor.getString(5));
                jsonArray.put(cursor.getString(6));
                jsonArray.put(cursor.getString(7));
            }
            jsonObject.put("Detalle",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray getJSONArray(){
        String s = "";
        JSONArray jsonArray = new JSONArray();

        jsonArray.put(this.COD_DISPOSITIVO);
        jsonArray.put(this.COD_PEDIDO);
        jsonArray.put(this.COD_PRODUCTO);
        jsonArray.put(this.CANTIDAD);
        jsonArray.put(this.PRECIO);
        jsonArray.put(this.DESCUENTO);
        jsonArray.put(this.IVA);
        jsonArray.put(this.F_UPDATE.toString());

        return jsonArray;
    }

    public JSONObject getJSON2(Cursor cursor){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DIS_CODIGO",cursor.getString(0));
            jsonObject.put("PED_CODIGO", cursor.getString(1));
            jsonObject.put("TRP_CODIGO", cursor.getString(2));
            jsonObject.put("CANTIDAD", cursor.getString(3));
            jsonObject.put("PRECIO", cursor.getString(4));
            jsonObject.put("DESCUENTO", cursor.getString(5));
            jsonObject.put("IVA", cursor.getString(6));
            jsonObject.put("F_UPDATE", cursor.getString(7));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
