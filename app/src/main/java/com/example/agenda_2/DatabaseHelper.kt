package com.example.agenda_2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Modelo de datos con ID incluido (para CRUD)
data class Registro(
    // AGENDA CONTACTOS

    var nombre: String,
    var apellido: String,
    var direccion: String,
    var telefono: String,
    var edad: String,
    var sexo: String,
    var descripcionContactos: String,

    var id: Int? = null // ID de las ADD AGENDA
)

data class Notas(
    var titulo: String,
    var descripcionNotas: String,
    var id: Int? = null
)


class DBHelper(context: Context) : SQLiteOpenHelper(context, "agenda.db", null, 1) {
    // TABLA DE AGENDA CONTACTOS
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
                descripcionContactos TEXT
            )"""
        )
        db.execSQL(
            """CREATE TABLE NOTAS(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT, 
                descripcionNotas TEXT
            )"""

        )


    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS registros")
        onCreate(db)

        db.execSQL("DROP TABLE IF EXISTS NOTAS")
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
            put("descripcionContactos", r.descripcionContactos)
        }
        val id = db.insert("registros", null, values)
        return if (id != -1L) {
            r.id = id.toInt()
            true
        } else {
            false
        }
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
                        descripcionContactos = cursor.getString(cursor.getColumnIndexOrThrow("descripcionContactos")),
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
            put("apellido", r.apellido)
            put("direccion", r.direccion)
            put("telefono", r.telefono)
            put("edad", r.edad)
            put("sexo", r.sexo)
            put("descripcionContactos", r.descripcionContactos)
        }
        return db.update("registros", values, "id=?", arrayOf(id.toString())) > 0
    }

    // DELETE
    fun eliminar(id: Int): Boolean {
        val db = writableDatabase
        return db.delete("registros", "id=?", arrayOf(id.toString())) > 0
    }

    // TABLA AGENDA NOTAS




    fun insertarNotas(r: Notas): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("titulo", r.titulo)
            put("descripcionNotas", r.descripcionNotas)
        }
        val id = db.insert("Notas", null, values)
        return if (id != -1L) {
            r.id = id.toInt()
            true
        } else {
            false
        }
    }

    // SELECT *
    fun obtenerTodosNotas(): MutableList<Notas> {
        val lista = mutableListOf<Notas>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM NOTAS", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Notas(
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                        descripcionNotas = cursor.getString(cursor.getColumnIndexOrThrow("descripcionNotas")),
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // UPDATE
    fun actualizarNotas(id: Int, r: Notas): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("titulo", r.titulo)

            put("descripcionNotas", r.descripcionNotas)
        }
        return db.update("Notas", values, "id=?", arrayOf(id.toString())) > 0
    }

    // DELETE
    fun eliminarNotas(id: Int): Boolean {
        val db = writableDatabase
        return db.delete("Notas", "id=?", arrayOf(id.toString())) > 0
    }

}
