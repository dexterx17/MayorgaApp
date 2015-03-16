package apps.denux.mayorga.modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import apps.denux.mayorga.helpers.DBHelper;
import apps.denux.mayorga.objetos.Evento;

/**
 * Created by dexter on 13/03/15.
 */
public class EventosDB {

    /**
     * Instancia a clase encargada de las operaciones con SQLITE directamente
     */
    DBHelper db;

    private static final String TABLA = "EVENTO";
    private static final String C_CODIGO ="CODIGO";
    private static final String C_OBJETO ="OBJETO";
    private static final String C_CODIGO_OBJETO ="CODIGO_OBJETO";
    private static final String C_OPERACION ="OPERACION";
    private static final String C_ACTUALIZACION ="ACTUALIZACION";

    private static final String DB_CREATE =  "create table "
            + TABLA
            +" ( "
            + C_CODIGO + " integer primary key autoincrement , "
            +C_OBJETO + " varchar(50) not null, "
            +C_CODIGO_OBJETO + " varchar(50) not null, "
            +C_OPERACION +" varchar(25) not null, "
            +C_ACTUALIZACION + " datetime "
            +" );";

    /**
     * Constructor de la clase EventosDB
     *  obtiene una instancia de la conexión a la BDD
     * @param context
     */
    public EventosDB(Context context) {
        this.db = DBHelper.getDBHelperInstance(context);
    }

    /**
     * Ejecuta el SQL para crear la tabla EVENTO
     * @param db
     */
    public static void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE);
    }

    /**
     * Ejeuta el SQL para recargar la tabla EVENTO
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
     * Devuelve un ArrayList de la clase Evento
     * @return ArrayList
     */
    public ArrayList<Evento> getList() {
        Cursor cursor= db.select("select * from EVENTO ",null);
        ArrayList<Evento> eventos =  Evento.fromCursor(cursor);
        cursor.close();
        return eventos;
    }

    /**
     * Devuelve un ArrayList de la clase Evento
     * @param codigo
     * @return ArrayList
     */
    public Evento get(int codigo) {
        Cursor cursor= db.select("select * from EVENTO WHERE CODIGO = ?",new String[]{String.valueOf(codigo)});
        Evento evento =  new Evento(cursor);
        cursor.close();
        return evento;
    }

    /**
     * Inserta un Evento en la BDD
     * @param evento
     * @return
     */
    public boolean insert(Evento evento){

        ContentValues valores = new ContentValues();
        valores.put(C_OBJETO, evento.OBJETO);
        valores.put(C_CODIGO_OBJETO,evento.CODIGO_OBJETO);
        valores.put(C_OPERACION,evento.OPERACION);
        valores.put(C_ACTUALIZACION, String.valueOf(evento.ACTUALIZACION));
        return db.insert(TABLA,valores)>0;
    }

    /**
     * Elimina un Evento de la BDD
     * @param codigo Clave primaria del evento
     * @return
     **/
    public boolean delete(int codigo){
        String where = C_CODIGO +" = ?";
        String[] whereArgs = {String.valueOf(codigo)};
        return db.delete(TABLA,where,whereArgs)>0;
    }
}
