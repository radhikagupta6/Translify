package com.kvr.translify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.kvr.translify.R;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";
    private String langugae_code;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;
    private Spinner subjectSpinner;
    private TextView preflang;
    private Button translate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        statusMessage = findViewById(R.id.status_message);
        textValue = findViewById(R.id.text_value);
        useFlash = findViewById(R.id.use_flash);
        preflang = findViewById(R.id.prefLang);
        translate = findViewById(R.id.translate_button);

        Button readTextButton = findViewById(R.id.read_text_button);
        readTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch Ocr capture activity.
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

        subjectSpinner = findViewById(R.id.subjects_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.subjects_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);
        subjectSpinner.setOnItemSelectedListener(this);


        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent translate = new Intent(MainActivity.this, TranslateActivity.class);
                translate.putExtra("textDetected", textValue.getText().toString().replace("\n", " "));
                translate.putExtra("key", langugae_code.replace("English", "en").replace("Hindi", "hi").replace("French", "fr").replace("Spanish", "es").replace("Russian", "ru").replace("Italian", "it").replace("Portuguese", "pt").replace(" Swedish", "sv").replace("German", "de").replace("Arabic", "ar").replace("Bengali", "bn"));
                startActivity(translate);
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        langugae_code = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(text);
                    subjectSpinner.setVisibility(View.VISIBLE);
                    preflang.setVisibility(View.VISIBLE);
                    translate.setVisibility(View.VISIBLE);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
