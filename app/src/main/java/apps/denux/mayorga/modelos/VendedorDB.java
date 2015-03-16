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
import apps.denux.mayorga.objetos.Vendedor;

/**
 * Created by dexter on 13/03/15.
 */
public class VendedorDB {
    DBHelper db;
    private static final String TABLA = "VENDEDOR";
    private static final String C_CEDULA = "CEDULA";
    private static final String C_APELLIDOS ="APELLIDOS";
    private static final String C_NOMBRES ="NOMBRES";
    private static final String C_TELEFONO ="TELEFONO";
    private static final String C_F_INGRESO ="F_INGRESO";
    private static final String C_PASSWORD ="PASS";
    private static final String C_COMISION ="COMISION";
    private static final String C_F_UPDATE ="F_UPDATE";

    private static final String CREATE_TABLE = "CREATE TABLE "+ TABLA +"("
            +C_CEDULA +" VARCHAR(10) NOT NULL PRIMARY KEY,"
            +C_APELLIDOS +" VARCHAR(30)  NULL,"
            +C_NOMBRES +" VARCHAR(50)  NULL,"
            +C_TELEFONO +" VARCHAR(10)  NULL,"
            +C_F_INGRESO +" TIMESTAMP  NULL,"
            +C_PASSWORD +" VARCHAR(255)  NULL,"
            +C_COMISION +" CHAR(1)  NULL,"
            +C_F_UPDATE +" TIMESTAMP  NULL"
            +");";
    private static final String DEFAULT_USER1 = "INSERT INTO "+TABLA +" VALUES( "
            +" '1600392359', "
            +" 'SANTANA LEON', "
            +" 'JAIME AUGUSTO' ,"
            +" '0983706086', "
            +" CURRENT_TIMESTAMP, "
            +" '1234', "
            +" 'S', "
            +" CURRENT_TIMESTAMP); ";

    private static final String DEFAULT_USER2 = "INSERT INTO "+TABLA +" VALUES( "
            +" '1804322913', "
            +" 'NUELA GUANANGA', "
            +" 'BYRON DANILO' ,"
            +" '0987654321', "
            +" CURRENT_TIMESTAMP, "
            +" '1111', "
            +" 'S', "
            +" CURRENT_TIMESTAMP); ";


    public VendedorDB (Context context) { db=DBHelper.getDBHelperInstance(context);
    }

    public static void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        Log.i("SQLite", "Se crea la tabla Vendedor");
        db.execSQL(DEFAULT_USER1);
        Log.i("SQLite", "Se agrega el usuario 1600392359:1234");
        db.execSQL(DEFAULT_USER2);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLA);
        db.execSQL(CREATE_TABLE);
        Log.i("SQLite", "Se recrea la tabla Vendedor");
        db.execSQL(DEFAULT_USER1);
        Log.i("SQLite", "Se reagrega el usuario 1600392359:1234");
        db.execSQL(DEFAULT_USER2);
    }

    public void open() throws SQLException {
        db.open();//Abrir la conexion
        db.getWritableDatabase();
    }

    public void close() {
        db.close();//cerrar la conexion
    }

    public boolean Logeado(String user, String pass){
        Log.i("SQLite", "Logeado "+user+"-"+pass);
        Cursor cursor= db.select("select * from VENDEDOR WHERE CEDULA = ? AND PASS = ?",new String[]{user,pass});
        Boolean res =  cursor.getCount()>0;
        cursor.close();
        return res;
    }

    public Vendedor get(String cedula) {
        Cursor cursor= db.select("select * from VENDEDOR WHERE CEDULA = ?",new String[]{cedula});
        Vendedor vendedor = new Vendedor(cursor);
        cursor.close();
        return vendedor;
    }

    /**
     * Verificar si determinado vendedor existo
     * @param cedula
     * @return
     */
    public boolean exist(String cedula){
        Cursor cursor= db.select("select * from VENDEDOR WHERE CEDULA= ? ",new String[]{cedula});
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
        Log.i("fecha",fecha.toString()+"-"+new Timestamp(new Date().getTime()).toString());
        Cursor c = db.select("select MAX(DATETIME("+C_F_UPDATE+")) as MAX FROM VENDEDOR",null);
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
     * Agrega un Vendedor
     * @param vendedor
     * @return
     */
    public long insert(Vendedor vendedor){
        ContentValues valores = new ContentValues();
        valores.put(C_CEDULA, vendedor.CEDULA);
        valores.put(C_APELLIDOS,vendedor.APELLIDOS);
        valores.put(C_NOMBRES,vendedor.NOMBRES);
        valores.put(C_TELEFONO,vendedor.TELEFONO);
        valores.put(C_F_INGRESO, String.valueOf(vendedor.F_INGRESO));
        valores.put(C_PASSWORD, vendedor.PASSWORD);
        valores.put(C_COMISION,  vendedor.COMISION);
        valores.put(C_F_UPDATE, String.valueOf(vendedor.F_UPDATE));

        return db.insert(TABLA,valores);
    }

    /**
     * Actualiza un Vendedor
     * @param vendedor
     * @return
     */
    public boolean update(Vendedor vendedor){
        ContentValues valores = new ContentValues();
        valores.put(C_APELLIDOS,vendedor.APELLIDOS);
        valores.put(C_NOMBRES,vendedor.NOMBRES);
        valores.put(C_TELEFONO,vendedor.TELEFONO);
        valores.put(C_F_INGRESO, String.valueOf(vendedor.F_INGRESO));
        valores.put(C_PASSWORD, vendedor.PASSWORD);
        valores.put(C_COMISION,  vendedor.COMISION);
        valores.put(C_F_UPDATE, String.valueOf(vendedor.F_UPDATE));

        String where = C_CEDULA +" = ? ";
        String[] whereArgs = {vendedor.CEDULA};

        return db.update(TABLA,valores,where,whereArgs)>0;
    }

    public void load(ArrayList<Vendedor> vendedorList){
        try {
            db.beginTransaction();
            for (int i = 0; i < vendedorList.size(); i++) {
                Log.i("Loading Vendedor", ":"+i);
                Vendedor vendedor = vendedorList.get(i);
                if (exist(vendedor.CEDULA.toString())) {
                    update(vendedor);
                    Log.i("vendedor actualizado", ":"+i);
                }else {
                    insert(vendedor);
                    Log.i("vendedor insertado", ":"+i);
                }
            }
            db.setTransactionOK();
        }finally {
            db.endTransaction();
        }
    }
}
