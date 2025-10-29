package com.vasensio.listilla

import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    //MODEL
    class Record(var intents: Int, var nom: String)
    var records: ArrayList<Record> = ArrayList<Record>()

    //ArrayAdapter
    var adapter: ArrayAdapter<Record>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        records.add(Record(33, "Manolo"))
        records.add(Record(12, "Pepe"))
        records.add(Record(42, "Laura"))

        adapter = object : ArrayAdapter<Record>(this,R.layout.list_item,records)
        {
            override fun getView(pos: Int, convertView: View?, container: ViewGroup): View {
                var convertView = convertView
                if (convertView == null) {
                    // inicialitzem l'element la View amb el seu layout
                    convertView = getLayoutInflater().inflate(R.layout.list_item, container, false)
                }
                // pintem imatge
                val bitmap = BitmapFactory.decodeStream(assets.open("homer.jpeg"))
                convertView.findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
                // "Pintem" valors (quan es refresca)
                convertView.findViewById<TextView>(R.id.nom).text = getItem(pos)?.nom
                convertView.findViewById<TextView>(R.id.intents).text =
                    getItem(pos)?.intents.toString()
                return convertView
            }
        }

        val lv = findViewById<ListView>(R.id.recordsView)
        lv.setAdapter(adapter)

        // Listener del botón para mostrar el diálogo
        findViewById<Button>(R.id.button).setOnClickListener {
            mostrarDialogAñadir()
        }
    }

    private fun mostrarDialogAñadir() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_record)
        dialog.setCancelable(true)

        val inputNom = dialog.findViewById<EditText>(R.id.inputNom)
        val inputIntents = dialog.findViewById<EditText>(R.id.inputIntents)
        val btnGuardar = dialog.findViewById<Button>(R.id.btnGuardar)
        val btnCancelar = dialog.findViewById<Button>(R.id.btnCancelar)

        btnGuardar.setOnClickListener {
            val nom = inputNom.text.toString()
            val intentsText = inputIntents.text.toString()

            if (nom.isNotEmpty() && intentsText.isNotEmpty()) {
                val intents = intentsText.toIntOrNull()
                if (intents != null) {
                    records.add(Record(intents, nom))
                    adapter?.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
