package ro.ase.fhcrypt;

import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AvailableKeys extends AsyncTask<URL, Void, String> {
    public List<Key> keyArrayList = new ArrayList<>();     // denumiri diferite ca sa nu se amestece de la o activitate la alta
    JSONArray algArray = null;

    @Override
    protected String doInBackground(URL... urls) {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)urls[0].openConnection();
            conn.setRequestMethod("GET");
            InputStream ist = conn.getInputStream();

            InputStreamReader isr = new InputStreamReader(ist);
            BufferedReader br =  new BufferedReader(isr);
            String linie = null;
            String buffer = "";

            while((linie = br.readLine())!= null)
            {
                buffer+=linie;
            }

            parseJSONFile(buffer);

            return buffer;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void parseJSONFile (String jsonStr) throws JSONException, NoSuchAlgorithmException {
        if(jsonStr != null)
        {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(jsonStr);

            algArray = jsonObject.getJSONArray("keys"); // asa se numeste vectorul din JSON

            for(int i = 0; i<algArray.length(); i++)
            {
                JSONObject obj = algArray.getJSONObject(i);

                String currentAlgorithm = obj.getString("AlgorithmName");
                String currentKeySize = obj.getString("KeySize");

                SecretKey key;
                KeyGenerator keyGenerator = null;

                switch (currentAlgorithm) {
                    case "Blowfish":
                        keyGenerator = KeyGenerator.getInstance("Blowfish");
                        keyGenerator.init(Integer.parseInt(currentKeySize));
                        break;
                    case "RC4":
                        keyGenerator = KeyGenerator.getInstance("RC4");
                        keyGenerator.init(Integer.parseInt(currentKeySize));
                        break;

                    case "AES":
                        keyGenerator = KeyGenerator.getInstance("AES");
                        keyGenerator.init(Integer.parseInt(currentKeySize));
                        break;
                }

                key = keyGenerator.generateKey();
                String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

                Key newKey = new Key(currentAlgorithm,Integer.parseInt(currentKeySize),encodedKey,0);
                keyArrayList.add(newKey);
            }
        }
        else
        {
            android.util.Log.e("parsareJSON", "JSON is null");
        }
    }

}
