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
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopLevelActivity extends Activity {
    private SQLiteDatabase db;
    private Cursor favoritesCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        setupOptionsListView();
        setupFavoritesListView();
    }

    private void setupOptionsListView() {
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(TopLevelActivity.this, PizzasCategoryActivity.class);
                    startActivity(intent);
                }
            }
        };

        ListView listView = findViewById(R.id.options);
        listView.setOnItemClickListener(itemClickListener);
    }

    private void setupFavoritesListView() {
        ListView listFavorites = findViewById(R.id.list_favorites);
        tryToMakeQueryOrShowToast(listFavorites);

        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, PizzaActivity.class);
                intent.putExtra(PizzaActivity.EXTRA_PIZZA_ID, (int)id);
                startActivity(intent);
            }
        });
    }

    private void tryToMakeQueryOrShowToast(ListView listFavorites) {
        try {
            makeQuery();
            addAdapter(listFavorites);
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    private void makeQuery() {
        SQLiteOpenHelper pizzasDatabaseHelper = new PizzasDatabaseHelper(this);
        db = pizzasDatabaseHelper.getReadableDatabase();

        favoritesCursor = db.query(
            "PIZZA",
            new String[]{"_id", "NAME"},
            "FAVORITE = 1",
            null,
            null,
            null,
            null
        );
    }

    private void addAdapter(ListView listFavorites) {
        CursorAdapter favoriteAdapter = new SimpleCursorAdapter(
            TopLevelActivity.this,
            android.R.layout.simple_list_item_1,
            favoritesCursor,
            new String[]{"NAME"},
            new int[]{android.R.id.text1},
            0
        );

        listFavorites.setAdapter(favoriteAdapter);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        
        Cursor newCursor = db.query(
            "PIZZA",
            new String[]{"_id", "NAME"},
            "FAVORITE = 1",
            null,
            null,
            null,
            null
        );
        
        ListView listFavorites = findViewById(R.id.list_favorites);
        CursorAdapter adapter = (CursorAdapter) listFavorites.getAdapter();
        adapter.changeCursor(newCursor);
        favoritesCursor = newCursor;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        favoritesCursor.close();
        db.close();
    }
}
