package gitlet;
import java.io.File;

import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable{
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */



    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String parent;

    public Commit(String message, String parent, Date timestamp) {
        this.message=message;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        _date = sdf.format(timestamp);
        if (parent==null) {
            _hashval = Utils.sha1(message, _date);
        }
        try {
            File commit_file = new File(".gitlet/commits/" + _hashval +".ser");
            FileOutputStream fileOut = new FileOutputStream(commit_file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);

        } catch (IOException e) {
         return;
      }


    }
      String _hashval;
      String _date;

}




