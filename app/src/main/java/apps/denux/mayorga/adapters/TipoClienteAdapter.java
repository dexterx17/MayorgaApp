package apps.denux.mayorga.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import apps.denux.mayorga.R;
import apps.denux.mayorga.objetos.TipoCliente;

/**
 * Created by dexter on 13/03/15.
 */
public class TipoClienteAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<TipoCliente> lista;

    public TipoClienteAdapter(Activity activity, ArrayList<TipoCliente> lista) {
        this.activity = activity;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }


    @Override
    public long getItemId(int position) {
        return lista.get(position).CODIGO;
    }

    /**
     * Vista temporal para reutilizar la vista una vez que fue inicializada
     */
    static class ViewHolder{
        protected TextView tvTipo;
    }

    /**
     * Recorre la lista hasta encontrar una coincidencia con parametro pasado y retorna su posicion
     * @param codigo
     * @return
     */
    public int getPosition(int codigo){
        for (int i = 0; i < lista.size(); i++) {
            TipoCliente tipo = lista.get(i);
            if(tipo.CODIGO==codigo)
                return i;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TipoCliente tipo = lista.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.tipo_listrow, parent, false);
            viewHolder.tvTipo =(TextView)convertView.findViewById(R.id.tvtTipo);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTipo.setText(tipo.DESCRIPCION);

        return  convertView;
    }
}
