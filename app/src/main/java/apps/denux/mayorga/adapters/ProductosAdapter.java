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
import apps.denux.mayorga.objetos.Producto;

/**
 * Created by dexter on 13/03/15.
 */
public class ProductosAdapter extends ArrayAdapter<Producto> {

    protected Activity activity;
    protected ArrayList<Producto> productosArrayList;

    NumberFormat formateador = new DecimalFormat("#");

    /**
     * Inicializa un Adaptador para la lista de Productos
     * @param activity
     * @param productosArrayList
     */
    public ProductosAdapter(Activity activity, ArrayList<Producto> productosArrayList) {
        super(activity,R.layout.producto_listrow,productosArrayList);
        this.activity = activity;
        this.productosArrayList = productosArrayList;
        formateador.setMinimumIntegerDigits(1);
        formateador.setMaximumFractionDigits(4);
    }

    /**
     * Vista temporal para reutilizar la vista una vez que fue inicializada
     */
    static class ViewHolder{
        protected TextView tvExistencia;
        protected TextView tvNombre;
        protected TextView tvPrecio1;
        protected TextView tvPrecio2;
        protected TextView tvPrecio3;
        protected TextView tvPrecio4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Producto producto = productosArrayList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.producto_listrow, parent, false);
            viewHolder.tvExistencia =(TextView)convertView.findViewById(R.id.tvpExistencia);
            viewHolder.tvNombre =(TextView)convertView.findViewById(R.id.tvpNombre);
            viewHolder.tvPrecio1 =(TextView)convertView.findViewById(R.id.tvpPrecio1);
            viewHolder.tvPrecio2 =(TextView)convertView.findViewById(R.id.tvpPrecio2);
            viewHolder.tvPrecio3 =(TextView)convertView.findViewById(R.id.tvpPrecio3);
            viewHolder.tvPrecio4 =(TextView)convertView.findViewById(R.id.tvpPrecio4);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvExistencia.setText(String.valueOf(producto.EXISTENCIA));
        viewHolder.tvNombre.setText(producto.NOMBRE);
        viewHolder.tvPrecio1.setText(formateador.format(producto.PRECIO1));
        viewHolder.tvPrecio2.setText(formateador.format(producto.PRECIO2));
        viewHolder.tvPrecio3.setText(formateador.format(producto.PRECIO3));
        viewHolder.tvPrecio4.setText(formateador.format(producto.PRECIO4));
        return  convertView;
    }
}
