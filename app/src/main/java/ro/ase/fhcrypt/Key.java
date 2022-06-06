package ro.ase.fhcrypt;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;


@Entity(tableName = "keys", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "idUser",
        childColumns = "userID",
        onDelete = CASCADE))

public class Key {

    @PrimaryKey(autoGenerate = true)
    private int keyID;
    private String keyType;
    private int keySize;
    private String keyValue;
    private int userID;

    public Key(){
        this.keyID = 0;
        this.keyType = "";
        this.keyValue = "";
        this.keySize = 0;
        this.userID = 0;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public Key(String keyType, int keySize, String keyValue, int userID) {
        this.keyID = 0;
        this.keyType = keyType;
        this.keySize = keySize;
        this.keyValue = keyValue;
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public String getKeyType() {
        return keyType;
    }


    public int getKeySize() {
        return keySize;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }


    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
