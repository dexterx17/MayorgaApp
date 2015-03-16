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

import apps.denux.mayorga.Constantes;
import apps.denux.mayorga.LoginActivity;
import apps.denux.mayorga.R;
import apps.denux.mayorga.modelos.PedidoDB;

/**
 * Created by dexter on 13/03/15.
 */
public class Pedidos extends ActionBarActivity implements PedidosFragment.Callbacks{

    PedidoDB pedidosDB;
    public static final String ARG_ITEM_ID = "PEDIDO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Pedidos");
        getSupportActionBar().setTitle("Listado de Pedidos");

        if (savedInstanceState == null) {
            PedidosFragment pedidosFragment = new PedidosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, pedidosFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onItemSelected(String id) {
/*        if(seleccionProducto) {
            Bundle args = new Bundle();
            args.putString(ProductoFragment.ARG_ITEM_ID, id);
            args.putString(ProductoFragment.ARG_ITEM_ORIGEN, "PEDIDO");
            ProductoFragment productoFragment = new ProductoFragment();
            //Pasando parametros al fragment
            productoFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productoFragment).addToBackStack(null).commit();

        }else {*/
        Bundle args = new Bundle();
        args.putString(ProductoFragment.ARG_ITEM_ID, id);
        Log.i("ITEMSELECTED", "::" + id);
        args.putSerializable("EVENTO", Constantes.FORM_OPERACIONES.VIEW);
        PedidosFragment pedidosFragment = new PedidosFragment();
        //Pasando parametros al fragment
        pedidosFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, pedidosFragment).addToBackStack(null).commit();
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_pedidos, menu);
        /*MenuItem mItem = menu.findItem(R.id.mi_buscar);
        SearchView sView = (SearchView) MenuItemCompat.getActionView(mItem);
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            */
        final MenuItem searchItem = menu.findItem(R.id.mi_buscar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                PedidosFragment pedidosFragment = new PedidosFragment();
                Bundle args = new Bundle();
                args.putString(ARG_ITEM_ID, s);
                pedidosFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, pedidosFragment).addToBackStack("productosFragment").commit();
                MenuItemCompat.collapseActionView(searchItem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem,new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, Clientes.class));
                return true;
            case R.id.mi_add_pedido:
                //ClienteFragment client = new ClienteFragment();
                //Bundle args = new Bundle();
                //args.putSerializable("EVENTO", Constantes.FORM_OPERACIONES.NEW);
                ////Pasando parametros al fragment
                //client.setArguments(args);
                //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, client).addToBackStack("clienteFragment").commit();
                ProductoAgregarFragment.fillMaps.clear();
                ProductoAgregarFragment.subTotal=0;
                ProductoAgregarFragment.ivaTotal=0;
                ProductoAgregarFragment.totalPagar=0;
                Intent intent = new Intent(getApplicationContext(), PedidoFragment.class);
                startActivity(intent);

                return true;
            case R.id.mi_sync_pedidos:
                /*if(LoginActivity.CONSTANTES.isOnline()) {

                }
                else {
                    Toast.makeText(this, "Error al conectarse al servidor, intentarlo mas tarde...", Toast.LENGTH_SHORT).show();
                }*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
