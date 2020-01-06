package m2.iscae.mr.maploaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStartMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartMap = findViewById(R.id.start_map);
        btnStartMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnStartMap) {
            Intent intent =new Intent(MainActivity.this,MapActivity.class);
            startActivity(intent);
        }
    }
}
