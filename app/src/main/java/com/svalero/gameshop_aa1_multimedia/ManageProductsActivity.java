package com.svalero.gameshop_aa1_multimedia;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.svalero.gameshop_aa1_multimedia.adapter.ProductAdapter;
import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Client;
import com.svalero.gameshop_aa1_multimedia.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {

    private List<Product> productList;
    private ProductAdapter productAdapter;
    private String username;
    private Long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("clientUsername");
        id = intentFrom.getLongExtra("client_id", 0L);

        productList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.productsRv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        productAdapter = new ProductAdapter(this, productList, intentFrom);
        recyclerView.setAdapter(productAdapter);
    }

    public void onResume(){
        super.onResume();
        productList.clear();
        final GameShopDatabase db = Room.databaseBuilder(this, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        productList.addAll(db.getProductDAO().getAll());
        db.close();
        productAdapter.notifyDataSetChanged();
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