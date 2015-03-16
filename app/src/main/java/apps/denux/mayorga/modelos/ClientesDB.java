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

import apps.denux.mayorga.helpers.DBHelper;
import apps.denux.mayorga.objetos.Cliente;

/**
 * Created by dexter on 13/03/15.
 */
public class ClientesDB {
    /**
     * Instancia a clase encargada de las operaciones con SQLITE directamente
     */
    DBHelper db;

    private static final String tabla = "CLIENTE";
    private static final String C_CODIGO ="CODIGO";
    private static final String C_RUCCI ="RUCCI";
    private static final String C_TIPO ="TIPO";
    private static final String C_IDENTIFICACION ="IDENTIFICACION";
    private static final String C_SUCURSAL ="SUCURSAL";
    private static final String C_EMPRESA ="EMPRESA";
    private static final String C_REPRESENTANTE ="REPRESENTANTE";
    private static final String C_CIUDAD ="CIUDAD";
    private static final String C_DIRECCION ="DIRECCION";
    private static final String C_DIRECCION2 ="DIRECCION2";
    private static final String C_TELFAX ="TELFAX";
    private static final String C_TELFAX2 ="TELFAX2";
    private static final String C_PROPIETARIO ="PROPIETARIO";
    private static final String C_FINGRESO ="FINGRESO";
    private static final String C_EMP_PUBLICA ="EMP_PUBLICA";
    private static final String C_ZONA ="ZONA";
    private static final String C_VENDEDOR ="VENDEDOR";
    private static final String C_ACTUALIZACION ="ACTUALIZACION";
    private static final String C_ACTUALIZACION_LOCAL ="ACTUALIZACION_LOCAL";

    private static final String DB_CREATE = "create table "
            + tabla
            +" ( "
            + C_CODIGO + " integer not null , "
            +C_RUCCI + " char(13) not null, "
            +C_TIPO +" integer not null, "
            +C_IDENTIFICACION + " integer not null, "
            +C_SUCURSAL + " integer not null, "
            +C_EMPRESA+" varchar(250) not null, "
            +C_REPRESENTANTE+ " varchar(120), "
            +C_CIUDAD + " varchar(60), "
            +C_DIRECCION + " varchar(250), "
            +C_DIRECCION2 + " varchar(250), "
            +C_TELFAX + " varchar(15), "
            +C_TELFAX2 + " varchar(15), "
            +C_PROPIETARIO + " varchar(120), "
            +C_FINGRESO + " datetime default current_timestamp, "
            +C_EMP_PUBLICA + " char(1), "
            +C_ZONA + " varchar(8), "
            +C_VENDEDOR + " integer, "
            +C_ACTUALIZACION + " datetime, "
            +C_ACTUALIZACION_LOCAL + " datetime, "
            +" primary key( "
            +C_CODIGO+","
            +C_RUCCI+","
            +C_TIPO+","
            +C_IDENTIFICACION+","
            +C_SUCURSAL+"));";

    private static final String SQL_SEQUENCE = "create table seq_clientes ( id integer primary key autoincrement)";

    private static final String INIT_SEQUENCE= "insert into seq_clientes (id) values(101000)";

    private static final String TRIGGER_SINC ="CREATE TRIGGER after_insert_cliente ON "+tabla+" BEGIN INSERT INTO ";

    /**
     * Constructor de la clase ClientesDB
     *  obtiene una instancia de la conexión a la BDD
     * @param context
     */
    public ClientesDB(Context context) {
        db=DBHelper.getDBHelperInstance(context);
    }

    /**
     * Ejecuta el SQL para crear la tabla CLIENTE y la tabla seq_cliente
     * @param db
     */
    public static void onCreate(SQLiteDatabase db){
        db.execSQL(DB_CREATE);
        db.execSQL(SQL_SEQUENCE);
        db.execSQL(INIT_SEQUENCE);
    }

    /**
     * Ejeuta el SQL para recargar la tabla CLIENTES y la tabla seq_cliente
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+tabla);
        db.execSQL(DB_CREATE);
        Log.i("SQLite", "Se recrea la tabla Clinte");
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

    /**
     * Devuelve un objeto Cliente
     * @param ruc
     * @return Cliente
     */
    public Cliente get(String ruc) {
        Cursor cursor= db.select("select * from CLIENTE WHERE RUCCI = ? ",new String[]{ruc});
        Cliente cliente = new Cliente(cursor);
        cursor.close();
        return cliente;
    }

    /**
     * Devuelve un ArrayList de todos los Clientes
     * @return ArrayList
     */
    public ArrayList<Cliente> getList() {
        Cursor cursor= db.select("select * from CLIENTE ORDER BY EMPRESA ASC",null);
        ArrayList<Cliente> clientes =  Cliente.fromCursor(cursor);
        cursor.close();
        return clientes;
    }

    /**
     * Devuelve un ArrayList de los Clientes que cumplan con LIKE %param%
     * @param param Cadena para buscar por nombre
     * @return ArrayList
     */
    public ArrayList<Cliente> getList(String param) {
        Cursor cursor= db.select("select * from CLIENTE WHERE EMPRESA LIKE ? order by EMPRESA ",new String[]{"%"+param+"%"});
        ArrayList<Cliente> clientes =  Cliente.fromCursor(cursor);
        cursor.close();
        return clientes;
    }

    /**
     * Inserta un Cliente en la BDD y un nuevo valor en la tabla seq_productos
     * @param cliente
     * @return
     */
    public boolean insertLocal(Cliente cliente){
        Cursor c;
        int max =0;
        boolean res = false;

        db.beginTransaction();
        c = db.select("select max(id) as MAX from seq_clientes",new String[]{});

        if(c!=null){
            if(c.moveToFirst())
                do{
                    max=c.getInt(c.getColumnIndex("MAX"));
                }while (c.moveToNext());
            c.close();
            ContentValues cv = new ContentValues();
            cv.put("id",max+1);
            db.insert("seq_clientes",cv);
        }

        ContentValues valores = new ContentValues();
        valores.put(C_CODIGO, max+1);
        valores.put(C_RUCCI, (String) cliente.RUCCI);
        valores.put(C_TIPO,cliente.TIPO);
        valores.put(C_IDENTIFICACION,cliente.IDENTIFICACION);
        valores.put(C_SUCURSAL,cliente.SUCURSAL);
        valores.put(C_EMPRESA, (String) cliente.EMPRESA);
        valores.put(C_REPRESENTANTE, (String) cliente.REPRESENTANTE);
        valores.put(C_CIUDAD, (String) cliente.CIUDAD);
        valores.put(C_DIRECCION, (String) cliente.DIRECCION);
        valores.put(C_DIRECCION2, (String) cliente.DIRECCION2);
        valores.put(C_TELFAX, (String) cliente.TELFAX);
        valores.put(C_TELFAX2, (String) cliente.TELFAX2);
        valores.put(C_PROPIETARIO, (String) cliente.PROPIETARIO);
        valores.put(C_EMP_PUBLICA, cliente.EMP_PUBLICA);
        valores.put(C_VENDEDOR,cliente.VENDEDOR);
        valores.put(C_ACTUALIZACION, String.valueOf(cliente.ACTUALIZACION));
        valores.put(C_ACTUALIZACION_LOCAL, String.valueOf(cliente.ACTUALIZACION_LOCAL));
        res= db.insert(tabla,valores)>0;
        db.setTransactionOK();
        db.endTransaction();

        return res;
    }

    /**
     * Inserta un Cliente en la BDD
     * @param cliente
     * @return
     */
    public boolean insert(Cliente cliente){

        ContentValues valores = new ContentValues();
        valores.put(C_CODIGO, cliente.CODIGO);
        valores.put(C_RUCCI, (String) cliente.RUCCI);
        valores.put(C_TIPO,cliente.TIPO);
        valores.put(C_IDENTIFICACION,cliente.IDENTIFICACION);
        valores.put(C_SUCURSAL,cliente.SUCURSAL);
        valores.put(C_EMPRESA, (String) cliente.EMPRESA);
        valores.put(C_REPRESENTANTE, (String) cliente.REPRESENTANTE);
        valores.put(C_CIUDAD, (String) cliente.CIUDAD);
        valores.put(C_DIRECCION, (String) cliente.DIRECCION);
        valores.put(C_DIRECCION2, (String) cliente.DIRECCION2);
        valores.put(C_TELFAX, (String) cliente.TELFAX);
        valores.put(C_TELFAX2, (String) cliente.TELFAX2);
        valores.put(C_PROPIETARIO, (String) cliente.PROPIETARIO);
        valores.put(C_EMP_PUBLICA, cliente.EMP_PUBLICA);
        valores.put(C_VENDEDOR,cliente.VENDEDOR);
        valores.put(C_ACTUALIZACION, String.valueOf(cliente.ACTUALIZACION));

        return db.insert(tabla,valores)>0;
    }

    /**
     * Edita un Cliente de la BDD
     * @param cliente
     * @return boolean true/false
     */
    public boolean update(Cliente cliente){
        ContentValues valores = new ContentValues();
        //valores.put(C_RUCCI, (String) cliente.RUCCI);
        valores.put(C_TIPO,cliente.TIPO);
        valores.put(C_IDENTIFICACION,cliente.IDENTIFICACION);
        //valores.put(C_SUCURSAL,cliente.SUCURSAL);
        valores.put(C_EMPRESA, (String) cliente.EMPRESA);
        valores.put(C_REPRESENTANTE, (String) cliente.REPRESENTANTE);
        valores.put(C_CIUDAD, (String) cliente.CIUDAD);
        valores.put(C_DIRECCION, (String) cliente.DIRECCION);
        valores.put(C_DIRECCION2, (String) cliente.DIRECCION2);
        valores.put(C_TELFAX, (String) cliente.TELFAX);
        valores.put(C_TELFAX2, (String) cliente.TELFAX2);
        valores.put(C_PROPIETARIO, (String) cliente.PROPIETARIO);
        valores.put(C_EMP_PUBLICA, cliente.EMP_PUBLICA);
        valores.put(C_VENDEDOR,cliente.VENDEDOR);
        valores.put(C_ACTUALIZACION,String.valueOf(cliente.ACTUALIZACION));
        valores.put(C_ACTUALIZACION_LOCAL, String.valueOf(cliente.ACTUALIZACION_LOCAL));

        String where = C_SUCURSAL +" = ? AND "+ C_RUCCI +" = ?";
        String[] whereArgs = {Integer.toString(cliente.SUCURSAL),cliente.RUCCI.toString()};

        return db.update(tabla,valores,where,whereArgs)>0;
    }

    /**
     * Verifica si un Cliente en base a su RUC y sucursal a la que pertenece
     * @param rucci
     * @param sucursal
     * @return
     */
    public boolean exist(String rucci, int sucursal){
        Cursor cursor= db.select("select * from CLIENTE WHERE RUCCI = ? AND SUCURSAL = ?",new String[]{rucci,String.valueOf(sucursal)});
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
        Cursor c = db.select("select MAX(DATETIME("+C_ACTUALIZACION+")) as MAX FROM CLIENTE",null);
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
        return fecha.getTime();
    }

    /**
     * Inserta o Actualizar Clientes en la bdd SQLITE desde un ArrayList de Clientes
     * @param clientes
     */
    public void load(ArrayList<Cliente> clientes){
        int partes=(int)clientes.size()/500;
        int total=clientes.size();
        Log.i("CLienteBD", "Numero de transacciones de 500:"+partes);
        for (int i = 1; i <= partes+1 ; i++) {
            int j=500*(i-1);
            int limite =(500*i);
            if(total<=limite)
                limite=total;
            db.beginTransaction();
            try{
                Log.i("Empezando trans", ":"+j);
                while((j<(limite))){
                    Log.i("Loading Cliente", ":"+j);
                    Cliente cliente = clientes.get(j++);
                    if (exist(cliente.RUCCI.toString(), cliente.SUCURSAL)) {
                        update(cliente);
                        Log.i("cliente actualizado", ":"+j);
                    }else {
                        insert(cliente);
                        Log.i("cliente insertado", ":"+j);
                    }
                }
                db.blockTransaction();
                db.setTransactionOK();
            }finally {
                db.endTransaction();
            }
        }
    }

    /**
     * Elimina un Cliente de la BDD
     * @param cliente
     * @return
     **/
    public boolean delete(Cliente cliente){
        String where = C_SUCURSAL +" = ? AND "+ C_RUCCI +" = ?";
        String[] whereArgs = {Integer.toString(cliente.SUCURSAL),cliente.RUCCI.toString()};
        return db.delete(tabla,where,whereArgs)>0;
    }
}
