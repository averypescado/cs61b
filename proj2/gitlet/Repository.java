package gitlet;
import java.io.File;
import java.util.Date;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");

     static void initCommand() {
        GITLET_DIR.mkdir();
        File staged = new File(".gitlet/staged");
        File blobs = new File(".gitlet/blobs");
        File unstaged = new File(".gitlet/unstaged");
        File currentBranch = new File(".gitlet/current");
        File commits = new File(".gitlet/commits");
        commits.mkdir();
        staged.mkdir();
        blobs.mkdir();
        unstaged.mkdir();
        currentBranch.mkdir();

        new Commit("initial commit",null, new Date(0));
        /**TODO Set the head to the current commit  */
        /**TODO set the main to the current branch */
    }

     static void add(String name) {
        File added_file = new File(name);
        File staging_placeholder= new File(".gitlet/staged/" + name);
        Utils.writeContents(staging_placeholder, Utils.readContents(added_file));

        /** TODO add file to staging area */
        /** TODO Get the blob from the file and see if its part of existing blobs for commit"
         *
         */

    }


}
