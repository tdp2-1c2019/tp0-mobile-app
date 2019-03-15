package tdp2.tp0app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setTitle(getIntent().getStringExtra("QUERY"));

        TextView response = findViewById(R.id.response);
        response.setText(getIntent().getStringExtra("RESPONSE"));
    }
}
