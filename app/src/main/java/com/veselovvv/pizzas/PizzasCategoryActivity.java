package com.veselovvv.pizzas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class PizzasCategoryActivity extends BaseActivity {
    private SQLiteDatabase database;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizzas_category);

        ListView listPizzas = findViewById(R.id.list_pizzas);
        PizzasDatabaseHelper.Base pizzasDatabaseHelper = new PizzasDatabaseHelper.Base(this); // TODO dry

        try {
            database = pizzasDatabaseHelper.getReadableDatabase(); // TODO dry
            cursor = pizzasDatabaseHelper.getPizzasCategoryCursor(database);
            listPizzas.setAdapter(
                    new SimpleCursorAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            cursor,
                            new String[]{"NAME"},
                            new int[]{android.R.id.text1},
                            0
                    )
            );
        } catch (SQLiteException e) {
            showDatabaseUnavailableToast(this);
        }

        listPizzas.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> listPizzas, View itemView, int position, long id) {
                        Intent intent = new Intent(PizzasCategoryActivity.this, PizzaActivity.class);
                        intent.putExtra(PizzaActivity.EXTRA_PIZZA_ID, (int) id);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }
}
