package ro.ase.fhcrypt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class DecryptFragment extends Fragment  implements AdapterView.OnItemSelectedListener {
    private Button decTextButton;
    private TextView decryptedText;
    private Spinner textsSpin;
    private List<String> encList;
    private String selectedKey;
    private List<Integer> keysIDList;

    private String selectedText;
    private List<Integer> textsIDList;
    private String userPassword;
    private String username;
    private AppDatabase appDatabase;
    private int userID;


    public DecryptFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_decrypt, container, false);
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getActivity());
        KeyDao keyDao = appDatabase.keyDao();
        FileDao fileDao = appDatabase.fileDao();

        decryptedText = view.findViewById(R.id.decryptedText);
        decTextButton = view.findViewById(R.id.decryptButton);
        textsSpin = view.findViewById(R.id.enc_spin);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        userPassword = sharedPreferences.getString("LoggedUserPassword", "");
        username = sharedPreferences.getString("LoggedUserName", "");

        UserDao userDao = appDatabase.userDao();
        userID = userDao.getUserID(username, userPassword);

        List<EncryptedText> listTexts = new ArrayList<EncryptedText>();
        encList = new ArrayList<String>();
        keysIDList = new ArrayList<Integer>();
        textsIDList = new ArrayList<Integer>();

        listTexts = fileDao.getAll(userID);

        for (int i = 0; i < listTexts.size(); i++) {
            String currentText = "Cheie: " + listTexts.get(i).getKeyID() + ", " + listTexts.get(i).getTextValue();
            encList.add(currentText);
            textsIDList.add(listTexts.get(i).getTextID());
        }

        textsSpin.setOnItemSelectedListener(this);


        ArrayAdapter aa2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, encList);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textsSpin.setAdapter(aa2);


        decTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index2 = encList.indexOf(selectedText);
                int textID = textsIDList.get(index2);

                EncryptedText currentText = fileDao.getEncryptedText(userID,textID);
                Key currentKey = keyDao.getSpecificKey(userID, currentText.getKeyID());
                SecretKey originalKey = null;
                String secretKey = currentKey.getKeyValue();
                byte[] encText = currentText.getTextValue();

                byte[] decodedKey = Base64.getDecoder().decode(secretKey);
                switch (currentKey.getKeyType()) {
                    case "Blowfish":
                        originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "Blowfish");
                        break;

                    case "RC4":
                        originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "RC4");
                    case "AES":
                        originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
                }
                String decText = null;
                try {
                    decText = Helper.decryptMsg(encText,originalKey,currentKey.getKeyType());

                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidParameterSpecException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String toPrint = decryptedText.getText().toString();
                toPrint = toPrint + " " + decText;
                decryptedText.setText(toPrint);
                int decNo = userDao.getDecNo(userID);
                userDao.updateDecNo(userID, decNo + 1);
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedText = encList.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}