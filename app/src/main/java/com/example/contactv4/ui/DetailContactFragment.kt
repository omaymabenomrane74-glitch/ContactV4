package com.example.contactv4.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.contactv4.data.Contact
import com.example.contactv4.data.ContactDao
import com.example.contactv4.data.ContactDbHelper
import com.example.contactv4.databinding.FragmentDetailContactBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailContactFragment : Fragment() {

    private var _binding: FragmentDetailContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var dao: ContactDao
    private var contactId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailContactBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        dao = ContactDao(
            ContactDbHelper.getInstance(requireContext())
        )

        contactId = arguments?.getLong("contact_id", 0L) ?: 0L

        if (contactId != 0L) {
            chargerContact()
        } else {
            binding.btnSupprimer.visibility = View.GONE
        }

        binding.btnEnregistrer.setOnClickListener {
            enregistrerContact()
        }

        binding.btnAnnuler.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnSupprimer.setOnClickListener {
            confirmerSuppression()
        }
    }

    private fun chargerContact() {

        viewLifecycleOwner.lifecycleScope.launch {

            val contact = withContext(Dispatchers.IO) {
                dao.getById(contactId)
            }

            if (contact != null && _binding != null) {
                binding.edtNom.setText(contact.nom)
                binding.edtPrenom.setText(contact.prenom)
                binding.edtTelephone.setText(contact.telephone)
                binding.edtEmail.setText(contact.email ?: "")

                binding.btnSupprimer.visibility = View.VISIBLE
            }
        }
    }

    private fun enregistrerContact() {

        val nom = binding.edtNom.text.toString().trim()
        val prenom = binding.edtPrenom.text.toString().trim()
        val telephone = binding.edtTelephone.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()

        if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty()) {

            Toast.makeText(
                requireContext(),
                "Remplissez les champs obligatoires",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val contact = Contact(
            id = contactId.toInt(),
            nom = nom,
            prenom = prenom,
            telephone = telephone
        )
        viewLifecycleOwner.lifecycleScope.launch {

            val resultat = withContext(Dispatchers.IO) {

                if (contactId == 0L) {
                    dao.insert(contact)
                } else {
                    dao.update(contact).toLong()
                }
            }

            if (resultat > 0) {

                Toast.makeText(
                    requireContext(),
                    "Contact enregistré",
                    Toast.LENGTH_SHORT
                ).show()

                parentFragmentManager.popBackStack()

            } else {

                Toast.makeText(
                    requireContext(),
                    "Erreur d'enregistrement",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun confirmerSuppression() {

        AlertDialog.Builder(requireContext())
            .setTitle("Supprimer")
            .setMessage("Voulez-vous supprimer ce contact ?")
            .setPositiveButton("Oui") { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {

                    withContext(Dispatchers.IO) {
                        dao.delete(contactId)
                    }

                    parentFragmentManager.popBackStack()
                }
            }
            .setNegativeButton("Non", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
