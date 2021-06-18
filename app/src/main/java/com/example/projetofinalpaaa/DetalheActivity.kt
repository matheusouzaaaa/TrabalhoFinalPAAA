package com.example.projetofinalpaaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.projetofinalpaaa.model.Animal
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DetalheActivity : AppCompatActivity() {
    companion object {

        const val RESULT_EDIT = 1
        const val RESULT_DELETE = 2
    }

    private lateinit var textoNomeDetalhe: EditText
    private lateinit var textoIdadeDetalhe: EditText
    private lateinit var textoTipoDetalhe: EditText
    private lateinit var keyFirebase: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val intent = intent
        val animal = intent.getSerializableExtra("animal") as Animal
        textoNomeDetalhe = findViewById<EditText>(R.id.textoNomeDetalhe).apply {
            setText(animal.nome)
        }
        textoIdadeDetalhe = findViewById<EditText>(R.id.textoIdadeDetalhe).apply {
            setText(animal.idade.toString())
        }
        textoTipoDetalhe = findViewById<EditText>(R.id.textoTipoDetalhe).apply {
            setText(animal.tipo)
        }
        keyFirebase = findViewById<TextView>(R.id.key).apply {
            text = animal.key.toString()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    fun editarAnimal(v: View?) {
        val animal = Animal(
            textoNomeDetalhe.text.toString(),
            textoIdadeDetalhe.text.toString().toInt(),
            textoTipoDetalhe.text.toString(),
            keyFirebase.text.toString()
        )
        val data = Intent()
        data.putExtra("animal", animal)
        setResult(RESULT_EDIT, data)
        finish()
    }

    fun excluirAnimal(v: View?) {
        val animal = Animal(
            textoNomeDetalhe.text.toString(),
            textoIdadeDetalhe.text.toString().toInt(),
            textoTipoDetalhe.text.toString(),
            keyFirebase.text.toString()
        )
        val data = Intent()
        data.putExtra("animal", animal)
        setResult(RESULT_DELETE, data)
        finish()
    }

    fun voltar(v: View?) {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout -> {
            logout()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        Firebase.auth.signOut()
        val intent = Intent (this, LoginActivity::class.java)
        this.startActivity(intent)
    }
}