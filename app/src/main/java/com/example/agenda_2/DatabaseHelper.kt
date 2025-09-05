package com.example.agenda_2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Modelo de datos con ID incluido (para CRUD)
data class Registro(
    var nombre: String,
    var apellido: String,
    var direccion: String,
    var telefono: String,
    var edad: String,
    var sexo: String,
    var descripcionContactos: String,
    var id: Int? = null // ID se asigna al insertar
)

class DBHelper(context: Context) : SQLiteOpenHelper(context, "agenda.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE registros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                apellido TEXT,
                direccion TEXT,
                telefono TEXT,
                edad TEXT,
                sexo TEXT,
                descripcion_CONTACTOS TEXT
            )"""
        )

        db.execSQL(
            """CREATE TABLE notas (
                id_notas INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT,
                descripcion_NOTAS TEXT
            )"""
        )





    }

    // TABLA AGENDA CONTACTOS

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS registros")
        onCreate(db)
    }

    // INSERT
    fun insertar(r: Registro): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", r.nombre)
            put("apellido", r.apellido)
            put("direccion", r.direccion)
            put("telefono", r.telefono)
            put("edad", r.edad)
            put("sexo", r.sexo)
        }
        val id = db.insert("registros", null, values)
        r.id = id.toInt()
        return id > 0
    }

    // SELECT *
    fun obtenerTodos(): MutableList<Registro> {
        val lista = mutableListOf<Registro>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM registros", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Registro(
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        apellido =cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                        direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                        telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                        edad = cursor.getString(cursor.getColumnIndexOrThrow("edad")),
                        sexo = cursor.getString(cursor.getColumnIndexOrThrow("sexo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // UPDATE
    fun actualizar(id: Int, r: Registro): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", r.nombre)
            put("direccion", r.direccion)
            put("telefono", r.telefono)
            put("edad", r.edad)
            put("sexo", r.sexo)
        }
        return db.update("registros", values, "id=?", arrayOf(id.toString())) > 0
    }

    // DELETE
    fun eliminar(id: Int): Boolean {
        val db = writableDatabase
        return db.delete("registros", "id=?", arrayOf(id.toString())) > 0
    }
}
