package com.example.addressbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.sqlite.DBHelper
import java.io.EOFException
import java.util.*

@Suppress("DEPRECATION")

class MainActivity : AppCompatActivity() {
    private lateinit var searchText: SearchView
    private lateinit var buttonAdd: Button
    private lateinit var listText: RecyclerView

    private lateinit var adapter: RecyclerAdapter

    companion object {
        const val EXTRA_KEY = "EXTRA"
    }
    private val request1 = 1
    private val request2 = 3

    private val dbHelper = DBHelper(this)
    private lateinit var list: MutableList<address>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchText = findViewById<SearchView>(R.id.searchText)
        buttonAdd = findViewById<Button>(R.id.buttonAdd)
        listText = findViewById<RecyclerView>(R.id.recyclerView)

        changeList()

        buttonAdd.setOnClickListener {
            val intent = Intent(this, Book_edit::class.java)
            intent.putExtra(EXTRA_KEY, (-1).toString())
            startActivityForResult(intent, request1)
        }

        searchText.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString().replace(" ",""))
                return false
            }
        }
        )
    }    fun filter(text: String) = if(text == ""){
        changeList()

    }else {
        list = (dbHelper.getContacts().filter { (it.name.toLowerCase() + it.surname.toLowerCase()).contains(text.toLowerCase())}).toMutableList()
        changeListFiltered(list)
    }

    private fun changeListFiltered(filters: MutableList<address>){
        adapter = RecyclerAdapter(filters) {
            if(it != -1) {
                val intent = Intent(this, Book1::class.java)
                intent.putExtra(EXTRA_KEY, it.toString())
                startActivityForResult(intent, request2)
            }

        }
        adapter.notifyItemInserted(filters.lastIndex)
        listText.layoutManager = LinearLayoutManager(this)
        listText.adapter = adapter
    }

    private fun changeList(){
        list = dbHelper.getContacts()
        adapter = RecyclerAdapter(list) {
            if(it != -1) {
                val intent = Intent(this, Book1::class.java)
                intent.putExtra(EXTRA_KEY, it.toString())
                startActivityForResult(intent, request2)
            }

        }
        adapter.notifyItemInserted(list.lastIndex)
        listText.layoutManager = LinearLayoutManager(this)
        listText.adapter = adapter
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        changeList()
    }
}