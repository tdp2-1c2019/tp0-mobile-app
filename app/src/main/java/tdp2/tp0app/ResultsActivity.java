package tdp2.tp0app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setTitle(getIntent().getStringExtra("QUERY"));

        ArrayList<Book> books = (ArrayList<Book>) getIntent().getSerializableExtra("RESPONSE");

        RecyclerView booksList = findViewById(R.id.booksList);
        booksList.setAdapter(new BooksAdapter(ResultsActivity.this, books));
    }
}
