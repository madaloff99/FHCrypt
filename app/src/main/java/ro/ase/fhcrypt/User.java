package ro.ase.fhcrypt;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int idUser;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "gender")
    private String gender;
    private int encryptedMessagesNo;
    private int decryptedMessagesNo;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public User(){
        this.idUser = 0;
        this.username = "";
        this.password = "";
        this.gender = "";
        this.decryptedMessagesNo = 0;
        this.encryptedMessagesNo = 0;
    }

    public User(String username, String password, String gender) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.decryptedMessagesNo = 0;
        this.encryptedMessagesNo = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getEncryptedMessagesNo() {
        return encryptedMessagesNo;
    }

    public void setEncryptedMessagesNo(int encryptedMessagesNo) {
        this.encryptedMessagesNo = encryptedMessagesNo;
    }

    public int getDecryptedMessagesNo() {
        return decryptedMessagesNo;
    }

    public void setDecryptedMessagesNo(int decryptedMessagesNo) {
        this.decryptedMessagesNo = decryptedMessagesNo;
    }
}
