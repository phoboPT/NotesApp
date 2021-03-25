package com.example.android.notas.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aplicacao.android.notas.R
import com.example.android.notas.entidade.Nota

class NotaAdapter : ListAdapter<Nota, NotaAdapter.NotaViewHolder>(NotaComparator()) {
    private lateinit var onItemClickListener: onItemclick
    public fun setOnItemClick(newOnItemClickListener: NotaAdapter.onItemclick) {
        onItemClickListener = newOnItemClickListener
    }

    interface onItemclick {
        fun onEditClick(position: Int)

        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        return NotaViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {

        val current = getItem(position)

        holder.bind(current.nota, current.id, current.title)


    }

    class NotaViewHolder(itemView: View, onItemclick: onItemclick) : RecyclerView.ViewHolder(itemView) {
        private val notaItemView: TextView = itemView.findViewById(R.id.textView)
        private val titleItemView: TextView = itemView.findViewById(R.id.textView2)
        val edit : LinearLayout = itemView.findViewById(R.id.btnEdit)
        val deleteItemView: Button = itemView.findViewById(R.id.btnDelete)
        val upadateItemView: Button = itemView.findViewById(R.id.btnEdit)

        var idItem: Int? = 0


        init {
            deleteItemView.setOnClickListener { v: View ->

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && idItem != null) {

                    onItemclick.onDeleteClick(idItem!!)
                }
                Toast.makeText(v.context, " deele", Toast.LENGTH_LONG).show()
            }

            upadateItemView.setOnClickListener { v: View ->
                val position = adapterPosition


                if (position != RecyclerView.NO_POSITION && idItem != null) {
                    onItemclick.onEditClick(idItem!!)
                }
                Toast.makeText(v.context, " update", Toast.LENGTH_LONG).show()
            }

        }

        fun bind(text1: String?, id: Int?, title: String) {
            notaItemView.text = text1
            titleItemView.text = title
            idItem = id
        }

        companion object {
            fun create(parent: ViewGroup, onItemClickListener: onItemclick): NotaViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recyclerview_item, parent, false)
                return NotaViewHolder(view, onItemClickListener)
            }
        }
    }

    class NotaComparator : DiffUtil.ItemCallback<Nota>() {
        override fun areItemsTheSame(oldItem: Nota, newItem: Nota): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Nota, newItem: Nota): Boolean {
            return oldItem.id == newItem.id
        }
    }
}