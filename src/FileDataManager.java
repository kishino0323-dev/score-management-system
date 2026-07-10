import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDataManager {
    private static final String DATA_DIR = "data";
    private static final String EXAMS_FILE = "data/exams.txt";
    private static final String RESULTS_FILE = "data/results.txt";

    private static void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void saveExams(List<Exam> exams) {
        ensureDataDirectory();
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXAMS_FILE))) {
            for (Exam exam : exams) {
                writer.println(
                        exam.getId() + "|" + exam.getName() + "|" + exam.getDescription() + "|" + exam.getGoukakuTen());
            }
        } catch (IOException e) {
            System.out.println("資格データの保存に失敗しました");
        }
    }

    public static void saveResults(Map<String, List<ExamResult>> resultsByExamId) {
        ensureDataDirectory();
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESULTS_FILE))) {
            for (String examId : resultsByExamId.keySet()) {
                List<ExamResult> results = resultsByExamId.get(examId);
                for (ExamResult result : results) {
                    writer.println(result.getResultId() + "|" + result.getExamId() + "|" + result.getScore() + "|"
                            + result.getStudyHours() + "|" + result.getTestDate());
                }
            }
        } catch (IOException e) {
            System.out.println("成績データの保存に失敗しました");
        }
    }

    public static List<Exam> loadExams() {
        List<Exam> exams = new ArrayList<>();
        File file = new File(EXAMS_FILE);

        if (!file.exists()) {
            return exams;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    String description = parts[2];
                    int targetScore = Integer.parseInt(parts[3]);
                    exams.add(new Exam(id, name, description, targetScore));
                }
            }
        } catch (IOException e) {
            System.out.println("資格データの読み込みに失敗しました");
        }

        return exams;
    }

    public static Map<String, List<ExamResult>> loadResults() {
        Map<String, List<ExamResult>> resultsByExamId = new HashMap<>();
        File file = new File(RESULTS_FILE);

        if (!file.exists()) {
            return resultsByExamId;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String resultId = parts[0];
                    String examId = parts[1];
                    int score = Integer.parseInt(parts[2]);
                    int studyHours = Integer.parseInt(parts[3]);
                    LocalDate testDate = LocalDate.parse(parts[4]);

                    ExamResult result = new ExamResult(resultId, examId, score, studyHours, testDate);
                    List<ExamResult> results = resultsByExamId.get(examId);
                    if (results == null) {
                        results = new ArrayList<>();
                        resultsByExamId.put(examId, results);
                    }
                    results.add(result);
                }
            }
        } catch (IOException e) {
            System.out.println("成績データの読み込みに失敗しました");
        }

        return resultsByExamId;
    }
}
