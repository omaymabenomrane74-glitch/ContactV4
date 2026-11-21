package com.example.contactv4.ui
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

import com.example.contactv4.R
import com.example.contactv4.data.Contact

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onClick: (Contact) -> Unit,
    private val onCall: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val nom: TextView = view.findViewById(R.id.txtNom)
        val prenom: TextView = view.findViewById(R.id.txtPrenom)
        val telephone: TextView = view.findViewById(R.id.txtTelephone)

        val btnAppeler: ImageButton =
            view.findViewById(R.id.btnAppeler)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int
    ) {
        val contact = contacts[position]

        holder.nom.text = contact.nom
        holder.prenom.text = contact.prenom
        holder.telephone.text = contact.telephone
        holder.itemView.setOnClickListener {
            onClick(contact)
        }
        holder.btnAppeler.setOnClickListener {
            onCall(contact)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun updateList(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
}
