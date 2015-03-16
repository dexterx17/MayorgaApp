package apps.denux.mayorga.controladores;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import apps.denux.mayorga.Constantes;
import apps.denux.mayorga.R;
import apps.denux.mayorga.modelos.ProductosDB;
import apps.denux.mayorga.objetos.Producto;

/**
 * Created by dexter on 13/03/15.
 */
public class ProductoFragment extends Fragment {

    public static final String ARG_ITEM_ID = "ID";
    public static final String ARG_ITEM_ORIGEN = "ORIGEN";
    private Producto mProducto;
    boolean productoYaEstaEnLista = false;
    private int indiceProductoYaEncontrado ;
    private ProductosDB productosDB;

    TextView tvNombre, tvMarca, tvIva, tvExistencia, tvPrecio1, tvPrecio2;
    TextView tvPrecio3, tvPrecio4, tvUnidad, tvPeso, tvDestacado;
    TextView tvActualizacion, tvSiEstaEnLista;
    TextView tvPrecioEnListaL, tvCantidadEnListaL, tvTotalEnListaL;
    TextView tvPrecioEnListaE, tvCantidadEnListaE, tvTotalEnListaE;
    CheckBox chkDestacado;
    private String nomProducto;
    private String origen;
    TableLayout tlProductoYaSeleccionado;

    /**
     * Inicializa los componentes de la vista
     * @param v
     */
    private void init(View v){
        tvNombre = (TextView)v.findViewById(R.id.tvpNombre);
        tvMarca = (TextView)v.findViewById(R.id.tvpMarca);
        tvIva = (TextView)v.findViewById(R.id.tvpIva);
        tvExistencia = (TextView)v.findViewById(R.id.tvpExistencia);
        tvPrecio1 = (TextView)v.findViewById(R.id.tvpPrecio1);
        tvPrecio2 = (TextView)v.findViewById(R.id.tvpPrecio2);
        tvPrecio3 = (TextView)v.findViewById(R.id.tvpPrecio3);
        tvPrecio4 = (TextView)v.findViewById(R.id.tvpPrecio4);
        tvUnidad = (TextView)v.findViewById(R.id.tvpUnidad);
        tvPeso = (TextView)v.findViewById(R.id.tvpPeso);
        tvDestacado = (TextView)v.findViewById(R.id.tvpDestacado);
        tvActualizacion = (TextView)v.findViewById(R.id.tvpActualizacion);
        chkDestacado =(CheckBox)v.findViewById(R.id.chkDestacado);
        chkDestacado.setEnabled(false);
        tvSiEstaEnLista = (TextView)v.findViewById(R.id.tvpSiEstaEnLista);
        //TextView solo Vistas
        tvPrecioEnListaL = (TextView)v.findViewById(R.id.tvpPrecioYaExiste);
        tvCantidadEnListaL = (TextView)v.findViewById(R.id.tvpCantidadYaExiste);
        tvTotalEnListaL = (TextView)v.findViewById(R.id.tvpTotalYaExiste);
        //TextView para escritura
        tvPrecioEnListaE  = (TextView)v.findViewById(R.id.tvpPrecioYaSeleccionado);
        tvCantidadEnListaE  = (TextView)v.findViewById(R.id.tvpCantidadYaSeleccionado);
        tvTotalEnListaE  = (TextView)v.findViewById(R.id.tvpTotalYaSeleccionado);
        tlProductoYaSeleccionado = (TableLayout)v.findViewById(R.id.tlProductoYaSeleccionado);

    }

    /**
     * Constructor: crea una instancia ClienteFragment
     */
    public ProductoFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.producto_fragment,container,false);
        //Obtengo los parametros para la vista
        Bundle bundle = getArguments();
        //Verifico que existan parametros y exista la constante que necesito
        if(bundle!=null && bundle.containsKey(ARG_ITEM_ID)) {
            productosDB = new ProductosDB(getActivity());
            try {
                nomProducto = bundle.getString(ARG_ITEM_ID);
                productosDB.open();
                mProducto = productosDB.get(bundle.getString(ARG_ITEM_ID));
                productosDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //if(bundle.containsKey(ARG_ITEM_ORIGEN))
        origen =  bundle.getString(ARG_ITEM_ORIGEN);
        productoYaEstaEnLista = false;
        // Toast.makeText(getActivity(), "Variable que viene desde Productos es "+ origen , Toast.LENGTH_LONG).show();
        Log.i("rootView", "Vista inicializada2");

        init(rootView);

        if(mProducto != null){
            tvNombre.setText(mProducto.NOMBRE.trim());
            tvMarca.setText(mProducto.MARCA);
            tvIva.setText(String.valueOf(mProducto.IVA)+"");
            tvExistencia.setText(String.valueOf(mProducto.EXISTENCIA));
            tvPrecio1.setText(String.valueOf(mProducto.PRECIO1));
            tvPrecio2.setText(String.valueOf(mProducto.PRECIO2));
            tvPrecio3.setText(String.valueOf(mProducto.PRECIO3));
            tvPrecio4.setText(String.valueOf(mProducto.PRECIO4));
            tvUnidad.setText(mProducto.UNIDAD);
            tvPeso.setText(String.valueOf(mProducto.PESO));
            tvActualizacion.setText(String.valueOf(mProducto.ACTUALIZACION));
            chkDestacado.setChecked(mProducto.DESTACADO);
           /* String codPrductoLista ="" ;
            String codProductNew = mProducto.CODIGO;
            if( ProductoAgregarFragment.fillMaps.size()>0){
                for(int i= 0; i < ProductoAgregarFragment.fillMaps.size(); i++){
                    codPrductoLista = String.valueOf(ProductoAgregarFragment.fillMaps.get(i).get("cCod"));
                    if(codProductNew.equals(codPrductoLista)){
                        Log.i("Desde PF ", "el producto si esta seleccionado en la lista");
                        productoYaEstaEnLista = true;
                        tlProductoYaSeleccionado.setVisibility(View.VISIBLE);
                        indiceProductoYaEncontrado = i;
                        tvPrecioEnListaE.setText(ProductoAgregarFragment.fillMaps.get(i).get("cPrecio"));
                        tvCantidadEnListaE.setText(ProductoAgregarFragment.fillMaps.get(i).get("cCantidad"));
                        tvTotalEnListaE.setText(ProductoAgregarFragment.fillMaps.get(i).get("cTotal"));
                        break;
                    }
                    //productoYaEstaEnLista = true; //Para probar
                } }
            //tvSiEstaEnLista.setText("");*/
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //if(origen != null){
        //Log.i("ProductoFragment ", " "+ origen);
        if(origen == "PEDIDO"){
            inflater.inflate(R.menu.menu_pedido_producto, menu);
            if(menu!=null){
                //oculto botones innecesarios
                menu.findItem(R.id.menu_buscar).setVisible(false);
                menu.findItem(R.id.menu_pedido_add_ok_producto).setVisible(false);
                menu.findItem(R.id.menu_finalizar_editar_producto).setVisible(false);
            }
        }
        if(origen == "EDITAR"){
            inflater.inflate(R.menu.menu_pedido_producto, menu);
            if(menu!=null){
                //oculto botones innecesarios
                menu.findItem(R.id.menu_buscar).setVisible(false);
                menu.findItem(R.id.menu_pedido_add_ok_producto).setVisible(false);
                //menu.findItem(R.id.menu_pedido_add_ok_producto).setVisible(false);
                //Log.i("ProductoFragment el visto y reresar al Maestro "," el origen es "+origen);
            }
        }
        if(origen == "PRODUCTOS"){
            inflater.inflate(R.menu.menu_item, menu);
            if(menu!=null){

            }
            //Obtengo los parametros para la vista
            Bundle bundle = getArguments();
            //Verifico que existan parametros y exista la constante que necesito
            if(bundle!=null && bundle.containsKey("EVENTO")) {
                Object i = bundle.get("EVENTO");
                if (i.equals(Constantes.FORM_OPERACIONES.VIEW)) {
                    menu.findItem(R.id.mi_edit_item).setVisible(true);
                    menu.findItem(R.id.mi_save_item).setVisible(false);

                } else
                if (i.equals(Constantes.FORM_OPERACIONES.UPDATE)) {
                    menu.findItem(R.id.mi_save_item).setVisible(true);
                    menu.findItem(R.id.mi_edit_item).setVisible(false);
                    chkDestacado.setEnabled(true);
                    tvDestacado.setTextColor(Color.BLACK);
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pedido_add_set_producto:
                Bundle args = new Bundle();
                args.putString(ProductoAgregarFragment.ARG_ITEM_ID, nomProducto);
                args.putString(ProductoAgregarFragment.ARG_ITEM_ORIGEN, "PEDIDO");
                if(productoYaEstaEnLista) {
                    args.putBoolean("PRODUCTO_EN_LISTA", true);
                    args.putInt("INDICE", indiceProductoYaEncontrado);
                }
                ProductoAgregarFragment productoAgregarFragment = new ProductoAgregarFragment();
                productoAgregarFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, productoAgregarFragment).addToBackStack("productoAgregarFragment").commit();

                return true;

            case R.id.mi_edit_item:
                Bundle bundle = new Bundle();
                bundle.putString(ProductoFragment.ARG_ITEM_ID, nomProducto);
                bundle.putString(ProductoFragment.ARG_ITEM_ORIGEN, "PRODUCTOS");
                bundle.putSerializable("EVENTO",Constantes.FORM_OPERACIONES.UPDATE);
                ProductoFragment productoFragment = new ProductoFragment();
                productoFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, productoFragment).addToBackStack("productoFragment").commit();

                return true;
            case R.id.mi_save_item:
                //Obtener ID_producto y el valor de chkDestacado
                if(DestacarProducto(nomProducto, chkDestacado.isChecked())){
                    Toast.makeText(getActivity(), "Producto Actualizado", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(getActivity(), "No se pudo actualizar el producto", Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean DestacarProducto(String codProducto, boolean destacado){
        try {
            productosDB.open();
            mProducto = productosDB.get(codProducto);
            mProducto.DESTACADO = destacado;
            if(productosDB.update(mProducto)) {
                productosDB.close();
                return true;
            }else {
                productosDB.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            productosDB.close();
        }
        return false;
    }


}
