package com.svalero.gameshop_aa1_multimedia;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Client;

public class ClientDetails extends AppCompatActivity {

    private TextView name;
    private TextView address;
    private TextView email;
    private TextView tlf;
    private TextView nif;
    private Client detailClient;
    private String username;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        Intent intentForm =  getIntent();
        username = intentForm.getStringExtra("clientUsername");
        id = intentForm.getLongExtra("client_id", 0L);
        detailClient = (Client) intentForm.getSerializableExtra("detail");

        name = findViewById(R.id.clientDetName);
        address = findViewById(R.id.clientDetAddress);
        email = findViewById(R.id.clientDetEmail);
        tlf = findViewById(R.id.clientDetTlf);
        nif = findViewById(R.id.clientDetNif);

        name.setText(detailClient.getName());
        address.setText(detailClient.getAdress());
        email.setText(detailClient.getEmail());
        tlf.setText(detailClient.getTlf());
        nif.setText(detailClient.getNif());
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