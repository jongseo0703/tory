package com.sinse.tory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductTest extends JFrame {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int BOX_SIZE = 80;
    private static final int DELAY = 30;

    private JPanel[] boxes;
    private Color[] colors;
    private int currentIndex = 0;

    public ProductTest() {
        setTitle("Centered Color Box Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 창 띄우기

        // 바깥쪽 컨테이너: BorderLayout
        setLayout(new BorderLayout());

        // 중앙에 위치할 박스 그리드 패널
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(ROWS, COLS, 2, 2));
        gridPanel.setPreferredSize(new Dimension(COLS * BOX_SIZE, ROWS * BOX_SIZE));
        gridPanel.setOpaque(false); // 배경 투명

        int totalBoxes = ROWS * COLS;
        boxes = new JPanel[totalBoxes];
        colors = new Color[totalBoxes];

        for (int i = 0; i < totalBoxes; i++) {
            boxes[i] = new JPanel();
            boxes[i].setPreferredSize(new Dimension(BOX_SIZE, BOX_SIZE));
            boxes[i].setBackground(Color.LIGHT_GRAY);
            gridPanel.add(boxes[i]);

            colors[i] = new Color(
                    (int)(Math.random() * 256),
                    (int)(Math.random() * 256),
                    (int)(Math.random() * 256)
            );
        }

        // 내부를 중앙 아래에 가깝게 배치하기 위한 패널
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 100));
        wrapperPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1; // 중앙보다 약간 아래
        gbc.anchor = GridBagConstraints.NORTH; // 위 기준 정렬
        gbc.insets = new Insets(100, 0, 50, 0); // 위쪽 여백 늘려서 아래쪽에 가깝게

        wrapperPanel.add(gridPanel, gbc);

        add(wrapperPanel, BorderLayout.CENTER);
        
        setSize(1440, 1024);

        Timer timer = new Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < boxes.length) {
                    boxes[currentIndex].setBackground(colors[currentIndex]);
                    currentIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductTest animation = new ProductTest();
            animation.setVisible(true);
        });
    }
}