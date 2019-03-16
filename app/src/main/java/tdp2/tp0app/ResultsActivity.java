package tdp2.tp0app;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setTitle(getIntent().getStringExtra("QUERY"));

//        TextView response = findViewById(R.id.response);
        String r = getIntent().getStringExtra("RESPONSE");
        Map<String, Object> retMap = new Gson().fromJson(
                r, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
//        response.setText(r);

//        TableLayout container = (TableLayout) findViewById(R.id.layout);
//
//        Button b = new Button(this);
//        container.addView(b);
//
//        Button b2 = new Button(this);
//        container.addView(b2);
//
//        Button b3 = new Button(this);
//        container.addView(b3);
//
//        Button b4 = new Button(this);
//        container.addView(b4);
    }
}
