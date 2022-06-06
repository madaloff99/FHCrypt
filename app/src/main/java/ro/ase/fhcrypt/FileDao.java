package ro.ase.fhcrypt;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FileDao {
    @Insert
    void insert(EncryptedText text);

    @Query("select * from enctexts where userID = :userID and textID = :fileID")
    EncryptedText getEncryptedText(int userID,  int fileID);

    @Query("select * from enctexts where userID = :userID")
    List<EncryptedText> getAll(int userID);

}
