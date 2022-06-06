package ro.ase.fhcrypt;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("UPDATE user SET encryptedMessagesNo = :encNo WHERE idUser = :userID")
    void updateEncNo(int userID, int encNo);

    @Query("UPDATE user SET decryptedMessagesNo = :decNo WHERE idUser = :userID")
    void updateDecNo(int userID, int decNo);

    @Query("SELECT * FROM user where username=(:username) and password=(:password)")
    User login(String username,String password);


    @Query("SELECT encryptedMessagesNo FROM user where idUser = :userId")
    int getEncNo(int userId);

    @Query("SELECT decryptedMessagesNo FROM user where idUser = :userId")
    int getDecNo(int userId);

    @Query("Select * from user where username = (:username) and password=(:password)")
    int getUserID(String username, String password);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT COUNT(*) from user")
    int countUsers();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
