
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.io.*;

public class MainFrame extends JFrame {

    JTextField dateField = new JTextField();
    JTextField sleepField = new JTextField();
    JTextField stepsField = new JTextField();
    JTextField moodField = new JTextField();

    JLabel resultLabel = new JLabel("目前風險：");

    DefaultTableModel model;
    JTable table;

    public MainFrame() {

        setTitle("智慧健康日誌與風險評估系統");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== 標題 =====
        JLabel title = new JLabel(
                "智慧健康日誌與風險評估系統",
                SwingConstants.CENTER);

        title.setFont(
                new Font(
                        "微軟正黑體",
                        Font.BOLD,
                        28));

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        0,
                        15,
                        0));

        add(title, BorderLayout.NORTH);

        // ===== 輸入區 =====

        JPanel inputPanel =
                new JPanel(
                        new GridLayout(
                                6,
                                2,
                                10,
                                10));

        inputPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20));

        dateField.setText(
                LocalDate.now().toString());

        inputPanel.add(new JLabel("日期"));
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("睡眠時數"));
        inputPanel.add(sleepField);

        inputPanel.add(new JLabel("步數"));
        inputPanel.add(stepsField);

        inputPanel.add(new JLabel("心情分數 (1~10)"));
        inputPanel.add(moodField);

        JButton addBtn =
                new JButton("新增紀錄");

        addBtn.setFont(
                new Font(
                        "微軟正黑體",
                        Font.BOLD,
                        16));

        addBtn.setBackground(
                new Color(
                        52,
                        152,
                        219));

        addBtn.setForeground(
                Color.WHITE);

        JButton clearBtn =
                new JButton("清空輸入");

        clearBtn.setFont(
                new Font(
                        "微軟正黑體",
                        Font.BOLD,
                        16));

        clearBtn.setBackground(
                new Color(
                        231,
                        76,
                        60));

        clearBtn.setForeground(
                Color.WHITE);

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(addBtn);
        buttonPanel.add(clearBtn);

        inputPanel.add(buttonPanel);

        resultLabel.setFont(
                new Font(
                        "微軟正黑體",
                        Font.BOLD,
                        22));

        inputPanel.add(resultLabel);

        add(inputPanel, BorderLayout.WEST);

        // ===== 表格 =====

        model =
                new DefaultTableModel();

        model.addColumn("日期");
        model.addColumn("睡眠");
        model.addColumn("步數");
        model.addColumn("心情");
        model.addColumn("風險");

        table =
                new JTable(model);

        table.setRowHeight(30);

        table.setFont(
                new Font(
                        "微軟正黑體",
                        Font.PLAIN,
                        14));

        table.getTableHeader()
                .setFont(
                        new Font(
                                "微軟正黑體",
                                Font.BOLD,
                                16));

        loadFileData();

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        // ===== 新增按鈕 =====

        addBtn.addActionListener(e -> {

            try {

                String date =
                        dateField.getText();

                double sleep =
                        Double.parseDouble(
                                sleepField.getText());

                int steps =
                        Integer.parseInt(
                                stepsField.getText());

                int mood =
                        Integer.parseInt(
                                moodField.getText());

                if (sleep < 0 || sleep > 24) {

                    JOptionPane.showMessageDialog(
                            null,
                            "睡眠時數必須介於 0~24");

                    return;
                }

                if (steps < 0) {

                    JOptionPane.showMessageDialog(
                            null,
                            "步數不能小於 0");

                    return;
                }

                if (mood < 1 || mood > 10) {

                    JOptionPane.showMessageDialog(
                            null,
                            "心情分數必須介於 1~10");

                    return;
                }

                String risk =
        HealthService
                .processHealthData(
                        date,
                        sleep,
                        steps,
                        mood);

                resultLabel.setText(
                        "目前風險：" + risk);

                if (risk.equals("LOW")) {

                    resultLabel.setForeground(
                            Color.GREEN);

                } else if (
                        risk.equals("MEDIUM")) {

                    resultLabel.setForeground(
                            Color.ORANGE);

                } else {

                    resultLabel.setForeground(
                            Color.RED);
                }

                model.addRow(
                        new Object[] {

                                date,
                                sleep,
                                steps,
                                mood,
                                risk
                        });

                

                sleepField.setText("");
                stepsField.setText("");
                moodField.setText("");

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "請輸入正確數值");
            }
        });

        // ===== 清空 =====

        clearBtn.addActionListener(e -> {

            sleepField.setText("");
            stepsField.setText("");
            moodField.setText("");
        });

        setVisible(true);
    }

    private void saveToFile(
            String date,
            double sleep,
            int steps,
            int mood,
            String risk) {

        try {

            FileWriter writer =
                    new FileWriter(
                            "health_logs.csv",
                            true);

            writer.write(
                    date + "," +
                    sleep + "," +
                    steps + "," +
                    mood + "," +
                    risk + "\n");

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void loadFileData() {

        try {

            File file =
                    new File(
                            "health_logs.csv");

            if (!file.exists()) {

                return;
            }

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data =
                        line.split(",");

                if (data.length == 5) {

                    model.addRow(
                            new Object[] {

                                    data[0],
                                    data[1],
                                    data[2],
                                    data[3],
                                    data[4]
                            });
                }
            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}