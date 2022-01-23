package com.veselovvv.pizzas;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PizzaActivity extends Activity {
    public static final String EXTRA_PIZZA_ID = "pizzaId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza);

        int pizzaId = (Integer) getIntent().getExtras().get(EXTRA_PIZZA_ID);
        SQLiteOpenHelper pizzasDatabaseHelper = new PizzasDatabaseHelper(this);
        tryToMakeQueryOrShowToast(pizzasDatabaseHelper, pizzaId);
    }

    private void tryToMakeQueryOrShowToast(SQLiteOpenHelper pizzasDatabaseHelper, int pizzaId) {
        try {
            makeQuery(pizzasDatabaseHelper, pizzaId);
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    private void makeQuery(SQLiteOpenHelper pizzasDatabaseHelper, int pizzaId) {
        SQLiteDatabase db = pizzasDatabaseHelper.getReadableDatabase();

        Cursor cursor = db.query(
            "PIZZA",
            new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
            "_id = ?",
            new String[]{Integer.toString(pizzaId)},
            null,
            null,
            null
        );

        if (cursor.moveToFirst()) updateUI(cursor);
        cursor.close();
        db.close();
    }

    private void updateUI(Cursor cursor) {
        String nameText = cursor.getString(0);
        String descriptionText = cursor.getString(1);
        int photoId = cursor.getInt(2);
        boolean isFavorite = (cursor.getInt(3) == 1);

        TextView name = findViewById(R.id.name);
        name.setText(nameText);

        TextView description = findViewById(R.id.description);
        description.setText(descriptionText);

        ImageView photo = findViewById(R.id.photo);
        photo.setImageResource(photoId);
        photo.setContentDescription(nameText);

        CheckBox favorite = findViewById(R.id.favorite);
        favorite.setChecked(isFavorite);
    }

    public void onFavoriteClicked(View view) {
        int pizzaId = (Integer) getIntent().getExtras().get(EXTRA_PIZZA_ID);
        new UpdatePizzaTask().execute(pizzaId);
    }

    private class UpdatePizzaTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues pizzaValues;

        protected void onPreExecute() {
            CheckBox favorite = findViewById(R.id.favorite);
            pizzaValues = new ContentValues();
            pizzaValues.put("FAVORITE", favorite.isChecked());
        }

        protected Boolean doInBackground(Integer... pizzas) {
            int pizzaId = pizzas[0];
            SQLiteOpenHelper pizzasDatabaseHelper = new PizzasDatabaseHelper(PizzaActivity.this);

            try {
                updateData(pizzasDatabaseHelper, pizzaId);
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        private void updateData(SQLiteOpenHelper pizzasDatabaseHelper, int pizzaId) {
            SQLiteDatabase db = pizzasDatabaseHelper.getWritableDatabase();

            db.update(
                "PIZZA",
                pizzaValues,
                "_id = ?",
                new String[]{Integer.toString(pizzaId)}
            );

            db.close();
        }

        protected void onPostExecute(Boolean success) {
            if (!success)
                Toast.makeText(PizzaActivity.this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
        }
    }
}
