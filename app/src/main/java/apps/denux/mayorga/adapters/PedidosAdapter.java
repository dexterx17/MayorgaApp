package apps.denux.mayorga.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import apps.denux.mayorga.R;
import apps.denux.mayorga.objetos.Pedido;

/**
 * Created by dexter on 13/03/15.
 */
public class PedidosAdapter extends ArrayAdapter<Pedido> {

    protected Activity activity;
    protected ArrayList<Pedido> pedidosArrayList;
    NumberFormat formateador = new DecimalFormat("#");

    public PedidosAdapter(Activity activity, ArrayList<Pedido>  pedidosArrayList) {
        super(activity,R.layout.producto_listrow,pedidosArrayList);
        this.activity = activity;
        this.pedidosArrayList = pedidosArrayList;
        formateador.setMinimumIntegerDigits(1);
        formateador.setMaximumFractionDigits(2);
    }
    static class ViewHolder{
        protected TextView tvPedidoEmpresa, tvPedidoRucci, tvPedidoTotal, tvPedidoCodigo, tvPedidoFecha;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pedido pedido = pedidosArrayList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.pedido_listrow, parent, false);
            viewHolder.tvPedidoEmpresa =(TextView)convertView.findViewById(R.id.tvEmpresa);
            viewHolder.tvPedidoRucci =(TextView)convertView.findViewById(R.id.tvRucci);
            viewHolder.tvPedidoTotal =(TextView)convertView.findViewById(R.id.tvTotal);
            viewHolder.tvPedidoCodigo =(TextView)convertView.findViewById(R.id.tvCodPedido);
            viewHolder.tvPedidoFecha =(TextView)convertView.findViewById(R.id.tvFecha);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvPedidoEmpresa.setText(String.valueOf(pedido.Empresa));
        viewHolder.tvPedidoRucci.setText(String.valueOf(pedido.RUCCI));
        viewHolder.tvPedidoTotal.setText(formateador.format(pedido.Total)+" $");
        viewHolder.tvPedidoCodigo.setText(String.valueOf(pedido.COD_PEDIDO));
        viewHolder.tvPedidoFecha.setText(String.valueOf(pedido.FECHA));
        return  convertView;
    }
}
