package com.example.agenda_2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.getbase.floatingactionbutton.FloatingActionButton
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var contenedorGuardados: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)
        contenedorGuardados = findViewById(R.id.contenedorGuardados)

        val layoutAgregar = findViewById<LinearLayout>(R.id.layoutAgregar)
        val layoutBuscar = findViewById<LinearLayout>(R.id.layoutBuscar)
        val layoutGuardadosContainer = findViewById<LinearLayout>(R.id.layoutGuardadosContainer)



        // CONEXION A BTNS FLOTANTES
        val fabMenu = findViewById<FloatingActionsMenu>(R.id.flotante_INI)
        val fabNotas = findViewById<FloatingActionButton>(R.id.sub_texto)
        val fabContacto = findViewById<FloatingActionButton>(R.id.contacto)






        // BTONES

        val btnMenuBuscar = findViewById<Button>(R.id.btnMenuBuscar)
        val btnMenuHistorial = findViewById<Button>(R.id.btnMenuHistorial)


        // CLASES DE LA TABLAS EDIT

        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editApellido = findViewById<EditText>(R.id.editApellido)
        val editDireccion = findViewById<EditText>(R.id.editDireccion)
        val editTelefono =  findViewById<EditText>(R.id.editTelefono)
        val editEdad = findViewById<EditText>(R.id.editEdad)
        val editSexo = findViewById<EditText>(R.id.editSexo)
        val editDescripcionContactos = findViewById<EditText>(R.id.editDescripcionContactos)





        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        val editBuscar = findViewById<EditText>(R.id.editBuscar)
        val textResultados = findViewById<TextView>(R.id.textResultados)
        val btnMostrarTodos = findViewById<Button>(R.id.btnMostrarTodos)


        // CONEXION BTN

        fabNotas.setOnClickListener {
            val intent = Intent(this, Agenda::class.java)
            startActivity(intent)
        }

        // Abrir ActivityMain (o el que corresponda) cuando presionen "Contacto"
        fabContacto.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // O la activity de contactos
            startActivity(intent)
        }














        // Menú navegación

        btnMenuBuscar.setOnClickListener {
            layoutAgregar.visibility = View.GONE
            layoutBuscar.visibility = View.VISIBLE
            layoutGuardadosContainer.visibility = View.GONE
        }
        btnMenuHistorial.setOnClickListener {
            layoutAgregar.visibility = View.GONE
            layoutBuscar.visibility = View.GONE
            layoutGuardadosContainer.visibility = View.VISIBLE
            mostrarGuardados()
        }

        // Guardar registro
        btnGuardar.setOnClickListener {
            val nuevo = Registro(
                nombre = editNombre.text.toString(),
                apellido = editApellido.text.toString(),
                direccion = editDireccion.text.toString(),
                telefono = editTelefono.text.toString(),
                edad = editEdad.text.toString(),
                sexo = editSexo.text.toString(),
                descripcionContactos = editDescripcionContactos.text.toString()
            )
            if (db.insertar(nuevo)) {
                Toast.makeText(this, "Guardado con éxito", Toast.LENGTH_SHORT).show()
                limpiarCampos(editNombre, editApellido ,editDireccion, editTelefono, editEdad, editSexo, editDescripcionContactos)
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
        }

        // Buscar registros mientras escribe
        editBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase()
                if (query.isEmpty()) {
                    textResultados.text = "" // No mostrar nada si está vacío
                } else {
                    val resultados = db.obtenerTodos().filter {
                        it.nombre.lowercase().contains(query) ||
                                it.apellido.lowercase().contains(query) ||
                                it.direccion.lowercase().contains(query) ||
                                it.telefono.lowercase().contains(query) ||
                                it.edad.lowercase().contains(query) ||
                                it.sexo.lowercase().contains(query)||
                                it.descripcionContactos.lowercase().contains(query)
                    }

                    textResultados.text = if (resultados.isEmpty()) ""
                    else resultados.joinToString("\n\n") { r ->
                        "ID: ${r.id}\nNombre: ${r.nombre}\nApellido: ${r.apellido}\nDireccion: ${r.direccion}\nTelefono: " +
                                "${r.telefono}\nEdad: ${r.telefono}\nEdad: ${r.edad}\nSexo: ${r.sexo}\nDescripcionContactos: ${r.descripcionContactos}"
                        // "ID: ${r.id}\nNombre: ${r.nombre}\nDirección: ${r.direccion}\nTel: ${r.telefono}\nEdad: ${r.edad}\nSexo: ${r.sexo}"
                    }
                }
            }
        })

        // Botón Mostrar todos dentro de Buscar
        btnMostrarTodos.setOnClickListener {
            val todos = db.obtenerTodos()
            textResultados.text = if (todos.isEmpty()) ""
            else todos.joinToString("\n\n") { r ->
                "ID: ${r.id}\nNombre: ${r.nombre}\nApellido: ${r.apellido}\nDireccion: ${r.direccion}\nTelefono: " +
                        "${r.telefono}\nEdad: ${r.telefono}\nEdad: ${r.edad}\nSexo: ${r.sexo}\nDescripcionContactos: ${r.descripcionContactos}"

                // "ID: ${r.id}\nNombre: ${r.nombre}\nDirección: ${r.direccion}\nTel: ${r.telefono}\nEdad: ${r.edad}\nSexo: ${r.sexo}"
            }
        }
    }

    private fun limpiarCampos(vararg campos: EditText) {
        for (campo in campos) campo.text.clear()
    }

    private fun mostrarGuardados() {
        contenedorGuardados.removeAllViews()
        val lista = db.obtenerTodos()

        for (registro in lista) {
            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 0, 0, 16)
            }

            //Declaraciones para los listenir

            val editNombre = EditText(this).apply { setText(registro.nombre) }
            val editApellido = EditText(this).apply { setText(registro.apellido) }
            val editDireccion = EditText(this).apply { setText(registro.direccion) }
            val editTelefono = EditText(this).apply { setText(registro.telefono) }
            val editEdad = EditText(this).apply { setText(registro.edad) }
            val editSexo = EditText(this).apply { setText(registro.sexo) }
            val descripcionContactos = EditText (this).apply { setText(registro.descripcionContactos) }

            layout.addView(editNombre)
            layout.addView(editApellido)
            layout.addView(editDireccion)
            layout.addView(editTelefono)
            layout.addView(editEdad)
            layout.addView(editSexo)
            layout.addView(descripcionContactos)

            val botonesLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val btnActualizar = Button(this).apply {
                text = "Actualizar"
                setOnClickListener {
                    val actualizado = Registro(
                        nombre = editNombre.text.toString(),
                        apellido = editApellido.text.toString(),
                        direccion = editDireccion.text.toString(),
                        telefono = editTelefono.text.toString(),
                        edad = editEdad.text.toString(),
                        sexo = editSexo.text.toString(),
                        descripcionContactos = descripcionContactos.text.toString(),
                        id = registro.id
                    )
                    if (registro.id != null && db.actualizar(registro.id!!, actualizado)) {
                        Toast.makeText(this@MainActivity, "Registro actualizado", Toast.LENGTH_SHORT).show()
                        mostrarGuardados()
                    }
                }
            }

            val btnEliminar = Button(this).apply {
                text = "Eliminar"
                setOnClickListener {
                    if (registro.id != null && db.eliminar(registro.id!!)) {
                        Toast.makeText(this@MainActivity, "Registro eliminado", Toast.LENGTH_SHORT).show()
                        mostrarGuardados()
                    }
                }
            }

            botonesLayout.addView(btnActualizar)
            botonesLayout.addView(btnEliminar)
            layout.addView(botonesLayout)

            contenedorGuardados.addView(layout)
        }
    }
}