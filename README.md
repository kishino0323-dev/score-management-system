# 成績管理システム

資格試験の勉強記録と成績を管理するJavaアプリケーションです。

## 機能

### 1. 資格名検索
- 部分一致検索で資格を検索
- 例：「基本情報」と入力すると「基本情報技術者試験」がヒット

### 2. 成績記録
- 資格ごとに得点を記録
- 学習時間を記録
- 受験日を記録

### 3. 合格目標点の設定
- 各資格ごとに目標点を設定可能
- デフォルト資格には初期値が設定されています

### 4. 成績確認
- 記録した成績を確認
- 資格ごとの成績一覧を表示

### 5. 統計情報
- **平均点**：記録した成績の平均値
- **最高得点**：最も高い得点
- **最低得点**：最も低い得点
- **成績記録数**：記録した試験の数
- **合計学習時間**：すべての試験の学習時間の合計

### 6. データの永続化
- すべての資格情報と成績は自動的にファイルに保存されます
- アプリを終了して再度起動しても、以前の記録が保持されます
- ファイルは `data/` ディレクトリに保存されます

## デフォルト資格一覧

| ID | 資格名 | 目標点 |
|---|---|---|
| IPA001 | 基本情報技術者試験 | 60 |
| IPA002 | 応用情報技術者試験 | 65 |
| IPA003 | 情報処理安全確保支援士試験 | 65 |
| ORACLE001 | Oracle Database認定資格 | 70 |
| AWS001 | AWS Certified Solutions Architect | 72 |
| JAVA001 | Oracle Certified Associate Java Programmer | 65 |
| TOEIC001 | TOEIC | 600 |
| EIKEN001 | 実用英語技能検定 | 75 |

## 使い方

### コンパイル

```bash
cd src
javac -encoding UTF-8 *.java
```

### 実行

```bash
java Main
```

### 操作方法

1. **メインメニュー**から操作を選択
2. **資格を検索**：資格名を入力して検索
3. **成績を記録**：資格を選択して成績、学習時間、受験日を入力
4. **成績を確認**：資格の成績一覧を表示
5. **統計情報**：成績の統計情報を表示

## クラス構成

### Exam.java
- 資格情報を管理するクラス
- 資格ID、名前、説明、目標点を保持

### ExamResult.java
- 試験成績情報を管理するクラス
- 成績、学習時間、受験日を保持

### ExamManager.java
- 資格と成績の一元管理
- 資格の追加、成績の記録

### ExamSearchService.java
- 資格の検索機能を提供
- キーワードによる部分一致検索

### FileDataManager.java
- ファイルI/O処理を担当
- 資格データと成績データを保存・読み込み
- CSV形式でファイルに永続化

### StatisticsService.java
- 成績の統計情報を計算
- 平均点、最高点、最低点、学習時間の合計

### ScoreManagementUI.java
- ユーザーインターフェース
- メニュー操作、入出力処理

### Main.java
- アプリケーションのエントリーポイント

## 日付形式

受験日の入力時は以下の形式を使用してください：

```
yyyy-MM-dd
例：2024-07-08
```

## 資格を追加する方法

### 方法1：プログラムコード内に追加（推奨）

`ExamManager.java` の `initializeExams()` メソッドに新しい資格を追加します。

**ステップ1：** `ExamManager.java` をテキストエディタで開く

**ステップ2：** 以下の部分を見つけて編集

```java
private void initializeExams() {
    addExam(new Exam("IPA001", "基本情報技術者試験", "基本情報技術者試験", 60));
    // ↓ この下に新しい資格を追加
    addExam(new Exam("ORACLE001", "Oracle Database認定資格", "Oracle Database", 70));
    // ...
}
```

**ステップ3：** 新しい資格を追加

```java
// 例：Linux認定資格を追加する場合
addExam(new Exam("LPI001", "LPIC-1 Linux認定試験", "Linux", 70));
```

### 資格追加の例

```java
// プログラミング言語系
addExam(new Exam("PYTHON001", "Python 3認定試験", "Python", 70));
addExam(new Exam("CPP001", "C++ 認定試験", "C++", 70));

// データベース系
addExam(new Exam("SQL001", "SQL 認定試験", "SQL", 65));
addExam(new Exam("MYSQL001", "MySQL認定試験", "MySQL", 65));

// クラウド系
addExam(new Exam("GCP001", "Google Cloud認定", "GCP", 70));
addExam(new Exam("AZURE001", "Microsoft Azure認定", "Azure", 70));
```

### 資格IDの命名規則（推奨）

| 分類 | 接頭辞 | 例 |
|---|---|---|
| IPA（情報処理推進機構） | IPA | IPA001 |
| クラウド | クラウド名+番号 | AWS001, GCP001 |
| データベース | DB名+番号 | ORACLE001, MYSQL001 |
| プログラミング言語 | 言語名+番号 | JAVA001, PYTHON001 |
| 言語系試験 | 試験名+番号 | TOEIC001, EIKEN001 |

### コンパイルと実行

新しい資格を追加した後は、再度コンパイルが必要です：

```bash
cd c:\Users\kou_k\OneDrive\Desktop\java\score-management-system\src
javac -encoding UTF-8 *.java
java Main
```

またはデスクトップの「成績管理システム.bat」を実行してください。

## データ保存について

### ファイル構成

```
score-management-system/
├── src/               # ソースコード
├── data/              # データファイル（自動生成）
│   ├── exams.txt      # 資格データ
│   └── results.txt    # 成績データ
└── README.md
```

### データファイルの形式

**exams.txt** - 資格情報（CSV形式）
```
資格ID|資格名|説明|目標点
IPA001|基本情報技術者試験|基本情報技術者試験|60
```

**results.txt** - 成績データ（CSV形式）
```
成績ID|資格ID|得点|学習時間|受験日
IPA001_1234567890|IPA001|75|50|2024-07-01
```

### バックアップ

データを安全に保つため、定期的に `data/` ディレクトリをバックアップすることをお勧めします。

## 今後の拡張機能

- データの永続化（ファイル保存）
- 合格/不合格判定
- グラフによる成績推移表示
- GUI化
- 複数ユーザー対応
- 実行時に資格を追加できるUI機能
