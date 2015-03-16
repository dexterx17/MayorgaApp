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

import java.util.ArrayList;

import apps.denux.mayorga.R;
import apps.denux.mayorga.Utiles;
import apps.denux.mayorga.adapters.ClientesAdapter;
import apps.denux.mayorga.modelos.ClientesDB;
import apps.denux.mayorga.objetos.Cliente;

/**
 * Created by dexter on 13/03/15.
 */
public class ClientesFragment extends ListFragment implements ListViewCompat.OnScrollListener{

    public static final String ARG_ITEM_ID = "CLIENTE";

    private static final String ESTADO_POSICION_ACTIVA = "posicion";

    private int posicionActiva = ListView.INVALID_POSITION;

    private Callbacks mCallbacks = sMyCallbacks;

    private ArrayList<Cliente> listClientes;

    TextView infoLista;

    private boolean mOcupado = false;

    ClientesAdapter adapter;
    ClientesDB clientesDB;
    Bundle bundle;
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
     * Crea un instancia vacia de ClientesFragment
     */
    public ClientesFragment() {

    }



    /**
     * Carga la vista para el listado de clientes e instancia el TextView en el footer
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clientes_fragment,container,false);
        infoLista = (TextView) view.findViewById(R.id.tvFooter);
        return view;
    }

    /**
     * Carga el ListView con los todos los clientes que cumplan con el parametro pasado al fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        bundle = getArguments();
        if(bundle!=null && bundle.containsKey(ARG_ITEM_ID)) {
            new loadList().execute();
        }else{
            Log.i("Fragment clientes", "No hay parametros ");
        }
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
     * En el footer muestro el item en el que se encuentra y el total de items === posicion/total
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int primero = view.getFirstVisiblePosition();
        infoLista.setText(String.valueOf(primero)+" / "+String.valueOf(listClientes.size()));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * Si la instancia del fragment ya existia vuelvo a seleccionar el item que estaba seleccionado
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey(ESTADO_POSICION_ACTIVA)){
            setPosicionActiva(posicionActiva);
        }
        if(listClientes==null){
            getListView().setEmptyView(Utiles.noItems("BUSCAR CLIENTES", getActivity(), getListView()));
        }else if(listClientes.size()==0){
            getListView().setEmptyView(Utiles.noItems("NO SE ENCONTRARON RESULTADOS!", getActivity(), getListView()));
            Toast.makeText(getActivity(), "NO SE ENCONTRARON RESULTADOS!", Toast.LENGTH_LONG);
        }
    }

    /**
     * Inicializa la instancia estática de la interfaz
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(!(activity instanceof Callbacks)){
            throw new IllegalStateException("La actividad debe implementar el Callback del fragment");
        }
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Reinicio la instancia a una vacia
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
        mCallbacks.onItemSelected(listClientes.get(position).RUCCI.toString());
    }

    /**
     * Guardo la posición seleccionada
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(posicionActiva !=ListView.INVALID_POSITION){
            outState.putInt(ESTADO_POSICION_ACTIVA,posicionActiva);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setPosicionActiva(int posicion) {
        if (posicion == ListView.INVALID_POSITION) {
            getListView().setItemChecked(posicionActiva, false);
        } else {
            getListView().setItemChecked(posicion, true);
        }
        posicionActiva = posicion;
    }

    private class loadList extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        protected void onPreExecute() {
            dialog.setMessage("Buscando datos... Espere por favor...");
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            clientesDB = new ClientesDB(getActivity());
            try {
                clientesDB.open();
                listClientes = clientesDB.getList(bundle.getString(ARG_ITEM_ID));
                adapter = new ClientesAdapter(getActivity(),listClientes);
                clientesDB.close();
            } catch (Exception e) {
                Log.i("ERROR :","e "+e.getMessage());
            } finally {
                clientesDB.close();

            }
            return null;
        }
        protected void onPostExecute(String content) {
            dialog.dismiss();
            setListAdapter(adapter);
        }
    }
}
