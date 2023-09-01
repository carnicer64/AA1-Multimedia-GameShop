package com.svalero.gameshop_aa1_multimedia;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
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

public class RegisterClient extends AppCompatActivity {
    private EditText regUsername;
    private EditText password;
    private EditText name;
    private EditText adress;
    private EditText email;
    private EditText tlf;
    private EditText nif;
    private Button registerButton;
    private Client edit;
    private String username;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        Intent intentFrom = getIntent();
        username = intentFrom.getStringExtra("clientUsername");
        id = intentFrom.getLongExtra("client_id", 0L);
        edit = (Client) intentFrom.getSerializableExtra("edit");

        regUsername = findViewById(R.id.regUsername);
        password = findViewById(R.id.regPassword);
        name = findViewById(R.id.regName);
        adress = findViewById(R.id.regAdress);
        email = findViewById(R.id.regEmail);
        tlf = findViewById(R.id.regTlf);
        nif = findViewById(R.id.regNif);
        registerButton = findViewById(R.id.btRegister);

        if(edit != null){
            //Si quieres editar
            registerButton.setText(R.string.edit);
            regUsername.setText(edit.getUsername());
            password.setText(edit.getPassword());
            name.setText(edit.getName());
            adress.setText(edit.getAdress());
            email.setText(edit.getEmail());
            tlf.setText(edit.getTlf());
            nif.setText(edit.getNif());

        }
    }

    public void register(View view){

        final GameShopDatabase db = Room.databaseBuilder(this, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        if(edit != null){
            try {
                Client client = new Client();
                client.setId(edit.getId());
                client.setUsername(regUsername.getText().toString());
                client.setPassword(password.getText().toString());
                client.setName(name.getText().toString());
                client.setAdress(adress.getText().toString());
                client.setEmail(email.getText().toString());
                client.setTlf(tlf.getText().toString());
                client.setNif(nif.getText().toString());

                db.getClientDAO().update(client);

                Intent intent = new Intent(this, ManageClientsActivity.class);
                intent.putExtra("client_id", id);
                intent.putExtra("clientUsername", username);
                startActivity(intent);

            } catch (SQLiteConstraintException sce){
                Snackbar.make(name, R.string.wrong, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        } else {
            try{
                Client client = new Client();
                client.setUsername(regUsername.getText().toString());
                client.setPassword(password.getText().toString());
                client.setName(name.getText().toString());
                client.setAdress(adress.getText().toString());
                client.setEmail(email.getText().toString());
                client.setTlf(tlf.getText().toString());
                client.setNif(nif.getText().toString());

                db.getClientDAO().insert(client);

                Toast.makeText(this, R.string.regSuccess, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("client_id", id);
                intent.putExtra("clientUsername", username);
                startActivity(intent);
            } catch (SQLiteConstraintException sce){
                Snackbar.make(name, R.string.wrong, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
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