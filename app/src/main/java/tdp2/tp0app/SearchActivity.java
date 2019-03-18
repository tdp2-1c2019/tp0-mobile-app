package tdp2.tp0app;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public boolean isNetworkAvailable() {
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

        if (!this.isNetworkAvailable()) {
            Snackbar.make(findViewById(R.id.search_layout), "No hay conectividad", Snackbar.LENGTH_LONG).show();
        }

        boolean hasAllPermissions =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!hasAllPermissions) {
            // Requests permissions
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1
            );
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (!this.isNetworkAvailable()) {
            Snackbar.make(findViewById(R.id.search_layout), "No hay conectividad", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        boolean hasConnectivity = isNetworkAvailable();
        if (Intent.ACTION_SEARCH.equals(intent.getAction()) && hasConnectivity) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        } else if (! hasConnectivity){
            Snackbar.make(findViewById(R.id.search_layout), "No hay conectividad", Snackbar.LENGTH_SHORT).show();
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
                if (!result.isEmpty()) {
                        Intent intent = new Intent(getBaseContext(), ResultsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("QUERY", query);
                        intent.putExtra("RESPONSE", result);
                        startActivity(intent);

                } else {
                    Snackbar.make(findViewById(R.id.search_layout), "No hay libros que coincidan con la búsqueda realizada. Intentelo nuevamente con alguna palabra clave diferente.", Snackbar.LENGTH_LONG).show();
                }
            } else {
                this.snackbar = Snackbar.make(findViewById(R.id.search_layout), "Ocurrio un error", Snackbar.LENGTH_SHORT);
            }
        }
    }
}
