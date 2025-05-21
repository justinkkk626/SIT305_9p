package com.example.justin9p;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.libraries.places.api.model.Place;

import java.util.Arrays;
import java.util.List;

public class PlacePickerActivity extends AppCompatActivity {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 Places API（如果还未初始化）
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDcQ-bSRd6nnZYrNlGcb_oARDq6Jzrm8ds");
        }

        // 指定要返回的字段
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        // 启动自动补全 UI
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String address = place.getAddress();

                // 将地址返回给调用者
                Intent resultIntent = new Intent();
                resultIntent.putExtra("address", address);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // 用户取消选择或出错
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
}
