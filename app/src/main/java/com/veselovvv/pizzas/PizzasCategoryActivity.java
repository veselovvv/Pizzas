package com.veselovvv.pizzas;

import android.app.Activity;
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
import android.widget.Toast;

public class PizzasCategoryActivity extends Activity {
    private static final String TABLE_NAME = "PIZZA"; // TODO dry

    private SQLiteDatabase database;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizzas_category);

        ListView listPizzas = findViewById(R.id.list_pizzas);
        SQLiteOpenHelper pizzasDatabaseHelper = new PizzasDatabaseHelper.Base(this); // TODO here and in other activities make a one instance

        try {
            database = pizzasDatabaseHelper.getReadableDatabase();
            cursor = database.query(
                    TABLE_NAME,
                    new String[]{"_id", "NAME"},
                    null,
                    null,
                    null,
                    null,
                    null
            );
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
            Toast.makeText(this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
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
