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
import com.aplicacao.android.notas.R
import com.example.android.notas.entidade.Nota
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN
import com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED
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

    // LocationRequest - Requirements for the location updates, i.e., how often you
// should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient has a new Location.
    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation: Location
    private val newAddProblemActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        sharedPreferences = getSharedPreferences(getString(R.string.user_creds_file_key), Context.MODE_PRIVATE)
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
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                currentLocation = p0.lastLocation
            }
        }
        createLocationRequest()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getPointsWS()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                currentLocation = p0.lastLocation
            }
        }
        createLocationRequest()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        mMap.isMyLocationEnabled = true
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()

        locationRequest.interval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    private fun getPointsWS() {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getAllOcorrencias()

        call.enqueue(object : Callback<List<Problem>> {
            override fun onResponse(call: Call<List<Problem>>, response: Response<List<Problem>>) {
                var allProblems = response.body()!!
                Log.d("ITEM", "entrou")
                for (problem in allProblems) {
                    val location = LatLng(problem.lat.toDouble(), problem.lon.toDouble())
                    if (problem.userId === userId) {
                        mMap.addMarker(MarkerOptions().position(location).title(problem.problem).icon(BitmapDescriptorFactory
                                .defaultMarker(HUE_RED)))
                    } else {
                        mMap.addMarker(MarkerOptions().position(location).title(problem.problem).icon(BitmapDescriptorFactory
                                .defaultMarker(HUE_GREEN)))
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))
                }
            }

            override fun onFailure(call: Call<List<Problem>>, t: Throwable) {
                Log.d("ITEM", "erro " + t.message)
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createProblem(title:String,problem:String) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.addProblem(problem = problem, lat = currentLocation.latitude.toString(), long = currentLocation.longitude.toString(), userId = userId.toString())
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
            val title = data?.getStringExtra(AddProblem.EXTRA_REPLY_TITLE)
            val problem = data?.getStringExtra(AddProblem.EXTRA_REPLY_PROBLEM)
            if (title != null && problem != null) {
                createProblem(title,problem)
                getPointsWS()
            }

        } else if (requestCode == 1) {
            Log.d("LOG", "EMPTY")
            Toast.makeText(this,
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show()
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val pnota = data?.getStringExtra(NovaNotaActivity.EXTRA_REPLY_Nota)
            val id = data?.getIntExtra(NovaNotaActivity.EXTRA_REPLY_ID, 0)
            val title = data?.getStringExtra(NovaNotaActivity.EXTRA_REPLY_TITLE)
            if (pnota != null && title != null) {
                if (id != null) {
                    Log.d("ITEM", "Nota " + pnota + " " + id)

                }
            }
        } else if (requestCode == 2) {
            Log.d("LOG", "EMPTY")
            Toast.makeText(this,
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show()
        }


    }
}