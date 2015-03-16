package apps.denux.mayorga.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import apps.denux.mayorga.Constantes;
import apps.denux.mayorga.R;
import apps.denux.mayorga.modelos.ClientesDB;
import apps.denux.mayorga.modelos.TipoClienteDB;
import apps.denux.mayorga.modelos.TipoIdentificacionDB;

/**
 * Created by dexter on 13/03/15.
 */
public class Clientes extends ActionBarActivity implements ClientesFragment.Callbacks {

        private boolean swDoblePanel;
        ClientesDB clientesDB;
        TipoIdentificacionDB tiposID;
        TipoClienteDB tiposDB;

        private boolean seleccion=false;

        /**
         * Carga el layout (activity_main.xml) y ademas agrega el FragmentProductos
         * @param savedInstanceState
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //Mostrar el boton arriba en el action bar
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Clientes");
            getSupportActionBar().setSubtitle("Listado de Clientes");

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null && bundle.containsKey("SELECCION")){
                seleccion=true;
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            }
          //  getSupportFragmentManager().addOnBackStackChangedListener(this);
            if(savedInstanceState == null){
                //Crear el fragment detalle y lo añade a la actividad usando una fragment transaction
                ClientesFragment clientesFragment = new ClientesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, clientesFragment).addToBackStack("clientesFragment").commit();
            }
        }

        /**
         * Carga el menu de opciones para los Clientes:
         * Agregar Cliente ,Sincronizar clientes, sincronizar tipos, sincronizar tipos de ID y un campo de busqueda
         * @param menu
         * @return
         */
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main_menu_clientes, menu);
            final MenuItem mItem = menu.findItem(R.id.mi_buscar);
            final SearchView sView = (SearchView) MenuItemCompat.getActionView(mItem);
            sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    ClientesFragment clientesFragment = new ClientesFragment();
                    Bundle args = new Bundle();
                    args.putString(ClientesFragment.ARG_ITEM_ID,s);
                    clientesFragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,clientesFragment).addToBackStack("clientesFragment").commit();
                    MenuItemCompat.collapseActionView(mItem);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
            MenuItemCompat.setOnActionExpandListener(mItem,new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    return true;
                }
            });
            return true;
        }

        /**
         * Realiza las operaciones de acuerdo al menu seleccionado
         * @param item
         * @return
         */
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.home:
                    NavUtils.navigateUpTo(this, new Intent(this, Clientes.class));
                    return true;
                case R.id.mi_add_cliente:
                    ClienteFragment client = new ClienteFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("EVENTO", Constantes.FORM_OPERACIONES.NEW);
                    //Pasando parametros al fragment
                    client.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, client).addToBackStack("clienteFragment").commit();

                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        /**
         * Callback metodo desde {@link apps.denux.mayorga.controladores.ClientesFragment.Callbacks}
         * indica que el item con el ID dado fue seleccionado
         * @param id
         */
        @Override
        public void onItemSelected(String id) {

            if(seleccion) {
                Intent i = new Intent();
                i.putExtra(ClientesFragment.ARG_ITEM_ID, id);
                setResult(RESULT_OK, i);
                finish();
            }else{
                Bundle args = new Bundle();
                args.putString(ClienteFragment.ARG_ITEM_ID,id);
                args.putSerializable("EVENTO",Constantes.FORM_OPERACIONES.VIEW);

                ClienteFragment client = new ClienteFragment();
                //Pasando parametros al fragment
                client.setArguments(args);

                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, client).addToBackStack("clienteFragment").commit();
            }
        }

    @Override
    public boolean onSupportNavigateUp() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            getSupportFragmentManager().popBackStack();
            return true;
        }else
            return super.onSupportNavigateUp();
    }
}
