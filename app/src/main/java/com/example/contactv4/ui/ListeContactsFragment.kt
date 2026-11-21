package com.example.contactv4.ui
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactv4.R
import com.example.contactv4.ui.MainActivity
import com.example.contactv4.data.ContactDao
import com.example.contactv4.data.ContactDbHelper
import com.example.contactv4.databinding.FragmentListeContactsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class ListeContactsFragment : Fragment() {
    private var _binding: FragmentListeContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dao: ContactDao
    private lateinit var adapter: ContactAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListeContactsBinding.inflate(
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
        adapter = ContactAdapter(
            emptyList(),

            onClick = { contact ->
                ouvrirDetail(contact.id.toLong())
            },
            onCall = { contact ->
                appeler(contact.telephone)
            }
        )
        binding.recyclerContacts.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerContacts.adapter = adapter
        binding.btnAjouter.setOnClickListener {
            ouvrirDetail(0L)
        }
        binding.btnParametres.setOnClickListener {
            (activity as? MainActivity)?.ouvrirParametres()
        }
        binding.edtRecherche.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    rechercher(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            }
        )

        chargerContacts()
    }

    private fun chargerContacts() {
        viewLifecycleOwner.lifecycleScope.launch {

            val resultat = withContext(Dispatchers.IO) {
                dao.getAll()
            }

            adapter.updateList(resultat)
        }
    }

    private fun rechercher(texte: String) {
        viewLifecycleOwner.lifecycleScope.launch {

            val resultat = withContext(Dispatchers.IO) {
                if (texte.isBlank()) {
                    dao.getAll()
                } else {
                    dao.search(texte)
                }
            }

            adapter.updateList(resultat)
        }
    }

    private fun ouvrirDetail(id: Long) {

        val fragment = DetailContactFragment()

        val bundle = Bundle()
        bundle.putLong("contact_id", id)

        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun appeler(numero: String) {

        if (numero.isBlank()) {

            Toast.makeText(
                requireContext(),
                "Numéro vide",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:$numero")
        )

        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        if (::dao.isInitialized && _binding != null) {
            chargerContacts()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
