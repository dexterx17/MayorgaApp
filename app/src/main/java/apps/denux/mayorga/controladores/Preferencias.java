package apps.denux.mayorga.controladores;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import apps.denux.mayorga.R;

/**
 * Created by dexter on 13/03/15.
 */
public class Preferencias extends PreferenceActivity {

        private static int prefs= R.xml.preferencias;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getClass().getMethod("getFragmentManager");
            AddResourceApi11AndGreater();
        } catch (NoSuchMethodException e) { //Api < 11
            AddResourceApiLessThan11();
        }
    }

        @SuppressWarnings("deprecation")
        protected void AddResourceApiLessThan11()
        {
            addPreferencesFromResource(prefs);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        protected void AddResourceApi11AndGreater()
        {
            getFragmentManager().beginTransaction().replace(android.R.id.content,
                    new PF()).commit();
        }


        public static class PF extends PreferenceFragment
        {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onCreate(final Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(Preferencias.prefs);
            }
        }
}
