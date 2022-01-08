package Model;

public class Frame {

    public enum Tag {
        LOGIN, SIGNUP, LOGOUT;
    }

    public final Tag tag;
    public String username;
    public final byte[] data;

    public Frame(Tag tagG, String username, byte[] dataG) {
        tag = tagG;
        this.username = username;
        data = dataG;
    }
}