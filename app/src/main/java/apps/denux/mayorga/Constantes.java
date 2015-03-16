package apps.denux.mayorga;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import apps.denux.mayorga.modelos.VendedorDB;
import apps.denux.mayorga.objetos.Cliente;
import apps.denux.mayorga.objetos.Objeto;
import apps.denux.mayorga.objetos.Pedido;
import apps.denux.mayorga.objetos.Producto;
import apps.denux.mayorga.objetos.SyncItem;
import apps.denux.mayorga.objetos.TipoCliente;
import apps.denux.mayorga.objetos.TipoIdentificacion;
import apps.denux.mayorga.objetos.Vendedor;

/**
 * Created by dexter on 13/03/15.
 */
public class Constantes {
    private Context context;

    private SharedPreferences sp;

    public static enum TIPO__REST{
        GET,
        PUT,
        POST,
        DELETE
    }

    public static enum FORM_OPERACIONES{
        VIEW,
        NEW,
        UPDATE,
        DELETE
    }

    public static String USER_REST;
    public static String PASS_REST;
    public static String SERVER_REST;
    public static int PORT_REST;
    public static String DEFAULT_FORMAT_RESPONSE;
    public static boolean AUTH_ENABLED;
    public static int TIMEOUTCONNECTION;
    public static int SETSOTIMEOUT;
    public static int SUCURSAL;
    public static String SUBDOMAIN;
    public static String USER_MAC;
    // Google project id
    public static String SENDER_ID;

    static final String DISPLAY_MESSAGE_ACTION =
            "apps.dexnu.appmayorga.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    public static String URL_TEST_CONEXION = "Notificaciones/test";
    public static String URL_GET_DEVICES = "Notificaciones/usersgcm";
    public static String URL_DELETE_DEVICE = "Notificaciones/usersgcm";
    public static String URL_POST_DEVICE = "Notificaciones/usergcm";

    public static String URL_GET_CLIENTES = "Clientes/clientes";
    public static String URL_GET_CLIENTE = "Clientes/cliente";
    public static String URL_GET_TIPOS_CLIENTE = "Clientes/tiposemp";
    public static String URL_GET_TIPOS_ID = "Clientes/tiposid";
    public static String URL_POST_CLIENTE = "Clientes/cliente";

    public static String URL_GET_PRODUCTOS = "Productos/productos";

    public static String URL_GET_VENDEDORES = "Vendedores/vendedores";
    public static String URL_GET_VENDEDOR = "Vendedores/vendedor";

    public static String URL_GET_PEDIDOS = "Pedidos/pedidos";
    public static String URL_POST_PEDIDO = "Pedidos/pedido";

    public static ArrayList<SyncItem> URLS_DOWNLOAD_UPDATES  = new ArrayList<SyncItem>() {{
        add(new  SyncItem(URL_GET_CLIENTES, new Cliente() ));
        add(new  SyncItem(URL_GET_TIPOS_ID, new TipoIdentificacion()     ));
        add(new  SyncItem(URL_GET_TIPOS_CLIENTE, new TipoCliente()       ));
        add(new  SyncItem(URL_GET_PRODUCTOS, new Producto()      ));
        add(new  SyncItem(URL_GET_VENDEDORES, new Vendedor()       ));
        add(new  SyncItem(URL_GET_PEDIDOS, new Pedido()       ));
    }};

    /**
     * Almacena al Vendedor actualmente logeado
     */
    public static Vendedor VENDEDOR ;

    public Constantes(Context context){
        this.context=context;
        loadPreferences();
    }

    /**
     * Inicializa las constantes de la APP con los valores definidas en las cofiguraciones generales(sharedpreferences)
     */
    private void loadPreferences(){
        Log.i("LOADPREFERENCES", "ingreso");
        try {
            sp = PreferenceManager.getDefaultSharedPreferences(this.context);
            USER_REST = sp.getString("USER", "admin");
            Log.i("LOADPREFERENCES3", "USER:" + sp.getString("USER", "DV:admin"));
            PASS_REST = sp.getString("PASS", "1234");
            Log.i("LOADPREFERENCES3", "PASS:" + sp.getString("PASS", "DV:1234"));
            SERVER_REST = sp.getString("SERVER", "192.168.1.8");
            //SERVER_REST = sp.getString("SERVER", "186.46.226.139");
            Log.i("LOADPREFERENCES3", "SERVER:" + sp.getString("SERVER", "DFV:192.168.1.8"));
            PORT_REST = Integer.parseInt(sp.getString("PORT", "80"));
            Log.i("LOADPREFERENCES3", "PORT:" + sp.getString("PORT", "DV80:"));
            DEFAULT_FORMAT_RESPONSE = sp.getString("REST_RESPONSE", "JSON");
            Log.i("LOADPREFERENCES3", "REST_RESPONSE:" + sp.getString("REST_RESPONSE", "DV:JSON"));
            AUTH_ENABLED = sp.getBoolean("REST_AUTH", true);
            Log.i("LOADPREFERENCES3", "REST_AUTH:" + sp.getBoolean("REST_AUTH", true));
            TIMEOUTCONNECTION = Integer.parseInt(sp.getString("TIMEOUTCONNECTION", "5000"));
            Log.i("LOADPREFERENCES3", "REST_AUTH:" + sp.getString("REST_AUTH", "DV:5000"));
            SETSOTIMEOUT = Integer.parseInt(sp.getString("SETSOTIMEOUT", "5000"));
            Log.i("LOADPREFERENCES3", "SETSOTIMEOUT:" + sp.getString("SETSOTIMEOUT", "DV:5000"));
            SUCURSAL = Integer.parseInt(sp.getString("SUCURSAL","1"));
            Log.i("LOADPREFERENCES3", "SUCURSAL:" + sp.getString("SUCURSAL", "DV:1"));

            SENDER_ID = sp.getString("SENDER_IR", "903913289319");
            Log.i("LOADPREFERENCES3", "SENDER_IR:" + sp.getString("SENDER_IR", "DV:903913289319"));


            WifiManager manager = (WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            USER_MAC = info.getMacAddress();
            Log.i("LA MAC",""+USER_MAC);
            SUBDOMAIN = sp.getString("SUBDOMAIN", "mayorga");
            Log.i("LOADPREFERENCES3", "SUBDOMAIN:" + sp.getString("SUBDOMAIN", "DV:mayorga"));
            Log.i("LOADPREFERENCES3", "constantes incializadas");
        }catch(NullPointerException e){
            Log.i("LOADPREFERENCES3", "Error al cargar constantes desde shared prefenreces");
        }

    }

    /**
     * Muestra un DialogMessage con el boton Aceptar
     * @param title Título del mensaje
     * @param message Contenido del mensaje
     * @param icon Icono que informe el tipo de mensaje
     */
    public void mostrarDialogoAlerta(String title, String message,Drawable icon)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(icon);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Dialogos", "Botón aceptar pulsado");
            }
        });
        builder.show();
    }

    /**
     * Verificac si el dispositivo tiene activa la conexión por red
     * @return true / false
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * Inicializa la variable estatica de VENDEDOR
     * @param user
     */
    public void login(String user){
        VendedorDB vendedorDB= new VendedorDB(this.context);
        try {
            vendedorDB.open();
            VENDEDOR  = vendedorDB.get(user);
            vendedorDB.close();
        } catch (SQLException e) {
            vendedorDB.close();
            e.printStackTrace();
        }
    }

    /**
     * Libera la variable estática VENDEDOR
     */
    public static void logout(){
        VENDEDOR = null;
    }

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
