package com.veselovvv.pizzas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

interface PizzasDatabaseHelper {
    Cursor getMainCursor(SQLiteDatabase database);
    Cursor getPizzaCursor(SQLiteDatabase database, Integer pizzaId);
    Cursor getPizzasCategoryCursor(SQLiteDatabase database);
    void updateDatabase(SQLiteDatabase database, ContentValues pizzaValues, int pizzaId);

    class Base extends SQLiteOpenHelper implements PizzasDatabaseHelper {
        private static final String DB_NAME = "pizzas";
        private static final int DB_VERSION = 2;
        private static final String NAME_KEY = "NAME";
        private static final String DESCRIPTION_KEY = "DESCRIPTION";
        private static final String IMAGE_RESOURCE_ID_KEY = "IMAGE_RESOURCE_ID";
        private static final String TABLE_NAME = "PIZZA";

        private final Context context;

        Base(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            updateMyDatabase(database, 0, DB_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            updateMyDatabase(database, oldVersion, newVersion);
        }

        public void updateMyDatabase(SQLiteDatabase database, int oldVersion, int newVersion) {
            if (oldVersion < 1) {
                insertPizzas(database);
                if (oldVersion < 2) database.execSQL("ALTER TABLE PIZZA ADD COLUMN FAVORITE NUMERIC;");
            }
        }

        public void insertPizzas(SQLiteDatabase database) {
            database.execSQL("CREATE TABLE PIZZA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");

            insertPizza(
                    database,
                    R.string.neapolitan_pizza,
                    R.string.neapolitan_pizza_description,
                    R.drawable.neapolitan_pizza
            );

            insertPizza(
                    database,
                    R.string.chicago_pizza,
                    R.string.chicago_pizza_description,
                    R.drawable.chicago_pizza
            );

            insertPizza(
                    database,
                    R.string.new_york_style_pizza,
                    R.string.new_york_style_pizza_description,
                    R.drawable.new_york_style_pizza
            );

            insertPizza(
                    database,
                    R.string.sicilian_pizza,
                    R.string.sicilian_pizza_description,
                    R.drawable.sicilian_pizza
            );

            insertPizza(
                    database,
                    R.string.greek_pizza,
                    R.string.greek_pizza_description,
                    R.drawable.greek_pizza
            );

            insertPizza(
                    database,
                    R.string.california_pizza,
                    R.string.california_pizza_description,
                    R.drawable.california_pizza
            );

            insertPizza(
                    database,
                    R.string.detroit_pizza,
                    R.string.detroit_pizza_description,
                    R.drawable.detroit_pizza
            );

            insertPizza(
                    database,
                    R.string.st_louis_pizza,
                    R.string.st_louis_pizza_description,
                    R.drawable.st_louis_pizza
            );
        }

        public void insertPizza(SQLiteDatabase database, int nameResId, int descriptionResId, int resourceId) {
            ContentValues pizzaValues = new ContentValues();
            pizzaValues.put(NAME_KEY, context.getString(nameResId));
            pizzaValues.put(DESCRIPTION_KEY, context.getString(descriptionResId));
            pizzaValues.put(IMAGE_RESOURCE_ID_KEY, resourceId);
            database.insert(TABLE_NAME, null, pizzaValues);
        }

        @Override
        public Cursor getMainCursor(SQLiteDatabase database) {
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
        public Cursor getPizzaCursor(SQLiteDatabase database, Integer pizzaId) {
            return database.query(
                    TABLE_NAME,
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?",
                    new String[]{Integer.toString(pizzaId)},
                    null,
                    null,
                    null
            );
        }

        @Override
        public Cursor getPizzasCategoryCursor(SQLiteDatabase database) {
            return database.query(
                    TABLE_NAME,
                    new String[]{"_id", "NAME"},
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void updateDatabase(SQLiteDatabase database, ContentValues pizzaValues, int pizzaId) {
            database.update(
                    TABLE_NAME,
                    pizzaValues,
                    "_id = ?",
                    new String[]{Integer.toString(pizzaId)}
            );
        }
    }
}
