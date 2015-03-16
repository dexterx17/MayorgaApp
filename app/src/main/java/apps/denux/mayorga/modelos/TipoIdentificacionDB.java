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
import apps.denux.mayorga.objetos.TipoIdentificacion;

/**
 * Created by dexter on 13/03/15.
 */
public class TipoIdentificacionDB {
    DBHelper db;
    private static final String TABLA = "TIPOSID";
    private static final String C_CODIGO = "CODIGO";
    private static final String C_IDENTIFICACION = "IDENTIFICACION";
    private static final String C_ACTUALIZACION ="ACTUALIZACION";

    private static final String DB_CREATE = "create table "
            +TABLA
            +" ( "
            +C_CODIGO + " integer primary key, "
            +C_IDENTIFICACION + " varchar(200), "
            +C_ACTUALIZACION + " datetime );";

    /**
     * Constructor de la clase TipoIdentificacionDB
     *  obtiene una instancia de la conexión a la BDD
     * @param context
     */
    public TipoIdentificacionDB(Context context) {
        this.db = DBHelper.getDBHelperInstance(context);
    }

    /**
     * Ejecuta el SQL para crear la tabla TIPOSID
     * @param db
     */
    public static void onCreate(SQLiteDatabase db){ db.execSQL(DB_CREATE);}

    /**
     * Ejeuta el SQL para recargar la tabla TIPOSID
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLA);
        db.execSQL(DB_CREATE);
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


    /**
     * Devuelve un ArrayList de todos los Clientes
     * @return ArrayList
     */
    public ArrayList<TipoIdentificacion> getList() {
        Cursor cursor= db.select("select * from TIPOSID",null);
        ArrayList<TipoIdentificacion> tipos =  TipoIdentificacion.fromCursor(cursor);
        cursor.close();
        return tipos;
    }

    /**
     * Devuelve un ArrayList de todos los Clientes
     * @return ArrayList
     */
    public Cursor getCursor() {
        Cursor cursor= db.select("select * from TIPOSID",null);
        cursor.close();
        return cursor;
    }

    /**
     * Inserta un TipoIdenficiacion en la BDD
     * @param tipo
     * @return
     */
    public long insert(TipoIdentificacion tipo){
        ContentValues valores = new ContentValues();
        valores.put(C_CODIGO, tipo.CODIGO);
        valores.put(C_IDENTIFICACION, (String) tipo.DESCRIPCION.toString().trim());
        valores.put(C_ACTUALIZACION, String.valueOf(tipo.ACTUALIZACION));
        return db.insert(TABLA,valores);
    }

    /**
     * Edita un TipoIdenficiacion de la BDD
     * @param tipo
     * @return boolean true/false
     */
    public boolean update(TipoIdentificacion tipo){
        ContentValues valores = new ContentValues();
        valores.put(C_IDENTIFICACION, (String) tipo.DESCRIPCION.toString().trim());
        valores.put(C_ACTUALIZACION, String.valueOf(tipo.ACTUALIZACION));
        String where = C_CODIGO +" = ?";
        String[] whereArgs = {Integer.toString(tipo.CODIGO)};
        return db.update(TABLA,valores,where,whereArgs)>0;
    }

    /**
     * Verifica si determinado Tipo existe en base a su clave primaria
     * @param codigo
     * @return true/false
     */
    public boolean exist(int codigo){
        Cursor cursor= db.select("select * from TIPOSID WHERE CODIGO = ?",new String[]{String.valueOf(codigo)});
        boolean res= cursor.getCount()>0;
        cursor.close();
        return res;
    }

    /**
     * Obtiene la ultima fecha en la que se actualizó algun elemento
     * @return
     */
    public long getLastUpdate(){
        Calendar cal = Calendar.getInstance();
        cal.set(1990,2,8,8,30);
        Timestamp fecha = new Timestamp(cal.getTimeInMillis());
        Log.i("fecha", fecha.toString() + "-" + new Timestamp(new Date().getTime()).toString());
        Cursor c = db.select("select MAX(DATETIME("+C_ACTUALIZACION+")) as MAX FROM TIPOSID",null);
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

    /**
     * Cargas los datos desde un ArrayList TipoIdentificacion hacia la bdd local
     * @param tipos
     */
    public void load(ArrayList<TipoIdentificacion> tipos){
        int partes=(int)tipos.size()/500;
        int total=tipos.size();
        Log.i(" trans 500:", ":" + partes);
        for (int i = 1; i <= partes+1 ; i++) {
            int j=500*(i-1);
            int limite =(500*i);
            if(total<=limite)
                limite=total;
            db.beginTransaction();
            try{
                Log.i("Empezando trans", ":"+j);
                while((j<(limite))){
                    Log.i("Loading TIPOID", ":"+j);
                    TipoIdentificacion tipo = tipos.get(j++);
                    if (exist(tipo.CODIGO)) {
                        update(tipo);
                    }else {
                        insert(tipo);
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
