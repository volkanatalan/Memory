package net.cafepp.mydatabase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
    private var mMemories = mutableListOf<Memory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        setupSearchEditText(searchEditText)
        setupListView(resultsListView)
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_mainactivity_add -> {
            val intent = Intent(this, AddMemoryActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    override fun onBackPressed() {
        val searchText = searchEditText.text.toString()

        when (searchText) {
            "" -> super.onBackPressed()

            else -> searchEditText.setText("")
        }
    }




    private fun setupSearchEditText(editText: EditText) {
        editText.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                val adapter = resultsListView.adapter as SearchListViewAdapter

                if (searchText.length > 1) {
                    val database = MemoryDatabase(this@MainActivity, null)
                    mMemories = database.getMemories(searchText)

                    adapter.memories = mMemories
                    adapter.notifyDataSetChanged()
                }
                else {
                    adapter.memories.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }




    private fun setupListView(listView: ListView) {
        val adapter = SearchListViewAdapter(this, mMemories)
        listView.adapter = adapter

        adapter.onTagClickListener = object : SearchListViewAdapter.OnTagClickListener {
            override fun onClick(tag: String) {
                searchEditText.setText(tag)
            }
        }
    }
}
