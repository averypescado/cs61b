package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String... args) {
        // TODO: what if args is empty?
        Repository repo = new Repository();
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                repo.initCommand();
                return;
            case "add":
                repo.add(args[1]);
                return;

            case "commit":
                if (args.length < 2 || args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                    return;
                } else {
                    repo.commit(args[1]);
                    return;
                }

            case "log":
                repo.log();

            case "rm":
                repo.rm(args[1]);

            case "find":
                repo.find(args[1]);

            case "status":
                repo.status();

            case "branch":
                repo.branch(args[1]);

            case "rm-branch":
                repo.rm_branch(args[1]);

            case "checkout":
                if (args.length==3){
                    repo.checkout(null, args[2]);
                }
                if (args.length==2){
                    repo.checkout_branch(args[1]);
                }
                if (args.length==4){
                    repo.checkout(args[1],args[3]);
                }
            case "global-log":
                repo.global_log();
            case "reset":

        }
    }
}
