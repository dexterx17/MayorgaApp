package apps.denux.mayorga.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import apps.denux.mayorga.helpers.DBHelper;
import apps.denux.mayorga.objetos.Pedido;

/**
 * Created by dexter on 13/03/15.
 */
public class PedidoDB {
    /**
     * Instancia a clase encargada de las operaciones con SQLITE directamente
     */
    DBHelper db;
    //Variables
    private static final String TABLE_PEDIDO = "PEDIDO";
    private static final String C_COD_DISPOSITIVO = "DIS_CODIGO";
    private static final String C_COD_PEDIDO = "PED_CODIGO";
    private static final String C_FECHA = "FECHA";
    private static final String C_RUCCI = "RUCCI";
    private static final String C_VEN_CODIGO = "VEN_CODIGO";
    private static final String C_RUTA = "RUTA";
    private static final String C_LONG = "LONG";
    private static final String C_LAT = "LAT";
    private static final String C_LOCK = "LOCK";
    private static final String C_UPDATE = "F_UPDATE";

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_PEDIDO +" ("
            +C_COD_DISPOSITIVO +" VARCHAR(20) NOT NULL, "
            +C_COD_PEDIDO +" INT NOT NULL, "
            +C_FECHA +" TIMESTAMP NOT NULL, "
            +C_RUCCI +" VARCHAR(13) NULL, "
            +C_VEN_CODIGO +" VARCHAR(10)  NOT  NULL, "
            +C_RUTA +" VARCHAR(60)  NULL, "
            +C_LONG +" DOUBLE, "
            +C_LAT +" DOUBLE, "
            +C_LOCK +" CHAR(1), "
            +C_UPDATE +" TIMESTAMP, "
            +"PRIMARY KEY ("+C_COD_PEDIDO+", "+C_COD_DISPOSITIVO+")"
            +");";

    private static final String SQL_SEQUENCE = "create table seq_pedidos ( id integer primary key autoincrement)";

    private static final String INIT_SEQUENCE= "insert into seq_pedidos (id) values(101000)";

    private static final String DEFAULT_PEDIDO1 = "INSERT INTO "+TABLE_PEDIDO +" VALUES( "
            +" 'DISPOSITIVO01', "
            +" 1, "
            +" CURRENT_TIMESTAMP ,"
            +" '1804322913', "
            +" '1800000000', "
            +" 'RUTA001', "
            +" 10.0, "
            +" 11.0," +
            "  'S'," +
            "  CURRENT_TIMESTAMP); ";

    private static final String DEFAULT_PEDIDO2 = "INSERT INTO "+TABLE_PEDIDO +" VALUES( "
            +" 'DISPOSITIVO02', "
            +" 2, "
            +" CURRENT_TIMESTAMP ,"
            +" '1804322913', "
            +" '1800000002', "
            +" 'RUTA002', "
            +" 10.0, "
            +" 11.0," +
            "  'S'," +
            "  CURRENT_TIMESTAMP); ";
    private static  final String borrarRegistros ="delete from "+TABLE_PEDIDO+";";

    /**
     * Constructor de la clase PedidoDB
     *  obtiene una instancia de la conexión a la BDD
     * @param context
     */
    public PedidoDB(Context context) {
        db=DBHelper.getDBHelperInstance(context);
    }

    /**
     * Ejecuta el SQL para crear la tabla PEDIDO y la tabla seq_cliente
     * @param db
     */
    public static void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        db.execSQL(SQL_SEQUENCE);
        db.execSQL(INIT_SEQUENCE);
        //db.execSQL(DEFAULT_PEDIDO1);
        //db.execSQL(DEFAULT_PEDIDO2);
        //Log.i("SQL ","Se crea la tabla pedido");
    }

    /**
     * Ejeuta el SQL para recargar la tabla CLIENTES y la tabla seq_cliente
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PEDIDO);
        db.execSQL(CREATE_TABLE);
        Log.i("SQLite", "Se recrea la tabla Pedido");
        db.execSQL("DROP TABLE IF EXISTS seq_clientes ");
        db.execSQL(SQL_SEQUENCE);
        db.execSQL(INIT_SEQUENCE);
    }

    /**
     * Abre la BDD y obtiene una bdd de modo escritura
     * @throws java.sql.SQLException
     */
    public void open() throws SQLException {
        db.open();
        db.getWritableDatabase();
    }

    /**
     * Cierra la conexión con la bdd
     */
    public void close() {
        db.close();
    }

    public int getId(){
        db.beginTransaction();
        Cursor cursor = db.select("SELECT MAX(id) AS id FROM seq_pedidos ",new String[]{});
        int id = 0;
        if(cursor!=null){
            if(cursor.moveToFirst())
                do{
                    id=cursor.getInt(cursor.getColumnIndex("id"));
                }while (cursor.moveToNext());
            cursor.close();
            ContentValues cv = new ContentValues();
            cv.put("id",id+1);
            db.insert("seq_pedidos",cv);
        }
        db.setTransactionOK();
        db.endTransaction();
        return (id+1);
    }
    /**
     * Devuelve un objeto Pedido
     * @param codigo
     * @return
     */
    public Pedido get(String codigo) {
        Cursor cursor= db.select("select * from PEDIDO where PED_CODIGO = ?",new String[]{codigo});
        Pedido pedido=  new Pedido(cursor);
        cursor.close();
        return pedido;
    }

    /**
     * Devuelve un ArrayList de todos los Pedidos
     * @return ArrayList
     */
    public ArrayList<Pedido> getList() {
        Cursor cursor= db.select("select * from PEDIDO order by FECHA",null);
        ArrayList<Pedido> pedidos =  Pedido.fromCursor(cursor);
        cursor.close();
        return pedidos;
    }

    public ArrayList<Pedido> getListJoin(String rucci) {
        String join = "SELECT P.DIS_CODIGO, P.PED_CODIGO,  P.FECHA , P.RUCCI, " +
                "(SELECT EMPRESA FROM CLIENTE WHERE RUCCI = P.RUCCI) AS EMPRESA, "+
                "(SELECT SUM((PRECIO * CANTIDAD) + IVA ) " +
                "FROM PEDIDOITEM " +
                "WHERE PED_CODIGO = P.PED_CODIGO " +
                ") AS TOTALXPRODUCTO " +
                "FROM PEDIDO P WHERE P.RUCCI LIKE ?;";
        Cursor cursor= db.select(join,new String[]{"%"+rucci+"%"});
        ArrayList<Pedido> pedidoArrayList =  Pedido.fromCursorJoin(cursor);
        cursor.close();
        return pedidoArrayList;
    }

    public ArrayList<Pedido> getListJoin() {
        String join = "SELECT P.DIS_CODIGO, P.PED_CODIGO,  P.FECHA , P.RUCCI, " +
                "(SELECT EMPRESA FROM CLIENTE WHERE RUCCI = P.RUCCI) AS EMPRESA, "+
                "(SELECT SUM((PRECIO * CANTIDAD) + IVA ) " +
                "FROM PEDIDOITEM " +
                "WHERE PED_CODIGO = P.PED_CODIGO " +
                ") AS TOTALXPRODUCTO " +
                "FROM PEDIDO P";
        Log.i("El join es "," "+join);
        Cursor cursor= db.select(join,null);
        ArrayList<Pedido> pedidos =  Pedido.fromCursorJoin(cursor);
        cursor.close();
        return pedidos;
    }

    public Cursor getListPedidos(){
        return db.select("select * from PEDIDO order by FECHA", null);
    }

    /**
     * Inserta un Pedido en la BDD y nuevo valor en la tabla seq_pedidos
     * @param pedido
     * @return
     */
    public long insert(Pedido pedido){
        db.beginTransaction();
        ContentValues valores = new ContentValues();
        valores.put(C_COD_PEDIDO, pedido.COD_PEDIDO);
        valores.put(C_COD_DISPOSITIVO,pedido.DISPOSITIVO);
        valores.put(C_FECHA, String.valueOf(pedido.FECHA));
        valores.put(C_RUCCI,pedido.RUCCI);
        valores.put(C_VEN_CODIGO, String.valueOf(pedido.VENDEDOR));
        valores.put(C_RUTA, pedido.RUTA);
        valores.put(C_LONG,  pedido.LONG);
        valores.put(C_LAT, String.valueOf(pedido.LAT));
        valores.put(C_LOCK, String.valueOf(pedido.LOCK));
        valores.put(C_UPDATE, String.valueOf(pedido.F_UPDATE));
        db.setTransactionOK();
        db.endTransaction();
        return db.insert(TABLE_PEDIDO,valores);
    }

    /**
     * Actualiza un pedido de la BDD
     * @param pedido
     * @return
     */
    public boolean update(Pedido pedido){
        ContentValues valores = new ContentValues();
        valores.put(C_COD_PEDIDO,pedido.COD_PEDIDO);
        valores.put(C_FECHA, String.valueOf(pedido.FECHA));
        valores.put(C_RUCCI,pedido.RUCCI);
        valores.put(C_VEN_CODIGO, String.valueOf(pedido.VENDEDOR));
        valores.put(C_RUTA, pedido.RUTA);
        valores.put(C_LONG,  pedido.LONG);
        valores.put(C_LAT, String.valueOf(pedido.LAT));
        valores.put(C_LOCK, String.valueOf(pedido.LOCK));
        valores.put(C_UPDATE, String.valueOf(pedido.F_UPDATE));
        String where = C_COD_DISPOSITIVO +" = ? ";
        String[] whereArgs = {pedido.DISPOSITIVO};

        return db.update(TABLE_PEDIDO,valores,where,whereArgs)>0;
    }
    public boolean delete (){
        return db.delete(TABLE_PEDIDO,null,null)>0;
    }

}
