package com.example.ugd11_d_190710362.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.ugd11_d_190710362.AddEditActivity
import com.example.ugd11_d_190710362.MainActivity
import com.example.ugd11_d_190710362.R
import com.example.ugd11_d_190710362.models.Mobil
import java.util.*
import kotlin.collections.ArrayList

class MobilAdapter (private var mobilList: List<Mobil>, context: Context) :
    RecyclerView.Adapter<MobilAdapter.ViewHolder>(),Filterable {

    private var filteredMobilList: MutableList<Mobil>
    private val context: Context

    init {
        filteredMobilList = ArrayList(mobilList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_mobil, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredMobilList.size
    }

    fun setMobilList(mobilList: Array<Mobil>) {
        this.mobilList = mobilList.toList()
        filteredMobilList = mobilList.toMutableList()
    }

    override fun onBIndViewHolder(holder: ViewHolder,position: Int) {
        val mobil = filteredMobilList[position]
        holder.tvNama.text = mobil.nama
        holder.tvHarga.text = mobil.harga

        holder.btnDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data anda ini?")
                .setNegativeButton("Batal",null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is MainActivity) mobil.id?.let { it1 ->
                        context.deleteMobil(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvMobil.setOnClickListener {
            val i = Intent(context, AddEditActivity::class.java)
            i.putExtra("id", mobil.id)
            if (context is MainActivity)
                context.startActivityForResult(i, MainActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Mobil> = java.util.ArrayList()
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(mobilList)
                } else {
                    for (mobil in mobilList) {
                        if (mobil.nama.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(mobil)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredMobilList.clear()
                filteredMobilList.addAll((filterResults.values as List<Mobil>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView
        var tvHarga: TextView
        var btnDelete: ImageButton
        var cvMobil: CardView

        init {
            tvNama = itemView.findViewById(R.id.tv_nama)
            tvHarga = itemView.findViewById(R.id.tv_harga)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvMobil = itemView.findViewById(R.id.cv_mobil)
        }
    }
}