package apps.denux.mayorga.controladores;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import apps.denux.mayorga.R;

/**
 * Created by dexter on 15/03/15.
 */
public class PedidoFragmentDetalle extends Fragment {
    public static double sumaTotal = 0;
    ListView lv;
    public static SimpleAdapter adapter;
    public static  String[] fromColumns;
    public  static  int[] toVistas;
    public boolean menuContextual = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.pedido_fragment_detalle, container, false);
        lv= (ListView)view.findViewById(R.id.listPedidos);
        //item mapping de layout_columnas
        //String[]
        fromColumns = new String[] {"cCod", "cProducto", "cIva", "cPrecio", "cCantidad","cTotal"};
        //Mapping de componentes
        //int[]
        toVistas = new int[] { R.id.cCod, R.id.cProducto, R.id.cIva, R.id.cPrecio, R.id.cCantidad, R.id.cTotal };
        if (ProductoAgregarFragment.fillMaps.size()>0)
        {
            adapter = new SimpleAdapter(getActivity(), ProductoAgregarFragment.fillMaps, R.layout.pedido_detalle_columnas_productos, fromColumns, toVistas);
            lv.setAdapter(adapter);
            /*sumaTotal=0;
            for(int i= 0; i < ProductoAgregarFragment.fillMaps.size(); i++){
                sumaTotal = sumaTotal + Double.valueOf(ProductoAgregarFragment.fillMaps.get(i).get("cTotal"));
                //Toast.makeText(getActivity(), ""+sumaTotal+" mas  "+ProductoAgregarFragment.fillMaps.get(i).get("cTotal"), Toast.LENGTH_LONG).show();
                //Log.i("Desde PF ", "Saco a PAF "+ProductoAgregarFragment.totalPagar);
            }*/
        }
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("EDITAR_PEDIDO")) {
            menuContextual = bundle.getBoolean("EDITAR_PEDIDO");
            Log.i("PFD ", " " + bundle.getBoolean("EDITAR_PEDIDO"));
        }
        if(!menuContextual) {
            registerForContextMenu(lv);
        }
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opciones");
        menu.add(0,v.getId(), 0, "Quitar");
        menu.add(0,v.getId(), 0, "Editar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String opcion = (String) item.getTitle();
        switch (opcion){
            case "Quitar":
                //Toast.makeText(getActivity(), "Eliminado el producto "+info.position, Toast.LENGTH_LONG).show();
                ProductoAgregarFragment.fillMaps.remove(info.position);
                adapter.notifyDataSetChanged();

                PedidoFragmentDetalle pedidoFragmentDetalle = new PedidoFragmentDetalle();
                getFragmentManager().beginTransaction().replace(R.id.frame_content_detalle, pedidoFragmentDetalle).commit();
                //Toast.makeText(getActivity(), "Total quitado productos "+ProductoAgregarFragment.totalPagar, Toast.LENGTH_LONG).show();

                ProductoAgregarFragment.totalPagar =0;
                for(int i= 0; i < ProductoAgregarFragment.fillMaps.size(); i++){
                    ProductoAgregarFragment.totalPagar = ProductoAgregarFragment.totalPagar + Double.valueOf(ProductoAgregarFragment.fillMaps.get(i).get("cTotal"));
                    //Toast.makeText(getActivity(), ""+sumaTotal+" mas  "+ProductoAgregarFragment.fillMaps.get(i).get("cTotal"), Toast.LENGTH_LONG).show();
                    //Log.i("Desde PF ", "Saco a PAF "+ProductoAgregarFragment.totalPagar);
                }
                Toast.makeText(getActivity(), "Total quitado productos "+ProductoAgregarFragment.totalPagar, Toast.LENGTH_LONG).show();

                break;
            case "Editar":

                break;
        }
        //Toast.makeText(getActivity(), "La opcion es "+opcion,  Toast.LENGTH_LONG).show();
        return super.onContextItemSelected(item);

    }
*/
}