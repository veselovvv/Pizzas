package com.veselovvv.pizzas;

import android.app.Activity;
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
import android.widget.Toast;

public class TopLevelActivity extends Activity {
    private static final String TABLE_NAME = "PIZZA";

    private SQLiteDatabase database;
    private Cursor favoritesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

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
            database = new PizzasDatabaseHelper.Base(this).getReadableDatabase();
            favoritesCursor = getCursor(database);
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
            Toast.makeText(this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
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

    Cursor getCursor(SQLiteDatabase database) {
        return database.query(
                TABLE_NAME,
                new String[]{"_id", "NAME"},
                "FAVORITE = 1",
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onRestart() {
        super.onRestart();

        Cursor newCursor = getCursor(database);
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
