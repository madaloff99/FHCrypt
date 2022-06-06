package ro.ase.fhcrypt;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface KeyDao {

    @Insert
    void insert(Key key);

    @Query("select * from keys where userID = :userID and keyID = :keyID")
    Key getSpecificKey(int userID, int keyID);


    @Query("select * from keys where userID = :userID")
    List<Key> getAll(int userID);

    @Delete
    void delete(Key key);

    @Query("select * from keys where userID = :userID and keyType = :algorithmName and keySize = :keySize")
    List <Key> getKey(int userID, String algorithmName, String keySize);

    @Query("select * from keys where userID = :userID and keyType = :algorithmName")
    List<Key> showSpecificKeys(int userID, String algorithmName);

    @Query("select count(*) from keys where userID = :userID and keyType = :algorithmType")
    int countSpecificKeys(int userID, String algorithmType);

    @Query("SELECT DISTINCT keySize FROM keys ORDER BY keySize ASC")
    List<Integer> getDistinctSizes();

    @Query("SELECT count(*) from keys where keySize in (SELECT keySize FROM keys ORDER BY keySize ASC) group by keySize")
    List<Integer> getDatesCount();
}
