package com.example.addressbook

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlite.DBHelper


@Suppress("DEPRECATION")

class Book1 : AppCompatActivity() {

    private lateinit var buttonBack: Button
    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button
    private lateinit var name: TextView
    private lateinit var surname: TextView
    private lateinit var birthData: TextView
    private lateinit var phone: TextView

    private val dbHelper = DBHelper(this)

    private val request_code = 2

    private lateinit var idGlobal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book1)
        val id = intent.getStringExtra(MainActivity.EXTRA_KEY)
        idGlobal = id.toString()

        name = this.findViewById<TextView>(/* id = */ R.id.name)
        surname = this.findViewById<TextView>(/* id = */ R.id.surname)
        birthData = findViewById<TextView>(R.id.birthData)
        phone = findViewById<TextView>(R.id.phoneNumber)

        buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonDelete = findViewById<Button>(R.id.buttonDelete)
        buttonEdit = findViewById<Button>(R.id.buttonEdit)

        val listContacts = dbHelper.getContacts()
        for (contact in listContacts) {
            if (contact.id.toString() == id) {
                name.text = contact.name
                surname.text = contact.surname
                birthData.text = contact.birthData
                phone.text = contact.phoneNumber
                break
            }

        }

        buttonBack.setOnClickListener {
            finishActivity()
        }
        buttonDelete.setOnClickListener {
            val listId = id.toString().toInt()
            DialogYesOrNo(
                this,
                "Удалить номер?",
                " "
            ) { dialog, id ->
                dbHelper.removeContact(listId)
                finishActivity()

            }

        }
        buttonEdit.setOnClickListener {
            val intent = Intent(this, Book_edit::class.java)
            intent.putExtra(MainActivity.EXTRA_KEY, id.toString())
            startActivityForResult(intent, request_code)
        }
    }

    fun DialogYesOrNo(
        activity: Activity,
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(activity)
        builder.setPositiveButton("Да"
        ) { dialog, id ->
            dialog.dismiss()
            listener.onClick(dialog, id)
        }

        builder.setNegativeButton("Нет", null)
        val alert = builder.create()
        alert.setTitle(title)
        alert.setMessage(message)
        alert.show()
    }

    private fun finishActivity() {
        val returnIntent = Intent()
        returnIntent.putExtra(Book_edit.RESULT_KEY, "1")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == request_code && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(Book_edit.RESULT_KEY)
            val listContacts = dbHelper.getContacts()
            for (contact in listContacts) {
                if (contact.id.toString() == idGlobal) {
                    name.text = contact.name
                    surname.text = contact.surname
                    birthData.text = contact.birthData
                    phone.text = contact.phoneNumber
                    break
                }
            }

        }
    }
}