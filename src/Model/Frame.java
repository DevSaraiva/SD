package Model;

import java.util.List;

public class Frame {

    public enum Tag {
        QUIT, LOGIN, SIGNUP, LOGOUT,;
    }

    public final Tag tag; // identifica da frame
    public final List<byte[]> data; // list das várias infos a enviar de forma agnóstica

    public Frame(Tag tagG, List<byte[]> dataG) {
        tag = tagG;
        data = dataG;
    }
}