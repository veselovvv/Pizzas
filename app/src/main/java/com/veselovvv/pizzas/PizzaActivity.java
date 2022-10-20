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
    public static final String EXTRA_PIZZA_ID = "pizzaId"; // TODO make private
    private static final String TABLE_NAME = "PIZZA"; // TODO dry
    private static final String FAVORITE_KEY = "FAVORITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza);

        SQLiteOpenHelper pizzasDatabaseHelper = new PizzasDatabaseHelper.Base(this);

        try {
            SQLiteDatabase database = pizzasDatabaseHelper.getReadableDatabase();
            Cursor cursor = database.query(
                    TABLE_NAME,
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(getPizzaId())},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) updateUI(cursor);
            cursor.close();
            database.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    Integer getPizzaId() {
        return (Integer) getIntent().getExtras().get(EXTRA_PIZZA_ID);
    }

    void updateUI(Cursor cursor) {
        String nameText = cursor.getString(0);
        String descriptionText = cursor.getString(1);
        int photoId = cursor.getInt(2);
        boolean isFavorite = (cursor.getInt(3) == 1);

        TextView nameTextView = findViewById(R.id.name);
        nameTextView.setText(nameText);

        TextView descriptionTextView = findViewById(R.id.description);
        descriptionTextView.setText(descriptionText);

        ImageView photoImageView = findViewById(R.id.photo);
        photoImageView.setImageResource(photoId);
        photoImageView.setContentDescription(nameText);

        CheckBox favoriteCheckBox = findViewById(R.id.favorite);
        favoriteCheckBox.setChecked(isFavorite);
    }

    public void onFavoriteClicked(View view) {
        new UpdatePizzaTask().execute(getPizzaId());
    }

    private class UpdatePizzaTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues pizzaValues;

        protected void onPreExecute() {
            CheckBox favorite = findViewById(R.id.favorite);
            pizzaValues = new ContentValues();
            pizzaValues.put(FAVORITE_KEY, favorite.isChecked());
        }

        protected Boolean doInBackground(Integer... pizzas) {
            int pizzaId = pizzas[0];
            SQLiteOpenHelper pizzasDatabaseHelper = new PizzasDatabaseHelper.Base(PizzaActivity.this);

            try {
                SQLiteDatabase database = pizzasDatabaseHelper.getWritableDatabase();
                database.update(
                        TABLE_NAME,
                        pizzaValues,
                        "_id = ?",
                        new String[]{Integer.toString(pizzaId)}
                );
                database.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success)
                Toast.makeText(PizzaActivity.this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
        }
    }
}
