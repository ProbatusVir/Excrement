package com.example.excrement

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore.Files
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.FileSystem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //initialize the numbers and display
        read()
        refreshNumberDisplay()
    }

    override fun onDestroy() {
        super.onDestroy()
        save()
    }

    //counter singleton, companion objects are good places to keep private member and static variables
    companion object Counter {
        private var m_count: Int = 0
        private const val SAVE_FILE_NAME: String = "save.bin"

        operator fun invoke(): Int {
            return ++m_count
        }

        fun count(): Int {
            return m_count
        }

        fun count(count: Int)
        {
            m_count = count
        }

    }

    private fun refreshNumberDisplay() {
        //Access the TextView item by its XML ID, pretty neat, not sure if this works at runtime...
        val tv: TextView = findViewById(R.id.num_display)
        tv.text = String.format(Counter.count().toString())
    }

    //These are abstractions for button interactions
    fun increment(v: View) {
        Counter()
        refreshNumberDisplay()
    }
    fun save(v: View) {
        save()
    }

    private fun save() {
        val context = applicationContext
        val file : File = File(context.filesDir, SAVE_FILE_NAME)

        if (file.exists())
            file.writeText(Counter.count().toString())
    }

    private fun read()
    {
        val context = applicationContext
        val file : File = File(context.filesDir, SAVE_FILE_NAME)

        if (file.exists())
            Counter.count(file.readText().toInt())
    }

    fun reset(v: View) {
        Counter.count(0)
        save()
        refreshNumberDisplay()
    }
}