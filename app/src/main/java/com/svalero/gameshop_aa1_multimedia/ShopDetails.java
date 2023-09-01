package com.svalero.gameshop_aa1_multimedia;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Client;
import com.svalero.gameshop_aa1_multimedia.domain.Shop;

public class ShopDetails extends AppCompatActivity {

    private TextView name;
    private TextView adress;
    private TextView tlf;
    private Button btMap;
    private Shop detailShop;
    private String username;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("clientUsername");
        id = intentFrom.getLongExtra("client_id", 0L);
        detailShop = (Shop) intentFrom.getSerializableExtra("detail");

        name = findViewById(R.id.shopDetName);
        adress = findViewById(R.id.shopDetAddress);
        tlf = findViewById(R.id.shopDetTlf);
        btMap = findViewById(R.id.shopDetNav);

        name.setText(detailShop.getName());
        adress.setText(detailShop.getAdress());
        tlf.setText(detailShop.getTlf());
    }

    public void gpsMap(View view) {
        Intent intent = new Intent(this, ShopMap.class);
        intent.putExtra("data", detailShop);
        this.startActivity(intent);
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