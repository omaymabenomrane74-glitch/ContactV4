package com.example.contactv4.data
object ContactContract {
    const val TABLE_NAME = "contacts"
    const val COL_ID = "_id"
    const val COL_NOM = "nom"
    const val COL_PRENOM = "prenom"
    const val COL_TELEPHONE = "telephone"
    const val COL_EMAIL = "email"
    const val COL_PHOTO_URI = "photo_uri"
    const val COL_LATITUDE = "latitude"
    const val COL_LONGITUDE = "longitude"

    const val CREATE_TABLE = """
        CREATE TABLE contacts (
            _id INTEGER PRIMARY KEY AUTOINCREMENT,
            nom TEXT NOT NULL,
            prenom TEXT NOT NULL,
            telephone TEXT NOT NULL,
            email TEXT,
            photo_uri TEXT,
            latitude REAL,
            longitude REAL
        )
    """
}