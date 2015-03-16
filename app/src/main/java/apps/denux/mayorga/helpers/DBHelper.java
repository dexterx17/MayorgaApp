package apps.denux.mayorga.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import apps.denux.mayorga.modelos.ClientesDB;
import apps.denux.mayorga.modelos.EventosDB;
import apps.denux.mayorga.modelos.PedidoDB;
import apps.denux.mayorga.modelos.PedidoItemDB;
import apps.denux.mayorga.modelos.ProductosDB;
import apps.denux.mayorga.modelos.TipoClienteDB;
import apps.denux.mayorga.modelos.TipoIdentificacionDB;
import apps.denux.mayorga.modelos.VendedorDB;

/**
 * Created by dexter on 13/03/15.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static String DB_PATH = "";
    private static final String DB_NAME ="mayorga_db";
    private SQLiteDatabase myDB;
    private final Context myContext;

    private static DBHelper mDBConecction;

    /**
     * Inicializa el DBHelper y crea o abre la conexión hacia la bdd
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = "/data/data/"+
                context.getApplicationContext().getPackageName()
                + "/databases/";
        context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);
        Log.i("SQLite", "Se crea la base de datos " + DB_NAME + "  en  " + DB_PATH + "  version " + 1);

        //copiarBaseDatosYaExistente();
    }

    /**
     * Retorna una instancia del BDHelper
     * @param context
     * @return DBHelper
     */
    public static synchronized DBHelper getDBHelperInstance(Context context){
        if(mDBConecction == null){
            mDBConecction= new DBHelper(context.getApplicationContext());
        }
        return mDBConecction;
    }

    /**
     * Abre la Base de Datos en modo READWRITE
     */
    public void open()
            throws SQLException {
        myDB= SQLiteDatabase.openDatabase(DB_PATH+DB_NAME,null,SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Cierra la Base de Datos si existe
     */
    public synchronized void close(){
        if(myDB!=null)
            myDB.close();
        super.close();
    }

    /**
     * Crea la base de datos por primera vez llamando al metodo onCreate
     * de las clases que mapean las tablas de la BDD
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("SQLite", "onCreate del DBHelper llamado" );
        VendedorDB.onCreate(db);
        TipoClienteDB.onCreate(db);
        TipoIdentificacionDB.onCreate(db);
        ClientesDB.onCreate(db);
        ProductosDB.onCreate(db);
        PedidoDB.onCreate(db);
        PedidoItemDB.onCreate(db);
        EventosDB.onCreate(db);
    }

    /**
     * Actualiza la base de datos en caso de actualizar la version llamando al metodo onUpgrade
     * de las clases que mapean las tablas de la BDD
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("SQLite", "onUpgrade del DBHelper llamado" );
        VendedorDB.onUpgrade(db, oldVersion, newVersion);
        TipoIdentificacionDB.onUpgrade(db,oldVersion,newVersion);
        TipoClienteDB.onUpgrade(db,oldVersion,newVersion);
        ClientesDB.onUpgrade(db,oldVersion,newVersion);
        ProductosDB.onUpgrade(db,oldVersion,newVersion);
        PedidoDB.onUpgrade(db,oldVersion,newVersion);
        PedidoItemDB.onUpgrade(db,oldVersion,newVersion);
        EventosDB.onUpgrade(db,oldVersion,newVersion);
    }

    /**
     * Ejecuta un Query de selección con los parametros que se le pasan
     * @param tabla
     * @param columns
     * @param where
     * @param whereArgs
     * @param groupBy
     * @param having
     * @param sortBy
     * @return
     */
    public Cursor select(String tabla,String[] columns,String where, String[] whereArgs,String groupBy, String having, String sortBy){
        return myDB.query(tabla,columns,where,whereArgs,groupBy,having,sortBy);
    }

    /**
     * Ejecuta un Query de selección en la tabla especificada y con los parametros WHERE correspondientes
     * @param query
     * @param selectionArgs
     * @return
     */
    public Cursor select(String query, String[] selectionArgs){
        return myDB.rawQuery(query,selectionArgs);
    }

    /**
     * Ejecuta un un Insert con los ContentValues recibidos en la tabla especificada
     * @param tabla
     * @param valores
     * @return
     */
    public long insert(String tabla,ContentValues valores){
        return myDB.insert(tabla,null,valores);
    }

    /**
     * Acutaliza los registros en la tabla especificada
     * @param tabla
     * @param initialValues
     * @param where
     * @param whereArgs
     * @return
     */
    public int update(String tabla, ContentValues initialValues, String where, String[] whereArgs ){
        return myDB.update(tabla,initialValues,where,whereArgs);
    }

    /**
     * Ejecuta un delete con los parametros especificados
     * @param tabla
     * @param where
     * @param whereArgs
     * @return
     */
    public int delete(String tabla, String where, String[] whereArgs){
        return myDB.delete(tabla,where,whereArgs);
    }

    /**
     * Inicia una transacción
     */
    public void beginTransaction(){
        myDB.beginTransaction();
    }

    /**
     * Valida la transacción
     */
    public void setTransactionOK(){
        myDB.setTransactionSuccessful();
    }

    /**
     * Termina la transacción
     */
    public void endTransaction(){
        myDB.endTransaction();
    }

    /**
     * Bloquea la transacción
     */
    public void blockTransaction(){
        myDB.yieldIfContendedSafely();
    }


    public void copiarBaseDatosYaExistente() {
        String ruta = DB_PATH;
        File archivoDB = new File(ruta + DB_NAME);
        if (archivoDB.exists()) {
            Log.i("Esta en copiarBaseDatosYaExistente  "+archivoDB.exists(), "Entro al metodo  ");
            try {
                InputStream IS = myContext.getAssets().open(DB_NAME);
                OutputStream OS = new FileOutputStream(archivoDB);
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = IS.read(buffer)) > 0) {
                    OS.write(buffer, 0, length);
                }
                OS.flush();
                OS.close();
                IS.close();
                Log.i("La bdd se copió ", " "+DB_NAME);
            } catch (FileNotFoundException e) {
                Log.e("ERROR", "Archivo no encontrado, " + e.toString());
            } catch (IOException e) {
                Log.e("ERROR", "Error al copiar la Base de Datos, " + e.toString());
            }
        }
    }
}
