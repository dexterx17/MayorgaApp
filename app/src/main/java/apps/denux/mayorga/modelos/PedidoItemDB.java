package apps.denux.mayorga.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

import apps.denux.mayorga.helpers.DBHelper;
import apps.denux.mayorga.objetos.PedidoItem;

/**
 * Created by dexter on 15/03/15.
 */
public class PedidoItemDB  {

    DBHelper db;
    private static final String TABLA_PEDIDOITEM = "PEDIDOITEM";
    private static final String C_COD_DISPOSITIVO = "DIS_CODIGO";
    private static final String C_COD_PEDIDO = "PED_CODIGO";
    //private static final String C_CODIGO3 = "TRP_CODIGO";
    private static final String C_COD_PRODUCTO = "PRD_CODIGO";
    private static final String C_CANTIDAD = "CANTIDAD";
    private static final String C_PRECIO = "PRECIO";
    private static final String C_DESCUENTO = "DESCUENTO";
    private static final String C_IVA = "IVA";
    private static final String C_UPDATE = "F_UPDATE";

    private static final String CREATE_TABLE = "CREATE TABLE "+ TABLA_PEDIDOITEM +"("
            +C_COD_DISPOSITIVO +" VARCHAR(20) NOT NULL, "
            +C_COD_PEDIDO +" INT NOT NULL, "
            //+C_CODIGO3 +" INT NOT NULL, "
            +C_COD_PRODUCTO +" VARCHAR(20) NOT NULL, "
            +C_CANTIDAD +" DOUBLE  NULL, "
            +C_PRECIO +" DOUBLE  NULL, "
            +C_DESCUENTO +" DOUBLE , "
            +C_IVA +" DOUBLE ,"
            +C_UPDATE +" TIMESTAMP ,"
            +"PRIMARY KEY ("+C_COD_PEDIDO+", "+C_COD_DISPOSITIVO+","+C_COD_PRODUCTO+")"
            +");";

    private static  final String borrarTabla ="DROP TABLE IF EXISTS "+TABLA_PEDIDOITEM+";";
    //private static  final String borrarRegistros ="delete from "+TABLA_PEDIDOITEM+";";
    public PedidoItemDB(Context context) { db=DBHelper.getDBHelperInstance(context);
    }

    public static void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        Log.i("SQLite", "Se crea la tabla pedidioitem" + CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_PEDIDOITEM);
        db.execSQL(CREATE_TABLE);
        Log.i("SQLite", "Se recrea la tabla pedidoitem");
    }

    public void open() throws SQLException {
        db.open();
        db.getWritableDatabase();
    }

    public PedidoItem get(String codigo) {
        Cursor cursor= db.select("SELECT * FROM PEDIDOITEM WHERE PED_CODIGO = ? ",new String[]{codigo});
        PedidoItem pedidoItem = new PedidoItem(cursor);
        cursor.close();
        return pedidoItem;
    }
    public Cursor get(int codigo) {
        Cursor cursor= db.select("SELECT * FROM PEDIDOITEM WHERE PED_CODIGO = ? ",new String[]{String.valueOf(codigo)});
        return cursor;
    }
    public Cursor getCursor(int codigo) {
        Cursor cursor= db.select("SELECT PI.PRD_CODIGO, (SELECT NOMBRE FROM PRODUCTOS WHERE CODIGO = PI.PRD_CODIGO) AS PRODUCTO, "+
                "PRECIO, IVA, CANTIDAD "+
                "FROM PEDIDOITEM PI WHERE PED_CODIGO = ? ",new String[]{String.valueOf(codigo)});
        return cursor;
    }


    public void close() {
        db.close();
    }
    public long insert(PedidoItem pedidoItem){
        ContentValues valores = new ContentValues();
        valores.put(C_COD_DISPOSITIVO, pedidoItem.COD_DISPOSITIVO);
        valores.put(C_COD_PEDIDO,pedidoItem.COD_PEDIDO);
        //valores.put(C_CODIGO3,pedidoItem.CODIGO3);
        valores.put(C_COD_PRODUCTO,pedidoItem.COD_PRODUCTO);
        valores.put(C_CANTIDAD,pedidoItem.CANTIDAD);
        valores.put(C_PRECIO,pedidoItem.PRECIO);
        valores.put(C_DESCUENTO,pedidoItem.DESCUENTO);
        valores.put(C_IVA,pedidoItem.IVA);
        valores.put(C_UPDATE, String.valueOf(pedidoItem.F_UPDATE));

        return db.insert(TABLA_PEDIDOITEM,valores);
    }
    public boolean update(PedidoItem pedidoItem){
        ContentValues valores = new ContentValues();
        valores.put(C_COD_PEDIDO,pedidoItem.COD_PEDIDO);
        //valores.put(C_CODIGO3,pedidoItem.CODIGO3);
        valores.put(C_COD_PRODUCTO,pedidoItem.COD_PRODUCTO);
        valores.put(C_CANTIDAD,pedidoItem.CANTIDAD);
        valores.put(C_PRECIO,pedidoItem.PRECIO);
        valores.put(C_DESCUENTO,pedidoItem.DESCUENTO);
        valores.put(C_IVA,pedidoItem.IVA);
        valores.put(C_UPDATE, String.valueOf(pedidoItem.F_UPDATE));
        //String where = C_COD_DISPOSITIVO +" = ? AND "+C_COD_PEDIDO+" = ? ";
        //String[] whereArgs = {pedidoItem.COD_DISPOSITIVO, String.valueOf(pedidoItem.COD_PEDIDO)};
        String where = C_COD_PEDIDO+" = ? ";
        String[] whereArgs = {String.valueOf(pedidoItem.COD_PEDIDO)};
        return db.update(TABLA_PEDIDOITEM,valores,where,whereArgs)>0;
    }

    public boolean delete (PedidoItem pedidoItem){
        String where =  C_COD_DISPOSITIVO +" = ? AND "+C_COD_PEDIDO+" = ?";// AND "+C_COD_PRODUCTO+" = ?";
        String[] whereArgs = {pedidoItem.COD_DISPOSITIVO, String.valueOf(pedidoItem.COD_PEDIDO)};//, String.valueOf(pedidoItem.COD_PRODUCTO)};
        return db.delete(TABLA_PEDIDOITEM,where,whereArgs)>0;
    }

}
