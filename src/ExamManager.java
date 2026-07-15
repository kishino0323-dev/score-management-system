import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamManager {
    private List<Exam> exams;
    private Map<String, List<ExamResult>> resultsByExamId;

    public ExamManager() {
        exams = new ArrayList<>();
        resultsByExamId = new HashMap<>();

        List<Exam> loadedExams = FileDataManager.loadExams();
        if (loadedExams.size() > 0) {
            exams.addAll(loadedExams);
            for (Exam exam : exams) {
                resultsByExamId.put(exam.getId(), new ArrayList<>());
            }
        } else {
            createDefaultExams();
            FileDataManager.saveExams(exams);
        }

        Map<String, List<ExamResult>> loadedResults = FileDataManager.loadResults();
        for (String examId : loadedResults.keySet()) {
            resultsByExamId.put(examId, loadedResults.get(examId));
        }
    }

    private void createDefaultExams() {
        addExam(new Exam("IPA001", "基本情報技術者試験", "基本情報技術者試験", 60));
        addExam(new Exam("IPA002", "応用情報技術者試験", "応用情報技術者試験", 65));
        addExam(new Exam("IPA003", "情報処理安全確保支援士試験", "情報処理安全確保支援士試験", 65));
        addExam(new Exam("ORACLE001", "Oracle Database認定資格", "Oracle Database", 70));
        addExam(new Exam("AWS001", "AWS Certified Solutions Architect", "AWS", 72));
        addExam(new Exam("JAVA001", "Oracle Certified Associate Java Programmer", "Java", 65));
        addExam(new Exam("JAVA002", "Java Silver", "Java", 39));
    }

    public void addExam(Exam exam) {
        exams.add(exam);
        resultsByExamId.put(exam.getId(), new ArrayList<>());
        FileDataManager.saveExams(exams);
    }

    public void recordResult(String examId, int score, int studyHours, LocalDate testDate) {
        Exam exam = findExam(examId);
        if (exam == null) {
            return;
        }

        List<ExamResult> results = resultsByExamId.get(examId);
        if (results == null) {
            results = new ArrayList<>();
            resultsByExamId.put(examId, results);
        }

        String resultId = examId + "_" + System.currentTimeMillis();
        ExamResult result = new ExamResult(resultId, examId, score, studyHours, testDate);
        results.add(result);
        saveData();
    }

    public List<ExamResult> getResults(String examId) {
        List<ExamResult> results = resultsByExamId.get(examId);
        if (results == null) {
            return new ArrayList<>();
        }
        return results;
    }

    public List<Exam> getAllExams() {
        return new ArrayList<>(exams);
    }

    private Exam findExam(String examId) {
        for (Exam exam : exams) {
            if (exam.getId().equals(examId)) {
                return exam;
            }
        }
        return null;
    }

    public void saveData() {
        FileDataManager.saveExams(exams);
        FileDataManager.saveResults(resultsByExamId);
    }

    public double calculateAverageScore(String examId) {
        List<ExamResult> results = getResults(examId);
        if (results.size() == 0) {
            return 0;
        }

        int sum = 0;
        for (ExamResult result : results) {
            sum += result.getScore();
        }
        return (double) sum / results.size();
    }

    public int getMaxScore(String examId) {
        List<ExamResult> results = getResults(examId);
        if (results.size() == 0) {
            return 0;
        }

        int max = results.get(0).getScore();
        for (ExamResult result : results) {
            if (result.getScore() > max) {
                max = result.getScore();
            }
        }
        return max;
    }

    public int getMinScore(String examId) {
        List<ExamResult> results = getResults(examId);
        if (results == null || results.isEmpty()) {
            return 0;
        }

        int min = results.get(0).getScore();
        for (ExamResult result : results) {
            if (result.getScore() < min) {
                min = result.getScore();
            }
        }
        return min;
    }

    public int getTotalStudyHours(String examId) {
        List<ExamResult> results = getResults(examId);

        int total = 0;
        for (ExamResult result : results) {
            total += result.getStudyHours();
        }
        return total;
    }

    public List<Exam> searchByKeyword(List<Exam> exams, String keyword) {
        List<Exam> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Exam exam : exams) {
            if (exam.getName().toLowerCase().contains(lowerKeyword) ||
                    exam.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(exam);
            }
        }

        return results;
    }

    public Exam searchById(List<Exam> exams, String id) {
        for (Exam exam : exams) {
            if (exam.getId().equals(id)) {
                return exam;
            }
        }
        return null;
    }
}