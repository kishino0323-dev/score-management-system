import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// 成績管理システムの Swing UI

public class ScoreManagementUI {
    private final ExamManager examManager;
    private final CardLayout cardLayout;
    private final JPanel cardsPanel;
    private final JLabel statusLabel;
    private final JLabel selectedExamLabel;
    private final JLabel averageLabel;
    private final JLabel totalHoursLabel;
    private final JTextField scoreField;
    private final JTextField studyHoursField;
    private final JTextField examNameField;
    private final JTextArea fileSummaryArea;
    private final JList<Exam> examList;
    private Exam selectedExam;

    public ScoreManagementUI() {
        this.examManager = new ExamManager();
        this.cardLayout = new CardLayout();
        this.cardsPanel = new JPanel(cardLayout);
        this.statusLabel = new JLabel("資格を選択してください。", JLabel.CENTER);
        this.selectedExamLabel = new JLabel("未選択");
        this.averageLabel = new JLabel("0.00点");
        this.totalHoursLabel = new JLabel("0時間");
        this.scoreField = new JTextField(10);
        this.studyHoursField = new JTextField(10);
        this.examNameField = new JTextField(20);
        this.fileSummaryArea = new JTextArea();
        this.examList = new JList<>();
        this.selectedExam = null;
    }

    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("成績管理システム");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(850, 550);
                frame.setLocationRelativeTo(null);

                JPanel navigationPanel = new JPanel();
                navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JButton topButton = new JButton("トップ画面");
                JButton scoreButton = new JButton("成績画面");
                JButton fileButton = new JButton("ファイル画面");

                topButton.addActionListener(e -> showCard("top"));
                scoreButton.addActionListener(e -> showCard("score"));
                fileButton.addActionListener(e -> showCard("file"));

                navigationPanel.add(topButton);
                navigationPanel.add(scoreButton);
                navigationPanel.add(fileButton);

                cardsPanel.add(createTopPanel(), "top");
                cardsPanel.add(createScorePanel(), "score");
                cardsPanel.add(createFilePanel(), "file");

                frame.add(navigationPanel, BorderLayout.NORTH);
                frame.add(cardsPanel, BorderLayout.CENTER);
                frame.add(statusLabel, BorderLayout.SOUTH);
                frame.setVisible(true);
            }
        });
    }

    private void showCard(String name) {
        cardLayout.show(cardsPanel, name);
        if (selectedExam != null) {
            updateStatistics();
        }
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("トップ画面", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(20f));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.add(new JLabel("登録済み資格一覧"), BorderLayout.NORTH);

        DefaultListModel<Exam> model = new DefaultListModel<>();
        for (Exam exam : examManager.getAllExams()) {
            model.addElement(exam);
        }
        examList.setModel(model);
        examList.setVisibleRowCount(8);
        examList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Exam selected = examList.getSelectedValue();
                if (selected != null) {
                    selectExam(selected);
                }
            }
        });
        leftPanel.add(new JScrollPane(examList), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        rightPanel.add(new JLabel("資格名を入力して登録"), gbc);
        gbc.gridy++;
        rightPanel.add(examNameField, gbc);

        gbc.gridy++;
        JButton registerButton = new JButton("資格を登録");
        registerButton.addActionListener(e -> registerExam());
        rightPanel.add(registerButton, gbc);

        gbc.gridy++;
        JButton goToScoreButton = new JButton("成績入力画面へ");
        goToScoreButton.addActionListener(e -> {
            if (selectedExam != null) {
                updateStatistics();
                showCard("score");
            } else {
                statusLabel.setText("資格を選択してください。");
            }
        });
        rightPanel.add(goToScoreButton, gbc);

        panel.add(title, BorderLayout.NORTH);
        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createScorePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("成績表示画面"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("選択中の資格: "), gbc);
        gbc.gridx = 1;
        panel.add(selectedExamLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("得点"), gbc);
        gbc.gridx = 1;
        panel.add(scoreField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("勉強時間（時間）"), gbc);
        gbc.gridx = 1;
        panel.add(studyHoursField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("平均点"), gbc);
        gbc.gridx = 1;
        panel.add(averageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("累計勉強時間"), gbc);
        gbc.gridx = 1;
        panel.add(totalHoursLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JButton saveButton = new JButton("記録して保存");
        saveButton.addActionListener(e -> saveCurrentResult());
        panel.add(saveButton, gbc);

        gbc.gridx = 1;
        JButton fileButton = new JButton("ファイル画面へ");
        fileButton.addActionListener(e -> showCard("file"));
        panel.add(fileButton, gbc);

        return panel;
    }

    private JPanel createFilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        infoPanel.add(new JLabel("ファイルパネル"), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel("保存内容"), gbc);
        gbc.gridy++;
        fileSummaryArea.setEditable(false);
        fileSummaryArea.setRows(8);
        fileSummaryArea.setLineWrap(true);
        fileSummaryArea.setWrapStyleWord(true);
        infoPanel.add(new JScrollPane(fileSummaryArea), gbc);

        JButton saveFileButton = new JButton("今回の入力をファイルに保存");
        saveFileButton.addActionListener(e -> saveCurrentResult());

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(saveFileButton, BorderLayout.SOUTH);
        return panel;
    }

    private void registerExam() {
        String name = examNameField.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setText("資格名を入力してください。");
            return;
        }

        Exam newExam = new Exam("EXAM" + System.currentTimeMillis(), name, name, 60);
        examManager.addExam(newExam);
        refreshExamList();
        examList.setSelectedValue(newExam, true);
        selectExam(newExam);
        examNameField.setText("");
        statusLabel.setText("資格を登録しました: " + name);
    }

    private void saveCurrentResult() {
        if (selectedExam == null) {
            statusLabel.setText("まず資格を選択してください。");
            return;
        }

        try {
            int score = Integer.parseInt(scoreField.getText().trim());
            int studyHours = Integer.parseInt(studyHoursField.getText().trim());
            examManager.recordResult(selectedExam.getId(), score, studyHours, LocalDate.now());
            updateStatistics();
            updateFileSummary();
            statusLabel.setText("保存しました: " + selectedExam.getName());
        } catch (NumberFormatException ex) {
            statusLabel.setText("得点と勉強時間は数値で入力してください。");
        }
    }

    private void selectExam(Exam exam) {
        this.selectedExam = exam;
        selectedExamLabel.setText(exam != null ? exam.getName() : "未選択");
        updateStatistics();
        updateFileSummary();
    }

    private void refreshExamList() {
        DefaultListModel<Exam> model = new DefaultListModel<>();
        for (Exam exam : examManager.getAllExams()) {
            model.addElement(exam);
        }
        examList.setModel(model);
    }

    private void updateStatistics() {

        if (selectedExam == null) {
            return;
        }

        List<ExamResult> results = examManager.getResults(
                selectedExam.getId());

        int totalScore = 0;
        int totalHours = 0;

        for (ExamResult result : results) {

            totalScore += result.getScore();
            totalHours += result.getStudyHours();
        }

        double average = 0;

        if (results.size() > 0) {

            average = (double) totalScore
                    / results.size();
        }

        averageLabel.setText(
                String.format(
                        "%.2f点",
                        average));

        totalHoursLabel.setText(
                totalHours + "時間");
    }

    private void updateFileSummary() {
        if (selectedExam == null) {
            fileSummaryArea.setText("資格が選択されていません。\nトップ画面から資格を選択してください。");
            return;
        }

        // List<ExamResult> results = examManager.getResults(selectedExam.getId());
        StringBuilder br = new StringBuilder();
        br.append("資格名: ").append(selectedExam.getName()).append("\n");
        br.append("目標点: ").append(selectedExam.getGoukakuTen()).append("点\n");
        br.append("平均点: ").append(String.format("%.2f", examManager.calculateAverageScore(selectedExam.getId())))
                .append("点\n");
        br.append("累計勉強時間: ").append(examManager.getTotalStudyHours(selectedExam.getId())).append("時間\n");
        br.append("保存先: data/results.txt");
        fileSummaryArea.setText(br.toString());
    }
}