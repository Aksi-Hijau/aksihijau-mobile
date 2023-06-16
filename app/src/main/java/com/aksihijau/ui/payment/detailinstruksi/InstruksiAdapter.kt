package com.aksihijau.ui.payment.detailinstruksi

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aksihijau.R
import com.aksihijau.api.InstructionsItem
import com.aksihijau.databinding.ItemExpandableInstruksiBinding

class InstruksiAdapter(private val instruksi : List<InstructionsItem>) : RecyclerView.Adapter<InstruksiAdapter.ListViewHolder>(){


    class ListViewHolder(binding: ItemExpandableInstruksiBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.titleInstruksi
        val body: TextView = binding.tvDetailInstruksi
        val btnExpand : Button = binding.btnDrop
        val expandLayout = binding.expandLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemExpandableInstruksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = instruksi.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val name= instruksi[position].title
        val body = instruksi[position].body
        holder.tvName.text = name
        holder.body.text = Html.fromHtml(body)

        holder.btnExpand.setOnClickListener {
            if(holder.expandLayout.visibility == View.GONE){
                holder.expandLayout.visibility = View.VISIBLE
                holder.btnExpand.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.baseline_arrow_drop_up_24,0,0)
            }else {
                holder.btnExpand.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.baseline_arrow_drop_down_24,0,0)
                holder.expandLayout.visibility = View.GONE
            }
        }



    }


}