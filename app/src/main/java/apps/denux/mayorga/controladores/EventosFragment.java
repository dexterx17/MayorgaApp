package apps.denux.mayorga.controladores;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import java.sql.SQLException;
import java.util.ArrayList;

import apps.denux.mayorga.adapters.EventosAdapter;
import apps.denux.mayorga.modelos.EventosDB;
import apps.denux.mayorga.objetos.Evento;

/**
 * Created by dexter on 15/03/15.
 */
public class EventosFragment extends ListFragment {

    private EventosAdapter adapter;
    private EventosDB eventosDB;
    private ArrayList<Evento> list;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reloadListView();
    }

    public void reloadListView(){
        eventosDB = new EventosDB(getActivity());
        try {
            eventosDB.open();
            list=eventosDB.getList();
            eventosDB.close();
            adapter = new EventosAdapter(getActivity(), list);
            setListAdapter(adapter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        reloadListView();
    }
}
