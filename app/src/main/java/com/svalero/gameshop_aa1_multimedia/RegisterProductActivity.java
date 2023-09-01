package com.svalero.gameshop_aa1_multimedia;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.Manifest;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Client;
import com.svalero.gameshop_aa1_multimedia.domain.Product;
import com.svalero.gameshop_aa1_multimedia.domain.Preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterProductActivity extends AppCompatActivity {

    private EditText name;
    private EditText cost;
    private EditText sale;
    private EditText barCode;
    private Button registerButton;
    private ImageView productPhoto;
    private Product edit;
    private Bitmap photo;
    private String username;
    private Long id;
    private Preferences preferences;
    private boolean newPhoto = false;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);

        final GameShopDatabase db = Room.databaseBuilder(this, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();

        try {
            preferences = db.getPreferencesDAO().getPreference();
        } catch (SQLiteConstraintException sce){
            Log.i("onCreate", "Error loading preferences");
        }


        checkCameraPermission();

        Intent intentForm = getIntent();
        username = intentForm.getStringExtra("clientUsername");
        id = intentForm.getLongExtra("client_id", 0L);
        edit = (Product) intentForm.getSerializableExtra("edit");

        name = findViewById(R.id.regProductName);
        cost = findViewById(R.id.regProductCost);
        sale = findViewById(R.id.regProductSale);
        barCode = findViewById(R.id.regProductBarcode);
        registerButton = findViewById(R.id.btProductRegister);
        productPhoto = findViewById(R.id.regProdImage);

        if(edit != null) {
            registerButton.setText("Edit");
            name.setText(edit.getName());
            cost.setText(String.valueOf(edit.getCost()));
            sale.setText(String.valueOf(edit.getSale()));
            barCode.setText(String.valueOf(edit.getBarCode()));
        }
    }

    public void register (View view) {
        final GameShopDatabase db = Room.databaseBuilder(this,GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();


        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "GameShop");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        //Le ponemos nombre al archivo y la extension
        File imageFile = new File(storageDir, System.currentTimeMillis() + ".jpg");
        Log.i("RegisterWorkplace", "register - filePath: " + imageFile);

        if (edit != null) {
            try {
                Product product = new Product();
                product.setId(edit.getId());
                product.setName(name.getText().toString());
                product.setCost(Double.parseDouble(cost.getText().toString()));
                product.setSale(Double.parseDouble(sale.getText().toString()));
                product.setBarCode(Integer.parseInt(barCode.getText().toString()));
                product.setImageURL(imageFile.toString());

                if(newPhoto) {
                    saveBitmapToFile(photo, imageFile);
                    product.setImageURL(imageFile.toString());
                    Log.i("register", product.toString());
                } else {
                    product.setImageURL(edit.getImageURL());
                }

                db.getProductDAO().update(product);

                Intent intent;
                if(!preferences.isBackRegister()){
                    intent = new Intent(this, ManageProductsActivity.class);
                } else {
                    intent = new Intent(this, MainActivity.class);
                }
                intent.putExtra("client_id", id);
                intent.putExtra("clientUsername", username);
                startActivity(intent);

            } catch (SQLiteConstraintException sce) {
                Snackbar.make(name, R.string.wrong, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        } else {
            try {

                saveBitmapToFile(photo, imageFile);
                Product product = new Product();
                product.setName(name.getText().toString());
                product.setCost(Double.parseDouble(cost.getText().toString()));
                product.setSale(Double.parseDouble(sale.getText().toString()));
                product.setBarCode(Integer.parseInt(barCode.getText().toString()));
                product.setImageURL(imageFile.toString());
                Log.i("RegisterProductActivity", "product - " + product);;
                db.getProductDAO().insert(product);

                Intent intent;
                if(!preferences.isBackRegister()){
                    intent = new Intent(this, ManageProductsActivity.class);
                } else {
                    intent = new Intent(this, MainActivity.class);
                }
                intent.putExtra("client_id", id);
                intent.putExtra("clientUsername", username);
                startActivity(intent);
            }  catch (SQLiteConstraintException sce) {
                Snackbar.make(name, R.string.wrong, BaseTransientBottomBar.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        }
    }

    public void makePhoto(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.photoERROR, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            productPhoto.setImageBitmap(photo);
            newPhoto = true;
        } else {
            newPhoto = false;
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},226);
            }
        }
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        try {
            // Crea un archivo para la imagen en el directorio público de imágenes del almacenamiento externo
            file.createNewFile();

            // Crea un flujo de salida para el archivo
            FileOutputStream fos = new FileOutputStream(file);

            // Comprime el Bitmap y lo escribe en el flujo de salida
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            // Cierra el flujo de salida
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
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