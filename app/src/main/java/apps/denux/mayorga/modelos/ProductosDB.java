package apps.denux.mayorga.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import apps.denux.mayorga.helpers.DBHelper;
import apps.denux.mayorga.objetos.Producto;

/**
 * Created by dexter on 13/03/15.
 */
public class ProductosDB {

    DBHelper db;
    //Variables
    private static final String TABLE_PRODUCTOS = "PRODUCTOS";
    private static final String C_CODIGO = "CODIGO";
    private static final String C_NOMBRE = "NOMBRE";
    private static final String C_MARCA = "MARCA";
    private static final String C_IVA = "IVA";
    private static final String C_EXISTENCIA = "EXISTENCIA";
    private static final String C_BARRA = "BARRA";
    private static final String C_PRECIO1 = "PRECIO1";
    private static final String C_PRECIO2 = "PRECIO2";
    private static final String C_PRECIO3 = "PRECIO3";
    private static final String C_PRECIO4 = "PRECIO4";
    private static final String C_UNIDAD = "UNIDAD";
    private static final String C_PESO = "PESO";
    private static final String C_DESTACADO = "DESTACADO";
    private static final String C_ACTUALIZACION ="ACTUALIZACION";

    //Crear la tabla
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_PRODUCTOS+" ( "+
            C_CODIGO +" VARCHAR(20) PRIMARY KEY,"+
            C_NOMBRE +" VARCHAR(120) NOT NULL,"+
            C_MARCA+  " VARCHAR(25) NOT NULL, "+
            C_IVA+  " DOUBLE, "+
            C_EXISTENCIA+  " DOUBLE, "+
            C_BARRA + " VARCHAR(30), "+
            C_PRECIO1+  " DOUBLE, "+
            C_PRECIO2+  " DOUBLE, "+
            C_PRECIO3+  " DOUBLE, "+
            C_PRECIO4+  " DOUBLE, "+
            C_UNIDAD+  " VARCHAR(10), "+
            C_PESO+  " DOUBLE,"+
            C_DESTACADO+  " BOOLEAN DEFAULT FALSE,"+
            C_ACTUALIZACION + " datetime "+
            " );";

    public ProductosDB(Context context) { this.db = DBHelper.getDBHelperInstance(context); }

    public static void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        Log.i("SQLite", "Se crea la tabla");
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PRODUCTOS);
        db.execSQL(CREATE_TABLE);
        Log.i("SQLite", "Se recrea la tabla Clinte");
    }
    public void open() throws SQLException {
        db.open();
        db.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    /**
     * Devuelve un objeto producto en base a su CODIGO
     * @param codigo
     * @return
     */
    public Producto get(String codigo) {
        Cursor cursor= db.select("SELECT * FROM  PRODUCTOS WHERE CODIGO = ?",new String[]{codigo});
        Producto producto = new Producto(cursor);
        cursor.close();
        return producto;
    }

    public ArrayList<Producto> getList() {
        Cursor cursor= db.select("SELECT * FROM PRODUCTOS ORDER BY NOMBRE ASC",null);
        ArrayList<Producto> productos = Producto.fromCursor(cursor);
        cursor.close();
        return productos;
    }

    public ArrayList<Producto> getList(String nameProduct) {
        Cursor cursor= db.select("SELECT * FROM PRODUCTOS WHERE NOMBRE LIKE ? ORDER BY NOMBRE ASC ",new String[]{"%"+nameProduct+"%"});
        ArrayList<Producto> productos = Producto.fromCursor(cursor);
        cursor.close();
        return productos;
    }

    public ArrayList<Double> getPrecios(String codigo) {
        Cursor cursor= db.select("SELECT NOMBRE, IVA, PRECIO1, PRECIO2, PRECIO3, PRECIO4 FROM  PRODUCTOS WHERE CODIGO = ?",new String[]{codigo});
        ArrayList<Double> precios = new ArrayList<Double>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do{
                    precios.add(cursor.getDouble(cursor.getColumnIndex(ProductosDB.C_PRECIO1)));
                    precios.add(cursor.getDouble(cursor.getColumnIndex(ProductosDB.C_PRECIO2)));
                    precios.add(cursor.getDouble(cursor.getColumnIndex(ProductosDB.C_PRECIO3)));
                    precios.add(cursor.getDouble(cursor.getColumnIndex(ProductosDB.C_PRECIO4)));
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return precios;
    }

    public ArrayList<Producto> getListDestacados() {
        Cursor cursor= db.select("SELECT * FROM PRODUCTOS WHERE DESTACADO = 'true' ORDER BY NOMBRE ASC ", null);
        ArrayList<Producto> productos = Producto.fromCursor(cursor);
        cursor.close();
        return productos;
    }

    public long insert(Producto producto){
        ContentValues valores = new ContentValues();
        valores.put(C_CODIGO, (String) producto.CODIGO);
        valores.put(C_NOMBRE, (String) producto.NOMBRE);
        valores.put(C_MARCA, (String) producto.MARCA);
        valores.put(C_IVA,producto.IVA);
        valores.put(C_EXISTENCIA,producto.EXISTENCIA);
        valores.put(C_BARRA, (String) producto.BARRA);
        valores.put(C_PRECIO1, producto.PRECIO1);
        valores.put(C_PRECIO2, producto.PRECIO2);
        valores.put(C_PRECIO3, producto.PRECIO3);
        valores.put(C_PRECIO4, producto.PRECIO4);
        valores.put(C_UNIDAD,(String) producto.UNIDAD);
        valores.put(C_PESO, producto.PESO);
        valores.put(C_ACTUALIZACION, String.valueOf(producto.ACTUALIZACION));
        return db.insert(TABLE_PRODUCTOS,valores);
    }


    public boolean update(Producto producto){
        ContentValues valores = new ContentValues();
        valores.put(C_NOMBRE,(String) producto.NOMBRE);
        valores.put(C_MARCA,(String) producto.MARCA);
        valores.put(C_IVA,producto.IVA);
        valores.put(C_EXISTENCIA,producto.EXISTENCIA);
        valores.put(C_BARRA,(String) producto.BARRA);
        valores.put(C_PRECIO1, producto.PRECIO1);
        valores.put(C_PRECIO2, producto.PRECIO2);
        valores.put(C_PRECIO3, producto.PRECIO3);
        valores.put(C_PRECIO4, producto.PRECIO4);
        valores.put(C_UNIDAD, (String) producto.UNIDAD);
        valores.put(C_PESO, producto.PESO);
        valores.put(C_DESTACADO, String.valueOf(producto.DESTACADO));
        valores.put(C_ACTUALIZACION, String.valueOf(producto.ACTUALIZACION));

        String where = C_CODIGO +" = ?";
        String[] whereArgs = {producto.CODIGO};

        return db.update(TABLE_PRODUCTOS,valores,where,whereArgs)>0;
    }

    public boolean DestacarProducto(Producto producto, boolean destacado){
        ContentValues valores = new ContentValues();
        valores.put(C_DESTACADO, String.valueOf(destacado));
        String where = C_CODIGO +" = ? ";
        String[] whereArgs = {producto.CODIGO};
        return db.update(TABLE_PRODUCTOS,valores,where,whereArgs)>0;
    }

    public boolean delete(Producto producto){
        String where = C_CODIGO +" = ? ";
        String[] whereArgs = {producto.CODIGO};
        return db.delete(TABLE_PRODUCTOS,where,whereArgs)>0;
    }

    public boolean exist(String codigo){
        Cursor cursor= db.select("select * from PRODUCTOS WHERE CODIGO = ?",new String[]{codigo});
        boolean res= cursor.getCount()>0;
        cursor.close();
        return res;
    }


    /**
     * Obtiene la ultima fecha en la que se actualizó algun elemento
     * @return long Milisegundos en los que se realizo la ultima actualización
     */
    public long getLastUpdate(){
        Calendar cal = Calendar.getInstance();
        cal.set(1990,2,8,8,30);
        Timestamp fecha = new Timestamp(cal.getTimeInMillis());
        Log.i("fecha",fecha.toString()+"-"+new Timestamp(new Date().getTime()).toString());
        Cursor c = db.select("select MAX(DATETIME("+C_ACTUALIZACION+")) as MAX FROM "+TABLE_PRODUCTOS,null);
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    String fech=c.getString(c.getColumnIndex("MAX"));
                    if(fech!=null)
                        fecha= Timestamp.valueOf(fech);
                }while(c.moveToNext());
            }
        }
        c.close();
        Log.i("fecha=> ",fecha.getTime()+""+fecha.toString());
        return fecha.getTime();
    }

    public void load(ArrayList<Producto> productos){
        int partes=(int)productos.size()/500;
        int total=productos.size();
        Log.i("N trans 500:", ":"+partes);
        for (int i = 1; i <= partes+1 ; i++) {
            int j=500*(i-1);
            int limite =(500*i);
            if(total<=limite)
                limite=total;
            db.beginTransaction();
            try{
                while((j<(limite))){
                    Log.i("Loading Producto", ":"+j);
                    Producto producto= productos.get(j++);
                    if (exist(producto.CODIGO)) {
                        update(producto);
                    }else {
                        insert(producto);
                    }
                }
                db.blockTransaction();
                db.setTransactionOK();
            }finally {
                db.endTransaction();
            }
        }
    }
}
