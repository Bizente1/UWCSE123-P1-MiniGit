import java.util.*;
import java.text.SimpleDateFormat;

public class Repository {

    private String name;
    private Commit head;
    private int size;
    /**
     * TODO: Implement your code here.
     */

    public Repository(String name){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("A null or empty name was given");
        }

        this.name = name;
        this.size = 0;
        this.head = null;
    }

    public String getRepoHead(){
        if(head == null){
            return null;
        }

        return head.id;
    }

    public int getRepoSize(){
        return size;
    }

    public String toString(){
        if(size == 0){
            return name + " - No commits";
        }

        return name + " - Current head: " + head.toString();
    }

    public boolean contains(String targetId){
        if(targetId == null){
            throw new IllegalArgumentException("target Id given was null");
        }

        Commit curr = head;
        while(curr != null){
            if(targetId.equals(curr.id)){
                return true;
            }
            curr = curr.past;
        }

        return false;
    }

    public String getHistory(int n){
        if(n <= 0){
            throw new IllegalArgumentException("number given was not positive");
        }

        String toReturn = "";
        Commit curr = head;
        int count = n;

        if(curr == null){
            return toReturn;
        }

        if(curr != null){
            toReturn += curr.toString();
            if(curr.past == null || count <= 1){
                return toReturn;
            }
            toReturn += "\n";
            curr = curr.past;
            count--;
        }
        while(curr.past != null && count > 1){
            toReturn += curr.toString() + "\n";
            curr = curr.past;
            count--;
        }

        return toReturn += curr.toString();
    }

    public String commit(String message){
        if(message == null){
            throw new IllegalArgumentException("message given was null");
        }

        size++;

        head = new Commit(message, head);
        return head.id;
   

        // head = new Commit(message);
        // return head.id;

    }

    public boolean drop(String targetId){
        if(targetId == null){
            throw new IllegalArgumentException("target Id given was null");
        }
        
        Commit curr = head;

        if(curr == null){
            return false;
        }

        if(targetId.equals(curr.id)){
                head = curr.past;
                size--;
                return true;
            }

        while(curr != null){
            if(curr.past != null && targetId.equals(curr.past.id)){
                curr.past = curr.past.past;
                size--;
                return true;
            }
            curr = curr.past;
        }

        return false;
    }

    public void synchronize(Repository other){
        if(other == null){
            throw new IllegalArgumentException("Other repository given was null");
        }
        
        Commit curr = head;

        // while (curr != null || ) {
            
        // }


    }

    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this 
     * class openly mention the fields of the class. This is fine 
     * because the fields of the Commit class are public. In general, 
     * be careful about revealing implementation details!
     */
    public static class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         * @param past A reference to the commit made immediately before this
         *             commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         *      "[identifier] at [timestamp]: [message]"
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
        * Resets the IDs of the commit nodes such that they reset to 0.
        * Primarily for testing purposes.
        */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}