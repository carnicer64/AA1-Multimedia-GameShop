package com.svalero.gameshop_aa1_multimedia;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Preferences;

public class RegisterMap extends AppCompatActivity implements Style.OnStyleLoaded {
    private MapView mapView;
    private PointAnnotationManager pointAnnotationManager;
    private Point point;
    private FusedLocationProviderClient fusedLocationClient;
    private Preferences preferences;
    private double gpsLongitude;
    private double gpsLatitude;
    private String username;
    private Long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intentForm =  getIntent();
        username = intentForm.getStringExtra("clientUsername");
        id = intentForm.getLongExtra("client_id", 0L);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        final GameShopDatabase db = Room.databaseBuilder(this, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        try {
            preferences = db.getPreferencesDAO().getPreference();
        } catch (SQLiteConstraintException sce){
            Log.i("onCreate", "Error loading preferences");
        } finally {
            db.close();
        }

        gps();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_map);

        mapView = findViewById(R.id.mapViewDetail);

        GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(mapView);
        gesturesPlugin.addOnMapClickListener(point -> {
            removeAllMarkers();
            this.point = point;
            addMarker(point);
            return true;
        });

        initializePointAnnotationManager();

    }

    public void saveMap(View view){
        Intent data = new Intent();
        data.putExtra("client_id", id);
        data.putExtra("clientUsername", username);

        if(point == null) {
            data.putExtra("latitude", gpsLatitude);
            data.putExtra("longitude", gpsLongitude);
        } else {
            data.putExtra("latitude", point.latitude());
            data.putExtra("longitude", point.longitude());
        }
        setResult(RESULT_OK, data);
        finish();

    }


    private void initializePointAnnotationManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
    }

    public void onStyleLoaded(Style style) {
    }

    private void addMarker(Point point) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.red_marker_foreground));
        pointAnnotationManager.create(pointAnnotationOptions);
    }

    private void addMarker(double latitude, double longitude, int id) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude))
                .withIconImage(BitmapFactory.decodeResource(getResources(), id));
        pointAnnotationManager.create(pointAnnotationOptions);
    }

    private void setCameraPosition(double latitude, double longitude) {
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .pitch(45.0)
                .zoom(15.5)
                .bearing(-17.6)
                .build();
        mapView.getMapboxMap().setCamera(cameraPosition);
    }

    private void removeAllMarkers() {
        pointAnnotationManager.deleteAll();
        point = null;
    }

    @SuppressLint("MissingPermission")
    private void gps() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                // Logic to handle location object
                if(preferences.isAutoMarker()){
                    gpsLongitude = location.getLongitude();
                    gpsLatitude = location.getLatitude();
                    addMarker(location.getLatitude(), location.getLongitude(), R.mipmap.red_marker_foreground);
                }

                setCameraPosition(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, R.string.locationNull, Toast.LENGTH_LONG).show();
            }
        });
    }




}