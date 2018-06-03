package com.sonicvoxel.pasicalro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    public static final String DOUBLE_RATIO = "DasIstDasVerhältnis";
    public static final String DOUBLE_EN0=  "AktivierungsEnergie";
    public static final String DOUBLE_TEMP= "Temperatur";
    public static final String INT_FARBSCHEMA ="Farbschema";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void bildZeigen(View view) {
        Intent intent = new Intent(this, ZeigeBild.class);
        EditText editText= (EditText) findViewById(R.id.RatioInput);
        double ratio = Double.parseDouble(editText.getText().toString());
        editText= (EditText) findViewById(R.id.En0Input);
        double en0 =Double.parseDouble(editText.getText().toString());
        if (ratio<0 | ratio>1) ratio=0.5;
        editText = (EditText) findViewById(R.id.TempInput);
        double T = Double.parseDouble(editText.getText().toString());

        Spinner spinner = (Spinner) findViewById(R.id.FarbschemaSpinner);
        int farbschema = spinner.getSelectedItemPosition();


        intent.putExtra(DOUBLE_RATIO,ratio);
        intent.putExtra(DOUBLE_EN0,en0);
        intent.putExtra(DOUBLE_TEMP,T);
        intent.putExtra(INT_FARBSCHEMA,farbschema);
        startActivity(intent);
    }

}
