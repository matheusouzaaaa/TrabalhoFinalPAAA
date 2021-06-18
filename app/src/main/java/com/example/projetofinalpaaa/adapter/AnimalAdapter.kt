package com.example.projetofinalpaaa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetofinalpaaa.R
import com.example.projetofinalpaaa.model.Animal

class AnimalAdapter (private var listaAnimais:ArrayList<Animal>) : RecyclerView.Adapter<AnimalAdapter.MyViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textoNome: TextView
        var textoIdade: TextView
        var textoTipo: TextView

        init {
            textoNome = view.findViewById(R.id.textoNome)
            textoIdade = view.findViewById(R.id.textoIdade)
            textoTipo = view.findViewById(R.id.textoTipo)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AnimalAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_animal, parent, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textoNome.text = listaAnimais.get(position).nome
        holder.textoIdade.text = listaAnimais.get(position).idade.toString()
        holder.textoTipo.text = listaAnimais.get(position).tipo
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClicked(
                holder.itemView,
                position
            )
        }
    }

    override fun getItemCount() = listaAnimais.size

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}