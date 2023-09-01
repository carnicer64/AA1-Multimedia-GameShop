package com.svalero.gameshop_aa1_multimedia;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Client;

public class Preferences extends AppCompatActivity {
    private CheckBox autoLogin;
    private CheckBox preBack;
    private CheckBox preRegister;
    private String username;
    private String password;
    private Long id;
    private com.svalero.gameshop_aa1_multimedia.domain.Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Intent intentFrom = getIntent();

        id = intentFrom.getLongExtra("client_id", 0L);
        username = intentFrom.getStringExtra("clientUsername");

        final GameShopDatabase db = Room.databaseBuilder(this, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        try {
            preferences = db.getPreferencesDAO().getPreference();
            Log.i("onCreate", "Preference: " + preferences.toString());


        } catch (
            SQLiteConstraintException sce){
            Log.i("onCreate", "Error loading preferences");
         } finally {
            db.close();
        }

        autoLogin = findViewById(R.id.preferencesLogin);
        preBack = findViewById(R.id.preferencesBack);
        preRegister = findViewById(R.id.preferencesRegister);

        autoLogin.setChecked(preferences.isAutoLogin());
        preBack.setChecked(preferences.isBackRegister());
        preRegister.setChecked(preferences.isAutoMarker());

    }

    public void register(View view){
        final GameShopDatabase db = Room.databaseBuilder(this, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        com.svalero.gameshop_aa1_multimedia.domain.Preferences newPreferences = new com.svalero.gameshop_aa1_multimedia.domain.Preferences();
        newPreferences.setId(preferences.getId());
        newPreferences.setBackRegister(preBack.isChecked());
        newPreferences.setAutoMarker(preRegister.isChecked());

        try{
            if(autoLogin.isChecked()){
                Client client = db.getClientDAO().getClientById(id);
                password = client.getPassword();
                username = client.getUsername();

                newPreferences.setAutoLogin(true);
                newPreferences.setPassword(password);
                newPreferences.setUsername(username);
            } else {
                newPreferences.setAutoLogin(false);
                newPreferences.setPassword("");
                newPreferences.setUsername("");
            }

            db.getPreferencesDAO().update(newPreferences);

            Snackbar.make(view, R.string.settingsSaved, BaseTransientBottomBar.LENGTH_LONG).show();
        } catch (SQLiteConstraintException sce){
            Log.i("onCreate", "Error loading preferences");
            Snackbar.make(view, R.string.settingsERROR, BaseTransientBottomBar.LENGTH_LONG).show();

        } finally {
            db.close();
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
            Intent intent = new Intent(this, Preferences.class);
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