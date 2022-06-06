package ro.ase.fhcrypt;


import static androidx.room.ForeignKey.CASCADE;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "enctexts", foreignKeys = { @ForeignKey(entity = User.class,
        parentColumns = "idUser",
        childColumns = "userID",
        onDelete = CASCADE),@ForeignKey(entity = Key.class, parentColumns = "keyID", childColumns = "keyID",onDelete = CASCADE)})

public class EncryptedText {
    @PrimaryKey(autoGenerate = true)
    private int textID;
    private byte[] textValue;
    private int userID;
    private int keyID;

    public EncryptedText(byte[] textValue, int userID, int keyID) {
        this.textID = 0;
        this.textValue = textValue;
        this.userID = userID;
        this.keyID = keyID;
    }

    public int getTextID() {
        return textID;
    }

    public void setTextID(int textID) {
        this.textID = textID;
    }

    public byte[] getTextValue() {
        return textValue;
    }

    public void setTextValue(byte[] textValue) {
        this.textValue = textValue;
    }

    public int getUserID() {
        return userID;
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
}
