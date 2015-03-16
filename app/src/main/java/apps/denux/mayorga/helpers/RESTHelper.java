package apps.denux.mayorga.helpers;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import apps.denux.mayorga.Constantes;

/**
 * Created by dexter on 13/03/15.
 */
public class RESTHelper  {

    private static String USER_REST;
    private static String PASS_REST;
    private static String SERVER_REST;
    private static int PORT_REST;
    private static String DEFAULT_FORMAT_RESPONSE;
    private static boolean AUTH_ENABLED;

    private HttpClient httpClient;
    private Credentials credentials;
    private HttpParams paramsHttp;

    public RESTHelper() {
        init_data();
    }

    private void init_data(){
        USER_REST= Constantes.USER_REST;
        PASS_REST=Constantes.PASS_REST;
        SERVER_REST = Constantes.SERVER_REST;
        PORT_REST=Constantes.PORT_REST;
        DEFAULT_FORMAT_RESPONSE=Constantes.DEFAULT_FORMAT_RESPONSE;
        AUTH_ENABLED=Constantes.AUTH_ENABLED;
    }
    /**
     *
     * @param formatResponse xml|json
     */
    public RESTHelper(String formatResponse) {
        init_data();
        this.DEFAULT_FORMAT_RESPONSE=formatResponse;
    }

    private String get_header(){
        switch (DEFAULT_FORMAT_RESPONSE){
            case "json":
                return "application/json";
            case "xml":
                return "application/xml";
            default:
                return "application/json";
        }
    }

    public void conectarse(){

        paramsHttp= new BasicHttpParams();
        //tiempo de espera por conexión
        HttpConnectionParams.setConnectionTimeout(paramsHttp, Constantes.TIMEOUTCONNECTION);
        //Tiempo de espera por datos
        HttpConnectionParams.setSoTimeout(paramsHttp, Constantes.SETSOTIMEOUT);
        httpClient = new DefaultHttpClient();

        if (AUTH_ENABLED) {
            credentials = new UsernamePasswordCredentials(USER_REST, PASS_REST);
            ((AbstractHttpClient) httpClient).getCredentialsProvider().setCredentials(new AuthScope(SERVER_REST, PORT_REST), credentials);
        }
        Log.i("Rest iniCIALIZA", " HELPER");
    }

    /**
     *
     * @param requestUrl
     * @param params
     * @return JSONArray|null
     */
    public JSONArray get(String requestUrl,String params) throws SocketTimeoutException,IOException,JSONException {
        StringBuilder  stringBuilder = new StringBuilder();
        stringBuilder.append("http://"+SERVER_REST+'/');
        if(!Constantes.SUBDOMAIN.isEmpty())
            stringBuilder.append(Constantes.SUBDOMAIN+'/');

        stringBuilder.append(requestUrl);
        stringBuilder.append(params);
        HttpGet get = new HttpGet(stringBuilder.toString());
        Log.i("GET::", stringBuilder.toString());
        conectarse();
        get.setHeader("content-type",get_header());
        get.setParams(paramsHttp);
        HttpResponse response = httpClient.execute(get);

        JSONObject resultado = new JSONObject(EntityUtils.toString(response.getEntity()));

        if(!resultado.getBoolean("error")) {
            if(!resultado.isNull("data"))
                return resultado.getJSONArray("data");
            else
                return null;
        }

        return null;
    }

    /**
     * Realiza una petición al servidor y retorna solo TRUE o FALSE
     * @param requestUrl
     * @return true|false
     */
    public boolean get(String requestUrl) throws SocketTimeoutException,IOException,JSONException{
        StringBuilder  stringBuilder = new StringBuilder();
        stringBuilder.append("http://"+SERVER_REST+'/');
        if(!Constantes.SUBDOMAIN.isEmpty())
            stringBuilder.append(Constantes.SUBDOMAIN+'/');
        stringBuilder.append(requestUrl);
        HttpGet get = new HttpGet(stringBuilder.toString());
        Log.i("GET::", stringBuilder.toString());

        conectarse();
        get.setHeader("content-type",get_header());
        get.setParams(paramsHttp);
        HttpResponse response = httpClient.execute(get);

        JSONObject resultado = new JSONObject(EntityUtils.toString(response.getEntity()));

        return !resultado.getBoolean("error");

    }

    /**
     * Ejecuta una llamada POST al servidor
     * @param requestUrl URL a la que se realizala petición ej: "Clientes/clientes
     * @param params Cadena compuesta por los parametros ej: "/ruc/180174485001/suc/1"
     * @param datos
     * @return boolean
     */
    public Boolean post(String requestUrl,String params, JSONObject datos)throws SocketTimeoutException,IOException,JSONException {
        StringBuilder  stringBuilder = new StringBuilder();
        stringBuilder.append("http://"+SERVER_REST+'/');
        if(!Constantes.SUBDOMAIN.isEmpty())
            stringBuilder.append(Constantes.SUBDOMAIN+'/');
        stringBuilder.append(requestUrl);
        stringBuilder.append(params);

        Log.i("POST::",stringBuilder.toString());

        HttpPost post = new HttpPost(stringBuilder.toString());
        conectarse();
        post.setHeader("content-type",get_header());
        StringEntity entity = new StringEntity(datos.toString());
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        JSONObject resultado = new JSONObject(EntityUtils.toString(response.getEntity()));
        return !resultado.getBoolean("error");

    }

    public Boolean delete(String requestUrl,String params) throws SocketTimeoutException,IOException, JSONException {
        StringBuilder  stringBuilder = new StringBuilder();
        stringBuilder.append("http://"+SERVER_REST+'/');
        if(!Constantes.SUBDOMAIN.isEmpty())
            stringBuilder.append(Constantes.SUBDOMAIN+'/');
        stringBuilder.append(requestUrl);
        stringBuilder.append(params);

        Log.i("DELETE::", stringBuilder.toString());

        HttpDelete delete = new HttpDelete(stringBuilder.toString());
        conectarse();
        delete.setHeader("content-type",get_header());
        HttpResponse response = httpClient.execute(delete);

        JSONObject resultado = new JSONObject(EntityUtils.toString(response.getEntity()));
        return !resultado.getBoolean("error");
    }
}
