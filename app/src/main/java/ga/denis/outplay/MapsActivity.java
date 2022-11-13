package ga.denis.outplay;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ga.denis.outplay.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    Button startButton;
    Marker poly1;
    Marker poly2;
    Marker poly3;
    Marker poly4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /*ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                        } else {
                            // No location access granted.
                        }
                    }
            );*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        mMap.setMapStyle(mapStyleOptions);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                LatLng pozice = new LatLng(location.getLatitude(),location.getLongitude());
                                CameraPosition position = new CameraPosition.Builder().
                                        target(pozice).
                                        tilt(0).
                                        zoom(19).
                                        bearing(0).
                                        build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                                mMap.addMarker(new MarkerOptions().position(pozice).title("Current position").icon(BitmapDescriptorFactory.fromAsset("kamera.bmp")).flat(true));
                                poly1 = mMap.addMarker(new MarkerOptions().position(mMap.getProjection().getVisibleRegion().farLeft).title("Playspace corner").icon(BitmapDescriptorFactory.fromAsset("crosshair.bmp")).draggable(true));
                                poly2 = mMap.addMarker(new MarkerOptions().position(mMap.getProjection().getVisibleRegion().farRight).title("Playspace corner").icon(BitmapDescriptorFactory.fromAsset("crosshair.bmp")).draggable(true));
                                poly3 = mMap.addMarker(new MarkerOptions().position(mMap.getProjection().getVisibleRegion().nearLeft).title("Playspace corner").icon(BitmapDescriptorFactory.fromAsset("crosshair.bmp")).draggable(true));
                                poly4 = mMap.addMarker(new MarkerOptions().position(mMap.getProjection().getVisibleRegion().nearRight).title("Playspace corner").icon(BitmapDescriptorFactory.fromAsset("crosshair.bmp")).draggable(true));
                                CameraPosition cameraPosition = new CameraPosition.Builder().
                                        target(pozice).
                                        tilt(0).
                                        zoom(18).
                                        bearing(0).
                                        build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                    @Override
                                    public void onMarkerDrag(@NonNull Marker marker) {

                                    }

                                    @Override
                                    public void onMarkerDragEnd(@NonNull Marker marker) {
                                        marker.setPosition(marker.getPosition());
                                    }

                                    @Override
                                    public void onMarkerDragStart(@NonNull Marker marker) {

                                    }
                                });
                            }
                        }
                    });

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
                    ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MapsActivity.this, GameplayActivity.class);
        intent.putExtra("poly1", poly1.getPosition());
        intent.putExtra("poly2", poly2.getPosition());
        intent.putExtra("poly3", poly3.getPosition());
        intent.putExtra("poly4", poly4.getPosition());
        startActivity(intent);
    }
}