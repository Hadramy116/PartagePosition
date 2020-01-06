package m2.iscae.mr.maploaction;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MapActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    private static final int permission_CALL_ID = 1234;
    FragmentManager fragmentManager = getFragmentManager();
    private LocationManager lm;
    private GoogleMap googleMap;
    //chnagement localisation
    private MapFragment mapFragment;

    private Location maPosition;

    private Button shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment  =(MapFragment)fragmentManager.findFragmentById(R.id.map2);

        shareBtn = (Button) findViewById(R.id.share);
        shareBtn.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @SuppressWarnings("MissingPermission")
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, permission_CALL_ID);

                return;
            }
        }

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);

        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0, this);

        }

        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);

        }

        LoadMap();
    }


    private void LoadMap() {

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapActivity.this.googleMap = googleMap;
                googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
                //mode d'affichage de la carte

                googleMap.setTrafficEnabled(true);
                //on autorise l'api à afficher le bouton pour accéder à notre position courante
                googleMap.setMyLocationEnabled(true);
                //définition du marqueur qui va se positionner sur le point qu'on désire afficher
               //googleMap.addMarker(new MarkerOptions().position(new LatLng(18.070147535, -15.95280180685)).title("voici votre  position"));

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lm != null) {
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(Location location) {
        maPosition = location;

        ///Toast.makeText(this, "Info_localisation" + latitude + "/" + longitude, Toast.LENGTH_LONG).show();
        if (googleMap != null) {
            LatLng geolocation = new LatLng(maPosition.getLatitude(), maPosition.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(geolocation)
                    .title("voici votre  position"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(geolocation));

        }


    }

    @Override
    public void onClick(View view) {

        if (view == shareBtn) {
            if (maPosition != null) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "https://maps.google.com/?ll="+ maPosition.getLatitude() +","+ maPosition.getLatitude());
                startActivity(Intent.createChooser(intent, "Partager"));
            } else {
                Toast.makeText(this, "!! Postion n'est pas disponible ", Toast.LENGTH_SHORT).show();
            }

        }

    }
}


