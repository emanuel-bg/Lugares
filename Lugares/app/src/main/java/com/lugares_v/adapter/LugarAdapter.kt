package com.lugares_v.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lugares_v.databinding.LugarFilaBinding
import com.lugares_v.model.Lugar
import com.lugares_v.ui.lugar.LugarFragmentDirections

class LugarAdapter:RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    //lista para gestionar la informacion de los lugares
    private var lista = emptyList<Lugar>()

    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        :RecyclerView.ViewHolder(itemBinding.root){

            fun dibuja(lugar: Lugar){
                itemBinding.tvNombre.text=lugar.nombre
                itemBinding.tvCorreo.text=lugar.correo
                itemBinding.tvTelefono.text=lugar.telefono
                itemBinding.tvWeb.text=lugar.web
                //Mostrar Imagen
                Glide.with(itemBinding.root.context).
                load(lugar.rutaImagen).circleCrop()
                    .into(itemBinding.imagen)

                //click para modificar el lugar
                itemBinding.vistaFila.setOnClickListener{
                    val accion=LugarFragmentDirections
                        .actionNavLugarToUpdateLugarFragment(lugar)
                    itemView.findNavController().navigate(accion)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding =
            LugarFilaBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        return LugarViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
       val lugar=lista[position]
        holder.dibuja(lugar)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun setData(lugares:List<Lugar>){
        lista=lugares
        notifyDataSetChanged()

    }



}