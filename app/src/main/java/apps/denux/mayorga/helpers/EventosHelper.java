package apps.denux.mayorga.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import apps.denux.mayorga.objetos.SyncItem;

/**
 * Created by dexter on 16/03/15.
 */
public class EventosHelper extends AsyncTask<ArrayList<Evento>,Integer,List<Boolean>> {

    ArrayList<Evento> lista;
    List<Boolean> resultados;

    ProgressDialog dialog;
    private Activity context;

    RESTHelper restHelper = new RESTHelper();

    public EventosHelper(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(this.context);
        dialog.setTitle("Sincronizando (push)");
        dialog.setMessage("Espere por favor, intetando conectar con el servidor...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(false);
        dialog.setMax(100);
        dialog.setIcon(R.drawable.ic_sync);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values[0]);
        if (resultados != null) {
            dialog.setMessage("("+(resultados.size() + 1) + "/" + lista.size()+ ") Enviando");
        }
    }

    @Override
    protected List<Boolean> doInBackground(ArrayList<Evento>... events) {
        ArrayList<Evento> eventos = new ArrayList<Evento>();
        lista = events[0];
        resultados = new ArrayList<Boolean>();
        for (int i = 0; i < lista.size(); i++) {
            Evento evento = lista.get(i);
            resultados.add(sendData(evento));
        }
        return resultados;
    }

    private Boolean sendData(Evento event) {
        boolean resultado = false;
        EventosDB eventosDB = new EventosDB(this.context);
        Log.i("EVENTO", "" + event.CODIGO_OBJETO + "/" + event.OBJETO + "/" + event.OPERACION);
        switch (event.OBJETO) {
            case "Cliente":
                Cliente cliente =getCliente(event.CODIGO_OBJETO);
                try {
                    resultado = restHelper.post(Constantes.URL_POST_CLIENTE,"",cliente.getJSON());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "Pedido":
                Pedido pedido = getPedido(event.CODIGO_OBJETO);
                try {
                    resultado = restHelper.post(Constantes.URL_POST_PEDIDO,"",pedido.getJSON());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        Log.i("NOTI:cliente", "evento borrado"+resultado );
        if(resultado){
            try {
                eventosDB.open();
                boolean res= eventosDB.delete(event.CODIGO);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                eventosDB.close();
            }
        }
        return resultado;
    }

    private Cliente getCliente(String codigo) {
        Cliente cliente= new Cliente();
        ClientesDB clientesDB = new ClientesDB(this.context);
        try {
            clientesDB.open();
            cliente =clientesDB.get(codigo);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            clientesDB.close();
        }
        return cliente;
    }

    private Pedido getPedido(String codigo) {
        Pedido pedido = new Pedido();

        PedidoDB pedidoDB = new PedidoDB(this.context);
        PedidoItemDB pedidoItemDB = new PedidoItemDB(this.context);
        try {
            pedidoDB.open();
            pedidoItemDB.open();
            pedido = pedidoDB.get(codigo);
            Cursor cursor = pedidoItemDB.get(Integer.valueOf(codigo));
            PedidoItem pedidoItem = new PedidoItem();
            //pedidoItemDB.get(event.CODIGO_OBJETO);
            //Log.i("PEDIDOREST:post","R=>"+result);
            // Log.i("PEDIDO REST:post", "R=> " + pedido.getJSON());
            while (cursor.moveToNext()){
                Log.i("PEDIDO_ITEM REST:post", "R=> " + pedidoItem.getJSON2(cursor));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            pedidoDB.close();
            pedidoItemDB.close();
        }
        return pedido;
    }

    @Override
    protected void onPostExecute(List<Boolean> booleans) {
        dialog.dismiss();
    }
}
