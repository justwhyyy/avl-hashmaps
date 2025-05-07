package test;

import main.datastructure.HashTableWithAVL;
import main.hash.HashFunction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class HashMapVisualizer extends JFrame {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static final int NUM_OPERATIONS = 10000; 
    private static final int NUM_RUNS = 3; 
    
    private JPanel chartPanel;
    private JPanel controlPanel;
    private JComboBox<String> testSelector;
    private JButton runButton;
    
    private static class PerformanceResult {
        double avlTime;
        double hashMapTime;
        double ratio;
        
        PerformanceResult(double avlTime, double hashMapTime) {
            this.avlTime = avlTime;
            this.hashMapTime = hashMapTime;
            this.ratio = avlTime / hashMapTime;
        }
    }
    
    public HashMapVisualizer() {
        setTitle("Hash Map Performance Comparison");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //init panels
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawString("Run a test to see results", getWidth() / 2 - 80, getHeight() / 2);
            }
        };
        chartPanel.setLayout(new BorderLayout());
        
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        
        //adding test selector
        String[] testOptions = {
            "Random Insertion", 
            "Sequential Insertion", 
            "Search Performance", 
            "Deletion Performance", 
            "Collision Handling", 
            "Search Under Collisions",
            "All Tests"
        };
        testSelector = new JComboBox<>(testOptions);
        
        //adding run button
        runButton = new JButton("Run Test");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runTest();
            }
        });
        
        //adding components to control panel
        controlPanel.add(new JLabel("Select Test: "));
        controlPanel.add(testSelector);
        controlPanel.add(runButton);
        
        //adding panels to frame
        add(chartPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void runTest() {
        runButton.setEnabled(false);
        testSelector.setEnabled(false);
        
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String selectedTest = (String) testSelector.getSelectedItem();
                    
                    if ("Random Insertion".equals(selectedTest)) {
                        PerformanceResult result = randomInsertionTest();
                        showBarChart("Random Insertion Performance", result);
                    } else if ("Sequential Insertion".equals(selectedTest)) {
                        PerformanceResult result = sequentialInsertionTest();
                        showBarChart("Sequential Insertion Performance", result);
                    } else if ("Search Performance".equals(selectedTest)) {
                        PerformanceResult result = searchTest();
                        showBarChart("Search Performance", result);
                    } else if ("Deletion Performance".equals(selectedTest)) {
                        PerformanceResult result = deletionTest();
                        showBarChart("Deletion Performance", result);
                    } else if ("Collision Handling".equals(selectedTest)) {
                        PerformanceResult result = collisionTest();
                        showBarChart("Collision Handling Performance", result);
                    } else if ("Search Under Collisions".equals(selectedTest)) {
                        PerformanceResult result = searchUnderCollisionsTest();
                        showBarChart("Search Under Collisions Performance", result);
                    } else if ("All Tests".equals(selectedTest)) {
                        showAllTests();
                    }
                } finally {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            runButton.setEnabled(true);
                            testSelector.setEnabled(true);
                        }
                    });
                }
            }
        });
        
        testThread.start();
    }
    
    private PerformanceResult randomInsertionTest() {
        System.out.println("\n===== Random Insertion Performance Test =====");
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
            HashMap<Integer, String> standardHashMap = new HashMap<>();
            
            Random random = new Random(run);
            
            //measure AVL HashMap insertion time
            long startTime = System.nanoTime();
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                int key = random.nextInt(NUM_OPERATIONS * 10);
                avlHashTable.insert(key, "Value-" + key);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap insertion time
            startTime = System.nanoTime();
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                int key = random.nextInt(NUM_OPERATIONS * 10);
                standardHashMap.put(key, "Value-" + key);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        double avgAVLTime = totalAVLTime / (NUM_RUNS * 1_000_000.0);
        double avgHashMapTime = totalHashMapTime / (NUM_RUNS * 1_000_000.0);
        
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            avgAVLTime, avgHashMapTime);
        System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap%n",
            avgAVLTime / avgHashMapTime);
            
        return new PerformanceResult(avgAVLTime, avgHashMapTime);
    }
    
    private PerformanceResult sequentialInsertionTest() {
        System.out.println("\n===== Sequential Insertion Performance Test =====");
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
            HashMap<Integer, String> standardHashMap = new HashMap<>();
            
            //measure AVL HashMap insertion time
            long startTime = System.nanoTime();
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                avlHashTable.insert(i, "Value-" + i);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap insertion time
            startTime = System.nanoTime();
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                standardHashMap.put(i, "Value-" + i);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        double avgAVLTime = totalAVLTime / (NUM_RUNS * 1_000_000.0);
        double avgHashMapTime = totalHashMapTime / (NUM_RUNS * 1_000_000.0);
        
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            avgAVLTime, avgHashMapTime);
        System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap%n",
            avgAVLTime / avgHashMapTime);
            
        return new PerformanceResult(avgAVLTime, avgHashMapTime);
    }
    
    private PerformanceResult searchTest() {
        System.out.println("\n===== Search Performance Test =====");
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
            HashMap<Integer, String> standardHashMap = new HashMap<>();
            
            //prep data
            Random random = new Random(run);
            List<Integer> keys = new ArrayList<>();
            
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                int key = random.nextInt(NUM_OPERATIONS * 10);
                avlHashTable.insert(key, "Value-" + key);
                standardHashMap.put(key, "Value-" + key);
                
                //saving keys for search test (50% chance)
                if (random.nextBoolean()) {
                    keys.add(key);
                }
            }
            
            //adding some non-existent keys
            for (int i = 0; i < NUM_OPERATIONS / 10; i++) {
                keys.add(NUM_OPERATIONS * 10 + i);
            }
            
            //measuring AVL HashMap search time
            long startTime = System.nanoTime();
            for (Integer key : keys) {
                avlHashTable.search(key);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measuring standard HashMap search time
            startTime = System.nanoTime();
            for (Integer key : keys) {
                standardHashMap.get(key);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        double avgAVLTime = totalAVLTime / (NUM_RUNS * 1_000_000.0);
        double avgHashMapTime = totalHashMapTime / (NUM_RUNS * 1_000_000.0);
        
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            avgAVLTime, avgHashMapTime);
        System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap%n",
            avgAVLTime / avgHashMapTime);
            
        return new PerformanceResult(avgAVLTime, avgHashMapTime);
    }
    
    private PerformanceResult deletionTest() {
        System.out.println("\n===== Deletion Performance Test =====");
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            HashTableWithAVL<Integer, String> avlHashTable = new HashTableWithAVL<>();
            HashMap<Integer, String> standardHashMap = new HashMap<>();
            
            //prep data
            Random random = new Random(run);
            List<Integer> keys = new ArrayList<>();
            
            for (int i = 0; i < NUM_OPERATIONS; i++) {
                int key = random.nextInt(NUM_OPERATIONS * 10);
                avlHashTable.insert(key, "Value-" + key);
                standardHashMap.put(key, "Value-" + key);
                
                //save keys for deletion test (33% chance)
                if (random.nextInt(3) == 0) {
                    keys.add(key);
                }
            }
            
            //measure AVL HashMap deletion time
            long startTime = System.nanoTime();
            for (Integer key : keys) {
                avlHashTable.delete(key);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap deletion time
            startTime = System.nanoTime();
            for (Integer key : keys) {
                standardHashMap.remove(key);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        double avgAVLTime = totalAVLTime / (NUM_RUNS * 1_000_000.0);
        double avgHashMapTime = totalHashMapTime / (NUM_RUNS * 1_000_000.0);
        
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            avgAVLTime, avgHashMapTime);
        System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap%n",
            avgAVLTime / avgHashMapTime);
            
        return new PerformanceResult(avgAVLTime, avgHashMapTime);
    }
    
    private PerformanceResult collisionTest() {
        System.out.println("\n===== Collision Handling Test =====");
        
        //create a bad hash function that always returns the same value
        class BadHashFunction implements HashFunction<Integer> {
            @Override
            public int hash(Integer key) {
                return 42; //always returns the same hash
            }
        }
        
        //create class for bad hash keys in standard HashMap
        class BadHashKey {
            private final int value;
            
            public BadHashKey(int value) {
                this.value = value;
            }
            
            @Override
            public int hashCode() {
                return 42; 
            }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                BadHashKey other = (BadHashKey) obj;
                return value == other.value;
            }
        }
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            //reset tables
            HashTableWithAVL<Integer, String> avlHashTable = 
                new HashTableWithAVL<Integer, String>(16, 0.75, new BadHashFunction());

            HashMap<BadHashKey, String> standardHashMap = new HashMap<>();

            int collisionOps = NUM_OPERATIONS / 10;
            
            //measure AVL HashMap with collisions
            long startTime = System.nanoTime();
            for (int i = 0; i < collisionOps; i++) {
                avlHashTable.insert(i, "Value-" + i);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap with collisions
            startTime = System.nanoTime();
            for (int i = 0; i < collisionOps; i++) {
                standardHashMap.put(new BadHashKey(i), "Value-" + i);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        double avgAVLTime = totalAVLTime / (NUM_RUNS * 1_000_000.0);
        double avgHashMapTime = totalHashMapTime / (NUM_RUNS * 1_000_000.0);
        
        System.out.printf("Average - AVL HashMap: %.3f ms, Standard HashMap: %.3f ms%n", 
            avgAVLTime, avgHashMapTime);
            
        if (avgAVLTime < avgHashMapTime) {
            System.out.printf("Relative Performance: AVL is %.2f times faster than standard HashMap under collisions%n",
                avgHashMapTime / avgAVLTime);
        } else {
            System.out.printf("Relative Performance: AVL is %.2f times slower than standard HashMap even under collisions%n",
                avgAVLTime / avgHashMapTime);
        }
            
        return new PerformanceResult(avgAVLTime, avgHashMapTime);
    }
    
    private PerformanceResult searchUnderCollisionsTest() {
        System.out.println("\n===== Search Under Collisions Test =====");
        
        //create a bad hash function that always returns the same value
        class BadHashFunction implements HashFunction<Integer> {
            @Override
            public int hash(Integer key) {
                return 42; 
            }
        }
        
        //create class for bad hash keys in standard HashMap
        class BadHashKey {
            private final int value;
            
            public BadHashKey(int value) {
                this.value = value;
            }
            
            @Override
            public int hashCode() {
                return 42; //always returns the same hash
            }
            
            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                BadHashKey other = (BadHashKey) obj;
                return value == other.value;
            }
        }
        
        long totalAVLTime = 0;
        long totalHashMapTime = 0;
        
        for (int run = 0; run < NUM_RUNS; run++) {
            //reset tables
            HashTableWithAVL<Integer, String> avlHashTable = 
                new HashTableWithAVL<Integer, String>(16, 0.75, new BadHashFunction());
            
            //use BadHashKey for standard HashMap
            HashMap<BadHashKey, String> standardHashMap = new HashMap<>();
            
            //reduced operations for collision test
            int collisionOps = NUM_OPERATIONS / 10;
            
            //insert data
            for (int i = 0; i < collisionOps; i++) {
                avlHashTable.insert(i, "Value-" + i);
            }
            
            List<BadHashKey> badKeys = new ArrayList<>();
            for (int i = 0; i < collisionOps; i++) {
                BadHashKey key = new BadHashKey(i);
                standardHashMap.put(key, "Value-" + i);
                badKeys.add(key);
            }
            
            //measure AVL HashMap search time with collisions
            long startTime = System.nanoTime();
            for (int i = 0; i < collisionOps; i++) {
                avlHashTable.search(i);
            }
            long endTime = System.nanoTime();
            long avlTime = endTime - startTime;
            totalAVLTime += avlTime;
            
            //measure standard HashMap search time with collisions
            startTime = System.nanoTime();
            for (BadHashKey key : badKeys) {
                standardHashMap.get(key);
            }
            endTime = System.nanoTime();
            long hashMapTime = endTime - startTime;
            totalHashMapTime += hashMapTime;
            
            System.out.printf("Run %d - AVL HashMap search: %.3f ms, Standard HashMap search: %.3f ms%n", 
                run + 1, avlTime / 1_000_000.0, hashMapTime / 1_000_000.0);
        }
        
        double avgAVLTime = totalAVLTime / (NUM_RUNS * 1_000_000.0);
        double avgHashMapTime = totalHashMapTime / (NUM_RUNS * 1_000_000.0);
        
        System.out.printf("Average - AVL HashMap search: %.3f ms, Standard HashMap search: %.3f ms%n", 
            avgAVLTime, avgHashMapTime);
            
        if (avgAVLTime < avgHashMapTime) {
            System.out.printf("Relative Search Performance: AVL is %.2f times faster than standard HashMap under collisions%n",
                avgHashMapTime / avgAVLTime);
        } else {
            System.out.printf("Relative Search Performance: AVL is %.2f times slower than standard HashMap even under collisions%n",
                avgAVLTime / avgHashMapTime);
        }
            
        return new PerformanceResult(avgAVLTime, avgHashMapTime);
    }
    
    private void showBarChart(String title, PerformanceResult result) {
        JPanel barChart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int padding = 50;
                int chartWidth = getWidth() - 2 * padding;
                int chartHeight = getHeight() - 2 * padding;
                int barWidth = chartWidth / 5;
                
                double maxTime = Math.max(result.avlTime, result.hashMapTime);

                g2d.setColor(Color.BLACK);
                g2d.drawLine(padding, getHeight() - padding, padding, padding);
                g2d.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int titleWidth = fm.stringWidth(title);
                g2d.drawString(title, (getWidth() - titleWidth) / 2, 30);
                
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                g2d.rotate(-Math.PI / 2);
                g2d.drawString("Time (ms)", -getHeight() / 2 - 30, 20);
                g2d.rotate(Math.PI / 2);

                int numYLabels = 5;
                for (int i = 0; i <= numYLabels; i++) {
                    double value = maxTime * i / numYLabels;
                    int y = getHeight() - padding - (int) (chartHeight * i / numYLabels);
                    g2d.drawLine(padding - 5, y, padding, y);
                    g2d.drawString(String.format("%.2f", value), padding - 40, y + 5);
                }
                
                int x1 = padding + chartWidth / 4 - barWidth / 2;
                int height1 = (int) (chartHeight * result.avlTime / maxTime);
                int y1 = getHeight() - padding - height1;
                
                g2d.setColor(new Color(65, 105, 225)); // Royal Blue
                g2d.fillRect(x1, y1, barWidth, height1);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x1, y1, barWidth, height1);
                
                g2d.drawString("AVL HashMap", x1 + barWidth / 2 - 40, getHeight() - padding + 20);
                g2d.drawString(String.format("%.2f ms", result.avlTime), x1 + barWidth / 2 - 30, y1 - 10);
                
                int x2 = padding + 3 * chartWidth / 4 - barWidth / 2;
                int height2 = (int) (chartHeight * result.hashMapTime / maxTime);
                int y2 = getHeight() - padding - height2;
                
                g2d.setColor(new Color(50, 205, 50)); // Lime Green
                g2d.fillRect(x2, y2, barWidth, height2);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x2, y2, barWidth, height2);
                
                g2d.drawString("Standard HashMap", x2 + barWidth / 2 - 60, getHeight() - padding + 20);
                g2d.drawString(String.format("%.2f ms", result.hashMapTime), x2 + barWidth / 2 - 30, y2 - 10);

                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                String ratioText;
                if (result.ratio > 1) {
                    ratioText = String.format("AVL is %.2fx slower than standard HashMap", result.ratio);
                } else {
                    ratioText = String.format("AVL is %.2fx faster than standard HashMap", 1 / result.ratio);
                }
                g2d.drawString(ratioText, (getWidth() - fm.stringWidth(ratioText)) / 2, getHeight() - 10);
            }
        };
        
        barChart.setBackground(Color.WHITE);
        
        chartPanel.removeAll();
        chartPanel.add(barChart, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }
    
    private void showAllTests() {
        final PerformanceResult[] results = new PerformanceResult[6];
        final String[] testNames = {
            "Random Insertion", 
            "Sequential Insertion", 
            "Search", 
            "Deletion", 
            "Collision Handling", 
            "Search Under Collisions"
        };
        
        results[0] = randomInsertionTest();
        results[1] = sequentialInsertionTest();
        results[2] = searchTest();
        results[3] = deletionTest();
        results[4] = collisionTest();
        results[5] = searchUnderCollisionsTest();
        
        JPanel allTestsChart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int padding = 80;
                int chartWidth = getWidth() - 2 * padding;
                int chartHeight = getHeight() - 2 * padding;
                int barWidth = chartWidth / (3 * testNames.length + testNames.length - 1);
                int groupWidth = 3 * barWidth;
                int gap = barWidth;
                
                //calculating max value for scaling
                double maxTime = 0;
                for (PerformanceResult result : results) {
                    maxTime = Math.max(maxTime, Math.max(result.avlTime, result.hashMapTime));
                }
                
 
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String title = "All Performance Tests Comparison";
                FontMetrics fm = g2d.getFontMetrics();
                int titleWidth = fm.stringWidth(title);
                g2d.drawString(title, (getWidth() - titleWidth) / 2, 30);

                g2d.setColor(Color.BLACK);
                g2d.drawLine(padding, getHeight() - padding, padding, padding);
                g2d.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding);

                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                g2d.rotate(-Math.PI / 2);
                g2d.drawString("Time (ms)", -getHeight() / 2 - 30, 20);
                g2d.rotate(Math.PI / 2);

                int numYLabels = 5;
                for (int i = 0; i <= numYLabels; i++) {
                    double value = maxTime * i / numYLabels;
                    int y = getHeight() - padding - (int) (chartHeight * i / numYLabels);
                    g2d.drawLine(padding - 5, y, padding, y);
                    g2d.drawString(String.format("%.2f", value), padding - 40, y + 5);
                }

                for (int i = 0; i < testNames.length; i++) {
                    int groupStartX = padding + i * (groupWidth + gap);

                    int x1 = groupStartX;
                    int height1 = (int) (chartHeight * results[i].avlTime / maxTime);
                    int y1 = getHeight() - padding - height1;
                    
                    g2d.setColor(new Color(65, 105, 225)); // Royal Blue
                    g2d.fillRect(x1, y1, barWidth, height1);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x1, y1, barWidth, height1);

                    int x2 = groupStartX + 2 * barWidth;
                    int height2 = (int) (chartHeight * results[i].hashMapTime / maxTime);
                    int y2 = getHeight() - padding - height2;
                    
                    g2d.setColor(new Color(50, 205, 50)); // Lime Green
                    g2d.fillRect(x2, y2, barWidth, height2);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x2, y2, barWidth, height2);
                    
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    String name = testNames[i];
                    int nameWidth = fm.stringWidth(name);
                    g2d.rotate(-Math.PI / 4);
                    int rotX = (int) (groupStartX + groupWidth / 2 - Math.sqrt(2) * nameWidth / 2);
                    int rotY = (int) (getHeight() - padding + 10 + Math.sqrt(2) * nameWidth / 2);
                    g2d.drawString(name, rotX, rotY);
                    g2d.rotate(Math.PI / 4);

                    g2d.setFont(new Font("Arial", Font.PLAIN, 9));
                    if (height1 > 20) {
                        g2d.drawString(String.format("%.1f", results[i].avlTime), x1 + barWidth / 4, y1 - 2);
                    }
                    if (height2 > 20) {
                        g2d.drawString(String.format("%.1f", results[i].hashMapTime), x2 + barWidth / 4, y2 - 2);
                    }
                }
                
                int legendX = padding;
                int legendY = padding - 20;
                
                g2d.setColor(new Color(65, 105, 225)); // Royal Blue
                g2d.fillRect(legendX, legendY, 15, 15);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(legendX, legendY, 15, 15);
                g2d.drawString("AVL HashMap", legendX + 20, legendY + 12);
                
                legendX += 100;
                
                g2d.setColor(new Color(50, 205, 50)); // Lime Green
                g2d.fillRect(legendX, legendY, 15, 15);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(legendX, legendY, 15, 15);
                g2d.drawString("Standard HashMap", legendX + 20, legendY + 12);
                
                g2d.setFont(new Font("Arial", Font.ITALIC, 12));
                String note = "Note: Lower is better. Times shown in milliseconds.";
                g2d.drawString(note, padding, getHeight() - 10);
            }
        };
        
        allTestsChart.setBackground(Color.WHITE);
        
        chartPanel.removeAll();
        chartPanel.add(allTestsChart, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HashMapVisualizer();
            }
        });
    }
}