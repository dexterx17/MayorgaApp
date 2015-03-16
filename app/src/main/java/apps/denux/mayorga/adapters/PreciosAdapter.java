package apps.denux.mayorga.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import apps.denux.mayorga.R;
import apps.denux.mayorga.objetos.TipoCliente;

/**
 * Created by dexter on 15/03/15.
 */
public class PreciosAdapter extends BaseAdapter {

    protected ArrayList<Double> list;
    protected Activity context;

    NumberFormat formateador = new DecimalFormat("#");

    public PreciosAdapter(ArrayList<Double> list, Activity context) {
        this.list = list;
        this.context = context;
        formateador.setMaximumFractionDigits(4);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
    public int getPosition(double codigo){
        for (int i = 0; i < list.size(); i++) {
            Double precio = list.get(i);
            if(precio==codigo)
                return i;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Double precio = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.tipo_listrow, parent, false);
            viewHolder.tvTipo =(TextView)convertView.findViewById(R.id.tvtTipo);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTipo.setText(formateador.format(precio));

        return  convertView;
    }
}
