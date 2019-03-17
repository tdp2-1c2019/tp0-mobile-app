package tdp2.tp0app;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public boolean isNetworkAvailabe() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.search);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        if (!this.isNetworkAvailabe()) {
            Toast.makeText(getApplicationContext(), "No hay conectividad", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        new APIRequestTask().execute(query);
    }

    protected class APIRequestTask extends AsyncTask<String, Void, ServiceResponse<ArrayList<Book>>> {
        private APIService apiService = new APIService();
        private String query;
        private Snackbar snackbar;

        public APIRequestTask() {
            this.snackbar = Snackbar.make(findViewById(R.id.search_layout), "Buscando...", Snackbar.LENGTH_INDEFINITE);
        }

        protected void onPreExecute() {
            this.snackbar.show();
        }

        protected ServiceResponse<ArrayList<Book>> doInBackground(String... params) {
            query = params[0];
            return apiService.getBooks(query);
        }

        protected void onPostExecute(ServiceResponse<ArrayList<Book>> response) {
            this.snackbar.dismiss();

            ServiceResponse.ServiceStatusCode statusCode = response.getStatusCode();

            ArrayList<Book> result = response.getServiceResponse();

            if (statusCode == ServiceResponse.ServiceStatusCode.SUCCESS && result != null) {
                Intent intent = new Intent(getBaseContext(), ResultsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("QUERY", query);
                intent.putExtra("RESPONSE", result);
                startActivity(intent);
            }
            else{
                this.snackbar = Snackbar.make(findViewById(R.id.search_layout), "Ocurrio un error", Snackbar.LENGTH_SHORT);
            }
        }
    }
}
