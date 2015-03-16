package apps.denux.mayorga.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import apps.denux.mayorga.R;
import apps.denux.mayorga.modelos.ClientesDB;
import apps.denux.mayorga.modelos.ProductosDB;
import apps.denux.mayorga.modelos.TipoClienteDB;
import apps.denux.mayorga.modelos.TipoIdentificacionDB;
import apps.denux.mayorga.modelos.VendedorDB;
import apps.denux.mayorga.objetos.Cliente;
import apps.denux.mayorga.objetos.Objeto;
import apps.denux.mayorga.objetos.Producto;
import apps.denux.mayorga.objetos.SyncItem;
import apps.denux.mayorga.objetos.TipoCliente;
import apps.denux.mayorga.objetos.TipoIdentificacion;
import apps.denux.mayorga.objetos.Vendedor;

/**
 * Created by dexter on 14/03/15.
 */
public class SyncHelper extends AsyncTask<ArrayList<SyncItem>,Integer,List<JSONArray>> {

    ArrayList<SyncItem> itemsillos;

    List<JSONArray> listDatos;
    int nURLS;
    /**
     * Barra de estado para informar que se esta realizando una descarga
     */
    ProgressDialog dialog;
    private Activity context;

    RESTHelper restHelper = new RESTHelper();

    public SyncHelper(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(this.context);
        dialog.setTitle("Sincronizando");
        dialog.setMessage("Espere por favor, intetando conectar con el servidor...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(false);
        dialog.setMax(100);
        dialog.setIcon(R.drawable.ic_sync);
        dialog.setCancelable(true);
      //  dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancelar",(DialogInterface.OnClickListener)null);
        dialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        dialog.setProgress(values[0]);
        if (listDatos != null) {
                dialog.setMessage("("+(listDatos.size() + 1) + "/" + nURLS+ ") Descargando");
        }
    }
    @Override
    protected List<JSONArray> doInBackground(ArrayList<SyncItem>... urls) {
        ArrayList<SyncItem> items= new ArrayList<SyncItem>();
        itemsillos=urls[0];
        items=urls[0];
        dialog.setMessage("Espere por favor, descargando actualizaciones...");
        nURLS=items.size();
        listDatos = new ArrayList<JSONArray>();

        for (int i = 0; i < items.size(); i++) {
            SyncItem item = items.get(i);
            listDatos.add(downloadInfo(item));
        }
        return listDatos;
    }

    private JSONArray downloadInfo(SyncItem item) {

        JSONArray jsonArray= null;
        try {
            publishProgress(0);
            long fecha = getLastUpdate(item.Objeto);
            publishProgress(5);
            jsonArray = restHelper.get(item.Url,"/last_update/"+fecha);
            publishProgress(50);
            if(jsonArray!=null)
                loadData(item.Objeto,jsonArray);
            publishProgress(100);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    private long getLastUpdate(Objeto objeto) {
        long resultado=0;

        if (objeto instanceof Cliente) {
            ClientesDB clientesDB = new ClientesDB(context);
            try {
                clientesDB.open();
                resultado = clientesDB.getLastUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                clientesDB.close();
            }
        }
        if (objeto instanceof Vendedor) {
            VendedorDB vendedorDB = new VendedorDB(context);
            try {
                vendedorDB.open();
                resultado = vendedorDB.getLastUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                vendedorDB.close();
            }
        }

        if (objeto instanceof TipoCliente) {
            TipoClienteDB tipoCDB = new TipoClienteDB(context);
            try {
                tipoCDB.open();
                resultado = tipoCDB.getLastUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                tipoCDB.close();
            }
        }

        if (objeto instanceof TipoIdentificacion) {
            TipoIdentificacionDB tipoIDB = new TipoIdentificacionDB(context);
            try {
                tipoIDB.open();
                resultado = tipoIDB.getLastUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                tipoIDB.close();
            }
        }

        if (objeto instanceof Vendedor) {
            VendedorDB vendedorDB = new VendedorDB(context);
            try {
                vendedorDB.open();
                resultado = vendedorDB.getLastUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                vendedorDB.close();
            }
        }

        if (objeto instanceof Producto) {
            ProductosDB productoDB = new ProductosDB(context);
            try {
                productoDB.open();
                resultado = productoDB.getLastUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                productoDB.close();
            }
        }

        return resultado;
    }
    @Override
    protected void onPostExecute(List<JSONArray> datos) {
        dialog.dismiss();
    }

    private void loadData(Objeto objeto,JSONArray array){

        if (objeto instanceof Cliente) {
            ClientesDB clientesDB = new ClientesDB(context);
            try {
                ArrayList<Cliente> clientes=  Cliente.fromJson(array);
                clientesDB.open();
                clientesDB.load(clientes);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                clientesDB.close();
            }
        }
        if (objeto instanceof Vendedor) {
            VendedorDB vendedorDB = new VendedorDB(context);
            try {
                ArrayList<Vendedor> vendedores=  Vendedor.fromJson(array);
                vendedorDB.open();
                vendedorDB.load(vendedores);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                vendedorDB.close();
            }
        }

        if (objeto instanceof TipoCliente) {
            TipoClienteDB tipoCDB = new TipoClienteDB(context);
            try {
                ArrayList<TipoCliente> tiposCliente=  TipoCliente.fromJson(array);
                tipoCDB.open();
                tipoCDB.load(tiposCliente);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                tipoCDB.close();
            }
        }

        if (objeto instanceof TipoIdentificacion) {
            TipoIdentificacionDB tipoIDB = new TipoIdentificacionDB(context);
            try {
                ArrayList<TipoIdentificacion> tiposId=  TipoIdentificacion.fromJson(array);
                tipoIDB.open();
                tipoIDB.load(tiposId);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                tipoIDB.close();
            }
        }

        if (objeto instanceof Vendedor) {
            VendedorDB vendedorDB = new VendedorDB(context);
            try {
                ArrayList<Vendedor> vendedores=  Vendedor.fromJson(array);
                vendedorDB.open();
                vendedorDB.load(vendedores);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                vendedorDB.close();
            }
        }

        if (objeto instanceof Producto) {
            ProductosDB productoDB = new ProductosDB(context);
            try {
                ArrayList<Producto> productos=  Producto.fromJson(array);
                productoDB.open();
                productoDB.load(productos);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                productoDB.close();
            }
        }
    }
}
