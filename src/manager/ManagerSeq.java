package manager;

public class ManagerSeq {

    private Integer seq = 0;

    public int getSeq() {
        return seq;
    }

    public int getNextSeq() {
        return ++seq;
    }
}
