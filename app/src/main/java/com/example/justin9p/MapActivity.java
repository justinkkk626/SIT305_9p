package com.example.justin9p;

import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        db = new DatabaseHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 默认显示墨尔本标记
        LatLng melbourne = new LatLng(-37.8136, 144.9631);
        mMap.addMarker(new MarkerOptions()
                .position(melbourne)
                .title("Melbourne")
                .snippet("Fixed marker example"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 10));

        // 点击“当前定位”按钮时显示假坐标
        mMap.setOnMyLocationButtonClickListener(() -> {
            Toast.makeText(MapActivity.this, "Current Location: 123.456, 78.910", Toast.LENGTH_SHORT).show();
            return false;
        });

        // 开启地图交互功能
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        // 加载数据库中所有位置项
        List<Item> itemList = db.getAllItems();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        LatLng lastPosition = null;

        for (Item item : itemList) {
            String locStr = item.getLocation();
            try {
                LatLng latLng;
                if (locStr.contains(",")) {
                    String[] parts = locStr.split(",");
                    double lat = Double.parseDouble(parts[0].trim());
                    double lng = Double.parseDouble(parts[1].trim());
                    latLng = new LatLng(lat, lng);
                } else {
                    List<android.location.Address> addressList = geocoder.getFromLocationName(locStr, 1);
                    if (addressList == null || addressList.isEmpty()) continue;
                    latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                }

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(item.getTitle())
                        .snippet(item.getDesc()));
                lastPosition = latLng;

            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 如果数据库中有标记，移动视角到最后一个位置
        if (lastPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, 10));
        }
    }
}
