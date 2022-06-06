package ro.ase.fhcrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button generateKey;
    private List<Key> keysList;
    private AppDatabase appDatabase;
    private String userPassword="";
    private String username="";
    private ListView listKey;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        appDatabase = AppDatabase.getAppDatabase(getActivity());
        final UserDao userDao = appDatabase.userDao();
        final KeyDao keyDao = appDatabase.keyDao();
        listKey=(ListView)view.findViewById(R.id.listKey);
        keysList = new ArrayList<Key>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        userPassword = sharedPreferences.getString("LoggedUserPassword", "");
        username = sharedPreferences.getString("LoggedUserName", "");

        AvailableKeys availableKeys = new AvailableKeys() {
            @Override
            protected void onPostExecute(String string) {
                int userID = userDao.getUserID(username, userPassword);
                for (int i = 0; i < keyArrayList.size(); i++) {
                    Key newKey = new Key(keyArrayList.get(i).getKeyType(), keyArrayList.get(i).getKeySize(), keyArrayList.get(i).getKeyValue(), userID);

                    List<Key> dbKeys = new ArrayList<Key>();

                    dbKeys = keyDao.getKey(userID, newKey.getKeyType(),
                            String.valueOf(newKey.getKeySize()));
                    if (dbKeys.size() == 0)
                        keyDao.insert(newKey);
                }
                List<Key> allKeys = new ArrayList<Key>();
                allKeys = keyDao.getAll(userID);
                CustomAdapter adapter=new CustomAdapter(getActivity(),allKeys,getLayoutInflater());
                listKey.setAdapter(adapter);
            }
        };

        try {
            availableKeys.execute(new URL("https://pastebin.com/raw/LbCYZSyA"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return view;
    }
}