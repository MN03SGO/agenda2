package com.example.agenda_2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Agenda : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var contenedorGuardadosNOTAS: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)


        db = DBHelper(this)
        contenedorGuardadosNOTAS = findViewById(R.id.contenedorGuardadosNOTAS)

        val layoutAgregarNOTAS = findViewById<LinearLayout>(R.id.layoutAgregarNOTAS)
        val layoutBuscarNOTAS= findViewById<LinearLayout>(R.id.layoutBuscarNOTAS)
        val layoutGuardadosContainerNOTAS = findViewById<LinearLayout>(R.id.layoutGuardadosContainerNOTAS)


        // BTONES

        val btnMenuBuscarNOTAS = findViewById<Button>(R.id.btnMenuBuscarNOTAS)
        val btnMenuHistorialNOTAS = findViewById<Button>(R.id.btnMenuHistorialNOTAS)


        // CLASES DE LA TABLAS EDIT

        val editTituloNOTAS = findViewById<EditText>(R.id.editTituloNOTAS)

        val editDescripcionNOTAS = findViewById<EditText>(R.id.editDescripcionNOTAS)

        // BTONES GUARDAR

        val btnGuardarNOTAS = findViewById<Button>(R.id.btnGuardarNOTAS)


        val editBuscarNOTAS = findViewById<EditText>(R.id.editBuscarNOTAS)
        val textResultadosNOTAS = findViewById<TextView>(R.id.textResultadosNOTAS)
        val btnMostrarNOTAS = findViewById<Button>(R.id.btnMostrarNOTAS)

        // Menú navegación

        btnMenuBuscarNOTAS.setOnClickListener {
            layoutAgregarNOTAS.visibility = View.GONE
            layoutBuscarNOTAS.visibility = View.VISIBLE
            layoutGuardadosContainerNOTAS.visibility = View.GONE
        }
        btnMenuHistorialNOTAS.setOnClickListener {
            layoutAgregarNOTAS.visibility = View.GONE
            layoutBuscarNOTAS.visibility = View.GONE
            layoutGuardadosContainerNOTAS.visibility = View.VISIBLE
            mostrarGuardados()

                //Log.e("Agenda", "Error al mostrar historial: ${e.message}")
                //Toast.makeText(this, "Error al mostrar historial", Toast.LENGTH_SHORT).show()

        }

        // Guardar registro
        btnGuardarNOTAS.setOnClickListener {
            val nuevo = Notas(
                titulo = editTituloNOTAS.text.toString(),
                descripcionNotas = editDescripcionNOTAS.text.toString()
            )
            if (db.insertarNotas(nuevo)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos(editTituloNOTAS, editDescripcionNOTAS)
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
        }

        // Buscar registros mientras escribe
        editBuscarNOTAS.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase()
                if (query.isEmpty()) {
                    textResultadosNOTAS.text = "" // No mostrar nada si está vacío
                } else {
                    val resultados = db.obtenerTodosNotas().filter {
                        it.titulo.lowercase().contains(query) ||
                                it.descripcionNotas.lowercase().contains(query)
                    }
                    textResultadosNOTAS.text = if (resultados.isEmpty()) ""
                    else resultados.joinToString("\n\n") { r ->
                        "ID: ${r.id}\nTitulo:   ${r.titulo}\nDescripcionNotas:  ${r.descripcionNotas}"
                        // "ID: ${r.id}\nNombre: ${r.nombre}\nDirección: ${r.direccion}\nTel: ${r.telefono}\nEdad: ${r.edad}\nSexo: ${r.sexo}"
                    }
                }
            }
        })
        // Botón Mostrar todos dentro de Buscar
        btnMostrarNOTAS.setOnClickListener {
            val todos = db.obtenerTodosNotas()
            textResultadosNOTAS.text = if (todos.isEmpty()) ""
            else todos.joinToString("\n\n") { r ->
                "ID: ${r.id}\nTitulo:   ${r.titulo}\nDescripcionNotas:  ${r.descripcionNotas}"

                // "ID: ${r.id}\nNombre: ${r.nombre}\nDirección: ${r.direccion}\nTel: ${r.telefono}\nEdad: ${r.edad}\nSexo: ${r.sexo}"
            }
        }
    }

    private fun limpiarCampos(vararg campos: EditText) {
        for (campo in campos) campo.text.clear()
    }

    private fun mostrarGuardados() {
        contenedorGuardadosNOTAS.removeAllViews()
        val lista = db.obtenerTodosNotas()

        for (registro in lista) {
            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 0, 0, 16)
            }
            //Declaraciones para los listenir

            val editTituloNOTAS = EditText(this).apply { setText(registro.titulo) }
            val editDescripcionNOTAS = EditText (this).apply { setText(registro.descripcionNotas) }

            layout.addView(editTituloNOTAS)
            layout.addView(editDescripcionNOTAS)

            val botonesLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val btnActualizar = Button(this).apply {
                text = "Actualizar"
                setOnClickListener {
                    val actualizado = Notas(
                        titulo = editTituloNOTAS.text.toString(),
                        descripcionNotas = editDescripcionNOTAS.text.toString(),
                        id = registro.id
                    )
                    if (registro.id != null && db.actualizarNotas(registro.id!!, actualizado)) {
                        Toast.makeText(this@Agenda, "Registro actualizado", Toast.LENGTH_SHORT).show()
                        mostrarGuardados()
                    }
                }
            }

            val btnEliminar = Button(this).apply {
                text = "Eliminar"
                setOnClickListener {
                    if (registro.id != null && db.eliminarNotas(registro.id!!)) {
                        Toast.makeText(this@Agenda, "Registro eliminado", Toast.LENGTH_SHORT).show()
                        mostrarGuardados()
                    }
                }
            }

            botonesLayout.addView(btnActualizar)
            botonesLayout.addView(btnEliminar)
            layout.addView(botonesLayout)

            contenedorGuardadosNOTAS.addView(layout)
        }
    }
}