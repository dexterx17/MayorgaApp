package apps.denux.mayorga.controladores;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apps.denux.mayorga.R;
import apps.denux.mayorga.adapters.PreciosAdapter;
import apps.denux.mayorga.modelos.ProductosDB;
import apps.denux.mayorga.objetos.Producto;

/**
 * Created by dexter on 15/03/15.
 */
public class ProductoAgregarFragment extends Fragment {
    public static final String ARG_ITEM_ID = "ID";
    public static final String ARG_ITEM_ORIGEN = "ORIGEN";

    TextView tvNombre;
    private Spinner spnPrecios;
    EditText etCantidad;

    private PreciosAdapter adapterPrecios;
    private String codProducto;
    private Double precio, sbTotal, total, iva, descuento;
    private Double granSubTotal, granIvaTotal, granDescuento, granTotalPagar;

    private int cantidad;
    Producto mProducto;
    private ProductosDB productosDB;

    public static final List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
    public static double totalPagar = 0;
    public static double subTotal = 0;
    public static double ivaTotal = 0;
    public static double descuentoTotal = 0;
    String origen;
    public static int indiceSeleccionadoListView;
    int indiceEditar;
    boolean productoYaExiste = false;

    DecimalFormat formateador = new DecimalFormat("###,###.##");

    private void init(View view){
        tvNombre = (TextView)view.findViewById(R.id.tvNombreAddProducto);
        etCantidad = (EditText)view.findViewById(R.id.etCantidadAddProducto);
        spnPrecios =(Spinner)view.findViewById(R.id.spnPrecioAddProducto);
    }
    public ProductoAgregarFragment(){ }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void loadPrecios(String codigo){
        try {
            productosDB.open();
            ArrayList<Double> precios = productosDB.getPrecios(codigo);
            adapterPrecios = new PreciosAdapter(precios, getActivity());
            spnPrecios.setAdapter(adapterPrecios);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            productosDB.close();
        }

    }

    private void initProducto(String codigo){
        productosDB = new ProductosDB(getActivity());
        try {
            productosDB.open();
            mProducto = productosDB.get(codigo);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            productosDB.close();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.producto_agregar_fragment,container,false);
        init(v);
        //DecimalFormat formateador = new DecimalFormat("###,###.##");
        //Obtengo los parametros para la vista
        Bundle bundle = getArguments();
        //Verifico que existan parametros y exista la constante que necesito
        //Si viene de Pedido para Editar
        if(bundle!=null && bundle.containsKey("EDITAR_PRODUCTO") && bundle.containsKey("ORIGEN") && bundle.containsKey("INDICE")){
            origen = bundle.getString(ARG_ITEM_ORIGEN);
            indiceEditar = bundle.getInt("INDICE");
            if(origen=="EDITAR") {
                Object o = fillMaps.get(indiceEditar);
                HashMap<?, ?> fullObject = (HashMap<?, ?>) o;
                String cant = (String) fullObject.get("cCantidad");
                codProducto= (String)fullObject.get("cCod");
                etCantidad.setText(cant);
            }

            initProducto(codProducto);

            Log.i("En PAF Origen " + origen + "  indice a editar " + indiceEditar + " ", " el producto " + codProducto);

            loadPrecios(codProducto);

        }  else //Si el producto ya esta en lista, buscar y modificar el ya existente
            if( bundle.containsKey(ARG_ITEM_ID) && bundle.containsKey(ARG_ITEM_ORIGEN) && bundle.containsKey("PRODUCTO_EN_LISTA")){
                origen = bundle.getString(ARG_ITEM_ORIGEN);
                indiceEditar = bundle.getInt("INDICE");
                productosDB = new ProductosDB(getActivity());
                mProducto = new Producto();
                productoYaExiste = bundle.getBoolean("PRODUCTO_EN_LISTA");
                Log.i("En PAF PRODUCTO_EN_LISTA " + origen + "  indice a editar " + indiceEditar + " ", " existe el producto? " + productoYaExiste);

                if(productoYaExiste) {
                    Object o = fillMaps.get(indiceEditar);
                    HashMap<?, ?> fullObject = (HashMap<?, ?>) o;
                    String cant = (String) fullObject.get("cCantidad");
                    codProducto= (String)fullObject.get("cCod");
                    etCantidad.setText(cant);
                }
                initProducto(codProducto);
                loadPrecios(codProducto);

            }
            else //Si viene de Pedido para añadir al detalle
                if (bundle.containsKey(ARG_ITEM_ID) && bundle.containsKey(ARG_ITEM_ORIGEN)){
                    origen = bundle.getString(ARG_ITEM_ORIGEN);
                    productosDB = new ProductosDB(getActivity());
                    mProducto = new Producto();
                    codProducto= bundle.getString(ARG_ITEM_ID);
                    Log.i("En PAF el Producto  " + codProducto," desde  "+origen);
                    initProducto(codProducto);
                    loadPrecios(codProducto);
                    loadPrecios(codProducto);
                }



        if(mProducto != null){
            tvNombre.setText(String.valueOf(mProducto.NOMBRE.trim()));
        }

        spnPrecios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                precio = (Double) spnPrecios.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Log.i("ProductoAgregarFragment ", " "+ origen);
        if(origen == "PEDIDO") {
            inflater.inflate(R.menu.menu_pedido_producto, menu);
            if (menu != null) {
                menu.findItem(R.id.menu_buscar).setVisible(false);
                menu.findItem(R.id.menu_pedido_add_set_producto).setVisible(false);
                menu.findItem(R.id.menu_finalizar_editar_producto).setVisible(false);
            }
        }
        if(origen == "EDITAR"){
            inflater.inflate(R.menu.menu_pedido_producto, menu);
            if(menu!=null){
                //oculto botones innecesarios
                menu.findItem(R.id.menu_buscar).setVisible(false);
                menu.findItem(R.id.menu_pedido_add_set_producto).setVisible(false);
                menu.findItem(R.id.menu_pedido_add_ok_producto).setVisible(false);
                //Log.i("ProductoFragment el visto y regresar al Maestro "," el origen es "+origen);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pedido_add_ok_producto:
                if(productoYaExiste){
                    int newIva =0;
                    Log.i("Aqui tiene que editar el q ya esta agregado y hay "+fillMaps.size(),"  ya esta? "+productoYaExiste);
                    if(etCantidad.getText().length() > 0){
                        cantidad = Integer.valueOf(etCantidad.getText().toString());
                        if(cantidad > 0) {
                            try {
                                Object o = fillMaps.get(indiceEditar);
                                HashMap<?, ?> fullObject = (HashMap<?, ?>)o;
                                String totalOld = (String) fullObject.get("cTotal");
                                String ivaOld = (String) fullObject.get("cIva");

                                //totalPagar = totalPagar - Double.valueOf(totalOld);
                                //subTotal = subTotal - Double.valueOf(totalOld);
                                //ivaTotal = ivaTotal - Double.valueOf(ivaOld);

                                fillMaps.get(indiceEditar).put("cIva",String.valueOf(Double.valueOf((precio * mProducto.IVA) / 100) * cantidad));
                                fillMaps.get(indiceEditar).put("cPrecio", precio.toString());

                                total = (precio+(precio*mProducto.IVA)/100) * cantidad;
                                //sbTotal = (precio*cantidad);
                                //iva = ((precio*mProducto.IVA)/100)*cantidad;

                                fillMaps.get(indiceEditar).put("cCantidad", String.valueOf(cantidad));
                                fillMaps.get(indiceEditar).put("cTotal", String.valueOf(formateador.format(total)));
                                //PedidoFragmentDetalle.adapter.notifyDataSetChanged();
                                //ivaTotal = ivaTotal + iva;
                                //subTotal = subTotal + sbTotal;
                                //totalPagar = totalPagar +total;
                                getActivity().getSupportFragmentManager().popBackStack(0,0);

                            }catch (Exception e){
                                Toast.makeText(getActivity(), "error " + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }else
                            Toast.makeText(getActivity(),"Debe ingresar una cantidad mayor a cero",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getActivity(), "Ingrese una cantidad", Toast.LENGTH_LONG).show();
                    }

                }else
                if(etCantidad.getText().length() > 0){
                    cantidad = Integer.valueOf(etCantidad.getText().toString());
                    if(cantidad > 0) {
                        try {
                            //*********************
                            //Revizar los cálculos del Maestro
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("cCod", codProducto);
                            map.put("cProducto", tvNombre.getText().toString());
                            map.put("cIva", String.valueOf(Double.valueOf((precio * mProducto.IVA) / 100) * cantidad));
                            map.put("cPrecio", precio.toString());
                            total = (precio+(precio*mProducto.IVA)/100) * cantidad;
                            //iva = ((precio*mProducto.IVA)/100)*cantidad;
                            //sbTotal = (precio*cantidad);
                            map.put("cCantidad", String.valueOf(cantidad));
                            map.put("cTotal", String.valueOf(formateador.format(total)));
                            fillMaps.add(map);
                            //ivaTotal = ivaTotal + iva;
                            //subTotal = subTotal + total;
                            //totalPagar = totalPagar + total;
                            //CalcularTotales();
                            Toast.makeText(getActivity(),"Producto agregado",Toast.LENGTH_SHORT).show();

                            getActivity().getSupportFragmentManager().popBackStack(0,0);

                        }catch (Exception e){
                            Toast.makeText(getActivity(), "error "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }else
                        Toast.makeText(getActivity(),"Debe ingresar una cantidad mayor a cero",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), "Ingrese una cantidad", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.menu_finalizar_editar_producto:
                if(etCantidad.getText().length() > 0){
                    cantidad = Integer.valueOf(etCantidad.getText().toString());
                    if(cantidad > 0) {
                        try {
                            Object o = fillMaps.get(indiceEditar);
                            HashMap<?, ?> fullObject = (HashMap<?, ?>)o;
                            //double totalOld = (double) fullObject.get("cTotal");
                            String totalOld = (String) fullObject.get("cTotal");
                            String ivaOld = (String) fullObject.get("cIva");

                            //if(totalPagar>0)=

                            //totalPagar = totalPagar - Double.valueOf(totalOld);
                            //subTotal = subTotal - Double.valueOf(totalOld);
                            //ivaTotal = ivaTotal - Double.valueOf(ivaOld);

                            fillMaps.get(indiceEditar).put("cPrecio", precio.toString());
                            fillMaps.get(indiceEditar).put("cIva",String.valueOf(Double.valueOf((precio * mProducto.IVA) / 100) * cantidad));
                            total = (precio+(precio*mProducto.IVA)/100) * cantidad;
                            //iva = (precio*mProducto.IVA)/100;
                            fillMaps.get(indiceEditar).put("cCantidad", String.valueOf(cantidad));
                            fillMaps.get(indiceEditar).put("cTotal", String.valueOf(formateador.format(total)));
                   //         PedidoFragmentDetalle.adapter.notifyDataSetChanged();
                            //ivaTotal = ivaTotal + iva;
                            //subTotal = subTotal + total;
                            //totalPagar = totalPagar +total;
                            Toast.makeText(getActivity(),"Producto editado",Toast.LENGTH_SHORT).show();

                            //POR VER Y PREGUNTAR*************************
                            Intent intent = new Intent();
                            //intent.setClass(getActivity(), PedidoFragment.class);
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finish();
                            //startActivity(intent);
                            //****************************************



                        }catch (Exception e){
                            Toast.makeText(getActivity(), "error "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }else
                        Toast.makeText(getActivity(),"Debe ingresar una cantidad mayor a cero",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), "Ingrese una cantidad", Toast.LENGTH_LONG).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void CalcularTotales1(){
        if(fillMaps.size()>0){
            granSubTotal = 0.0; granIvaTotal = 0.0; granDescuento =0.0; granTotalPagar=0.0;
            double precio =0.0, iva =0.0;
            int cantidad=0;
            for (int i= 0; i < ProductoAgregarFragment.fillMaps.size(); i++) {
                Object o = fillMaps.get(i);
                HashMap<?, ?> fullObject = (HashMap<?, ?>) o;
                precio = Double.valueOf((String)fullObject.get("cPrecio"));
                iva = Double.valueOf((String)fullObject.get("cIva"));
                cantidad = Integer.valueOf((String)fullObject.get("cCantidad"));
                granSubTotal = precio*cantidad;
                granIvaTotal = granIvaTotal + iva * cantidad;
            }
        }
        granTotalPagar = granSubTotal+granIvaTotal-granDescuento;
        subTotal = granSubTotal;
        ivaTotal = granIvaTotal;
        totalPagar = granSubTotal+granIvaTotal-granDescuento;
    }


}
