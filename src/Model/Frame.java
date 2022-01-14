package Model;

public class Frame {

    public enum Tag {
        LOGIN, SIGNUP, LOGOUT;
    }

    public final Tag tag;
    public final byte[] data;

    public Frame(Tag tagG, byte[] dataG) {
        tag = tagG;
        data = dataG;
    }
}