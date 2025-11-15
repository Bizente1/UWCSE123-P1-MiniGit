import java.util.*;
import java.text.SimpleDateFormat;
// Bisente Deleon-Oronia
// 10/29/25
// CSE 123
// Programming Assignment 0: Ciphers
// Trien Vuong

//This class is a Repository that keeps track of your 
//code at different points in time by making Commits.
public class Repository {

    private String name;
    private Commit head;
    private int size;


    /*  
    Pre: This is the Constructor for the class, it takes a String to name the Repo
        and will throw a IllegalArgumentException if the String given is null or empty

    Post: Will create a Repository Object with no Commits and a size of zero
    */
    public Repository(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("A null or empty name was given");
        }

        this.name = name;
        this.size = 0;
        this.head = null;
    }

    /*  
    Pre: None

    Post: Returns the id of the current repo head as a String
        If the there is no current Repo Head it will return as null
    */
    public String getRepoHead() {
        if (head == null) {
            return null;
        }

        return head.id;
    }

    /*  
    Pre: None

    Post: Returns the size of the current repo as a integer
    */
    public int getRepoSize() {
        return size;
    }

    /*  
    Pre: None

    Post: Returns a String with the format: 
        <Repo Name> - Current head: <Commit id> at <Commit date> at <Commit TimeStamp + Time Zone>: <Commit message> 

        If the Repo has no commits, it will instead return:
        <Repo Name> - No commits
    */
    public String toString() {
        if (size == 0) {
            return name + " - No commits";
        }

        return name + " - Current head: " + head.toString();
    }

    /*
    Pre: This method takes a String that is suppose to match to a id of a pervious Commit
        If the given String is null a IllegalArgumentException will be thrown

    Post: Returns true if id given is matches a id of a commits in the repository
        If the id given does not match any id of a commit in the repository then it will return false
    */
    public boolean contains(String targetId) {
        if (targetId == null) {
            throw new IllegalArgumentException("target Id given was null");
        }

        Commit curr = head;
        while (curr != null) {
            if (targetId.equals(curr.id)) {
                return true;
            }
            curr = curr.past;
        }

        return false;
    }

    /*
    Pre: Takes a integer for how many commits you want to look back in the repository.
        If the integer value given is equal or less than zero a IllegalArgumentException will be thrown.

    Post: Returns a String value with all the commints going back the number of the integer given.
        If the integer given is equal to or more than the amount of commits in the repository it 
        will only print out the all commits in the repository.
        If there are no commits in the repository it will return a empty String.
    */
    public String getHistory(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("number given was not positive");
        }

        String toReturn = "";
        Commit curr = head;
        int count = n;

        while (curr != null && count > 0) {
            if(curr.past != null  && count > 0){
                toReturn += curr.toString() + "\n";
                curr = curr.past;
                count--;
            }else{
                toReturn += curr.toString();
                curr = curr.past;
                count--;
            }
        }

        return toReturn;

    }

    /*
    Pre: Take a String that will be the message for the Commit you are making.
        If the given String is null a IllegalArgumentException will be thrown

    Post: Creates a Commit that has its message set as the one given.
        It increase the size of the repository and sets the head of repository
        to be the commit just created.
    */
    public String commit(String message) {
        if (message == null) {
            throw new IllegalArgumentException("message given was null");
        }

        size++;

        head = new Commit(message, head);
        System.out.println(head.timeStamp);
        return head.id;

    }

    /*
    Pre: Takes a String which is the Id of the commit you want to remove from the repository.
        If the String given is null then a error will be thrown.

    Post: Returns True or False whether the Commit was found and dropped or not.
        If the Commit is found and drop, the size of the Repo will decrease by one.
        If the repo removed is the head then the Commit before the head will become the new head.
    */
    public boolean drop(String targetId) {
        if (targetId == null) {
            throw new IllegalArgumentException("target Id given was null");
        }

        Commit curr = head;

        if (curr == null) {
            return false;
        }

        if (targetId.equals(curr.id)) {
            head = curr.past;
            size--;
            return true;
        }

        while (curr != null) {
            if (curr.past != null && targetId.equals(curr.past.id)) {
                curr.past = curr.past.past;
                size--;
                return true;
            }
            curr = curr.past;
        }

        return false;
    }

    /*
    Pre: Takes another Repository as a parameter.
        If the Repository given is null a IllegalArgumentException will be thrown.

    Post: Takes the given Repository and inserts the Commits from the given Repository into the
        Repository that called this method and leaves the given 
        Repository empty and with size zero.
        If the head of the given Repository is greater than the head of the current Repository
        it will become the new head of the current Repository.
        If the Repository given is empty then the current list remains unchanged.
        If the current Repository is empty then all the commits from the given Repository will
        be moved to the current Repository
    */
    public void synchronize(Repository other) {
        if (other == null) {
            throw new IllegalArgumentException("Other repository given was null");
        }
        
        if (head == null) {
            head = other.head;
            other.head = null;
            size = other.size;
            other.size = 0;
        }

        Commit curr = head;
        Commit temp = other.head;



        if ((head != null && other.head != null) && (head.timeStamp < other.head.timeStamp)) {
            temp = other.head.past;
            other.head.past = head;
            head = other.head;
            other.head = temp;
            curr = head;
            other.size--;
            size++;
        }


        while (curr != null && other.head != null) {
            if (curr.past == null) {
                curr.past = other.head;
                other.head = null;
                size += other.size;
                other.size = 0;
            }else if(curr.past.timeStamp < other.head.timeStamp) {
                temp = other.head.past;
                other.head.past = curr.past;
                curr.past = other.head;
                other.head = temp;
                other.size--;
                size++;
            }
            curr = curr.past;

        }

        System.out.println(size);

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
         * 
         * @param message A message describing the changes made in this commit. Should
         *                be non-null.
         * @param past    A reference to the commit made immediately before this
         *                commit.
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
         * 
         * @param message A message describing the changes made in this commit. Should
         *                be non-null.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         * "[identifier] at [timestamp]: [message]"
         * 
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