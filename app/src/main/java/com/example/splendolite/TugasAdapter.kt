package com.example.splendolite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class TugasAdapter(private val listTugas: ArrayList<Tugas>) :
    RecyclerView.Adapter<TugasAdapter.TugasViewHolder>() {

    class TugasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama = view.findViewById<TextView>(R.id.tvNamaTugasRow)
        val desc = view.findViewById<TextView>(R.id.tvDescTugasRow)
        val deadline = view.findViewById<TextView>(R.id.tvDeadlineRow)
        val btnHapus = view.findViewById<Button>(R.id.btnHapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TugasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tugas, parent, false)
        return TugasViewHolder(view)
    }

    override fun onBindViewHolder(holder: TugasViewHolder, position: Int) {
        val item = listTugas[position]

        // Menampilkan data ke UI
        holder.nama.text = item.namaTugas
        holder.desc.text = item.deskripsi

        // Menampilkan Waktu Deadline secara Dinamis
        if (!item.tanggal.isNullOrEmpty() && !item.jam.isNullOrEmpty()) {
            holder.deadline.text = "Deadline: ${item.tanggal} - ${item.jam}"
        } else {
            holder.deadline.text = "Waktu: Belum diatur"
        }

        // --- FITUR EDIT (Pindah ke AddFragment dengan Data) ---
        holder.itemView.setOnClickListener {
            val fragment = AddFragment()
            val bundle = Bundle()

            // KEY DI SINI HARUS SAMA DENGAN it.getString DI ADDFRAGMENT
            bundle.putString("id", item.id)
            bundle.putString("namaTugas", item.namaTugas)
            bundle.putString("deskripsi", item.deskripsi)
            bundle.putString("tanggal", item.tanggal)
            bundle.putString("jam", item.jam)

            fragment.arguments = bundle

            val activity = it.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // --- FITUR DELETE (CRUD Firebase) ---
        holder.btnHapus.setOnClickListener {
            if (item.id != null) {
                val dbRef = FirebaseDatabase.getInstance().getReference("tugas_uas").child(item.id)
                dbRef.removeValue().addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = listTugas.size
}