package com.example.rastreadorfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BaseDatos extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fittracker_v2.db"; // Cambié el nombre para forzar nueva BD
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_COMIDAS = "comidas";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ALIMENTO = "alimento";
    public static final String COLUMN_NOTAS = "notas";
    public static final String COLUMN_MOMENTO = "momento";
    public static final String COLUMN_CALORIAS = "calorias"; // Ahora guarda ENTEROS (INTEGER)
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_SALUDABLE = "es_saludable";

    public BaseDatos(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_COMIDAS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ALIMENTO + " TEXT, " +
                COLUMN_NOTAS + " TEXT, " +
                COLUMN_MOMENTO + " TEXT, " +
                COLUMN_CALORIAS + " INTEGER, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_SALUDABLE + " INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMIDAS);
        onCreate(db);
    }

    public long agregarRegistro(RegistroComida registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALIMENTO, registro.getAlimento());
        values.put(COLUMN_NOTAS, registro.getNotas());
        values.put(COLUMN_MOMENTO, registro.getMomento());
        values.put(COLUMN_CALORIAS, registro.getCalorias());
        values.put(COLUMN_FECHA, registro.getFecha());
        values.put(COLUMN_SALUDABLE, registro.isEsSaludable() ? 1 : 0);
        long id = db.insert(TABLE_COMIDAS, null, values);
        db.close();
        return id;
    }

    public List<RegistroComida> obtenerTodosLosRegistros() {
        List<RegistroComida> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMIDAS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String alimento = cursor.getString(1);
                String notas = cursor.getString(2);
                String momento = cursor.getString(3);
                int calorias = cursor.getInt(4); // Leemos int
                String fecha = cursor.getString(5);
                boolean saludable = cursor.getInt(6) == 1;

                lista.add(new RegistroComida(id, alimento, notas, momento, calorias, fecha, saludable));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // Método para borrar (útil para el adaptador)
    public void eliminarRegistro(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMIDAS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Método actualizar (abreviado para ahorrar espacio, úsalo si lo necesitas en Detalle)
    public int actualizarRegistro(RegistroComida registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALIMENTO, registro.getAlimento());
        values.put(COLUMN_MOMENTO, registro.getMomento());
        values.put(COLUMN_CALORIAS, registro.getCalorias());
        values.put(COLUMN_SALUDABLE, registro.isEsSaludable() ? 1 : 0);
        return db.update(TABLE_COMIDAS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(registro.getId())});
    }
}