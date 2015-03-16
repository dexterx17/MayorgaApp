package apps.denux.mayorga.controladores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import apps.denux.mayorga.R;
import apps.denux.mayorga.Utiles;
import apps.denux.mayorga.adapters.PedidosAdapter;
import apps.denux.mayorga.modelos.PedidoDB;
import apps.denux.mayorga.objetos.Pedido;

/**
 * Created by dexter on 15/03/15.
 */
public class PedidosFragment  extends ListFragment {

    public static final String ARG_ITEM_ID = "PEDIDO";
    private static final String ESTADO_POSICION_ACTIVA = "posicion";
    private int posicionActiva = ListView.INVALID_POSITION;
    private Callbacks mCallbacks = sMyCallbacks;
    private ArrayList<Pedido> listArrayPedidos;
    TextView infoLista;
    ListView lsPedidos;
    private boolean mOcupado = false;

    public interface Callbacks{

        public void onItemSelected(String id);
    }
    private static Callbacks sMyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public PedidosFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pedidos_fragment,container,false);

        //infoLista = (TextView) view.findViewById(R.id.tvFooter);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        PedidoDB pedidosDB = new PedidoDB(getActivity());
        Bundle bundle = getArguments();
        try {
            pedidosDB.open();
            if(bundle!=null && bundle.containsKey(ARG_ITEM_ID)) {
                String rucString = bundle.getString(ARG_ITEM_ID);
                listArrayPedidos = pedidosDB.getListJoin(rucString);
                Toast.makeText(getActivity(), "La selección es " + rucString, Toast.LENGTH_LONG).show();
                // / listPedidos = pedidosDB.getList(bundle.getString(ARG_ITEM_ID));
                PedidosAdapter pedidosAdapter = new PedidosAdapter(getActivity(),listArrayPedidos);
                setListAdapter(pedidosAdapter);
            }else{
                Log.i("Fragment Pedidos", "No hay parametros, mostrar pedidos ");
                //listArrayPedidos= pedidosDB.getList();
                listArrayPedidos = pedidosDB.getListJoin();
                PedidosAdapter pedidosAdapter = new PedidosAdapter(getActivity(),listArrayPedidos);
                setListAdapter(pedidosAdapter);
                //lsPedidos.setAdapter(adapter);

            }

            pedidosDB.close();
        } catch (SQLException e) {
            pedidosDB.close();
            e.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.containsKey(ESTADO_POSICION_ACTIVA)){
            setPosicionActiva(posicionActiva);
        }
        if(listArrayPedidos==null){
            getListView().setEmptyView(Utiles.noItems("BUSCAR PEDIDOS", getActivity(), getListView()));
        }else if(listArrayPedidos.size()==0){
            getListView().setEmptyView(Utiles.noItems("NO SE ENCONTRARON RESULTADOS!", getActivity(), getListView()));
            Toast.makeText(getActivity(),"NO SE ENCONTRARON RESULTADOS!",Toast.LENGTH_LONG);
        }
    }
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallbacks.onItemSelected(listArrayPedidos.get(position).DISPOSITIVO.toString());

        //Toast.makeText(getActivity(), "Es el pedido " +listArrayPedidos.get(position).COD_PEDIDO, Toast.LENGTH_LONG).show();

        Intent intent = new Intent();
        intent.setClass(getActivity(), PedidoFragment.class);
        intent.putExtra("PEDIDO_RUCII", listArrayPedidos.get(position).RUCCI);
        intent.putExtra("PED_CODIGO", listArrayPedidos.get(position).COD_PEDIDO);
        intent.putExtra("PEDIDO_EMPRESA", listArrayPedidos.get(position).Empresa);
        intent.putExtra("PEDIDO_FECHA", listArrayPedidos.get(position).FECHA.toString());
        startActivity(intent);

        //PedidoFragmentDetalle pedidoFragment = new PedidoFragmentDetalle();
        //getFragmentManager().beginTransaction().replace(R.id.content_frame, pedidoFragment).addToBackStack(null).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(posicionActiva !=ListView.INVALID_POSITION){
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
