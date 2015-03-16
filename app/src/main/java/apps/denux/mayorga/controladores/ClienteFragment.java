package apps.denux.mayorga.controladores;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import apps.denux.mayorga.Constantes;
import apps.denux.mayorga.R;
import apps.denux.mayorga.Validation;
import apps.denux.mayorga.adapters.TipoClienteAdapter;
import apps.denux.mayorga.adapters.TipoIdAdapter;
import apps.denux.mayorga.modelos.ClientesDB;
import apps.denux.mayorga.modelos.EventosDB;
import apps.denux.mayorga.modelos.TipoClienteDB;
import apps.denux.mayorga.modelos.TipoIdentificacionDB;
import apps.denux.mayorga.objetos.Cliente;
import apps.denux.mayorga.objetos.Evento;
import apps.denux.mayorga.objetos.TipoCliente;
import apps.denux.mayorga.objetos.TipoIdentificacion;

/**
 * Created by dexter on 13/03/15.
 */
public class ClienteFragment extends Fragment{

    public static final String ARG_ITEM_ID = "ID";

    private Cliente mCliente;

    private ClientesDB clientesDB;
    private EventosDB eventosDB;
    private Evento evento;
    TipoClienteAdapter adapterTipoCliente;
    TipoIdAdapter adapterTipoId;

    EditText etRUCCI;
    Spinner spTipo;
    Spinner spIdentificacion;
    EditText etEmpresa;
    EditText etRepresentante;
    EditText etCiudad;
    EditText etDir1;
    EditText etDir2;
    EditText etTel1;
    EditText etTel2;
    EditText etPropietario;
    CheckBox chPublica;
    EditText etCodigo;
    EditText etVendedor;
    EditText etSucursal;
    //Estatico para clave primaria de clientes
    int SUCURSAL = 1;

    private Menu menusillo;
    /**
     * Inicializa los componentes de la vista
     * @param v
     */
    private void init(View v){
        etRUCCI = (EditText)v.findViewById(R.id.etcRUCCI);
        etRUCCI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(etRUCCI);
            }
        });
        spTipo =(Spinner)v.findViewById(R.id.spcTipo);
        spIdentificacion = (Spinner)v.findViewById(R.id.spcIdentificacion);
        etEmpresa =(EditText)v.findViewById(R.id.etcEmpresa);
        etEmpresa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(etEmpresa);
            }
        });
        etRepresentante =(EditText)v.findViewById(R.id.etcRepresentante);
        etCiudad =(EditText)v.findViewById(R.id.etcCiudad);
        etDir1 =(EditText)v.findViewById(R.id.etcDireccion1);
        etDir2 =(EditText)v.findViewById(R.id.etcDireccion2);
        etTel1 =(EditText)v.findViewById(R.id.etcTelfax1);
        etTel2 =(EditText)v.findViewById(R.id.etcTelfax2);
        etPropietario =(EditText)v.findViewById(R.id.etcPropietario);
        chPublica=(CheckBox)v.findViewById(R.id.chbcEmpPublica);
        etCodigo =(EditText)v.findViewById(R.id.etcCodigo);
        etVendedor =(EditText)v.findViewById(R.id.etcVendedor);
        etSucursal =(EditText)v.findViewById(R.id.etcSucursal);
    }

    /**
     * Activa o desactiva los componentes de la vista para edición
     * @param estado
     */
    private void setEnabledComponents(boolean estado){
        etRUCCI.setFocusable(estado);
        etEmpresa.setFocusable(estado);
        spTipo.setEnabled(estado);
        spIdentificacion.setEnabled(estado);
        etRepresentante.setFocusable(estado);
        etCiudad.setFocusable(estado);
        etDir1.setFocusable(estado);
        etDir2.setFocusable(estado);
        etTel1.setFocusable(estado);
        etTel2.setFocusable(estado);
        etPropietario.setFocusable(estado);
        chPublica.setEnabled(estado);
    }

    /**
     * Valida el formulario, conotrolando los campos obligatorios
     * @return true/false
     */
    private boolean checkForm(){
        if(!Validation.hasText(etRUCCI)||!Validation.hasText(etEmpresa))
            return false;
        else
            return true;
    }

    /**
     * Constructor: crea una instancia ClienteFragment
     */
    public ClienteFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cliente_fragment,container,false);

        //Obtengo los parametros para la vista
        Bundle bundle = getArguments();
        //Verifico que existan parametros y exista la constante que necesito
        if(bundle!=null && bundle.containsKey(ARG_ITEM_ID)) {
            clientesDB = new ClientesDB(getActivity());
            try {
                clientesDB.open();
                mCliente = clientesDB.get(bundle.getString(ARG_ITEM_ID));
                clientesDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Log.i("rootView", "Vista inicializada");

        init(rootView);
        loadTiposId();
        loadTiposCliente();

        //Si el cliente existe, seteo los datos en la vista caso contrario obtengo un nuevo codigo y le asigno el vendedor actual
        if(mCliente != null){
            etRUCCI.setText(mCliente.RUCCI);
            spTipo.setSelection(adapterTipoCliente.getPosition(mCliente.TIPO));
            spIdentificacion.setSelection(adapterTipoId.getPosition(mCliente.IDENTIFICACION));
            etEmpresa.setText(mCliente.EMPRESA.toString());
            etRepresentante.setText(mCliente.REPRESENTANTE.toString());
            etCiudad.setText(mCliente.CIUDAD.toString());
            etDir1.setText(mCliente.DIRECCION);
            etDir2.setText(mCliente.DIRECCION2);
            etTel1.setText(mCliente.TELFAX);
            etTel2.setText(mCliente.TELFAX2);
            etPropietario.setText(mCliente.PROPIETARIO);
            chPublica.setChecked(mCliente.EMP_PUBLICA);
            etCodigo.setText(String.valueOf(mCliente.CODIGO));
            etVendedor.setText(String.valueOf(mCliente.VENDEDOR));
            etSucursal.setText(String.valueOf(mCliente.SUCURSAL));
            Log.i("rootView","mCliente NOT NULL");
        }else{
            etCodigo.setText("0");
            etVendedor.setText(Constantes.VENDEDOR.CEDULA);
            etSucursal.setText(String.valueOf(Constantes.SUCURSAL));
        }

        //Verifico que existan parametros y exista la constante que necesito
        if(bundle!=null && bundle.containsKey("EVENTO")) {
            Object i = bundle.get("EVENTO");
            if (i.equals(Constantes.FORM_OPERACIONES.VIEW)) {
                setEnabledComponents(false);
            } else if (i.equals(Constantes.FORM_OPERACIONES.NEW) || (i.equals(Constantes.FORM_OPERACIONES.UPDATE))) {
                setEnabledComponents(true);
            }
        }

        return rootView;
    }

    /**
     * Añade el menu cliente, y alterna la visibilidad de los items guardar y editar
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
        //Escondo el boton de Agregar cliente
        if(menu!=null){
            menu.findItem(R.id.mi_add_cliente).setVisible(false);
        }
        //Obtengo los parametros para la vista
        Bundle bundle = getArguments();
        //Verifico que existan parametros y exista la constante que necesito
        if(bundle!=null && bundle.containsKey("EVENTO")) {
            Object i = bundle.get("EVENTO");
            if (i.equals(Constantes.FORM_OPERACIONES.VIEW)) {
                menu.findItem(R.id.mi_edit_item).setVisible(true);
                menu.findItem(R.id.mi_save_item).setVisible(false);

            } else if (i.equals(Constantes.FORM_OPERACIONES.NEW) || (i.equals(Constantes.FORM_OPERACIONES.UPDATE))) {
                menu.findItem(R.id.mi_save_item).setVisible(true);
                menu.findItem(R.id.mi_edit_item).setVisible(false);
            }
        }
        menusillo=menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_edit_item:
                Bundle args = new Bundle();
                ClienteFragment client = new ClienteFragment();
                args.putString(ClienteFragment.ARG_ITEM_ID,mCliente.RUCCI.toString());
                args.putSerializable("EVENTO",Constantes.FORM_OPERACIONES.UPDATE);
                client.setArguments(args);

                getFragmentManager().beginTransaction().replace(R.id.content_frame, client).addToBackStack("clienteFragment").commit();
                return true;
            case R.id.mi_save_item:
                if(checkForm()){
                    if (save())
                        getFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getActivity(), "El formulario contiene errores", Toast.LENGTH_LONG);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Obtiene los datos del formulario y los envia a la bdd
     *
     * @return true/false
     */
    private boolean save(){
        boolean res=false;
        Cliente cliente = new Cliente();
        cliente.RUCCI=etRUCCI.getText().toString();
        cliente.SUCURSAL=Integer.parseInt(etSucursal.getText().toString());
        cliente.TIPO= (int) spTipo.getSelectedItemId();
        cliente.IDENTIFICACION= (int) spIdentificacion.getSelectedItemId();
        cliente.EMPRESA=etEmpresa.getText().toString();
        cliente.REPRESENTANTE=etRepresentante.getText().toString();
        cliente.CIUDAD=etCiudad.getText().toString();
        cliente.DIRECCION=etDir1.getText().toString();
        cliente.DIRECCION2=etDir2.getText().toString();
        cliente.TELFAX=etTel1.getText().toString();
        cliente.TELFAX2=etTel1.getText().toString();
        cliente.PROPIETARIO=etPropietario.getText().toString();
        cliente.EMP_PUBLICA=chPublica.isChecked();
        cliente.ACTUALIZACION= new Timestamp(new Date().getTime());
        cliente.CODIGO=Integer.parseInt(etCodigo.getText().toString());
        cliente.VENDEDOR=Integer.parseInt(etVendedor.getText().toString());
        clientesDB = new ClientesDB(getActivity());
        eventosDB = new EventosDB(getActivity());
        //Inicializo el vento par la sincronización
        evento = new Evento();
        evento.OBJETO="Cliente";
        evento.ACTUALIZACION=new Timestamp(new Date().getTime());
        try {
            clientesDB.open();
            if (clientesDB.exist(cliente.RUCCI.toString(), cliente.SUCURSAL)) {
                if(clientesDB.update(cliente)) {
                    evento.OPERACION="UPDATE";
                    evento.CODIGO_OBJETO=cliente.RUCCI.toString();
                    Toast.makeText(getActivity(), "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show();
                    res= true;
                } else {
                    Toast.makeText(getActivity(), "Error al actualizar cliente", Toast.LENGTH_SHORT).show();
                    res=false;
                }
            }else {
                if(clientesDB.insertLocal(cliente)) {
                    evento.OPERACION="INSERT";
                    evento.CODIGO_OBJETO=cliente.RUCCI.toString();
                    Toast.makeText(getActivity(), "Cliente insertado correctamente", Toast.LENGTH_SHORT).show();
                    res= true;
                }else {
                    Toast.makeText(getActivity(), "Error al inesrtar cliente", Toast.LENGTH_SHORT).show();
                    res = false;
                }
            }
            clientesDB.close();
            if(res){
                eventosDB.open();
                if(eventosDB.insert(evento))
                    Toast.makeText(getActivity(), "Cliente registrado para sincronizar", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Error al registrar cliente para sincronizar", Toast.LENGTH_SHORT).show();
                eventosDB.close();
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
    }

    /**
     * Carga los Tipos de identificación de en el Spinner spcIdentificación
     */
    private void loadTiposId(){
        TipoIdentificacionDB tiposDB = new TipoIdentificacionDB(getActivity());

        ArrayList<TipoIdentificacion> arrayList;
        Cursor lista;

        try {
            tiposDB.open();
            arrayList = tiposDB.getList();
            tiposDB.close();
            adapterTipoId = new TipoIdAdapter(getActivity(),arrayList);
            spIdentificacion.setAdapter(adapterTipoId);

        } catch (SQLException e) {
            tiposDB.close();
            e.printStackTrace();
        }
    }

    /**
     * Carga los Tipos de cliente en el Spinner spcTipo
     */
    private void loadTiposCliente(){
        TipoClienteDB tiposDB = new TipoClienteDB(getActivity());
        ArrayList<TipoCliente> arrayList;
        try {
            tiposDB.open();
            arrayList = tiposDB.getList();
            tiposDB.close();
            adapterTipoCliente = new TipoClienteAdapter(getActivity(),arrayList);
            spTipo.setAdapter(adapterTipoCliente);
        } catch (SQLException e) {
            tiposDB.close();
            e.printStackTrace();
        }
    }

}
