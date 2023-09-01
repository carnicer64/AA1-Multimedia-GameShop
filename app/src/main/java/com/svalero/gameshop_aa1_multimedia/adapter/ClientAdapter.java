package com.svalero.gameshop_aa1_multimedia.adapter;

import static com.svalero.gameshop_aa1_multimedia.db.Constants.DATABASE_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.gameshop_aa1_multimedia.ClientDetails;
import com.svalero.gameshop_aa1_multimedia.R;
import com.svalero.gameshop_aa1_multimedia.RegisterClient;
import com.svalero.gameshop_aa1_multimedia.db.GameShopDatabase;
import com.svalero.gameshop_aa1_multimedia.domain.Client;
import com.svalero.gameshop_aa1_multimedia.domain.Product;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.SuperheroHolder> {
    public Context context;
    public List<Client> clientList;
    Intent intentFrom;

    public ClientAdapter(Context context, List<Client> clientList, Intent intentFrom){
        this.context = context;
        this.clientList = clientList;
        this.intentFrom = intentFrom;
    }


    public SuperheroHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        return new SuperheroHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.SuperheroHolder holder, int position) {
        holder.name.setText(clientList.get(position).getName());
        holder.username.setText(clientList.get(position).getUsername());
        holder.email.setText(clientList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public class SuperheroHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public TextView name;
        public TextView email;
        public Button details;
        public Button edit;
        public Button delete;
        public View parentView;

        public SuperheroHolder(View view){
            super(view);
            parentView = view;

            username = view.findViewById(R.id.clientItemUsername);
            name = view.findViewById(R.id.clientItemName);
            email = view.findViewById(R.id.clientItemEmail);
            details = view.findViewById(R.id.clientItemDetails);
            edit = view.findViewById(R.id.clientItemsEdit);
            delete = view.findViewById(R.id.clientItemsDelete);

            details.setOnClickListener(V -> detailsClient(getAdapterPosition()));
            edit.setOnClickListener(V -> editClient(getAdapterPosition()));
            delete.setOnClickListener(V -> deleteClient(getAdapterPosition()));

        }

        public void deleteClient(int position){
            AlertDialog.Builder delete = new AlertDialog.Builder(context);
            delete.setMessage(R.string.sure).setTitle(R.string.clientDelete)
                    .setPositiveButton(R.string.yes, (dialog, id) -> {
                        final GameShopDatabase db =  Room.databaseBuilder(context, GameShopDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
                        Client client = clientList.get(position);
                        db.getClientDAO().delete(client);
                        clientList.remove(client);
                        notifyItemRemoved(position);
                    }).setNegativeButton(R.string.no, (dialog, id) -> {
                        dialog.dismiss();
                    });
            AlertDialog dialog = delete.create();
            dialog.show();
        }

        public void editClient(int position){
            Client client = clientList.get(position);
            Intent intent = new Intent(context, RegisterClient.class);
            String username = intentFrom.getStringExtra("username");
            Long id = intentFrom.getLongExtra("client_id", 0L);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            intent.putExtra("edit", client);
            context.startActivity(intent);
        }

        public void detailsClient(int position){
            Client client = clientList.get(position);
            Intent intent = new Intent(context, ClientDetails.class);
            String username = intentFrom.getStringExtra("username");
            Long id = intentFrom.getLongExtra("client_id", 0L);
            intent.putExtra("client_id", id);
            intent.putExtra("clientUsername", username);
            intent.putExtra("detail", client);
            context.startActivity(intent);
        }
    }
}
