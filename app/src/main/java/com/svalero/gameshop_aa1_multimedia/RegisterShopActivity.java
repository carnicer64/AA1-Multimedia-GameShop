package com.svalero.gameshop_aa1_multimedia;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Client;
import com.svalero.gameshop_aa1_multimedia.domain.Preferences;
import com.svalero.gameshop_aa1_multimedia.domain.Shop;

public class RegisterShopActivity extends AppCompatActivity {

    private EditText name;
    private EditText adress;
    private EditText tlf;
    private EditText latitude;
    private EditText longitude;
    private Button registerButton;
    private Shop edit;
    private double dlatitude;
    private double dlongitude;
    static final int REQUEST_MAP_CAPTURE = 2;
    private Preferences preferences;
    private String username;
    private Long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);

        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("clientUsername");
        id = intentFrom.getLongExtra("client_id", 0L);
        edit = (Shop) intentFrom.getSerializableExtra("edit");

        checkLocationPermission();

        final GameShopDatabase db = Room.databaseBuilder(this, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        try {
            preferences = db.getPreferencesDAO().getPreference();
        } catch (SQLiteConstraintException sce){
            Log.i("onCreate", "Error loading preferences");
        } finally {
            db.close();
        }

        name = findViewById(R.id.regShopName);
        adress = findViewById(R.id.regShopAdress);
        tlf = findViewById(R.id.regShopTlf);
        latitude = findViewById(R.id.regShopLat);
        longitude = findViewById(R.id.regShopLong);
        registerButton = findViewById(R.id.btShopRegister);

        if(edit != null){
            registerButton.setText("Edit");
            name.setText(edit.getName());
            adress.setText(edit.getAdress());
            tlf.setText(edit.getTlf());
            latitude.setText(edit.getLatitude());
            longitude.setText(edit.getLongitude());
        }
    }

    public void getMap(View view){
        Intent intent = new Intent(this, RegisterMap.class);
        intent.putExtra("client_id", id);
        intent.putExtra("clientUsername", username);
        startActivityForResult(intent, REQUEST_MAP_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_MAP_CAPTURE && resultCode == RESULT_OK){
            dlatitude = data.getDoubleExtra("latitude", 0.);
            dlongitude = data.getDoubleExtra("longitude", 0.);
            Toast.makeText(this, R.string.OK, Toast.LENGTH_LONG).show();
        }

        if(requestCode == REQUEST_MAP_CAPTURE && resultCode == RESULT_CANCELED){
            Toast.makeText(this, R.string.noOK, Toast.LENGTH_LONG).show();
        }

        latitude.setText(String.valueOf(dlatitude));
        longitude.setText(String.valueOf(dlongitude));
    }

    public void register(View view) {
        final GameShopDatabase db = Room.databaseBuilder(this,GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        if (edit != null){
            try {
                Shop shop = new Shop();
                shop.setId(edit.getId());
                shop.setName(name.getText().toString());
                shop.setAdress(adress.getText().toString());
                shop.setTlf(tlf.getText().toString());
                shop.setLatitude(latitude.getText().toString());
                shop.setLongitude(longitude.getText().toString());

                db.getShopDAO().update(shop);

                Intent intent;
                if(!preferences.isBackRegister()){
                    intent = new Intent(this, ManageShopsActivity.class);
                    intent.putExtra("client_id", id);
                    intent.putExtra("clientUsername", username);
                } else {
                    intent = new Intent(this, MainActivity.class);
                    intent.putExtra("client_id", id);
                    intent.putExtra("clientUsername", username);
                }
                startActivity(intent);


            } catch (SQLiteConstraintException sce){
                Snackbar.make(name, R.string.wrong, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        } else {
            try {
                Shop shop = new Shop();
                shop.setName(name.getText().toString());
                shop.setAdress(adress.getText().toString());
                shop.setTlf(tlf.getText().toString());
                shop.setLatitude(latitude.getText().toString());
                shop.setLongitude(longitude.getText().toString());

                db.getShopDAO().insert(shop);

                Toast.makeText(this, R.string.regSuccess, Toast.LENGTH_LONG).show();

                Intent intent;
                if(!preferences.isBackRegister()){
                    intent = new Intent(this, ManageShopsActivity.class);
                    intent.putExtra("client_id", id);
                    intent.putExtra("clientUsername", username);
                } else {
                    intent = new Intent(this, MainActivity.class);
                    intent.putExtra("client_id", id);
                    intent.putExtra("clientUsername", username);
                }

                startActivity(intent);
            } catch (SQLiteConstraintException sce) {
                Snackbar.make(name, R.string.wrong, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        }
    }

    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.menuMain).setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.menuProfile){
            GameShopDatabase db = Room.databaseBuilder(this, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            Client client = db.getClientDAO().getClientById(id);

            Intent intent = new Intent(this, ClientDetails.class);
            intent.putExtra("detail", client);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            startActivity(intent);

        } else if(item.getItemId() == R.id.menuMain){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            startActivity(intent);
        } else if(item.getItemId() == R.id.menuShops){
            Intent intent = new Intent(this, ManageShopsActivity.class);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            startActivity(intent);
        } else if(item.getItemId() == R.id.menuProducts){
            Intent intent = new Intent(this, ManageProductsActivity.class);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            startActivity(intent);
        } else if(item.getItemId() == R.id.menuUsers){
            Intent intent = new Intent(this, ManageClientsActivity.class);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            startActivity(intent);
        } else if(item.getItemId() == R.id.menuPreferences){
            Intent intent = new Intent(this, com.svalero.gameshop_aa1_multimedia.Preferences.class);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            startActivity(intent);
        }
        return false;
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("client_id", id);
        intent.putExtra("clientUsername", username);
        startActivity(intent);
    }
}