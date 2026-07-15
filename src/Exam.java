import java.util.Objects;

/**
 * 資格試験情報を表すクラス
 */
public class Exam {
    private String id;
    private String name;
    private String description;
    private int goukakuTen; // 合格目標点

    public Exam(String id, String name, String description, int goukakuTen) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.goukakuTen = goukakuTen;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getGoukakuTen() {
        return goukakuTen;
    }

    public void setGoukakuTen(int goukakuTen) {
        this.goukakuTen = goukakuTen;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (目標点: %d点)", id, name, goukakuTen);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Exam exam = (Exam) o;
        return Objects.equals(id, exam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}