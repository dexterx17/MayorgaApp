package apps.denux.mayorga.controladores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.internal.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import apps.denux.mayorga.R;
import apps.denux.mayorga.Utiles;
import apps.denux.mayorga.adapters.ProductosAdapter;
import apps.denux.mayorga.modelos.ProductosDB;
import apps.denux.mayorga.objetos.Producto;

/**
 * Created by dexter on 13/03/15.
 */
public class ProductosFragment extends ListFragment implements ListViewCompat.OnScrollListener{

    public static final String ARG_ITEM_ID = "PRODUCTO";
    private static final String ESTADO_POSICION_ACTIVA = "posicion";

    private int posicionActiva = ListView.INVALID_POSITION;

    private Callbacks mCallbacks = sMyCallbacks;

    private ArrayList<Producto> productosArrayList;

    TextView infoLista, cabecera;

    private ProgressDialog progressDialog;
    private ConsultaTask tarea;
    ProductosDB productosDB;
    String nameProduct;
    ProductosAdapter adapter;
    /**
     * Interfaz para poderse comunicar con este fragmente
     */
    public interface Callbacks{

        public void onItemSelected(String id);
    }

    /**
     * Instancia estatica de la interfaz
     */
    private static Callbacks sMyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Crea un instancia vacia de ProductosFragment
     */
    public ProductosFragment() {
    }

    /**
     * Carga la vista para el listado de productos e instancia el TextView en el footer
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productos_fragment,container,false);
        infoLista = (TextView) view.findViewById(R.id.tvFooter);
        //cabecera = (TextView) view.findViewById();
        return view;
    }

    /**
     * Instancio un Listener para el Scroll
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnScrollListener(this);
    }

    /**
     * Carga el ListView con los productos que cumplan con el parametro que recibe el fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //ProductosDB productosDB = new ProductosDB(getActivity());
        productosDB = new ProductosDB(getActivity());
        Bundle bundle = getArguments();

        if(bundle!=null && bundle.containsKey(ARG_ITEM_ID)) {

            //progressDialog = ProgressDialog.show(getActivity(), "Busacando", "Espere unos segundos...");//, true, false);
            tarea = new ConsultaTask();
            nameProduct = bundle.getString(ARG_ITEM_ID);
            tarea.execute();

        }else{
            //Cargar los productos destacados
            Log.i("ProductosFragment ", "Carga los productos destacados");
            try {
                productosDB.open();
                productosArrayList = productosDB.getListDestacados();
                adapter = new ProductosAdapter(getActivity(),productosArrayList);
                productosDB.close();
                setListAdapter(adapter);
            } catch (SQLException e) {
                e.printStackTrace();
                productosDB.close();
            }
        }
    }
    /**
     * Subclase privada que crea un hilo aparte para realizar
     * las acciones que deseemos.
     */
    private class ConsultaTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Void doInBackground(String... params) {

            //Aqui se realizan las operaciones necesarias, Realizar la búsqueda con hilo en segundo plano
            try {
                productosDB.open();
                productosArrayList = productosDB.getList(nameProduct);
                adapter = new ProductosAdapter(getActivity(),productosArrayList);
                productosDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
                productosDB.close();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Buscando", "Espere unos segundos...");//, true, false);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (ProductosFragment.this.progressDialog != null) {
                ProductosFragment.this.progressDialog.dismiss();
                setListAdapter(adapter);
            }
        }
    }

    /**
     * Si la lista esta vacia muesto un mensaje en el footer
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState !=null && savedInstanceState.containsKey(ESTADO_POSICION_ACTIVA)){
            setPosicionActiva(posicionActiva);
        }
        if(productosArrayList==null){
            getListView().setEmptyView(Utiles.noItems("BUSCAR PRODUCTOS", getActivity(), getListView()));
        }else if(productosArrayList.size()==0){
            getListView().setEmptyView(Utiles.noItems("NO SE ENCONTRARON RESULTADOS!", getActivity(), getListView()));
            Toast.makeText(getActivity(), "NO SE ENCONTRARON RESULTADOS!", Toast.LENGTH_LONG);
        }
    }

    /**
     * En el footer muestro el item en el que se encuentra y el total de items === posicion/total
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int primero = view.getFirstVisiblePosition();
        infoLista.setText(String.valueOf(primero)+" / "+String.valueOf(productosArrayList.size()));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * Inicializa la instancia estática de la interfaz
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof Callbacks)){
            throw new IllegalStateException("La actividad debe implementar el Callback de ProductosFragment");
        }
        mCallbacks=(Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sMyCallbacks;
    }

    /**
     * Notifico a la interfaz pública que se ejecuto el evento selección
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallbacks.onItemSelected(productosArrayList.get(position).CODIGO.toString());
    }

    /**
     * Guardo la posición seleccionada en caso de exister
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (posicionActiva != ListView.INVALID_POSITION) {
            outState.putInt(ESTADO_POSICION_ACTIVA,posicionActiva);
        }
    }

    private void setPosicionActiva(int posicion) {
        if (posicion == ListView.INVALID_POSITION) {
            getListView().setItemChecked(posicionActiva, false);
        } else {
            getListView().setItemChecked(posicion, true);
        }
        posicionActiva = posicion;
    }


}
