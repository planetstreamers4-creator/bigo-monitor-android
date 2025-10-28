package com.example.bigostreammonitor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bigostreammonitor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StreamerViewModel
    private lateinit var adapter: StreamerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[StreamerViewModel::class.java]

        adapter = StreamerAdapter { streamer ->
            showOptions(streamer)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allStreamers.observe(this) { streamers ->
            adapter.submitList(streamers)
        }

        binding.btnAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_streamer, null)
        val etId = dialogView.findViewById<EditText>(R.id.etBigoId)
        val etName = dialogView.findViewById<EditText>(R.id.etName)

        AlertDialog.Builder(this)
            .setTitle("Adicionar Streamer")
            .setView(dialogView)
            .setPositiveButton("Adicionar") { _, _ ->
                val id = etId.text.toString().trim()
                if (id.isNotEmpty()) {
                    viewModel.insert(Streamer(bigoId = id, name = etName.text.toString().trim()))
                    Toast.makeText(this, "Adicionado!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showOptions(streamer: Streamer) {
        AlertDialog.Builder(this)
            .setTitle(streamer.name ?: streamer.bigoId)
            .setItems(arrayOf("Remover", "Toggle Monitor")) { _, which ->
                when (which) {
                    0 -> viewModel.delete(streamer)
                    1 -> viewModel.toggleMonitoring(streamer.id, !streamer.isMonitoring)
                }
            }
            .show()
    }
}
