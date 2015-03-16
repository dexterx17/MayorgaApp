package apps.denux.mayorga;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import apps.denux.mayorga.modelos.VendedorDB;

/**
 * Created by dexter on 13/03/15.
 */
public class LoginActivity extends Activity {

        private String USER = "", PASS = "";
        private TextView txtUser, txtPass;
        private Button btnInicioSesion;
        private TextView tvTest;

        public static Constantes CONSTANTES;
        VendedorDB vendedorDB;
        int intentosFallidos=0;
        long horaBloqueo =0;
        SharedPreferences prefe;
        Date date;
        DateFormat dateFormat;
        int bloqueoTemporal = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_user);

            CONSTANTES = new Constantes(this);
            txtUser	 = (TextView)this. findViewById(R.id.txtUser );
            txtPass	 = (TextView)this. findViewById(R.id.txtPassword);
            btnInicioSesion = (Button)this.findViewById(R.id.btnInicioSesion);

            bloqueoTemporal();

            btnInicioSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(InicioSesion()){
                        //inicializa variables de bloqueo
                        reiniciarContadorBloqueo();
                        String ci = txtUser.getText().toString();
                        //Setea un objetipo tipo Vendedor global
                        CONSTANTES.login(ci);
                        //Pasa la cedula del vendedor al main activity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("cedula", ci);
                        startActivity(intent);
                    }
                    else{
                        procesarIntentoFallido();
                    }
                }
            });
    }

    private void procesarIntentoFallido(){
        if(intentosFallidos >= 3){
            Toast.makeText(getApplicationContext(), "Su aplicación esta temporamlmente bloqueda",Toast.LENGTH_LONG).show();
            btnInicioSesion.setBackgroundColor(Color.RED);
            btnInicioSesion.setText("Aplicación temporalmente bloqueda");
            btnInicioSesion.setEnabled(false);
            //(MODE_PRIVATE indica que solo esta aplicación puede consultar el archivo XML que se crea)
            //SharedPreferences prefe=getSharedPreferences("intentosFallidos", Context.MODE_PRIVATE);
            horaBloqueo = new Date().getTime();
            SharedPreferences.Editor editor=prefe.edit();
            //Mediante el método putString almacenamos los puntos
            editor.putLong("horaBloquedada", horaBloqueo);
            //Luego debemos llamar al método commit de la clase editor para que el dato
            //quede almacenado en forma permanente en el archivo de preferencias
            bloqueoTemporal ++;
            if(bloqueoTemporal>=3){
                //enviar notificacion al servidor
                mostrarDialogoAlerta("Bloqueo parcial en el servido","Contactese con el administrador");
                btnInicioSesion.setText("Aplicación bloqueda parcilmente en el servidor");
                bloqueoTemporal = 0;
            }else{
                mostrarDialogoAlerta("Bloqueo temporal", "Vuelva a intentarlo dentro de 1 minuto");
            }
            editor.putInt("bloqueosTemporales", bloqueoTemporal);
            editor.commit();
            //horaBloqueo = prefe.getLong("horaBloquedada", new Date().getTime());
        }else{
            intentosFallidos++;
            mostrarDialogoAlerta("Información","Usuario o contraseña incorrectos.");
        }
    }

    private void reiniciarContadorBloqueo(){
        SharedPreferences.Editor editor=prefe.edit();
        horaBloqueo = 0;
        editor.putLong("horaBloquedada", horaBloqueo);
        editor.commit();
        intentosFallidos =0;
    }

    private void bloqueoTemporal(){
        //(MODE_PRIVATE indica que solo esta aplicación puede consultar el archivo XML que se crea)
        prefe=getSharedPreferences("intentosFallidos", Context.MODE_PRIVATE);
        horaBloqueo = prefe.getLong("horaBloquedada", 0);
        bloqueoTemporal = prefe.getInt("bloqueosTemporales", 0);
        Log.i("bloqueoTemporal ", " " + bloqueoTemporal);
        //Verifica si esta bloqueado temporalmente  (1000 = 1 segundo)
        if(horaBloqueo !=0){
            long horaDesbloqueo = horaBloqueo+60000;
            long horaActual =  new Date().getTime();
            //Verifica si ya paso el tiempo de bloqueo temporal
            if(horaActual > horaDesbloqueo){
                horaBloqueo =0;
                SharedPreferences.Editor editor=prefe.edit();
                editor.putLong("horaBloquedada", horaBloqueo);
                editor.commit();
            }else{
                btnInicioSesion.setBackgroundColor(Color.RED);
                btnInicioSesion.setText("Aplicacion bloqueda temporalmente");
                btnInicioSesion.setEnabled(false);
            }
        }
    }
    public boolean InicioSesion(){
        boolean result = false;
        String usr = txtUser.getText().toString();
        String pass = txtPass.getText().toString();
        VendedorDB vendedorDB= new VendedorDB(getBaseContext());
        try {
            vendedorDB.open();
            result = vendedorDB.Logeado(usr, pass);
            vendedorDB.close();
        } catch (SQLException e) {
            vendedorDB.close();
            e.printStackTrace();
        }
        return result;
    }

    private void mostrarDialogoAlerta(String titulo, String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Información");
        builder.setTitle(titulo);
        //builder.setMessage("Usuario o contraseña incorrectos.");
        builder.setMessage(mensaje);
        builder.setIcon(android.R.drawable.ic_secure);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Dialogos", "Botón aceptar pulsado");
            }
        });
        builder.show();
    }
}
