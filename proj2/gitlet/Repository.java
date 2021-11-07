package gitlet;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.StandardOpenOption;
import java.io.File;
import java.util.Date;
import java.io.FileOutputStream;
import java.util.List;

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

      void initCommand() {
        GITLET_DIR.mkdir();
        File staged = new File(".gitlet/staged");
        File blobs = new File(".gitlet/blobs");
        File unstaged = new File(".gitlet/unstaged");
        File currentBranch = new File(".gitlet/current");
        File currentname = new File(".gitlet/current/master.ser");
        File heads = new File(".gitlet/heads");
        File commits = new File(".gitlet/commits");
        File next = new File(".gitlet/heads/master.ser");

        try{
            heads.mkdir();
            commits.mkdir();
            staged.mkdir();
            blobs.mkdir();
            unstaged.mkdir();
            currentBranch.mkdir();

            Commit initial = new Commit("initial commit",null, null);
            FileOutputStream fileout = new FileOutputStream(next);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileout);
            objectOut.writeObject(initial);
            Files.copy(next.toPath(), currentname.toPath());

        } catch (IOException e){
            return;

        }



        /**TODO Set the head to the current commit  */
        /**TODO set the main to the current branch */
    }

      void add(String name) {
        File added_file = new File(name);
        File staging_placeholder= new File(".gitlet/staged/" + name);
        File saverm = new File(".gitlet/unstaged/" + name);

         if (!added_file.exists()) {
             System.out.println("File does not exist.");
             return;
         }
         if (saverm.exists()) {
             saverm.delete();
         }
         String hash = Utils.sha1(name, Utils.readContents(added_file));

         if (getCurrentCommit().getBlobs() != null && getCurrentCommit().getBlobs().containsValue(hash)){
             return;
         }
         Utils.writeContents(staging_placeholder, Utils.readContents(added_file));
    }

     void commit(String message) {
        HashMap<String, String> blobs = new HashMap<String, String>();
        File staging = new File(".gitlet/staged");
        File unstage = new File(".gitlet/unstaged");
        File parent = new File(".gitlet/current");
        File workingD = new File(".");
        String par = getCurrentCommit().get_hashval();

        Commit currentCommit = getCurrentCommit();

        Commit newCommit = null;

        if (staging.listFiles().length == 0
                && unstage.listFiles().length == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        else {
            for (File file: staging.listFiles()) {
                Blob next= new Blob(file.getName(), Utils.readContents(file));
                blobs.put(file.getName(),next.getHash());
                file.delete();
            }
            for (File file : unstage.listFiles()){
                file.delete();
            }
            for (File file : workingD.listFiles()) {
                if (getCurrentCommit().getMessage().compareTo("initial commit")==0){
                    break;
                }
                if ((currentCommit.getBlobs().get(file.getName()) != null) && (blobs.get(file.getName())==null)) {
                    File unstaged = new File(".gitlet/unstaged/" + file.getName());
                    if (unstaged.exists()) {
                        unstaged.delete();
                        continue;
                    }
                    else {
                        blobs.put(file.getName(), currentCommit.getBlobs().get(file.getName()));
                    }

                }
            }
            newCommit = new Commit(message, par, blobs);
            updateBranch(newCommit);

        }
    }

    void rm(String name) {
        File deleted_file = new File(name);
        File staged = new File(".gitlet/staging/" +name);
        File unstaged = new File(".gitlet/unstaged/" +name);
        Commit current_commit=getCurrentCommit();

        if (staged.exists()) {
            if  ((current_commit.getBlobs() != null) && (current_commit.getBlobs().get(name) != null)) {
                Utils.writeContents(unstaged, Utils.readContents(deleted_file));
                staged.delete();
            }
        }
        if ((current_commit.getMessage().compareTo("initial commit")!=0) && (getCurrentCommit().getBlobs().get(name) != null)) {
            Utils.writeContents(unstaged, Utils.readContents(deleted_file));
            if (!deleted_file.exists()) {
                Blob deletedblob = deserializeBlob(".gitlet/blobs/"
                        + current_commit.getBlobs().get(name) + ".ser");
                Utils.writeContents(unstaged, deletedblob.getFileContents());
                return;
            }

        }
        else {
            System.out.println("No reason to remove this file");
        }


    }

    void log() {

         Commit current= getCurrentCommit();

         Commit parent = null;
         int i =1;
         if (current.getMessage().compareTo("initial commit")==0){
             i+=1;
         }
         else{
             parent = getCommit(current.getParent());
         }
         if (current.getMessage().compareTo("initial commit") ==0){
             return;
         }

         while (current.getMessage().compareTo("initial commit") !=0) {
             System.out.println("===");
             System.out.println("commit " + current.get_hashval());
             System.out.println("Date: "+current.get_date());
             System.out.println(current.getMessage() + "");
             System.out.println("");


             if (i > 1) {
                 return;
             }
             current = parent;
             if (parent.getParent() == null) {
                 break;
             }
             parent = getCommit(current.getParent());
         }
             if (current.getMessage().compareTo("initial commit") == 0) {
                 System.out.println("===");
                 System.out.println("Commit " +current.get_hashval());
                 System.out.println("Date: "+current.get_date());
                 System.out.println(current.getMessage());
                 return;
             }
         }

    void global_log(){
        File commits = new File(".gitlet/commits");
        for (File file: commits.listFiles()) {
            getCommit(file.getName()).getLog();
        }


    }
    void find(String commit_message) {
          File commits= new File(".gitlet/commits");
          List<String> list=Utils.plainFilenamesIn(commits);
          int i =1;
        for ( String commit :  list){
            Boolean message=getCommit(commit).getMessage().equals(commit_message);
            if (message){
                System.out.println(commit);
                i+=1;
            }
        }
        if (i <2) {
            System.out.println("No commit with that message dude");
        }
        return;

    }

    void status(){
          File branches = new File(".gitlet/heads");
          File staged = new File(".gitlet/staged");
          File unstaged = new File(".gitlet/unstaged");

          List<String> branch_list =Utils.plainFilenamesIn(branches);
          List<String> stage_list=Utils.plainFilenamesIn(staged);
          List<String> unstage_list=Utils.plainFilenamesIn(unstaged);


          System.out.println("=== Branches ===");
          for ( String branch :  branch_list){
                System.out.println(branch);
          }
          System.out.println("");

          System.out.println("=== Staged Files ===");
          for ( String staged_file :  stage_list){
            System.out.println(staged_file);
          }
          System.out.println("");

          System.out.println("=== Removed Files ===");
          for ( String staged_file :  unstage_list){
                System.out.println(unstage_list);
          }
          System.out.println("");

    }

    void branch(String branch_name) {
        File new_head = new File(".gitlet/heads/"+ branch_name + ".ser");
        File current_commit_path = new File(".gitlet/current");
        String path = null;

        for (File file: current_commit_path.listFiles()) {
            path = file.getName();
        }

        if (new_head.exists()){
            System.out.println("A branch with that name already exists");
            return;
        }

        else{
            Commit current_commit = deserializedCommit(".gitlet/current/" + path);
            try {
                FileOutputStream fileOut= new FileOutputStream(new_head);
                ObjectOutputStream obj = new ObjectOutputStream(fileOut);
                obj.writeObject(current_commit);
            }

            catch (IOException e){
                System.out.println("Trouble with the output");
            }
        }
    }

    void rm_branch(String branch) {
        File to_remove = new File(".gitlet/heads/"+ branch + ".ser");
        File current = new File(".gitlet/current/"+ branch + ".ser");
        if (!to_remove.exists()) {
            System.out.println("A branch with that name does not exist.");
        }
        if (current.exists()){
            System.out.println("You cant remove the current branch");

        }
        to_remove.delete();
    }

    void reset(String commit_id) {
          Commit currCommit =getCurrentCommit();
          Commit resetCommit = null;
          File wd = new File(".");
          File staged = new File(".gitlet/staged");
          File commits = new File(".gitlet/commits");
          for (File fileCommit : commits.listFiles()){
              if (fileCommit.getName().contains(commit_id)){
                  resetCommit = deserializedCommit(".gitlet/commits/"+fileCommit.getName());
              }
              break;
          }
          if (resetCommit == null){
              System.out.println("No commit with that id exists.");
              return;
          }
          for (File file :wd.listFiles()){
              String names = file.getName();
              if (names.contains(".") || names.contains("Make") || names.contains("DS")) {
                  continue;
              }
              if (!file.isFile()) {
                  continue;
              }
              String wdhash = Utils.sha1(names, Utils.readContents(file));
              String resetHash = resetCommit.getBlobs().get(names);
              String currHash = currCommit.getBlobs().get(names);
              if (currHash!= null && resetHash != null && !currHash.equals(resetHash)
                      && !currHash.equals(wdhash) ) {
                  System.out.println("There is an untracked file in the way; delete it or add it first.");
                  return;
              }

          }
        for (File file : wd.listFiles()) {
            file.delete();
        }
        for (File fils : staged.listFiles()) {
            fils.delete();
        }
        for (String nameFile : resetCommit.getBlobs().keySet()){
            String newHash = resetCommit.getBlobs().get(nameFile);
            Blob blob = deserializeBlob(".gitlet/blobs/" + newHash + ".ser");
            File make = new File(nameFile);
            Utils.writeContents(make, blob.getFileContents());
        }
        updateBranch(resetCommit);

    }

    void checkout(String commitPath, String file_name){
          File dir = new File(".");
        File staging = new File(".gitlet/staged");
          Commit curr = null;
          if (commitPath == null) {
              curr = getCurrentCommit();
          }
          else{
              curr = deserializedCommit(commitPath);
          }

          if (file_name==null){
              Commit current = getCurrentCommit();
              for (File file : dir.listFiles()){
                  if (file.getName().contains(".gitignore")
                          || file.getName().contains("Make") || file.getName().contains(".DS")) {
                      continue;
                  }
                  String name = file.getName();
                  if (curr.getBlobs() != null && curr.getBlobs().get(name) != null){
                      if (current.getBlobs() != null && !current.getBlobs().get(name).equals(current.getBlobs().get(name))){
                          System.out.println("There is an untracked file in the way; delete it or add it first.");
                          return;
                      }
                      file.delete();
                      File new_file = new File(name);
                      Blob blob = deserializeBlob(".gitlet/blobs/" + curr.getBlobs().get(name) + ".ser");
                      Utils.writeContents(new_file,blob.getFileContents());
                  }
                  if (curr.getBlobs() != null & curr.getBlobs().get(name)==null){
                      if (current.getBlobs().get(name) != null
                              && !current.getBlobs().get(name).equals(curr.getBlobs().get(name))) {
                          System.out.println("There is an untracked file in the way; delete it or add it first.");
                          return;
                      }
                      file.delete();
                  }

                  }
              for (File file : staging.listFiles()) {
                  file.delete();
              }
              for (String hash : curr.getBlobs().values()) {
                  Blob that = deserializeBlob(".gitlet/blobs/" + hash + ".ser");
                  String name = that.getName();
                  File create = new File(name);
                  Utils.writeContents(create, that.getFileContents());
              }
              updateBranch(curr);
              return;


              }
          File addedfile = new File(file_name);
        if (curr.getMessage().compareTo("initial commit") == 0) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        Commit current_commit=getCurrentCommit();
        if (curr.getBlobs() != null) {
            for (String blob_name : current_commit.getBlobs().keySet()) {
                if (blob_name.compareTo(file_name) == 0) {
                    Blob new_blob = deserializeBlob(".gitlet/blobs/" + current_commit.getBlobs().get(file_name) + ".ser");
                    Utils.writeContents(addedfile, new_blob.getFileContents());
                    return;

                }
            }
            System.out.print("File doesnt exist for that");
        }
    }
    void checkout_branch(String branch_name){
        File branch = new File(".gitlet/heads/"+ branch_name + ".ser");
        File no_curr = new File(".gitlet/current/"+ branch_name + ".ser");
        File staging = new File(".gitlet/staged");

        File working_d = new File(".");

        for (File file : working_d.listFiles()) {
            if (!file.isFile()) {
                continue;
            }
            if (file.getName().contains(".gitignore") || file.getName().contains("Make")
                    || file.getName().contains(".DS")) {
                continue;
            }
            File check = new File(".gitlet/blobs/" + Utils.sha1(file.getName(), Utils.readContents(file)) + ".ser");
            if (!check.exists()) {
                System.out.println("There is an untracked file in the way; delete it or add it first.");
                return;
            }
        }

        if (no_curr.exists()) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        if (!branch.exists()) {
            System.out.println("No such branch exists.");
            return;
        }



        Commit current_commit = deserializedCommit(".gitlet/heads/"+ branch_name + ".ser");

        File current_branch = new File(".gitlet/current/"+ branch_name + ".ser");
        File old_current = new File(".gitlet/current/" + getCurrentCommitBranch());
        try {
            FileOutputStream fileOut = new FileOutputStream(current_branch);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(current_commit);
            old_current.delete();
        } catch (IOException e){
            System.out.println("This did not work");
        }

        if (current_commit.getBlobs() != null){
            for (String name : current_commit.getBlobs().keySet()){
                File updated_file = new File(name);
                String hash = current_commit .getBlobs().get(name);
                Blob blob = deserializeBlob(".gitlet/blobs/"+ hash +".ser");
                Utils.writeContents(updated_file,blob.getFileContents());
            }
        }

        for (File file : staging.listFiles()){
            file.delete();
        }
    }

    void fileIDCheckout(String commit_id, String file_name){
          File commit = new File(".gitlet/commits");
          for (File file : commit.listFiles()){
              if (file.getName() ==commit_id+".ser" || file.getName().contains(commit_id)){
                  checkout(".gitlet/commits/"+file.getName(),file_name);
                  return;
              }
          }
    }




    Blob deserializeBlob(String path) {
         Blob newBlob = null;
         File path_file = new File(path);
         try {
             FileInputStream f = new FileInputStream(path_file);
             ObjectInputStream objn = new ObjectInputStream(f);
             newBlob = (Blob) objn.readObject();
         }
         catch (IOException e) {
             String msg = "IOException while loading in deserialization Blob.";
             return null;
         }
         catch (ClassNotFoundException e) {
             String msg = "ClassNotFoundException while loading myCat.";
             System.out.println(msg);
         }
         return newBlob;
    }

    Commit getCurrentCommit() {
         String branches = null;
         Commit previous = null;
         File current_branch = new File(".gitlet/current");
         for (File file: current_branch.listFiles()) {
             branches = file.getName();
         }

         return deserializedCommit(".gitlet/heads/" + branches);
    }

    Commit getCommit(String hash) {
         Commit newCommit = null;
         File looking= new File(".gitlet/commits/" + hash);
         if (looking.exists()) {
             return deserializedCommit(".gitlet/commits/" + hash);
         }
         else {
             return deserializedCommit(".gitlet/commits/" + hash + ".ser");
         }


    }

    String getCurrentCommitBranch(){
        String curr_branch = null;
        File currentB = new File(".gitlet/current");
        for (File file : currentB.listFiles()){
            curr_branch = file.getName();
        }
        return curr_branch;
    }

    Commit deserializedCommit(String path) {
         Commit newCommit = null;
         File pathy = new File(path);
         try {
             FileInputStream f = new FileInputStream(pathy);
             ObjectInputStream objn = new ObjectInputStream(f);
             newCommit = (Commit) objn.readObject();
         }
         catch (IOException e) {
             String msg = "IOException while loading deserializecommit";
             System.out.println(msg);
        }
         catch (ClassNotFoundException e) {
             String msg = "ClassNotFoundException while loading myCat.";
             System.out.println(msg);
         }
         return newCommit;
    }

    void updateBranch(Commit new_com ) {
         String nextBranch = null;
         Commit next = null;
         File parent = new File(".gitlet/current");
         for (File file : parent.listFiles()) {
             nextBranch=file.getName();
         }

         try {
             File path = new File(".gitlet/heads/" + nextBranch);
             FileOutputStream fileOut = new FileOutputStream(path);
             ObjectOutputStream Out = new ObjectOutputStream(fileOut);
             Out.writeObject(new_com);
         } catch (IOException e) {
             System.out.println("Didn't work for some reason");
         }

         try {
             File branch_path = new File(".gitlet/current/" + nextBranch);
             FileOutputStream current_file_out = new FileOutputStream(branch_path);
             ObjectOutputStream path_out = new ObjectOutputStream(current_file_out);
             path_out.writeObject(new_com);
         }
         catch (IOException e) {
             System.out.println("Didn't work for some reason");
         }
    }


}
