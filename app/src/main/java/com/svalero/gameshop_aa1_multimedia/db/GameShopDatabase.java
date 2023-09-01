package com.svalero.gameshop_aa1_multimedia.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.svalero.gameshop_aa1_multimedia.db.dao.ClientDAO;
import com.svalero.gameshop_aa1_multimedia.db.dao.OrderDAO;
import com.svalero.gameshop_aa1_multimedia.db.dao.PreferencesDAO;
import com.svalero.gameshop_aa1_multimedia.db.dao.ProductDAO;
import com.svalero.gameshop_aa1_multimedia.db.dao.ShopDAO;
import com.svalero.gameshop_aa1_multimedia.domain.Client;
import com.svalero.gameshop_aa1_multimedia.domain.Order;
import com.svalero.gameshop_aa1_multimedia.domain.Preferences;
import com.svalero.gameshop_aa1_multimedia.domain.Product;
import com.svalero.gameshop_aa1_multimedia.domain.Shop;
import com.svalero.gameshop_aa1_multimedia.util.DateConverter;

@Database(entities = {Client.class, Order.class, Product.class, Shop.class, Preferences.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class GameShopDatabase extends RoomDatabase {
    public abstract ClientDAO getClientDAO();

    public abstract OrderDAO getOrderDAO();

    public abstract ProductDAO getProductDAO();

    public abstract ShopDAO getShopDAO();

    public abstract PreferencesDAO getPreferencesDAO();
}
