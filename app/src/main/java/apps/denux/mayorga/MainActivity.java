package apps.denux.mayorga;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import apps.denux.mayorga.controladores.Clientes;
import apps.denux.mayorga.controladores.Pedidos;
import apps.denux.mayorga.controladores.Preferencias;
import apps.denux.mayorga.controladores.Productos;
import apps.denux.mayorga.controladores.Servidor;


public class MainActivity extends ActionBarActivity {


    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private CharSequence tituloSeccion;
    private CharSequence tituloApp;
    private String[] opciones;
    private ActionBarDrawerToggle drawerToggle;
    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ma=this;
        setContentView(R.layout.activity_main);

        opciones = new String[]{"PEDIDOS", "CLIENTES", "PRODUCTOS", "SINCRONIZAR", "SALIR"};
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), android.R.layout.simple_list_item_1, opciones));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                FragmentManager fManager;
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent();
                        intent.setClass(MainActivity.this, Pedidos.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent();
                        intent.setClass(MainActivity.this, Clientes.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent();
                        intent.setClass(MainActivity.this, Productos.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent();
                        intent.setClass(MainActivity.this, Servidor.class);
                        startActivity(intent);
                        break;
                    case 4:
                        //unbindService(sConnection);
                        Constantes.logout();
                        finish();
                        break;
                }


                drawerList.setItemChecked(position, true);

                tituloSeccion = opciones[position];
                getSupportActionBar().setTitle(tituloSeccion);

                drawerLayout.closeDrawer(drawerList);
            }
        });
        tituloSeccion = tituloApp = getTitle();
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(tituloSeccion);
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(tituloSeccion);
                ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean abierto = drawerLayout.isDrawerOpen(drawerList);

        if(abierto)
            menu.findItem(R.id.action_settings).setVisible(false);
        else
            menu.findItem(R.id.action_settings).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsActivity = new Intent(getBaseContext(),
                        Preferencias.class);
                startActivity(settingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
