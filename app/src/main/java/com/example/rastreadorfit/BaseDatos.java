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

    // Nombre y versión de la base de datos (Cambiamos el nombre para que sea nueva)
    private static final String DATABASE_NAME = "fittracker.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla y columnas adaptadas a Comida
    public static final String TABLE_COMIDAS = "comidas";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ALIMENTO = "alimento";       // Antes titulo
    public static final String COLUMN_NOTAS = "notas";             // Antes descripcion
    public static final String COLUMN_MOMENTO = "momento";         // Antes materia
    public static final String COLUMN_CALORIAS = "nivel_calorico"; // Antes prioridad
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_SALUDABLE = "es_saludable";  // Antes completada

    public BaseDatos(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creamos la tabla con la estructura de FitTracker
        String query = "CREATE TABLE " + TABLE_COMIDAS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ALIMENTO + " TEXT, " +
                COLUMN_NOTAS + " TEXT, " +
                COLUMN_MOMENTO + " TEXT, " +
                COLUMN_CALORIAS + " TEXT, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_SALUDABLE + " INTEGER)"; // Guardamos boolean como 0 o 1
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Resetea la tabla si cambiamos la versión
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMIDAS);
        onCreate(db);
    }

    // --- MÉTODOS CRUD ACTUALIZADOS ---

    // 1. AGREGAR COMIDA
    public long agregarRegistro(RegistroComida registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ALIMENTO, registro.getAlimento());
        values.put(COLUMN_NOTAS, registro.getNotas());
        values.put(COLUMN_MOMENTO, registro.getMomento());
        values.put(COLUMN_CALORIAS, registro.getNivelCalorico());
        values.put(COLUMN_FECHA, registro.getFecha());
        values.put(COLUMN_SALUDABLE, registro.isEsSaludable() ? 1 : 0);

        long id = db.insert(TABLE_COMIDAS, null, values);
        db.close();
        return id;
    }

    // 2. OBTENER TODAS LAS COMIDAS
    public List<RegistroComida> obtenerTodosLosRegistros() {
        List<RegistroComida> lista = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_COMIDAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String alimento = cursor.getString(1);
                String notas = cursor.getString(2);
                String momento = cursor.getString(3);
                String calorias = cursor.getString(4);
                String fecha = cursor.getString(5);
                boolean saludable = cursor.getInt(6) == 1;

                RegistroComida registro = new RegistroComida(id, alimento, notas, momento, calorias, fecha, saludable);
                lista.add(registro);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // 3. ACTUALIZAR UN REGISTRO
    public int actualizarRegistro(RegistroComida registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ALIMENTO, registro.getAlimento());
        values.put(COLUMN_NOTAS, registro.getNotas());
        values.put(COLUMN_MOMENTO, registro.getMomento());
        values.put(COLUMN_CALORIAS, registro.getNivelCalorico());
        values.put(COLUMN_FECHA, registro.getFecha());
        values.put(COLUMN_SALUDABLE, registro.isEsSaludable() ? 1 : 0);

        return db.update(TABLE_COMIDAS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(registro.getId())});
    }

    // 4. ELIMINAR REGISTRO
    public void eliminarRegistro(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMIDAS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}