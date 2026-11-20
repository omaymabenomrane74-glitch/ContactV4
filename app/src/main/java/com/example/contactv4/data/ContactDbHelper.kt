package com.example.contactv4.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ContactContract.CREATE_TABLE)
        ajouterContactsDemo(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS ${ContactContract.TABLE_NAME}")
        onCreate(db)
    }

    private fun ajouterContactsDemo(db: SQLiteDatabase) {
        val contacts = listOf(
            Contact(nom = "Ben Ali", prenom = "Ahmed", telephone = "22111222"),
            Contact(nom = "Trabelsi", prenom = "Sarra", telephone = "55123456"),
            Contact(nom = "Mansour", prenom = "Youssef", telephone = "99123456"),
            Contact(nom = "Hamdi", prenom = "Amira", telephone = "20123456"),
            Contact(nom = "Jaziri", prenom = "Ali", telephone = "98123456")
        )

        contacts.forEach { contact ->
            val values = ContentValues().apply {
                put(ContactContract.COL_NOM, contact.nom)
                put(ContactContract.COL_PRENOM, contact.prenom)
                put(ContactContract.COL_TELEPHONE, contact.telephone)
            }

            db.insert(ContactContract.TABLE_NAME, null, values)
        }
    }

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1

        @Volatile
        private var INSTANCE: ContactDbHelper? = null

        fun getInstance(context: Context): ContactDbHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ContactDbHelper(
                    context.applicationContext
                ).also { INSTANCE = it }
            }
        }
    }
}