package apps.denux.mayorga.controladores;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import apps.denux.mayorga.Constantes;
import apps.denux.mayorga.R;
import apps.denux.mayorga.Utiles;
import apps.denux.mayorga.helpers.EventosHelper;
import apps.denux.mayorga.helpers.RESTHelper;
import apps.denux.mayorga.helpers.SyncHelper;
import apps.denux.mayorga.modelos.ClientesDB;
import apps.denux.mayorga.modelos.EventosDB;
import apps.denux.mayorga.modelos.PedidoDB;
import apps.denux.mayorga.modelos.PedidoItemDB;
import apps.denux.mayorga.objetos.Cliente;
import apps.denux.mayorga.objetos.Evento;
import apps.denux.mayorga.objetos.Pedido;
import apps.denux.mayorga.objetos.PedidoItem;

/**
 * Created by dexter on 15/03/15.
 */
public class ServidorFragment extends Fragment implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "1";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "771344046493";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Demo";

    Button btnRegistrar;
    Button btnUnregistrar;
    Button btnSincronizar;
    Button btnSincronizarPush;
    Button btnConexionInternet;
    Button btnConexionServer;
    CheckBox chbInternet;
    CheckBox chbServer;
    TextView mDisplay;
    GoogleCloudMessaging gcm;
    Context context;

    String regid;

    AtomicInteger msgId = new AtomicInteger();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.servidor, container, false);
        mDisplay = (TextView) vista.findViewById(R.id.display);
        btnRegistrar = (Button) vista.findViewById(R.id.btnRegistrarGCM);
        btnUnregistrar = (Button) vista.findViewById(R.id.btnUnregistrarGCM);
        btnSincronizar = (Button) vista.findViewById(R.id.btnSincronizarTodo);
        btnSincronizarPush = (Button) vista.findViewById(R.id.btnSincronizarPush);
        btnConexionInternet = (Button) vista.findViewById(R.id.btnConexionInternet);
        btnConexionServer = (Button) vista.findViewById(R.id.btnConexionServidor);
        chbInternet = (CheckBox) vista.findViewById(R.id.chbConexionInternet);
        chbServer = (CheckBox) vista.findViewById(R.id.chbConexionServidor);

        context = getActivity();

        btnRegistrar.setOnClickListener(this);
        btnUnregistrar.setOnClickListener(this);
        btnConexionServer.setOnClickListener(this);
        btnSincronizar.setOnClickListener(this);
        btnSincronizarPush.setOnClickListener(this);

        if(verificiarConexionInternet())
            verificarConexionServer();
        return vista;
    }

    /**
     * Captura los clicks de los boytones
     * @param view
     */
    public void onClick(final View view) {

        switch (view.getId()){
            case R.id.btnRegistrarGCM:
                // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
                if (checkPlayServices()) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = getRegistrationId(context);

                    if (regid.isEmpty()) {
                        registerInBackground();
                    }
                } else {
                    Log.i(TAG, "No valid Google Play Services APK found.");
                }
                break;
            case R.id.btnSincronizarPush:
                enviarActualizaciones();
                break;
            case R.id.btnUnregistrarGCM:
                removeRegistrationId(context);
                unregisterInBackground();
                break;
            case R.id.btnSincronizarTodo:
                sincronizarPendientes();
                break;
            case R.id.btnConexionInternet:
                verificiarConexionInternet();
                break;
            case R.id.btnConexionServidor:
                if(verificiarConexionInternet())
                    verificarConexionServer();
                break;
        }
    }

    private void sincronizarPendientes() {
        SyncHelper syncHelper = new SyncHelper(getActivity());
        syncHelper.execute(Constantes.URLS_DOWNLOAD_UPDATES);
    }

    /**
     * Recorre la tabla de eventos y realiza las llamadas al webserver para sincronizar los datos
     */
    private void enviarActualizaciones() {
        EventosDB eventosDB = new EventosDB(getActivity());
        ArrayList<Evento> eventos;
        try {
            eventosDB.open();
            eventos = eventosDB.getList();
            EventosHelper restHelper = new EventosHelper(getActivity());
            restHelper.execute(eventos);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            eventosDB.close();
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");

            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Removes the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     */
    private void removeRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(PROPERTY_REG_ID);
        editor.remove(PROPERTY_APP_VERSION);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    Utiles.register(context, Constantes.USER_MAC, Constantes.USER_MAC, regid);

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    /**
     * UnRegisters the application with GCM servers asynchronously.
     * <p>
     * Remove the registration ID and the app versionCode from the
     * shared preferences.
     */
    private void unregisterInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    gcm.unregister();
                    msg = "Device unregistered";

                    Utiles.unregister(context, regid);

                    removeRegistrationId(context);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }


    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(Servidor.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }



    /**
     * Realiza una petición al server para verificar si tene conexión
     */
    private void verificarConexionServer(){
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {

                boolean resultado = false;
                RESTHelper restHelper = new RESTHelper();
                try {
                    resultado = restHelper.get(Constantes.URL_TEST_CONEXION);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return resultado;
            }

            @Override
            protected void onPostExecute(Boolean resultado) {
                chbServer.setChecked(resultado);
            }
        }.execute(null, null, null);
    }

    /**
     * Verifica si el dispositivo puede conectarse a internet
     */
    private boolean verificiarConexionInternet(){
        boolean resultado = new Constantes(context).isOnline();
        chbInternet.setChecked(resultado);
        return resultado;
    }
}
