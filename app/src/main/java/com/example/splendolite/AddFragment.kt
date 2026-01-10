package com.example.splendolite

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddFragment : Fragment() {

    private var taskId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        val etNama = view.findViewById<EditText>(R.id.etNamaTugas)
        val etDesc = view.findViewById<EditText>(R.id.etDeskripsi)
        val etTanggal = view.findViewById<EditText>(R.id.etTanggal)
        val etJam = view.findViewById<EditText>(R.id.etJam)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpan)
        val tvHeader = view.findViewById<TextView>(R.id.tvHeaderAdd)

        // --- BAGIAN MASALAH TADI ---
        arguments?.let {
            taskId = it.getString("id")
            // DISESUAIKAN: Pakai "namaTugas" dan "deskripsi" agar sinkron dengan Adapter & Model
            etNama.setText(it.getString("namaTugas"))
            etDesc.setText(it.getString("deskripsi"))
            etTanggal.setText(it.getString("tanggal"))
            etJam.setText(it.getString("jam"))

            btnSimpan.text = "UPDATE DATA"
            tvHeader?.text = "Update Tugas"
        }

        etTanggal.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                val formatTgl = String.format("%02d/%02d/%d", day, month + 1, year)
                etTanggal.setText(formatTgl)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        etJam.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                etJam.setText(String.format("%02d:%02d", hour, minute))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        btnSimpan.setOnClickListener {
            val namaInput = etNama.text.toString().trim()
            val descInput = etDesc.text.toString().trim()
            val tglInput = etTanggal.text.toString().trim()
            val jamInput = etJam.text.toString().trim()

            if (namaInput.isEmpty()) {
                etNama.error = "Judul tugas tidak boleh kosong!"
                return@setOnClickListener
            }

            val dbRef = FirebaseDatabase.getInstance().getReference("tugas_uas")
            val id = taskId ?: dbRef.push().key ?: ""

            // DISESUAIKAN: Mengikuti parameter data class Tugas kamu
            val dataTugas = Tugas(id, namaInput, descInput, tglInput, jamInput)

            dbRef.child(id).setValue(dataTugas).addOnCompleteListener { task ->
                if (task.isSuccessful && isAdded) {
                    if (tglInput.isNotEmpty() && jamInput.isNotEmpty()) {
                        setAlarm(namaInput, descInput, tglInput, jamInput)
                    }
                    val pesan = if (taskId == null) "Tugas Berhasil Ditambahkan!" else "Tugas Berhasil Diperbarui!"
                    Toast.makeText(requireContext(), pesan, Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
        return view
    }

    private fun setAlarm(judul: String, pesan: String, tanggal: String, jam: String) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intentIzin = Intent().apply {
                    action = android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                }
                startActivity(intentIzin)
                return
            }
        }

        val intent = Intent(requireContext(), NotificationReceiver::class.java).apply {
            putExtra("title", "Ingat Tugas: $judul")
            putExtra("desc", pesan)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()
        val tglSplit = tanggal.split("/")
        val jamSplit = jam.split(":")

        if (tglSplit.size == 3 && jamSplit.size == 2) {
            calendar.set(Calendar.DAY_OF_MONTH, tglSplit[0].toInt())
            calendar.set(Calendar.MONTH, tglSplit[1].toInt() - 1)
            calendar.set(Calendar.YEAR, tglSplit[2].toInt())
            calendar.set(Calendar.HOUR_OF_DAY, jamSplit[0].toInt())
            calendar.set(Calendar.MINUTE, jamSplit[1].toInt())
            calendar.set(Calendar.SECOND, 0)

            if (calendar.timeInMillis > System.currentTimeMillis()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }
}