package apps.denux.mayorga.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import apps.denux.mayorga.R;
import apps.denux.mayorga.objetos.Cliente;

/**
 * Created by dexter on 13/03/15.
 */
public class ClientesAdapter  extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Cliente> clienteArrayList;

    /**
     * Inicializa un Adaptador para la lista de clientes
     * @param activity
     * @param clienteArrayList
     */
    public ClientesAdapter(Activity activity, ArrayList<Cliente> clienteArrayList) {
        this.activity = activity;
        this.clienteArrayList = clienteArrayList;
    }

    @Override
    public int getCount() {
        return clienteArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return clienteArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return clienteArrayList.get(position).CODIGO;
    }

    /**
     * Vista temporal para reutilizar la vista una vez que fue inicializada
     */
    static class ViewHolder{
        protected TextView tvEmpresa;
        protected TextView tvDireccion;
    }

    /**
     * Devuelve una fila de la lista clientes (cliente_listrow.xml)
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cliente cliente = clienteArrayList.get(position);
        ViewHolder viewHolder;
        if(convertView== null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView= inflater.inflate(R.layout.cliente_listrow,parent,false);
            viewHolder.tvEmpresa=(TextView)convertView.findViewById(R.id.tvcEmpresa);
            viewHolder.tvDireccion=(TextView)convertView.findViewById(R.id.tvcDireccion);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tvEmpresa.setText(cliente.EMPRESA);
        viewHolder.tvDireccion.setText(cliente.DIRECCION);

        return convertView;
    }
}