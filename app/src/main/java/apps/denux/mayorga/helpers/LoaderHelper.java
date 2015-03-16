package apps.denux.mayorga.helpers;

import android.app.Activity;

import org.json.JSONArray;

import java.sql.SQLException;
import java.util.ArrayList;

import apps.denux.mayorga.modelos.ClientesDB;
import apps.denux.mayorga.modelos.ProductosDB;
import apps.denux.mayorga.modelos.TipoClienteDB;
import apps.denux.mayorga.modelos.TipoIdentificacionDB;
import apps.denux.mayorga.modelos.VendedorDB;
import apps.denux.mayorga.objetos.Cliente;
import apps.denux.mayorga.objetos.Objeto;
import apps.denux.mayorga.objetos.Producto;
import apps.denux.mayorga.objetos.TipoCliente;
import apps.denux.mayorga.objetos.TipoIdentificacion;
import apps.denux.mayorga.objetos.Vendedor;

/**
 * Created by dexter on 15/03/15.
 */
public class LoaderHelper {

    private Activity context;

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
