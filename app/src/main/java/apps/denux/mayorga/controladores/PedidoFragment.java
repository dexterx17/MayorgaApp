package apps.denux.mayorga.controladores;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import apps.denux.mayorga.Constantes;
import apps.denux.mayorga.R;
import apps.denux.mayorga.modelos.ClientesDB;
import apps.denux.mayorga.modelos.EventosDB;
import apps.denux.mayorga.modelos.PedidoDB;
import apps.denux.mayorga.modelos.PedidoItemDB;
import apps.denux.mayorga.objetos.Cliente;
import apps.denux.mayorga.objetos.Evento;
import apps.denux.mayorga.objetos.Pedido;
import apps.denux.mayorga.objetos.PedidoItem;

/**
 * Created by dexter on 15/03/15.
 */
public class PedidoFragment extends ActionBarActivity {
    private EditText etRuc, etEmpresa, etTotal, etFecha, etSubTotal, etIva, etDescuento, etObservacion;
    private ListView lvProductosDetalle;
    private int codigoPedido, borrar =12;
    private  Double granSubTotal, granIvaTotal, granDescuento, granTotalPagar;
    private  String empresaPedido, rucciPedido, fechaPedido;
    DecimalFormat formateador = new DecimalFormat("###,###.##");
    public SimpleAdapter adapter;
    public boolean verPedido = false, editPedido=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido_fragment);

        getSupportActionBar().setTitle("Pedidos");
        getSupportActionBar().setTitle("Pedido");

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        etRuc = (EditText)this.findViewById(R.id.etRuc);
        etEmpresa = (EditText)this.findViewById(R.id.etEmpresa);
        etTotal = (EditText)this.findViewById(R.id.etTotal);
        etFecha = (EditText)this.findViewById(R.id.etFecha);
        etFecha.setText(dateFormat.format(date));
        etSubTotal = (EditText)this.findViewById(R.id.etSTotal);
        etDescuento = (EditText)this.findViewById(R.id.etDescuento);
        etObservacion = (EditText)this.findViewById(R.id.etObservacion);
        lvProductosDetalle = (ListView)this.findViewById(R.id.listPedidos);
        etIva =(EditText)this.findViewById(R.id.etIva);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null && bundle.containsKey("PEDIDO_RUCII") && bundle.containsKey("PED_CODIGO")){
            verPedido = true;
            rucciPedido = bundle.getString("PEDIDO_RUCII");
            codigoPedido = bundle.getInt("PED_CODIGO");
            empresaPedido = bundle.getString("PEDIDO_EMPRESA");
            fechaPedido = bundle.getString("PEDIDO_FECHA");
            etFecha.setText(fechaPedido);
            etRuc.setText(rucciPedido);
            etEmpresa.setText(empresaPedido);
            //Obtener datos del detalle PedidoItem
            PedidoItemDB pedidoItemDB = new PedidoItemDB(this);
            PedidoItem pedidoItem = new PedidoItem();
            ProductoAgregarFragment.fillMaps.clear();
            double sb=0, iva=0, total=0;
            try {
                pedidoItemDB.open();
                Cursor cursor = pedidoItemDB.getCursor(codigoPedido);
                while (cursor.moveToNext()){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("cCod", cursor.getString(0));
                    map.put("cProducto", cursor.getString(1));
                    map.put("cPrecio", cursor.getString(2));
                    map.put("cIva", cursor.getString(3));
                    map.put("cCantidad", cursor.getString(4));
                    sb = sb + (Double.valueOf(cursor.getString(2))*Double.valueOf(cursor.getString(4)));
                    iva = iva + Double.valueOf(cursor.getString(3));
                    total = (Double.valueOf(cursor.getString(2)) * Double.valueOf(cursor.getString(4)))+ Double.valueOf(cursor.getString(3));
                    map.put("cTotal", String.valueOf(formateador.format(total)));
                    ProductoAgregarFragment.fillMaps.add(map);
                }

                Bundle args = new Bundle();
                args.putBoolean("EDITAR_PEDIDO", true);
                PedidoFragmentDetalle pedidoFragmentDetalle = new PedidoFragmentDetalle();
                pedidoFragmentDetalle.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content_detalle, pedidoFragmentDetalle).commit();

                ProductoAgregarFragment.subTotal = sb;
                ProductoAgregarFragment.ivaTotal = iva;
                ProductoAgregarFragment.totalPagar = sb+iva;
                pedidoItemDB.close();
                //adapter = new SimpleAdapter(getApplicationContext(), ProductoAgregarFragment.fillMaps, R.layout.pedido_detalle_columnas_productos, PedidoFragmentDetalle.fromColumns, PedidoFragmentDetalle.toVistas);
                //PedidoFragmentDetalle.adapter = adapter;
                //Toast.makeText(this, " "+pedidoItem.COD_PRODUCTO+" Cantidad "+pedidoItem.CANTIDAD,Toast.LENGTH_LONG).show();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        //PedidoFragmentDetalle pedidoFragmentDetalle = new PedidoFragmentDetalle();
        //getSupportFragmentManager().beginTransaction().replace(R.id.frame_content_detalle, pedidoFragmentDetalle).commit();

        etIva.setText(formateador.format(ProductoAgregarFragment.ivaTotal));
        etSubTotal.setText(formateador.format(ProductoAgregarFragment.subTotal));
        etTotal.setText(formateador.format(ProductoAgregarFragment.totalPagar));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("onCreateOptionsMenu editPedido = " + editPedido, " y verpedido = " + verPedido);
        getMenuInflater().inflate(R.menu.menu_pedido, menu);
        if(verPedido && menu!=null){
            menu.findItem(R.id.menu_pedido_list_productos).setVisible(false);
            menu.findItem(R.id.menu_pedido_list_user).setVisible(false);
            menu.findItem(R.id.menu_guardar_pedido).setVisible(false);
            menu.findItem(R.id.menu_cancelar_pedido).setVisible(false);
            menu.findItem(R.id.menu_pedido_editar_pedido).setVisible(true);
        }else
        {
            menu.findItem(R.id.menu_pedido_editar_pedido).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.clear();
        Log.i("onPrepareOptionsMenu "," editPedido = "+editPedido);
        if(editPedido) {
            //Log.i("onPrepareOptionsMenu "," editPedido = "+editPedido);
            menu.findItem(R.id.menu_pedido_list_productos).setVisible(true);
            menu.findItem(R.id.menu_pedido_list_user).setVisible(false);
            menu.findItem(R.id.menu_guardar_pedido).setVisible(true);
            menu.findItem(R.id.menu_cancelar_pedido).setVisible(true);
            menu.findItem(R.id.menu_pedido_editar_pedido).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_pedido_list_user:
                Intent intent = new Intent(getApplicationContext(), Clientes.class);
                intent.putExtra("SELECCION", true);
                startActivityForResult(intent, 1);

                return true;
            case R.id.menu_pedido_list_productos:
                Intent intent1 = new Intent(getApplicationContext(), Productos.class);
                intent1.putExtra("SELECT_PRODUCTO", true);
                intent1.putExtra("ORIGEN", "PEDIDO");
                startActivityForResult(intent1, 2);
                return true;
            case R.id.menu_guardar_pedido:
                if(editPedido){
                    if(!Editar()){
                        Toast.makeText(getApplicationContext(), "El Pedido no se pudo editar!", Toast.LENGTH_LONG).show();
                    }else {
                        editPedido = false; verPedido = false;
                        invalidateOptionsMenu();
                    }
                }else {
                    if(!Guardar()){
                        Toast.makeText(getApplicationContext(), "El Pedido no se pudo guardar!",Toast.LENGTH_LONG).show();
                    }
                }
                return  true;

            case R.id.menu_pedido_editar_pedido:
                editPedido=true;
                invalidateOptionsMenu();
                Bundle args = new Bundle();
                args.putBoolean("EDITAR_PEDIDO", false);
                PedidoFragmentDetalle pedidoFragmentDetalle = new PedidoFragmentDetalle();
                pedidoFragmentDetalle.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content_detalle, pedidoFragmentDetalle).commit();

                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    if(bundle != null && bundle.containsKey(ClientesFragment.ARG_ITEM_ID)) {
                        String varRuc = bundle.getString(ClientesFragment.ARG_ITEM_ID);
                        ClientesDB clientesDB = new ClientesDB(this);
                        Cliente cliente = new Cliente();
                        try {
                            clientesDB.open();
                            cliente = clientesDB.get(varRuc);
                            clientesDB.close();
                            if(cliente != null){
                                etRuc.setText(cliente.RUCCI.toString());
                                etEmpresa.setText(cliente.EMPRESA.toString());
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    PedidoFragmentDetalle pedidoFragmentDetalle = new PedidoFragmentDetalle();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_content_detalle, pedidoFragmentDetalle).commit();
                    DecimalFormat formateador = new DecimalFormat("###,###.##");
                    CalcularTotales();
                    etIva.setText(formateador.format(ProductoAgregarFragment.ivaTotal));
                    etSubTotal.setText(formateador.format(ProductoAgregarFragment.subTotal));
                    etTotal.setText(formateador.format(ProductoAgregarFragment.totalPagar));
                }
                break;

            case 3:
                Log.i("Regeso resultado del 3 "," editar producto de la lista");
                CalcularTotales();
                etIva.setText(formateador.format(ProductoAgregarFragment.ivaTotal));
                etSubTotal.setText(formateador.format(ProductoAgregarFragment.subTotal));
                etTotal.setText(formateador.format(ProductoAgregarFragment.totalPagar));
                break;
        }

    }

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
                ProductoAgregarFragment.fillMaps.remove(info.position);
                PedidoFragmentDetalle.adapter.notifyDataSetChanged();
                //ProductoAgregarFragment.totalPagar =0;
                //ProductoAgregarFragment.ivaTotal =0;
                /*for(int i= 0; i < ProductoAgregarFragment.fillMaps.size(); i++){
                    ProductoAgregarFragment.totalPagar = ProductoAgregarFragment.totalPagar + Double.valueOf(ProductoAgregarFragment.fillMaps.get(i).get("cTotal"));
                    ProductoAgregarFragment.ivaTotal = ProductoAgregarFragment.ivaTotal + Double.valueOf(ProductoAgregarFragment.fillMaps.get(i).get("cIva"));
                    //ProductoAgregarFragment.subTotal = ProductoAgregarFragment.subTotal + Double.valueOf(ProductoAgregarFragment.fillMaps.get(i))
                }*/
                CalcularTotales();
                etIva.setText(formateador.format(ProductoAgregarFragment.ivaTotal));
                etSubTotal.setText(formateador.format(ProductoAgregarFragment.subTotal));
                etTotal.setText(formateador.format(ProductoAgregarFragment.totalPagar));
                break;
            case "Editar":
                //1. Llevar el origen
                //2. Llevar el id del producto
                //3. Mostrar el fragment AgregarProducto
                //4. Mostrar el menu de actualizar
                //5. Regresar a PedidoFragment

                //PedidoFragmentDetalle pedidoFragmentDetalle = new PedidoFragmentDetalle();
                //getSupportFragmentManager().beginTransaction().replace(R.id.frame_content_detalle, pedidoFragmentDetalle).commit();
                //Toast.makeText(getApplicationContext(), "Se actualizo FD",Toast.LENGTH_LONG).show();


                Intent intent1 = new Intent(getApplicationContext(), Productos.class);
                intent1.putExtra("EDITAR_PRODUCTO",true);
                //args.putString(ProductoAgregarFragment.ARG_ITEM_ORIGEN, "PEDIDO");
                intent1.putExtra("ORIGEN","EDITAR");
                intent1.putExtra("INDICE",info.position);
                startActivityForResult(intent1, 3);
                return true;

            //break;
                /*
                Bundle args = new Bundle();
                args.putInt("POSICION", info.position);
                args.putString(ProductoEditarFragment.ARG_ITEM_ORIGEN, "EDITAR");
                ProductoEditarFragment productoEditarFragment = new ProductoEditarFragment();
                //Pasando parametros al fragment
                productoEditarFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_content_detalle, productoEditarFragment).commit();
                */
        }
        //Toast.makeText(getActivity(), "La opcion es "+opcion,  Toast.LENGTH_LONG).show();
        return super.onContextItemSelected(item);

    }

    public void onBackPressed() {
        //inicializarVriablesPedido();
        super.onBackPressed();
    }

    public void inicializarVriablesPedido(){
        ProductoAgregarFragment.totalPagar = 0;
        ProductoAgregarFragment.ivaTotal = 0;
        ProductoAgregarFragment.fillMaps.clear();
        PedidoFragmentDetalle.adapter.notifyDataSetChanged();
        etSubTotal.setText(String.valueOf(ProductoAgregarFragment.totalPagar));
        etIva.setText(formateador.format(ProductoAgregarFragment.ivaTotal));
        etRuc.setText("");
        etEmpresa.setText("");
        etTotal.setText("");;
    }

    public void CalcularTotales(){
        if(ProductoAgregarFragment.fillMaps.size()>0){
            granSubTotal = 0.0; granIvaTotal = 0.0; granDescuento =0.0; granTotalPagar=0.0;
            double precio =0.0, iva =0.0;
            int cantidad=0;
            for (int i= 0; i < ProductoAgregarFragment.fillMaps.size(); i++) {
                Object o = ProductoAgregarFragment.fillMaps.get(i);
                HashMap<?, ?> fullObject = (HashMap<?, ?>) o;
                precio = Double.valueOf((String)fullObject.get("cPrecio"));
                iva = Double.valueOf((String)fullObject.get("cIva"));
                cantidad = Integer.valueOf((String)fullObject.get("cCantidad"));
                granSubTotal = granSubTotal +(precio*cantidad);
                granIvaTotal = granIvaTotal + iva;

            }
            granTotalPagar = granSubTotal+granIvaTotal-granDescuento;
            ProductoAgregarFragment.subTotal = granSubTotal;
            ProductoAgregarFragment.ivaTotal = granIvaTotal;
            ProductoAgregarFragment.totalPagar = granSubTotal+granIvaTotal-granDescuento;
        }else
        {
            granTotalPagar = 0.0;
            ProductoAgregarFragment.subTotal = 0.0;
            ProductoAgregarFragment.ivaTotal = 0.0;
            ProductoAgregarFragment.totalPagar = 0.0;
        }
    }

    public boolean Guardar(){
        boolean guardado = false, res = false;
        EventosDB eventosDB = new EventosDB(this);
        Evento evento = new Evento();
        if(ProductoAgregarFragment.fillMaps.size()>0 && etRuc.getText().length()>0) {
            int id = 0;
            PedidoDB pedidoDB = new PedidoDB(this);
            try {
                pedidoDB.open();
                id = pedidoDB.getId();
                pedidoDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            long insertadoMaestro = 0;
            Pedido pedido = new Pedido();
            //OBTENER CÓDIGO DE MAESTRO_PEDIDO
            pedido.DISPOSITIVO = Constantes.USER_MAC;
            pedido.COD_PEDIDO = id;
            pedido.FECHA = Timestamp.valueOf(String.valueOf(etFecha.getText()));
            pedido.RUCCI = String.valueOf(etRuc.getText());
            pedido.VENDEDOR = Constantes.VENDEDOR.CEDULA;//Constantes.VENDEDOR.CEDULA;
            pedido.RUTA = "LA RUTA"; //
            pedido.LONG = 4;
            pedido.LAT = 4;
            pedido.LOCK = "N";
            pedido.F_UPDATE = Timestamp.valueOf(String.valueOf(etFecha.getText()));
            //Inicializo el vento par la sincronización
            evento.OBJETO="Pedido";
            evento.ACTUALIZACION=new Timestamp(new Date().getTime());
            try {
                pedidoDB.open();
                insertadoMaestro = pedidoDB.insert(pedido);
                evento.OPERACION="INSERT";
                evento.CODIGO_OBJETO = String.valueOf(pedido.COD_PEDIDO);
                pedidoDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (insertadoMaestro > 0) {
                //Para insertar en PedidoItem
                PedidoItemDB pedidoItemDB = new PedidoItemDB(this);
                PedidoItem pedidoItem = new PedidoItem();
                //Barrido de la lista para extraer los datos y guardar en la BDD
                try {
                    for (int i = 0; i < ProductoAgregarFragment.fillMaps.size(); i++) {
                        pedidoItemDB.open();
                        pedidoItem.COD_DISPOSITIVO = Constantes.USER_MAC;
                        pedidoItem.COD_PEDIDO = id;
                        Object o = ProductoAgregarFragment.fillMaps.get(i);
                        HashMap<?, ?> fullObject = (HashMap<?, ?>) o;
                        pedidoItem.COD_PRODUCTO = (String) fullObject.get("cCod");
                        pedidoItem.CANTIDAD = Double.valueOf((String) fullObject.get("cCantidad"));
                        pedidoItem.PRECIO = Double.valueOf((String) fullObject.get("cPrecio"));
                        pedidoItem.DESCUENTO = 0.0;
                        pedidoItem.IVA = Double.valueOf((String) fullObject.get("cIva"));
                        pedidoItem.F_UPDATE = Timestamp.valueOf(String.valueOf(etFecha.getText()));
                        if (pedidoItemDB.insert(pedidoItem) > 0)
                            guardado = true;
                        pedidoItemDB.close();
                    }
                    inicializarVriablesPedido();
                    if(guardado){
                        Toast.makeText(getApplicationContext(), "Pedido se gurado correctamente",Toast.LENGTH_SHORT).show();
                        eventosDB.open();
                        if(eventosDB.insert(evento))
                            Toast.makeText(this, "Pedido registrado para sincronizar", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(this, "Error al registrar el Pedido para sincronizar", Toast.LENGTH_SHORT).show();
                        eventosDB.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return guardado;
    }
    public  boolean Editar(){
        boolean editado = false;
        EventosDB eventosDB = new EventosDB(this);
        Evento evento = new Evento();
        if(ProductoAgregarFragment.fillMaps.size()>0) {
            //Obtener variables del Maestro
            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.COD_DISPOSITIVO = Constantes.USER_MAC;
            pedidoItem.COD_PEDIDO = codigoPedido;
            PedidoItemDB pedidoItemDB = new PedidoItemDB(this);
            long actualizado = 0;
            evento.OBJETO = "Pedido";
            evento.ACTUALIZACION = new Timestamp(new Date().getTime());
            try {
                pedidoItemDB.open();
                if (pedidoItemDB.delete(pedidoItem)) {
                    pedidoItemDB.close();
                    evento.OPERACION = "UPDATE";
                    evento.CODIGO_OBJETO = String.valueOf(pedidoItem.COD_PEDIDO);
                    for (int i = 0; i < ProductoAgregarFragment.fillMaps.size(); i++) {
                        pedidoItemDB.open();
                        //pedidoItem.COD_DISPOSITIVO = Constantes.USER_MAC;
                        pedidoItem.COD_PEDIDO = codigoPedido;
                        Object o = ProductoAgregarFragment.fillMaps.get(i);
                        HashMap<?, ?> fullObject = (HashMap<?, ?>) o;
                        pedidoItem.COD_PRODUCTO = (String) fullObject.get("cCod");
                        pedidoItem.CANTIDAD = Double.valueOf((String) fullObject.get("cCantidad"));
                        pedidoItem.PRECIO = Double.valueOf((String) fullObject.get("cPrecio"));
                        pedidoItem.DESCUENTO = 0.0;
                        pedidoItem.IVA = Double.valueOf((String) fullObject.get("cIva"));
                        pedidoItem.F_UPDATE = Timestamp.valueOf(String.valueOf(etFecha.getText()));
                        if (pedidoItemDB.insert(pedidoItem) > 0)
                            editado = true;
                        pedidoItemDB.close();
                    }
                }
                inicializarVriablesPedido();
                if (editado) {
                    Toast.makeText(getApplicationContext(), "Pedido se edito correctamente", Toast.LENGTH_SHORT).show();
                    eventosDB.open();
                    if (eventosDB.insert(evento))
                        Toast.makeText(this, "Pedido registrado para sincronizar", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Error al registrar el Pedido para sincronizar", Toast.LENGTH_SHORT).show();
                    eventosDB.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return editado;
    }
}




