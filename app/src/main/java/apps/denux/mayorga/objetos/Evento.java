package apps.denux.mayorga.objetos;

import android.database.Cursor;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by dexter on 13/03/15.
 */
public class Evento {
    public int CODIGO;
    public String OBJETO;
    public String CODIGO_OBJETO;
    public String OPERACION;
    public Timestamp ACTUALIZACION;

    /**
     * Constructor, crea una instancia vacia de Evento
     */
    public Evento() {
    }

    /**
     * Crea un Objeto Evento que sera enviado al servidor para registrar los cambios realizados
     * desde las tablets
     * @param CODIGO
     * @param OBJETO
     * @param CODIGO_OBJETO
     * @param OPERACION
     * @param ACTUALIZACION
     */
    public Evento(int CODIGO, String OBJETO, String CODIGO_OBJETO, String OPERACION, Timestamp ACTUALIZACION) {
        this.CODIGO = CODIGO;
        this.OBJETO = OBJETO;
        this.CODIGO_OBJETO = CODIGO_OBJETO;
        this.OPERACION = OPERACION;
        this.ACTUALIZACION = ACTUALIZACION;
    }

    public Evento(Cursor c) {
        if(c!=null){
            if(c.moveToFirst()){
                do {
                    this.CODIGO = c.getInt(c.getColumnIndex("CODIGO"));
                    this.OBJETO = c.getString(c.getColumnIndex("OBJETO"));
                    this.CODIGO_OBJETO = c.getString(c.getColumnIndex("CODIGO_OBJETO"));
                    this.OPERACION = c.getString(c.getColumnIndex("OPERACION"));
                    this.ACTUALIZACION = Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                }while(c.moveToNext());
            }
        }
    }

    public static ArrayList<Evento> fromCursor(Cursor c){
        ArrayList<Evento> eventos = new ArrayList<Evento>();
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    Evento evento= new Evento();
                    evento.CODIGO = c.getInt(c.getColumnIndex("CODIGO"));
                    evento.OBJETO = c.getString(c.getColumnIndex("OBJETO"));
                    evento.CODIGO_OBJETO = c.getString(c.getColumnIndex("CODIGO_OBJETO"));
                    evento.OPERACION = c.getString(c.getColumnIndex("OPERACION"));
                    evento.ACTUALIZACION = Timestamp.valueOf(c.getString(c.getColumnIndex("ACTUALIZACION")));
                    eventos.add(evento);

                }while(c.moveToNext());
            }
        }
        if(c!=null && !c.isClosed()){
            c.close();
        }
        return eventos;
    }
}
