import java.time.LocalDate;

//テスト結果のDAO
public class ExamResult {
    private String resultId;
    private String examId;
    private int score;
    private int studyHours;
    private LocalDate testDate;

    public ExamResult(String resultId, String examId, int score, int studyHours, LocalDate testDate) {
        this.resultId = resultId;
        this.examId = examId;
        this.score = score;
        this.studyHours = studyHours;
        this.testDate = testDate;
    }

    public String getResultId() {
        return resultId;
    }

    public String getExamId() {
        return examId;
    }

    public int getScore() {
        return score;
    }

    public int getStudyHours() {
        return studyHours;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    @Override
    public String toString() {
        return String.format("成績: %d点 | 学習時間: %d時間 | 受験日: %s", score, studyHours, testDate);
    }
}
