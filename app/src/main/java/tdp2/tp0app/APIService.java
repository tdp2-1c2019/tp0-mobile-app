package tdp2.tp0app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class APIService {
    private static final String BASE_URL = "https://tp0-api.herokuapp.com/api/books?q=";

    public ServiceResponse<ArrayList<Book>> getBooks(String query) {

        HttpURLConnection client = null;

        try {
            String encodedQuery = URLEncoder.encode(query);
            String apiUrl = BASE_URL + encodedQuery;
            URL url = new URL(apiUrl);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("Accept", "application/json");

            client.connect();



            BufferedReader br;
            if (200 <= client.getResponseCode() && client.getResponseCode() <= 299) {
                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(client.getErrorStream()));
            }

            // Convert result to string
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            String stringResult = sb.toString();

            // Convert result string to JSON object
            JSONObject result = new JSONObject(stringResult);

            if (result.has("message")) {
                // There was an error
                throw new IllegalStateException(result.getString("message"));
            }

            JSONArray booksJson = result.getJSONArray("items");
            ArrayList<Book> books = new ArrayList<>();

            for (int i = 0; i < booksJson.length(); i++) {
                JSONObject bookJson = booksJson.getJSONObject(i);
                books.add(Book.fromJsonObject(bookJson));
            }

            Collections.sort(books, new BookSort());

            return new ServiceResponse<>(ServiceResponse.ServiceStatusCode.SUCCESS, books);

        } catch (Exception exception) {
            Log.e("Exception", exception.getMessage());
            return null;
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }
}
