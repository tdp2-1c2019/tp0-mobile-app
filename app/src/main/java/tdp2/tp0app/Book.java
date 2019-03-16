package tdp2.tp0app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    public String name;
    public List<String> authors;
    public String description;
    public List<String> categories;

    private static List<String> convertToStringArray(JSONArray jsonArray){
        List<String> list = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                list.add( jsonArray.getString(i) );
            } catch (JSONException e) {
                Log.e("Book", e.getMessage());
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Book fromJsonObject(JSONObject bookJson) throws JSONException {
        Book book = new Book();
        book.name = bookJson.has("name") ? bookJson.getString("name") : "";
        book.description = bookJson.getString("description");

        if (bookJson.has("authors")){
            book.authors = convertToStringArray(bookJson.getJSONArray("authors"));
        }

        if (bookJson.has("categories")){
            book.categories = convertToStringArray(bookJson.getJSONArray("categories"));
        }

        return book;
    }
}
