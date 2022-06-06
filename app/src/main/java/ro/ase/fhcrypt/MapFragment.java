
package ro.ase.fhcrypt;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import com.google.android.gms.maps.SupportMapFragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private EditText etSource;
    private EditText etDestination;
    private Button btTrack;
    private List<LatLng> latLngList;
    private GoogleMap mMap;
    private List<String> locations;


    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        etSource = view.findViewById(R.id.et_source);
        etDestination = view.findViewById(R.id.et_dest);
        btTrack = view.findViewById(R.id.bt_track);
        latLngList = new ArrayList<LatLng>();
        locations = new ArrayList<String>();
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sSource = etSource.getText().toString();
                String sDest = etDestination.getText().toString();

                locations.add(sSource);
                locations.add(sDest);

                if (sSource.equals("") || sDest.equals(""))
                    Toast.makeText(getActivity(), "Enter both locations", Toast.LENGTH_LONG).show();
                else {
                    try {
                        latLngList.add(getLocationInfo(getActivity(), sSource));
                        latLngList.add(getLocationInfo(getActivity(), sDest));

                        PolylineOptions plo = new PolylineOptions();
                        MarkerOptions markerOptions = new MarkerOptions();
                        for (int i = 0; i < latLngList.size(); i++) {
                            mMap.addMarker(new MarkerOptions().position(latLngList.get(i)).title(locations.get(i)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngList.get(i)));
                            markerOptions.position(latLngList.get(i)).title(locations.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            plo.add(latLngList.get(i));
                        }

                        plo.color(Color.BLACK);
                        plo.width(20);
                        mMap.addPolyline(plo);

                        Marker m = mMap.addMarker(markerOptions);
                        m.showInfoWindow();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        return view;
    }

    private LatLng getLocationInfo(Context context, String sSource) throws IOException {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(sSource, 5);

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}