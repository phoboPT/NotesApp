package com.example.android.notas

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aplicacao.android.notas.R
import com.example.android.notas.Adapter.CustomWindow
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mMap: GoogleMap
    private var userId: Int = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation: Location
    private val newAddProblemActivityRequestCode: Int = 1
    private var ZOOM_MAP: Float = 15.0f
    private val LOCATION_PERMISSION_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val types = arrayOf("Traffic", "USA", "China", "Japan", "Other")
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        sharedPreferences =
            getSharedPreferences(getString(R.string.user_creds_file_key), Context.MODE_PRIVATE)
        userId = sharedPreferences.getInt(getString(R.string.userId), 0)

        val fab = findViewById<FloatingActionButton>(R.id.fab1)
        fab.setOnClickListener {
            val intent = Intent(this@MapsActivity, AddProblem::class.java)
            intent.putExtra("LAT", currentLocation.latitude)
            intent.putExtra("LONG", currentLocation.longitude)
            startActivityForResult(intent, newAddProblemActivityRequestCode)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    Toast.makeText(this@MapsActivity, "Hey", Toast.LENGTH_LONG).show()
                    currentLocation = location
                    updateCameraBearing(mMap, location.bearing)
                }
            }
        }

        createLocationRequest()
    }

    private fun updateCameraBearing(googleMap: GoogleMap?, bearing: Float) {
        if (googleMap == null) return
        val camPos = CameraPosition
            .builder(
                googleMap.cameraPosition // current Camera
            )
            .bearing(bearing)
            .zoom(ZOOM_MAP)
            .build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos))
    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //coloca a bolinha na localização
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    currentLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, ZOOM_MAP))
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
            return
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLastLocation()
        getPointsWS()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                currentLocation = p0.lastLocation
            }
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun getPointsWS() {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllOcorrencias()
        mMap.clear()
        call.enqueue(object : Callback<List<Problem>> {
            override fun onResponse(call: Call<List<Problem>>, response: Response<List<Problem>>) {
                var allProblems = response.body()!!
                Log.d("ITEM", "entrou")
                var markerOptions = MarkerOptions()
                for (problem in allProblems) {
                    val location = LatLng(problem.lat.toDouble(), problem.lon.toDouble())
                    markerOptions = if (problem.userId == userId) {
                        MarkerOptions().position(location).title(problem.problem).snippet(problem.type).icon(
                            BitmapDescriptorFactory
                                .defaultMarker(HUE_RED)
                        )
                    } else {
                        MarkerOptions().position(location).title(problem.problem).snippet(problem.type).icon(
                            BitmapDescriptorFactory
                                .defaultMarker(HUE_GREEN)
                        )
                    }

                    val customInfoWindow = CustomWindow(this@MapsActivity)

                    mMap!!.setInfoWindowAdapter(customInfoWindow)

                    val marker = mMap!!.addMarker(markerOptions)
                    marker.tag = problem.problem
                    marker.showInfoWindow()
                    mMap.setOnInfoWindowClickListener {
                        val intent = Intent(this@MapsActivity, AddProblem::class.java)
                        intent.putExtra("LAT", problem.lat)
                        intent.putExtra("LONG", problem.lon)
                        intent.putExtra("PROBLEM", problem.problem)
                        intent.putExtra("USERID", problem.userId)
                        intent.putExtra("ID", problem.id)
                        startActivityForResult(intent, 2)
                    }
                }
            }

            override fun onFailure(call: Call<List<Problem>>, t: Throwable) {
                Log.d("ITEM", "erro " + t.message)
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createProblem(problem: String,type:String) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.addProblem(
            problem = problem,
            lat = currentLocation.latitude.toString(),
            long = currentLocation.longitude.toString(),
            userId = userId.toString(),
            type = type
        )
        call.enqueue(object : Callback<Problem> {
            override fun onResponse(call: Call<Problem>, response: Response<Problem>) {
                if (response.isSuccessful) {
                    val problem = response.body()
                    if (problem?.id!! > 0) {
                        Toast.makeText(
                            this@MapsActivity,
                            "Login realizado com sucesso! ",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("ITEM", "hey " + response.body().toString())

                        val sydney = LatLng(problem.lat.toDouble(), problem.lon.toDouble())
                        mMap.addMarker(MarkerOptions().position(sydney).title("Marker"))


                    } else {
                        Toast.makeText(
                            this@MapsActivity,
                            "login falhou",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<Problem>, t: Throwable) {
                Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newAddProblemActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val problem = data?.getStringExtra(AddProblem.EXTRA_REPLY_PROBLEM)
            val type = data?.getStringExtra(AddProblem.EXTRA_REPLY_TYPE)
            if (type != null && problem != null) {
                createProblem(problem,type)
                getPointsWS()
            }

        } else if (requestCode == 1) {
            Log.d("LOG", "EMPTY")
            Toast.makeText(
                this,
                R.string.empty_fields,
                Toast.LENGTH_SHORT
            ).show()
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val problem = data?.getStringExtra(AddProblem.EXTRA_REPLY_PROBLEM)
            val id = data?.getIntExtra(AddProblem.EXTRA_REPLY_ID, 0)
            val userId = data?.getIntExtra(AddProblem.EXTRA_REPLY_USERID, 0)
            val lat = data?.getStringExtra(AddProblem.EXTRA_REPLY_LAT)
            val long = data?.getStringExtra(AddProblem.EXTRA_REPLY_LONG)
            val type = data?.getStringExtra(AddProblem.EXTRA_REPLY_TYPE)


            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.updateProblem(
                problem = problem,
                lat = lat,
                long = long,
                userId = userId,
                id = id,
                type = type
            )
            call.enqueue(object : Callback<Problem> {
                override fun onResponse(call: Call<Problem>, response: Response<Problem>) {
                    getPointsWS()
                }

                override fun onFailure(call: Call<Problem>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })


        } else if (requestCode == 2) {
            Log.d("LOG", "EMPTY")
            Toast.makeText(
                this,
                R.string.empty_fields,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}