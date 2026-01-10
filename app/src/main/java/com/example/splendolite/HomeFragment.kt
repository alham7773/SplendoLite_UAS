package com.example.splendolite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var rvTugas: RecyclerView
    private lateinit var adapter: TugasAdapter
    private lateinit var list: ArrayList<Tugas>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Cari ID rvTugas (BUKAN listViewTugas lagi)
        rvTugas = view.findViewById(R.id.rvTugas)
        rvTugas.layoutManager = LinearLayoutManager(requireContext())

        list = arrayListOf()
        adapter = TugasAdapter(list)
        rvTugas.adapter = adapter

        FirebaseDatabase.getInstance().getReference("tugas_uas")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (data in snapshot.children) {
                        val tugas = data.getValue(Tugas::class.java)
                        if (tugas != null) list.add(tugas)
                    }
                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        return view
    }
}