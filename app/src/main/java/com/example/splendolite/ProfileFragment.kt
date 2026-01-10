package com.example.splendolite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvUserEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        // Tampilkan email user yang sedang login agar lebih keren
        val user = FirebaseAuth.getInstance().currentUser
        tvUserEmail?.text = user?.email ?: "Alham - UAS 2026"

        btnLogout?.setOnClickListener {
            // Memanggil fungsi logout resmi di MainActivity
            (activity as? MainActivity)?.performLogout()
        }

        return view
    }
}