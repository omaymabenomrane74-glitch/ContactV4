package com.example.contactv4.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.contactv4.R
import com.example.contactv4.databinding.FragmentCarteContactBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
class CarteContactFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentCarteContactBinding? = null

    private var latitude = 0.0
    private var longitude = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarteContactBinding.inflate(
            inflater, container, false
        )

        latitude = arguments?.getDouble("latitude") ?: 0.0
        longitude = arguments?.getDouble("longitude") ?: 0.0

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        if (latitude !in -90.0..90.0 ||
            longitude !in -180.0..180.0
        ) return

        val position = LatLng(latitude, longitude)

        googleMap.addMarker(
            MarkerOptions()
                .position(position)
                .title("Contact")
        )

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(position, 15f)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
