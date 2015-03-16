package apps.denux.mayorga.controladores;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import apps.denux.mayorga.Constantes;
import apps.denux.mayorga.R;
import apps.denux.mayorga.Utiles;
import apps.denux.mayorga.adapters.TabsPagerAdapter;
import apps.denux.mayorga.helpers.RESTHelper;
import apps.denux.mayorga.helpers.SyncHelper;
import apps.denux.mayorga.modelos.EventosDB;
import apps.denux.mayorga.objetos.Evento;

/**
 * Created by dexter on 13/03/15.
 */
public class Servidor extends ActionBarActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servidor_pager);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();

        actionBar.setTitle("SINCRONIZAR");
        actionBar.setSubtitle("Herramientas de sincronización");
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = actionBar.newTab().setText("Generales").setTabListener(this);
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText("Eventos").setTabListener(this);
        actionBar.addTab(tab);


        reloadPager();
    }

    private void reloadPager(){
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
