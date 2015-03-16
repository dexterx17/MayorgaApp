package apps.denux.mayorga.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import apps.denux.mayorga.R;
import apps.denux.mayorga.objetos.Evento;

/**
 * Created by dexter on 15/03/15.
 */
public class EventosAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Evento> eventosList;

    public EventosAdapter(Activity activity, ArrayList<Evento> eventosList) {
        this.activity = activity;
        this.eventosList = eventosList;
    }

    @Override
    public int getCount() {
        return eventosList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eventosList.get(position).CODIGO;
    }

    /**
     * Vista temporal para reutilizar la vista una vez que fue inicializada
     */
    static class ViewHolder{
        protected TextView tvOperacion;
        protected TextView tvObjeto;
        protected TextView tvFecha;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Evento evento = eventosList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder= new ViewHolder();
            convertView = activity.getLayoutInflater().inflate(R.layout.evento_listrow, parent, false);
            viewHolder.tvFecha = (TextView) convertView.findViewById(R.id.tvEfecha);
            viewHolder.tvObjeto = (TextView) convertView.findViewById(R.id.tvEobjeto);
            viewHolder.tvOperacion = (TextView) convertView.findViewById(R.id.tvEoperacion);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvOperacion.setText(evento.OPERACION);
        viewHolder.tvObjeto.setText(evento.OBJETO);
        viewHolder.tvFecha.setText(evento.ACTUALIZACION.toString());
        return convertView;
    }
}
