package com.veselovvv.pizzas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TopLevelActivity extends BaseActivity {
    private PizzasDatabaseHelper.Base pizzasDatabaseHelper;
    private SQLiteDatabase database;
    private Cursor favoritesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        pizzasDatabaseHelper = new PizzasDatabaseHelper.Base(this); //TODO dry

        setupOptionsListView();
        setupFavoritesListView();
    }

    void setupOptionsListView() {
        ListView listView = findViewById(R.id.options);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                        if (position == 0) {
                            Intent intent = new Intent(TopLevelActivity.this, PizzasCategoryActivity.class);
                            startActivity(intent);
                        }
                    }
                }
        );
    }

    void setupFavoritesListView() {
        ListView listFavorites = findViewById(R.id.list_favorites);

        try {
            database = pizzasDatabaseHelper.getReadableDatabase(); //TODO dry
            favoritesCursor = pizzasDatabaseHelper.getMainCursor(database);
            listFavorites.setAdapter(
                    new SimpleCursorAdapter(
                            TopLevelActivity.this,
                            android.R.layout.simple_list_item_1,
                            favoritesCursor,
                            new String[]{"NAME"},
                            new int[]{android.R.id.text1},
                            0
                    )
            );
        } catch (SQLiteException e) {
            showDatabaseUnavailableToast(this);
        }

        listFavorites.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                        Intent intent = new Intent(TopLevelActivity.this, PizzaActivity.class);
                        intent.putExtra(PizzaActivity.EXTRA_PIZZA_ID, (int) id);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onRestart() {
        super.onRestart();

        Cursor newCursor = pizzasDatabaseHelper.getMainCursor(database);
        ListView listFavorites = findViewById(R.id.list_favorites);
        CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
        adapter.changeCursor(newCursor);
        favoritesCursor = newCursor;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoritesCursor.close();
        database.close();
    }
}
