package ro.ase.fhcrypt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.eazegraph.lib.utils.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptFragment extends Fragment  implements AdapterView.OnItemSelectedListener {
    private EditText toEncrypt;
    private Button encTextButton;
    private Spinner keysSpin;

    private String textToEncrypt;
    private List<String> keysList;
    private String selectedKey;
    private List<Integer> keysIDList;
    private String userPassword;
    private String username;
    private AppDatabase appDatabase;
    private int userID;


    public EncryptFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_encrypt, container, false);
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getActivity());
        KeyDao keyDao = appDatabase.keyDao();
        toEncrypt = view.findViewById(R.id.toEncrypt);
        encTextButton = view.findViewById(R.id.encryptButton);
        keysSpin = view.findViewById(R.id.key_spin);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        userPassword = sharedPreferences.getString("LoggedUserPassword", "");
        username = sharedPreferences.getString("LoggedUserName", "");

        UserDao userDao = appDatabase.userDao();

        FileDao fileDao = appDatabase.fileDao();

        userID = userDao.getUserID(username, userPassword);

        List<Key> listKey = new ArrayList<Key>();
        keysList = new ArrayList<String>();
        keysIDList = new ArrayList<Integer>();

        listKey = keyDao.getAll(userID);

        for(int i=0;i<listKey.size();i++)
        {
            String currentKey = "Algoritm: "+listKey.get(i).getKeyType()+" , Size: " + listKey.get(i).getKeySize()+" , Value: " + listKey.get(i).getKeyValue();
            keysList.add(currentKey);
            keysIDList.add(listKey.get(i).getKeyID());
        }


        keysSpin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, keysList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keysSpin.setAdapter(aa);

        encTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToEncrypt = toEncrypt.getText().toString();
                int index = keysList.indexOf(selectedKey);
                int keyID = keysIDList.get(index);
                Key currentKey = keyDao.getSpecificKey(userID,keyID);
                SecretKey originalKey = null;
                String secretKey = currentKey.getKeyValue();
                String encryptedData = "";

                byte[] cipherText = null;


                byte[] decodedKey = Base64.getDecoder().decode(secretKey);
                switch (currentKey.getKeyType()){
                    case "Blowfish":
                            originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "Blowfish");
                            break;
                    case "RC4":
                            originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "RC4");
                            break;
                    case "AES":
                            originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
                            break;
                }
                try {
                    cipherText = Helper.encryptMsg(textToEncrypt,originalKey,currentKey.getKeyType());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidParameterSpecException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                EncryptedText encrText = new EncryptedText(cipherText,userID,keyID);
                fileDao.insert(encrText);
                int encNo = userDao.getEncNo(userID);
                userDao.updateEncNo(userID,encNo+1);

                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedKey = keysList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}