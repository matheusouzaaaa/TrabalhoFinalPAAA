package com.example.projetofinalpaaa

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.projetofinalpaaa.model.Animal
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    fun cadastrarAnimal(view: View?) {
        val textoNomeCadastro = findViewById(R.id.textoNomeCadastro) as EditText
        val textoIdadeCadastro = findViewById(R.id.textoIdadeCadastro) as EditText
        val textoTipoCadastro = findViewById(R.id.textoTipoCadastro) as EditText

        val nome = textoNomeCadastro.text.toString()
        val idade = textoIdadeCadastro.text.toString().toInt()
        val tipo = textoTipoCadastro.text.toString()
        val animal = Animal(nome, idade, tipo)
        val it = Intent().apply {
            putExtra("animal", animal)
        }
        setResult(Activity.RESULT_OK, it)

        finish()
    }

    fun cancelarCadastro(view: View?) {
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