package apps.denux.mayorga;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

import apps.denux.mayorga.helpers.RESTHelper;
import apps.denux.mayorga.objetos.Dispositivo;

/**
 * Created by dexter on 13/03/15.
 */
public class Utiles {
    /**
     * Crea un TexView con un mensaje personalizado para ser mostrado en las listas vacias
     * @param text
     * @param context
     * @param listView
     * @return
     */
    public static TextView noItems(String text,FragmentActivity context,ListView listView) {
        TextView emptyView = new TextView(context);
        //Make sure you import android.widget.LinearLayout.LayoutParams;
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        //Instead of passing resource id here I passed resolved color
        //That is, getResources().getColor((R.color.gray_dark))
        //emptyView.setTextColor(
        emptyView.setText(text);
        emptyView.setTextSize(12);
        emptyView.setVisibility(View.GONE);
        emptyView.setGravity(Gravity.CENTER_VERTICAL
                | Gravity.CENTER_HORIZONTAL);

        //Add the view to the list view. This might be what you are missing
        ((ViewGroup) listView.getParent()).addView(emptyView);

        return emptyView;
    }

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();
    private static Dispositivo dispositivo;

    public static void register(final Context context, String nombre, String email, final String regId){

        dispositivo= new Dispositivo(Constantes.USER_MAC,regId,10000);

        RESTHelper restHelper = new RESTHelper();

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register on our server
        // As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d("SERVER CX", "Attempt #" + i + " to register");
            try {
                Constantes.displayMessage(context, context.getString(
                        R.string.server_registering, i, MAX_ATTEMPTS));
                restHelper.post(Constantes.URL_POST_DEVICE,"",dispositivo.getJSON());
                String message = context.getString(R.string.server_registered);
                Constantes.displayMessage(context, message);
                return;
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e("SErver", "Failed to register on attempt " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d("SErver", "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d("SErver", "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String message = context.getString(R.string.server_register_error,
                MAX_ATTEMPTS);
        Constantes.displayMessage(context, message);
    }

    /**
     * Unregister this account/device pair within the server.
     */
    public static void unregister(final Context context, final String regId) {
        Log.i("Server", "unregistering device (regId = " + regId + ")");

        dispositivo = new Dispositivo(regId);

        RESTHelper restHelper = new RESTHelper();
        try {
            try {
                restHelper.delete(Constantes.URL_DELETE_DEVICE, "regId/" + regId.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.server_unregistered);
            Constantes.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.server_unregister_error,
                    e.getMessage());
            Constantes.displayMessage(context, message);
        }
    }
}
