package gitlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public Commit(String message, String parent, HashMap<String, String> blobs) {
        _message = message;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        _blobs = blobs;
        _date = sdf.format(date);
        _parent=parent;
        if (_parent == null) {
            _hashval = Utils.sha1(message, _date);
        } else {
            String blobsha = null;
            ArrayList<String> blobarray = new ArrayList<String>();
            for (String ugh : blobs.values()) {
                if (blobsha == null) {
                    blobsha = Utils.sha1(ugh);
                } else {
                    blobsha = Utils.sha1(blobsha, ugh);
                }
            }
            _hashval = Utils.sha1(message, _date, parent, blobsha);
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


    HashMap<String, String> getBlobs() {
        return _blobs;
    }

    String getMessage() {
        return _message;
    }


    String get_hashval() {
        return _hashval;
    }

    String getParent(){
        if (_parent == null){
            return null;
        }
        return _parent;
    }

    String get_date(){
        return _date;
    }

    void getLog(){
        System.out.println("===");
        System.out.println("Commit " + get_hashval());
        get_date();
        System.out.println(getMessage());
        System.out.println("");
        return;

    }


    HashMap<String, String> _blobs;
    String _hashval;
    String _date;
    String _message;
    String _parent;



}




