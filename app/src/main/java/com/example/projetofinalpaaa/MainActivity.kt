package com.example.projetofinalpaaa

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetofinalpaaa.adapter.AnimalAdapter
import com.example.projetofinalpaaa.databinding.ActivityMainBinding
import com.example.projetofinalpaaa.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), AnimalAdapter.OnItemClickListener {
    private val REQ_CADASTRO = 1;
    private val REQ_DETALHE = 2;
    private var listaAnimais: ArrayList<Animal> = ArrayList()
    private var posicaoAlterar = -1

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AnimalAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val db = Firebase.firestore

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        viewManager = LinearLayoutManager(this)
        viewAdapter = AnimalAdapter(listaAnimais)
        viewAdapter.onItemClickListener = this

        listar()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
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

    override fun onItemClicked(view: View, position: Int) {
        val it = Intent(this, DetalheActivity::class.java)
        this.posicaoAlterar = position
        val animal = listaAnimais.get(position)
        it.putExtra("animal", animal)
        startActivityForResult(it, REQ_DETALHE)
    }

    fun abrirFormulario(view: View) {
        val it = Intent(this, CadastroActivity::class.java)
        startActivityForResult(it, REQ_CADASTRO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CADASTRO) {
            if (resultCode == Activity.RESULT_OK) {
                val animal = data?.getSerializableExtra("animal") as Animal

                // Add a new document with a generated ID
                db.collection("animais")
                    .add(animal)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "DocumentSnapshot added")
                    }
                    .addOnFailureListener { e ->
                        Log.w(ContentValues.TAG, "Error adding document", e)
                    }

                viewAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == REQ_DETALHE) {
            if (resultCode == DetalheActivity.RESULT_EDIT) {
                val animal = data?.getSerializableExtra("animal") as Animal
                listaAnimais.set(this.posicaoAlterar, animal)

                // atualizar no banco firestore

                db.collection("animais").document(animal.key.toString())
                    .update("idade",animal.idade?.toInt(), "nome", animal.nome.toString(), "tipo" ,animal.tipo.toString()
                    )
                    .addOnSuccessListener { document ->
                        Toast.makeText(this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show()
                        Log.d(ContentValues.TAG, "DocumentSnapshot added")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
                        Log.w("Firebase", "Error adidin documen", e)
                    }
                viewAdapter.notifyDataSetChanged()
            } else if (resultCode == DetalheActivity.RESULT_DELETE) {
                val animal = data?.getSerializableExtra("animal") as Animal
                listaAnimais.removeAt(this.posicaoAlterar)

                db.collection("animais").document(animal.key.toString())
                    .delete()
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Deletado com sucesso", Toast.LENGTH_SHORT).show()
                        Log.d(ContentValues.TAG, "Registro deletado com sucesso!")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
                        Log.w("Firebase", "Error deleting document", e)
                    }

                viewAdapter.notifyDataSetChanged()
            }
        }
    }

    fun listar(){
        // listar do firebase
        db.collection("animais").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.e("Firestore Error", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){

                        var animal = Animal(
                            dc.document.toObject(Animal::class.java).nome,
                            dc.document.toObject(Animal::class.java).idade,
                            dc.document.toObject(Animal::class.java).tipo,
                            dc.document.id)
                        listaAnimais.add(animal)
                    }
                }

                viewAdapter.notifyDataSetChanged()
            }

        })
    }
}