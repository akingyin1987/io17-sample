package io.dwak.kotlinsample.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import io.dwak.kotlinsample.R
import javax.inject.Inject

class MainActivity : BaseActivity() {

  @Inject lateinit var noteViewModel: NoteViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main2)
    setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

    val noteList = findViewById<RecyclerView>(R.id.note_list).apply {
      layoutManager = LinearLayoutManager(this@MainActivity)
    }
    val adapter = NoteItemViewHolderAdapter()
        .apply { setOnItemClickListener { toast(it.title) } }
        .also { noteList.adapter = it }

    noteViewModel.notes()
        .observe(this, Observer {
          adapter.items = it
        })

    findViewById<FloatingActionButton>(R.id.fab).apply {
      setOnClickListener {
        showInputDialog()
      }
    }
  }

  private fun showInputDialog() {
    val layoutInflater = LayoutInflater.from(this)
    val promptView = layoutInflater.inflate(R.layout.dialog_note_entry, null, false)
    val titleText = promptView.findViewById<EditText>(R.id.title_input)
    val contents = promptView.findViewById<EditText>(R.id.contents_input)
    AlertDialog.Builder(this)
        .setView(promptView)
        .setPositiveButton("Save", { _, _ ->
          noteViewModel.addNote("${titleText.text}", "${contents.text}")
        })
        .setNegativeButton("Cancel", null)
        .create()
        .show()
  }

}

fun Activity.toast(message: String) {
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
