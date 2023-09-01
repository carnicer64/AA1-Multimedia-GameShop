package com.svalero.gameshop_aa1_multimedia.adapter;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.gameshop_aa1_multimedia.ProductDetail;
import com.svalero.gameshop_aa1_multimedia.RegisterProductActivity;
import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Product;
import com.svalero.gameshop_aa1_multimedia.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.SuperheroHolder> {
    public Context context;
    public List<Product> productList;
    Intent intentFrom;

    public ProductAdapter(Context context, List<Product> productList, Intent intentFrom) {
        this.context = context;
        this.productList = productList;
        this.intentFrom = intentFrom;
    }


    @NonNull
    @Override
    public SuperheroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new SuperheroHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.SuperheroHolder holder, int position) {
        Log.i("Product Adapter - onBinedViewHOlder", "Product: " + productList.get(position));
        Product product = productList.get(position);
        holder.name.setText(String.valueOf(product.getName()));
        holder.barCode.setText(String.valueOf(product.getBarCode()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class SuperheroHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView barCode;
        public Button details;
        public Button edit;
        public Button delete;
        public View parentView;

        public SuperheroHolder(View view){
            super(view);
            parentView = view;
            name = view.findViewById(R.id.productItemName);
            barCode = view.findViewById(R.id.productItemBarCode);
            details = view.findViewById(R.id.productItemDetail);
            edit = view.findViewById(R.id.productItemEdit);
            delete = view.findViewById(R.id.productItemDelete);

            details.setOnClickListener(V -> detailsProduct(getAdapterPosition()));
            edit.setOnClickListener(V -> editProduct(getAdapterPosition()));
            delete.setOnClickListener(v -> deleteProduct(getAdapterPosition()));
        }

        public void deleteProduct(int position){
            AlertDialog.Builder delete = new AlertDialog.Builder(context);
            delete.setMessage(R.string.sure).setTitle(R.string.productDelete)
                    .setPositiveButton(R.string.yes, (dialog, id) -> {
                        final GameShopDatabase db = Room.databaseBuilder(context, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                        Product product = productList.get(position);
                        db.getProductDAO().delete(product);
                        productList.remove(product);
                        notifyItemRemoved(position);
                    }).setNegativeButton(R.string.no, (dialog, id) -> {
                        dialog.dismiss();
                    });
            AlertDialog dialog = delete.create();
            dialog.show();
        }

        public void editProduct(int position){
            Product product = productList.get(position);
            Intent intent = new Intent(context, RegisterProductActivity.class);
            String username = intentFrom.getStringExtra("username");
            Long id = intentFrom.getLongExtra("client_id", 0L);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            intent.putExtra("edit", product);
            context.startActivity(intent);
        }

        public void detailsProduct(int position){
            Product product = productList.get(position);
            Intent intent = new Intent(context, ProductDetail.class);
            String username = intentFrom.getStringExtra("username");
            Long id = intentFrom.getLongExtra("client_id", 0L);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            intent.putExtra("detail", product);
            context.startActivity(intent);
        }
    }
}
