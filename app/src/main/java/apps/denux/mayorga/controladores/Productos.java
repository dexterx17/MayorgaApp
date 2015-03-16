package apps.denux.mayorga.controladores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import apps.denux.mayorga.Constantes;
import apps.denux.mayorga.R;
import apps.denux.mayorga.modelos.ProductosDB;

/**
 * Created by dexter on 13/03/15.
 */
public class Productos extends ActionBarActivity implements ProductosFragment.Callbacks {

    private boolean swDoblePanel;
    public static final String ARG_ITEM_ID = "PRODUCTO";
    public static final String ARG_ITEM_ORIGEN = "ORIGEN";

    private boolean seleccionProducto = false;
    private boolean editarProducto = false;

    ProductosDB productosDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Productos");
        getSupportActionBar().setSubtitle("Listado de Productos");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("SELECT_PRODUCTO")) {
            seleccionProducto = true;
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            if (savedInstanceState == null) {
                ProductosFragment productosFragment = new ProductosFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productosFragment).commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productosFragment).addToBackStack("productosFragment").commit();
                Log.i("savedInstanceState ", "ES NULL");
            }
        } else if (bundle != null && bundle.containsKey("EDITAR_PRODUCTO") && bundle.containsKey("INDICE")) {
            editarProducto = true;
            if (editarProducto) {
                int indiceEditar = bundle.getInt("INDICE");
                Bundle args = new Bundle();
                args.putBoolean("EDITAR_PRODUCTO", true);
                args.putInt("INDICE", indiceEditar);
                args.putString(ProductoAgregarFragment.ARG_ITEM_ORIGEN, "EDITAR");
                ProductoAgregarFragment productoAgregarFragment = new ProductoAgregarFragment();
                productoAgregarFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productoAgregarFragment).addToBackStack("productoAgregarFragment").commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productoAgregarFragment).commit();
                Log.i("Esta en productos y pasa a ", "ProductoAgregarFragment para edición");
            }
        }

        if (savedInstanceState == null) {
            ProductosFragment productosFragment = new ProductosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productosFragment).addToBackStack("productosFragment").commit();
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (seleccionProducto) {
            Bundle args = new Bundle();
            args.putString(ProductoFragment.ARG_ITEM_ID, id);
            args.putString(ProductoFragment.ARG_ITEM_ORIGEN, "PEDIDO");
            ProductoFragment productoFragment = new ProductoFragment();
            //Pasando parametros al fragment
            productoFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productoFragment).addToBackStack(null).commit();

        } else {
            Bundle args = new Bundle();
            args.putString(ProductoFragment.ARG_ITEM_ID, id);
            Log.i("ITEMSELECTED", "::" + id);
            args.putSerializable("EVENTO", Constantes.FORM_OPERACIONES.VIEW);
            args.putString(ProductoFragment.ARG_ITEM_ORIGEN, "PRODUCTOS");
            ProductoFragment productoFragment = new ProductoFragment();
            //Pasando parametros al fragment
            productoFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productoFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_productos, menu);

        //oculto botones innecesarios
        if (menu != null && !seleccionProducto) {
            menu.findItem(R.id.menu_finalizar_select_producto).setVisible(false);
        }


        final MenuItem searchItem = menu.findItem(R.id.menu_buscar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Cuando presiono buscar
            @Override
            public boolean onQueryTextSubmit(String s) {
                ProductosFragment productosFragment = new ProductosFragment();
                Bundle args = new Bundle();
                args.putString(ARG_ITEM_ID, s);
                productosFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productosFragment).addToBackStack("productosFragment").commit();
                MenuItemCompat.collapseActionView(searchItem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        });
        //return true;
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //aumento
            case R.id.menu_finalizar_select_producto:
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
                return true;
            case R.id.menu_pedido_list_user:
                return true;
            case R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, Clientes.class));
                return true;
            case R.id.mi_sync_productos:
                Toast.makeText(this, "Sync Productos", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}