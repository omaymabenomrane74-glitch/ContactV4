package com.example.contactv4.data
import android.content.ContentValues
import android.database.Cursor
class ContactDao(private val helper: ContactDbHelper) {
    // Ajouter un contact
    fun insert(contact: Contact): Long {

        val db = helper.writableDatabase
        val values = contactToValues(contact)

        return db.insert(
            ContactContract.TABLE_NAME,
            null,
            values
        )
    }

    // Modifier un contact
    fun update(contact: Contact): Int {

        val db = helper.writableDatabase
        val values = contactToValues(contact)

        return db.update(
            ContactContract.TABLE_NAME,
            values,
            "${ContactContract.COL_ID} = ?",
            arrayOf(contact.id.toString())
        )
    }

    // Supprimer un contact
    fun delete(id: Long): Int {

        val db = helper.writableDatabase
        return db.delete(
            ContactContract.TABLE_NAME,
            "${ContactContract.COL_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // Afficher tous les contacts
    fun getAll(): List<Contact> {

        val db = helper.readableDatabase
        val cursor = db.query(
            ContactContract.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${ContactContract.COL_NOM} COLLATE NOCASE ASC"
        )
        return cursorToList(cursor)
    }
    // Récupérer un contact par ID
    fun getById(id: Long): Contact? {

        val db = helper.readableDatabase

        val cursor = db.query(
            ContactContract.TABLE_NAME,
            null,
            "${ContactContract.COL_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                cursorToContact(it)
            } else {
                null
            }
        }
    }

    // Rechercher un contact
    fun search(texte: String): List<Contact> {

        val db = helper.readableDatabase
        val recherche = "%$texte%"

        val cursor = db.query(
            ContactContract.TABLE_NAME,
            null,
            """
                ${ContactContract.COL_NOM} LIKE ? OR
                ${ContactContract.COL_PRENOM} LIKE ? OR
                ${ContactContract.COL_TELEPHONE} LIKE ?
            """.trimIndent(),
            arrayOf(recherche, recherche, recherche),
            null,
            null,
            "${ContactContract.COL_NOM} COLLATE NOCASE ASC"
        )

        return cursorToList(cursor)
    }

    // Transformer un contact en ContentValues
    private fun contactToValues(contact: Contact): ContentValues {

        return ContentValues().apply {

            put(ContactContract.COL_NOM, contact.nom)

            put(ContactContract.COL_PRENOM, contact.prenom)

            put(ContactContract.COL_TELEPHONE, contact.telephone)

            put(ContactContract.COL_EMAIL, contact.email)
        }
    }

    // Transformer Cursor en liste de contacts
    private fun cursorToList(cursor: Cursor): List<Contact> {

        val contacts = mutableListOf<Contact>()

        cursor.use {

            while (it.moveToNext()) {
                contacts.add(cursorToContact(it))
            }
        }

        return contacts
    }

    // Transformer une ligne SQLite en Contact
    private fun cursorToContact(cursor: Cursor): Contact {

        return Contact(

            id = cursor.getInt(
                cursor.getColumnIndexOrThrow(
                    ContactContract.COL_ID
                )
            ),
            nom = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    ContactContract.COL_NOM
                )
            ),

            prenom = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    ContactContract.COL_PRENOM
                )
            ),

            telephone = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    ContactContract.COL_TELEPHONE
                )
            ),

            email = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    ContactContract.COL_EMAIL
                )
            )
        )
    }
}
