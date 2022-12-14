package ga.denis.outplay;

import android.location.Location;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Checkpoint {
    LatLng lokace;
    GoogleMap mMap;
    Marker me;
    Marker hrac;

    public Checkpoint(GoogleMap mMap, LatLng lokace, @Nullable Marker hrac) {
        this.lokace = lokace;
        this.mMap = mMap;
        this.hrac = hrac;
        me = mMap.addMarker(new MarkerOptions().position(lokace).title("Checkpoint").icon(BitmapDescriptorFactory.fromAsset("checkpoint.bmp")).flat(true).anchor(0.5f,0.5f).draggable(true));
    }

    public Checkpoint(GoogleMap mMap, LatLng poly1, LatLng poly2, LatLng poly3, LatLng poly4, Double x, Double y, @Nullable Marker hrac) {
        this.mMap = mMap;
        this.hrac = hrac;
        this.lokace = new LatLng((poly1.latitude + ((poly2.latitude - poly1.latitude) * y / 100)) + (y / 100 * ((poly4.latitude + ((poly3.latitude - poly4.latitude) * y / 100)) - (poly1.latitude + ((poly2.latitude - poly1.latitude) * y / 100)))), (poly1.longitude + ((poly2.longitude - poly1.longitude) * x / 100)) + (x / 100 * ((poly4.longitude + ((poly3.longitude - poly4.longitude) * x / 100)) - (poly1.longitude + ((poly2.longitude - poly1.longitude) * x / 100)))));
        me = mMap.addMarker(new MarkerOptions().position(lokace).title("Checkpoint").icon(BitmapDescriptorFactory.fromAsset("checkpoint.bmp")).flat(true).anchor(0.5f,0.5f).draggable(true));
    }

    public boolean inside() {
        boolean a = false;

        float[] results = new float[1];
        Location.distanceBetween(me.getPosition().latitude, me.getPosition().longitude, hrac.getPosition().latitude, hrac.getPosition().longitude, results);
        if(results[0] <= 15) {
            a = true;
            me.setIcon(BitmapDescriptorFactory.fromAsset("crosshair.bmp"));
        }

        return a;
    }

    public LatLng getLocation() {
        return lokace;
    }
}
