package com.lugares_v.ui.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.type.LatLng
import com.lugares_v.databinding.FragmentGalleryBinding
import com.lugares_v.model.Lugar
import com.lugares_v.viewmodel.GalleryViewModel
import com.lugares_v.viewmodel.LugarViewModel

class GalleryFragment : Fragment(),OnMapReadyCallback {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var googleMap:GoogleMap
    private  var mapReady=false


    //Se toma la info de los lugares...
    private lateinit var lugarViewModel: LugarViewModel


    //cuando se crea el activity
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Se solicita la actualizacion del mapa.... para presentarlo en la pantalla
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        lugarViewModel =
            ViewModelProvider(this)[LugarViewModel::class.java]


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
//Se ejecuta cuando el mapara esta listo para mostrarse...
    override fun onMapReady(map: GoogleMap) {
        map.let{

            googleMap=it
            mapReady=true
            //Se buscan los lugares para dibujar en el mapa...
            lugarViewModel.getAllData.observe(viewLifecycleOwner){
                updateMap(it)
                ubicaGPS()
            }
        }

    }
    private fun ubicaGPS() {
        val ubicacion: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if(ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            //Si no tengo los permisos los solicito
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),105)
        }else
        {
            var latitud=0.0
            var longitud=0.0
            ubicacion.lastLocation.addOnSuccessListener { location: Location? ->

                if (location!=null){
               latitud=location.latitude
                    longitud=location.longitude

                }else
                {
                    latitud=9.97
                    longitud=-84.00
                }
            }
                .addOnFailureListener{
                    latitud=9.97
                    longitud=-84.00
                }
            val camara=CameraUpdateFactory.newLatLngZoom(com.google.android.gms.maps.model.LatLng(latitud,longitud),15f)
            googleMap.animateCamera(camara)

        }
    }

    private fun updateMap(lugares:List<Lugar>) {
       if(mapReady){

           lugares.forEach{
               if (it.latitud?.isFinite()==true && it.longitud?.isFinite()==true){
                   val marca= com.google.android.gms.maps.model.LatLng(it.latitud, it.longitud)
                   googleMap.addMarker(MarkerOptions().position(marca).title(it.nombre))
               }
           }
       }
    }
}