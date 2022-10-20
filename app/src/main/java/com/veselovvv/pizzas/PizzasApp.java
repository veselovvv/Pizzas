package com.veselovvv.pizzas;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class PizzasApp extends Application {
    private PizzasDatabaseHelper.Base pizzasDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        pizzasDatabaseHelper = new PizzasDatabaseHelper.Base(this);
    }

    public PizzasDatabaseHelper.Base getPizzasDatabaseHelper() {
        return pizzasDatabaseHelper;
    }

    public SQLiteDatabase getReadableDatabase() {
        return pizzasDatabaseHelper.getReadableDatabase();
    }
}
