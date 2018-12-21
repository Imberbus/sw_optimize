package com.swrunes.gui;

import com.swrunes.swrunes.Bestiary;
import com.swrunes.swrunes.ConfigInfo;
import com.swrunes.swrunes.Crawler;
import com.swrunes.swrunes.PetType;
import com.swrunes.swrunes.PetType.RuneSkill;
import com.swrunes.swrunes.RuneType;
import com.swrunes.swrunes.RuneType.RuneSet;
import com.swrunes.swrunes.SwManager;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

import static com.swrunes.gui.Application.displayRune2Table;
import static com.swrunes.gui.PetCompare.detectPet;
import static com.swrunes.swrunes.PetType.petLabels;
import static com.swrunes.swrunes.SwManager.getPetRune;

/**
 * @author tuanha
 */
public class PetManager extends javax.swing.JFrame {

    public static boolean runAlone = false;
    public static PetType curPetDetail = null;
    static PetManager instance;
    public RuneSet mainEquipSet = null;
    public List<RuneSet> equipList = new ArrayList();
    public List<PetType> BossList = new ArrayList();
    PetType curBoss = null;
    boolean loadingData = false;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JButton jButton2;
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckOnlyPets;
    private JCheckBox jCheckShowBaseStat;
    private JComboBox<String> jComboAllPet;
    private JComboBox<String> jComboBoss;
    private JComboBox<String> jComboEquipRunes;
    private JComboBox<String> jComboLeaderSkill;
    private JComboBox<String> jComboNumDebuff;
    private JComboBox<String> jComboPetElement;
    private JComboBox<String> jComboPetHave;
    private JComboBox<String> jComboSkillFilter;
    private JComboBox<String> jComboSkillScale;
    private JDialog jDialogPetDetail;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JLabel jLabelIcon1;
    private JLabel jLabelIconDark;
    private JLabel jLabelIconDetail;
    private JLabel jLabelIconFire;
    private JLabel jLabelIconLight;
    private JLabel jLabelIconWater;
    private JLabel jLabelIconWind;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane12;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JScrollPane jScrollPane5;
    private JScrollPane jScrollPane6;
    private JScrollPane jScrollPane7;
    private JScrollPane jScrollPane9;
    private JTabbedPane jTabbedAllPets;
    private JTabbedPane jTabbedPane1;
    private JTable jTableAllPets;
    private JTable jTableExtraPetInfo;
    private JTable jTableFullDamage;
    private JTable jTablePetsCompare;
    private JTable jTableRuneDetail;
    private JTable jTableSkillDamage;
    private JTable jTableStatMain;
    private JTextField jTextBossStats;
    private JTextField jTextPetName;
    private JTextField jTextRuneSetInfo;
    private JTextField jTextSkillDesc;
    private JTextPane jTextSkillExplain;
    private JTextArea jTextSkills;
    private JTextField jTextTotalPets;

    /**
     * Creates new form PetManager
     */
    public PetManager() {
        int fontSize = Application.FONT_SIZE[ConfigInfo.getInstance().fontSize];
        //System.out.println("Font size : " + fontSize);
        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Tahoma", Font.PLAIN, fontSize));

        initComponents();

        jTableFullDamage.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (jTableFullDamage.getSelectedRow() > -1) {
                    // print first column value from selected row
                    //System.out.println(jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString());
                    String petName = jTableFullDamage.getValueAt(jTableFullDamage.getSelectedRow(), 0).toString();
                    String skillName = jTableFullDamage.getValueAt(jTableFullDamage.getSelectedRow(), 2).toString();
                    jTextPetName.setText(petName);
                    PetType p2 = SwManager.getPet(petName);
                    if (p2 != null) {
                        updatePetMath(p2, skillName, p2.currentEquip);
                    }
                    //System.out.println("Select : " + petName + " ; skill = " + skillName);

                }
            }
        });

        jTableAllPets.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (jTableAllPets.getSelectedRow() > -1) {
                    // print first column value from selected row
                    //System.out.println(jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString());
                    String petName = jTableAllPets.getValueAt(jTableAllPets.getSelectedRow(), 0).toString();
                    jTextPetName.setText(petName);

                    if (!jDialogPetDetail.isVisible()) {
                        jDialogPetDetail.pack();
                        jDialogPetDetail.setVisible(true);
                    }
                    LoadPetDetail(petName);
                }
            }
        });

        jTablePetsCompare.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
                    // your valueChanged overridden method
                    String petName = jTablePetsCompare.getValueAt(row, 0).toString();
                    if (!jDialogPetDetail.isVisible()) {
                        jDialogPetDetail.pack();
                        jDialogPetDetail.setVisible(true);
                    }
                    LoadPetDetail(petName);
                }
            }
        });
        jTablePetsCompare.getSelectionModel().addListSelectionListener(new ListSelectionListenerImpl());

        loadData();
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public static void clearTable(final JTable table) {
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                table.setValueAt("", i, j);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PetManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PetManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PetManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PetManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //System.out.println("Run Rune Manage alone");
        runAlone = true;
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PetManager().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogPetDetail = new javax.swing.JDialog();
        jComboPetHave = new javax.swing.JComboBox<>();
        jComboAllPet = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabelIconWater = new javax.swing.JLabel();
        jLabelIconFire = new javax.swing.JLabel();
        jLabelIconWind = new javax.swing.JLabel();
        jLabelIconLight = new javax.swing.JLabel();
        jLabelIconDark = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextSkills = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableStatMain = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableExtraPetInfo = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTableRuneDetail = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableSkillDamage = new javax.swing.JTable();
        jLabelIconDetail = new javax.swing.JLabel();
        jLabelIcon1 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckShowBaseStat = new javax.swing.JCheckBox();
        jCheckOnlyPets = new javax.swing.JCheckBox();
        jTextTotalPets = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextPetName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboLeaderSkill = new javax.swing.JComboBox<>();
        jComboBoss = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTabbedAllPets = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAllPets = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableFullDamage = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTablePetsCompare = new javax.swing.JTable();
        jTextBossStats = new javax.swing.JTextField();
        jComboSkillFilter = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboEquipRunes = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextSkillDesc = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextRuneSetInfo = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextSkillExplain = new javax.swing.JTextPane();
        jLabel10 = new javax.swing.JLabel();
        jComboNumDebuff = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jComboSkillScale = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jComboPetElement = new javax.swing.JComboBox<>();

        jDialogPetDetail.setTitle("Pet Detail");

        jComboPetHave.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        jComboAllPet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        jLabel3.setText("Pets you have");

        jLabel4.setText("All Pets");

        jLabelIconWater.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Water_Icon.png"))); // NOI18N
        jLabelIconWater.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelIconWater.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelIconWaterMouseClicked(evt);
            }
        });

        jLabelIconFire.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Fire_Icon.png"))); // NOI18N
        jLabelIconFire.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelIconFire.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelIconFireMouseClicked(evt);
            }
        });

        jLabelIconWind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Wind_Icon.png"))); // NOI18N
        jLabelIconWind.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelIconWind.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelIconWindMouseClicked(evt);
            }
        });

        jLabelIconLight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Light_Icon.png"))); // NOI18N
        jLabelIconLight.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelIconLight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelIconLightMouseClicked(evt);
            }
        });

        jLabelIconDark.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Dark_Icon.png"))); // NOI18N
        jLabelIconDark.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabelIconDark.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelIconDarkMouseClicked(evt);
            }
        });

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextSkills.setColumns(20);
        jTextSkills.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTextSkills.setLineWrap(true);
        jTextSkills.setRows(5);
        jScrollPane2.setViewportView(jTextSkills);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jTabbedPane1.addTab("Skills", jPanel1);

        jTableStatMain.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Stat", "Base", "Rune", "Final"
                }
        ));
        jTableStatMain.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane5.setViewportView(jTableStatMain);

        jTableExtraPetInfo.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {"Pet", null},
                        {"Final Damage", null},
                        {"Damage (GW)", null},
                        {"Effective HP", null},
                        {"HP", null},
                        {"DEF", null},
                        {"SPD", null},
                        {"RuneSet", null},
                        {"Skill", null},
                        {"Skill Multy", null}
                },
                new String[]{
                        "Stat", "Value"
                }
        ));
        jScrollPane9.setViewportView(jTableExtraPetInfo);

        jTableRuneDetail.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jTableRuneDetail.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null},
                        {null, null, null, null, null, null}
                },
                new String[]{
                        "Rune1", "Rune2", "Rune3", "Rune4", "Rune5", "Rune6"
                }
        ));
        jTableRuneDetail.setToolTipText("Click on the Lock icon at bottom to lock the Pet Runes.");
        jTableRuneDetail.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableRuneDetail.setAutoscrolls(false);
        jScrollPane12.setViewportView(jTableRuneDetail);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 155, Short.MAX_VALUE))
                                        .addComponent(jScrollPane12))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(44, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Stats", jPanel2);

        jTableSkillDamage.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane3.setViewportView(jTableSkillDamage);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(154, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 134, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Skill Damage", jPanel3);

        jLabelIconDetail.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jDialogPetDetailLayout = new javax.swing.GroupLayout(jDialogPetDetail.getContentPane());
        jDialogPetDetail.getContentPane().setLayout(jDialogPetDetailLayout);
        jDialogPetDetailLayout.setHorizontalGroup(
                jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogPetDetailLayout.createSequentialGroup()
                                .addContainerGap(34, Short.MAX_VALUE)
                                .addGroup(jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 848, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jDialogPetDetailLayout.createSequentialGroup()
                                                .addGroup(jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jComboAllPet, 0, 165, Short.MAX_VALUE)
                                                        .addComponent(jComboPetHave, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(28, 28, 28)
                                                .addComponent(jLabelIconDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabelIconFire, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelIconWater)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabelIconWind)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelIconLight)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelIconDark)))
                                .addContainerGap())
        );
        jDialogPetDetailLayout.setVerticalGroup(
                jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jDialogPetDetailLayout.createSequentialGroup()
                                .addGroup(jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jDialogPetDetailLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabelIconFire, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabelIconWater, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabelIconWind, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabelIconLight, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabelIconDark, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jDialogPetDetailLayout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addGroup(jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jComboAllPet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jDialogPetDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jComboPetHave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3)))
                                        .addGroup(jDialogPetDetailLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabelIconDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(11, 11, 11)
                                .addComponent(jTabbedPane1)
                                .addContainerGap())
        );

        jLabelIcon1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        setTitle("Pet Managers");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jCheckBox1.setText("Only Awaken");

        jCheckShowBaseStat.setText("Show Base Stats");
        jCheckShowBaseStat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckShowBaseStatActionPerformed(evt);
            }
        });

        jCheckOnlyPets.setText("Only pets you have");
        jCheckOnlyPets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckOnlyPetsActionPerformed(evt);
            }
        });

        jTextTotalPets.setText("0");

        jLabel1.setText("Total pets");

        jLabel2.setText("Pet Name");

        jButton1.setText("show Pet Detail");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboLeaderSkill.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No Leader", "30% ATK", "44% ATK", "30% DEF", "40% DEF"}));

        jComboBoss.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboBoss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBossActionPerformed(evt);
            }
        });

        jLabel5.setText("Leader Skill");

        jLabel6.setText("Boss/Enemy");

        jTabbedAllPets.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedAllPetsStateChanged(evt);
            }
        });

        jTableAllPets.setAutoCreateRowSorter(true);
        jTableAllPets.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTableAllPets.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Id", "Img", "Stats", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "eff_HP", "Damage"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableAllPets);

        jTabbedAllPets.addTab("Pets Stats", jScrollPane1);

        jTableFullDamage.setAutoCreateRowSorter(true);
        jTableFullDamage.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTableFullDamage.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Name", "Avatar", "Skill", "Multy", "Hits", "SkillUp", "DMG", "Full DMG", "Atk+,DEF-", "ATK+,DEF-"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTableFullDamage);

        jTabbedAllPets.addTab("Compare your pets skills damage (current equipped runes)", jScrollPane4);

        jTablePetsCompare.setAutoCreateRowSorter(true);
        jTablePetsCompare.setFont(jTablePetsCompare.getFont());
        jTablePetsCompare.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                        "Name", "Avatar", "Skill", "Multy", "Skill Up", "Hits", "CD", "SPD", "ATK", "DEF", "Normal", "DMG", "BUFF+,-DEF"
                }
        ) {
            Class[] types = new Class[]{
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTablePetsCompare);

        jTabbedAllPets.addTab("Compare All pets skills (equip with same runes)", jScrollPane6);

        jTabbedAllPets.setSelectedIndex(2);

        jComboSkillFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"All Skills", "Aoe Skills", "Ignore def Skills", "Multi hits skills", "Multi Hits Random", "Ignore Chloe buff", "Ignore Damage Random", "Single Target", " "}));
        jComboSkillFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSkillFilterActionPerformed(evt);
            }
        });

        jLabel7.setText("Skill Filters");

        jComboEquipRunes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboEquipRunes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboEquipRunesActionPerformed(evt);
            }
        });

        jLabel8.setText("Equip with");

        jLabel9.setText("Runes");

        jButton2.setText("Add pet to account");
        jButton2.setToolTipText("If you dont have this pet <br> Add this to your account to have fun with it !");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane7.setViewportView(jTextSkillExplain);

        jLabel10.setText("Skill Damage Explain");

        jComboNumDebuff.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"0", "1", "2", "3", "4", "5", "6"}));

        jLabel11.setText("Num debuff");

        jComboSkillScale.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"All Skills", "Skill scale Boss HP", "Skill scale Speed", "Skill scale Defense", "Skill Scale Debuff", "Skill Scale HP", "Skill AOE"}));
        jComboSkillScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSkillScaleActionPerformed(evt);
            }
        });

        jLabel12.setText("Sill Scale");

        jComboPetElement.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"All", "Fire", "Water", "Wind", "Light", "Dark"}));
        jComboPetElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboPetElementActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel1)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jTextTotalPets, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel6)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboBoss, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jTextPetName, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel8)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboEquipRunes, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jTextBossStats, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(32, 32, 32)
                                                                .addComponent(jLabel11)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboNumDebuff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(29, 29, 29)
                                                                .addComponent(jLabel5)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboLeaderSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel9)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jTextRuneSetInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE))))
                                        .addComponent(jTabbedAllPets, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextSkillDesc)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel10)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jCheckBox1)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jCheckShowBaseStat)
                                                                .addGap(28, 28, 28)
                                                                .addComponent(jCheckOnlyPets)
                                                                .addGap(91, 91, 91)
                                                                .addComponent(jComboPetElement, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel7)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboSkillFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jLabel12)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboSkillScale, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 97, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jCheckBox1)
                                        .addComponent(jCheckShowBaseStat)
                                        .addComponent(jCheckOnlyPets)
                                        .addComponent(jComboSkillFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7)
                                        .addComponent(jComboSkillScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12)
                                        .addComponent(jComboPetElement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextPetName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jComboEquipRunes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9)
                                        .addComponent(jTextRuneSetInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jComboLeaderSkill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel5))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jComboBoss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel6)
                                                .addComponent(jTextBossStats, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jComboNumDebuff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel11)
                                                .addComponent(jTextTotalPets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel1)))
                                .addGap(2, 2, 2)
                                .addComponent(jTextSkillDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTabbedAllPets, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton2)))
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void showDialogDetail(String petName) {
        jDialogPetDetail.pack();
        jDialogPetDetail.setVisible(true);
        LoadPetDetail(petName);
    }

    private void jCheckShowBaseStatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckShowBaseStatActionPerformed
        // TODO add your handling code here:
        updateAllPetsTable();
        updateCompareDmgPetsTable();
    }//GEN-LAST:event_jCheckShowBaseStatActionPerformed

    private void jCheckOnlyPetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckOnlyPetsActionPerformed
        // TODO add your handling code here:
        updateAllPetsTable();
    }//GEN-LAST:event_jCheckOnlyPetsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jTableAllPets.getSelectedRow() > -1) {
            // print first column value from selected row
            //System.out.println(jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString());
            String petName = jTableAllPets.getValueAt(jTableAllPets.getSelectedRow(), 0).toString();
            jTextPetName.setText(petName);

            jDialogPetDetail.pack();
            jDialogPetDetail.setVisible(true);
            LoadPetDetail(petName);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabelIconDarkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelIconDarkMouseClicked
        // TODO add your handling code here:
        if (curPetDetail != null) {
            String u1 = curPetDetail.u_name + " (Dark)";
            //System.out.println(u1);
            String p2 = SwManager.petFamily.get(u1);
            if (p2 != null) {
                //System.out.println("Found dark : " + p2);
                LoadPetDetail(p2);
            }
        }
    }//GEN-LAST:event_jLabelIconDarkMouseClicked

    private void jLabelIconLightMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelIconLightMouseClicked
        // TODO add your handling code here:
        if (curPetDetail != null) {
            String u1 = curPetDetail.u_name + " (Light)";
            //System.out.println(u1);
            String p2 = SwManager.petFamily.get(u1);
            if (p2 != null) {
                //System.out.println("Found dark : " + p2);
                LoadPetDetail(p2);
            }
        }
    }//GEN-LAST:event_jLabelIconLightMouseClicked

    private void jLabelIconFireMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelIconFireMouseClicked
        // TODO add your handling code here:
        if (curPetDetail != null) {
            String u1 = curPetDetail.u_name + " (Fire)";
            //System.out.println(u1);
            String p2 = SwManager.petFamily.get(u1);
            if (p2 != null) {
                //System.out.println("Found dark : " + p2);
                LoadPetDetail(p2);
            }
        }
    }//GEN-LAST:event_jLabelIconFireMouseClicked

    private void jLabelIconWaterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelIconWaterMouseClicked
        // TODO add your handling code here:
        if (curPetDetail != null) {
            String u1 = curPetDetail.u_name + " (Water)";
            //System.out.println(u1);
            String p2 = SwManager.petFamily.get(u1);
            if (p2 != null) {
                //System.out.println("Found dark : " + p2);
                LoadPetDetail(p2);
            }
        }
    }//GEN-LAST:event_jLabelIconWaterMouseClicked

    private void jLabelIconWindMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelIconWindMouseClicked
        // TODO add your handling code here:
        if (curPetDetail != null) {
            String u1 = curPetDetail.u_name + " (Wind)";
            //System.out.println(u1);
            String p2 = SwManager.petFamily.get(u1);
            if (p2 != null) {
                //System.out.println("Found dark : " + p2);
                LoadPetDetail(p2);
            }
        }
    }//GEN-LAST:event_jLabelIconWindMouseClicked

    private void jComboBossActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBossActionPerformed
        // TODO add your handling code here:
        if (loadingData) {
            return;
        }
        if (jComboBoss.getSelectedIndex() >= 0) {
            curBoss = BossList.get(jComboBoss.getSelectedIndex());
            if (curBoss != null) {
                if (curBoss.name.startsWith("Raid") || curBoss.name.startsWith("Rift")) {
                    jComboNumDebuff.setSelectedIndex(5);
                } else {
                    jComboNumDebuff.setSelectedIndex(1);
                }

                jTextBossStats.setText("HP : " + Application.formatNumber(curBoss.hp) + " ; DEF : " + curBoss.def + " ; SPD = " + curBoss.spd);
                updateDmgPetsTable();
                updateCompareDmgPetsTable();
            }
        }
    }//GEN-LAST:event_jComboBossActionPerformed

    private void jComboSkillFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSkillFilterActionPerformed
        if (loadingData) {
            return;
        }
        updateDmgPetsTable();
        updateCompareDmgPetsTable();
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboSkillFilterActionPerformed

    private void jComboEquipRunesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboEquipRunesActionPerformed
        // TODO add your handling code here:
        if (loadingData) {
            return;
        }
        if (jComboEquipRunes.getSelectedItem() == null) {
            return;
        }
        if (curBoss == null) {
            return;
        }
        updateCompareDmgPetsTable();
    }//GEN-LAST:event_jComboEquipRunesActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //add pet to json file
        String petName = jTextPetName.getText();
        if (SwManager.pets.containsKey(petName.toLowerCase())) {
            JOptionPane.showMessageDialog(null, "You already have this pet", "Fail", JOptionPane.ERROR_MESSAGE);
        } else {
            SwManager.addPet(petName);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    public void selectRuneEquip(Function<RuneSet, Integer> dmgFunc) {
        int best = 0;
        int bestId = -1;
        for (int i = 0; i < jComboEquipRunes.getItemCount(); i++) {
            String s1 = jComboEquipRunes.getItemAt(i);
            PetType p2 = SwManager.getPet(s1);
            if (p2 != null && p2.currentEquip != null) {
                if (dmgFunc.apply(p2.currentEquip) > best) {
                    best = dmgFunc.apply(p2.currentEquip);
                    bestId = i;
                }
            }
        }
        if (bestId >= 0) {
            loadingData = true;
            jComboEquipRunes.setSelectedIndex(bestId);
            loadingData = false;
        }
    }

    private void jComboSkillScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSkillScaleActionPerformed
        // TODO add your handling code here:
        if (loadingData) {
            return;
        }
        String scale = jComboSkillScale.getSelectedItem().toString().toLowerCase();
        //System.out.println("Scale : " + scale);
        selectRuneEquip(x -> (x.atk * x.cd));
        if (scale.contains("scale defense")) {
            selectRuneEquip(x -> (x.def * x.cd));
        }
        if (scale.contains("scale speed")) {
            selectRuneEquip(x -> (x.spd * x.cd * x.atk));
        }
        if (scale.contains("scale hp")) {
            selectRuneEquip(x -> (x.hp * x.cd));
        }
        updateDmgPetsTable();
        updateCompareDmgPetsTable();
    }//GEN-LAST:event_jComboSkillScaleActionPerformed

    private void jTabbedAllPetsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedAllPetsStateChanged
        // TODO add your handling code here:
        //System.out.println("Tab change : " + jTabbedAllPets.getSelectedIndex());
        if (jTablePetsCompare.getRowCount() < 5) {
            updateCompareDmgPetsTable();
        }
        if (jTableAllPets.getRowCount() < 5) {
            updateAllPetsTable();
        }
        if (jTableFullDamage.getRowCount() < 5) {
            updateDmgPetsTable();
        }
    }//GEN-LAST:event_jTabbedAllPetsStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        //System.out.println("Window closing : " + runAlone);
        if (runAlone) {
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jComboPetElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboPetElementActionPerformed
        // TODO add your handling code here:
        updateDmgPetsTable();
        updateCompareDmgPetsTable();
    }//GEN-LAST:event_jComboPetElementActionPerformed

    void LoadPetDetail(String petName) {
        //System.out.println("LoadPetDetail : " + petName);
        PetType curPet = detectPet(petName);
        jLabelIconDetail.setIcon(new ImageIcon(Crawler.crawlPetPicture(curPet.a_name)));

        if (curPet.runesEquip > 0) {
            displayRune2Table(curPet.currentEquip, jTableRuneDetail);
        } else {
            clearTable(jTableRuneDetail);
        }
        Bestiary.PetInfo pSkill = SwManager.skillPetInfo.get(petName);
        int k = 0;

        if (curPet != null) {
            jDialogPetDetail.setTitle("Pet Detail - " + curPet.full_name + " - " + curPet.stars + "*");
            //System.out.println("uname : " + curPet.u_name + " : " + SwManager.petFamily.get(curPet.u_name));
            curPetDetail = curPet;
        }

        /*jLabelIconFire.setIcon(new ImageIcon(Crawler.crawlPetPicture(SwManager.petFamily.get(curPet.u_name+" (Fire)"))));
        jLabelIconWater.setIcon(new ImageIcon(Crawler.crawlPetPicture(SwManager.petFamily.get(curPet.u_name+" (Water)"))));
        jLabelIconWind.setIcon(new ImageIcon(Crawler.crawlPetPicture(SwManager.petFamily.get(curPet.u_name+" (Wind)"))));
        jLabelIconDark.setIcon(new ImageIcon(Crawler.crawlPetPicture(SwManager.petFamily.get(curPet.u_name+" (Dark)"))));
        jLabelIconLight.setIcon(new ImageIcon(Crawler.crawlPetPicture(SwManager.petFamily.get(curPet.u_name+" (Light)"))));*/
        //jTableSkills.getColumnModel().getColumn(1).setCellRenderer(new RenderWrapText());
        //jTextSkills.setContentType( "text/html" );
        jTextSkills.setText("");
        int num = 0;
        PetType pet = SwManager.getInstance().searchPets(petName);
        Application.displayStatMain(jTableStatMain, curPet);
        Application.showPetFinalStats(curPet, jTableExtraPetInfo);

        if (pSkill != null) {
            for (Bestiary.SkillWikiInfo sk : pSkill.skills) {
                String skillMulty = sk.multy + " . " + sk.tooltip;
                if (sk.multy == null || sk.multy.trim().length() < 2) {
                    skillMulty = "";
                }
                String s2 = "";
                if (!sk.skill_desc.contains("Leader Skill")) {
                    num++;
                    s2 = "Skill " + num + " : [";
                }
                jTextSkills.setText(jTextSkills.getText() + s2 + sk.skill_desc.replace(":", "] :") + " . " + skillMulty + "\n");
                String lvup = "";
                for (String p1 : sk.skill_up) {
                    lvup += "   *" + p1 + "\n";
                }
                lvup += "\n";
                jTextSkills.setText(jTextSkills.getText() + lvup);
            }
        }
        //jTextSkills.setText(jTextSkills.getText()+"</body></html>");
//jTextSkills.setText( "<html><body>Hello, world</body></html>" );
    }

    public void updateAllPetsTable() {
        if (loadingData) {
            return;
        }
        if (jTabbedAllPets.getSelectedIndex() != 0) {
            return;
        }

        int curRow = jTableAllPets.getRowCount();
        List<PetType> petList = new ArrayList();
        boolean showBaseStat = jCheckShowBaseStat.isSelected();

        if (jCheckOnlyPets.isSelected()) {
            for (PetType p1 : SwManager.pets.values()) {
                if (p1.stars > 4) {
                    petList.add(p1);
                }
            }
        } else {
            showBaseStat = true;
            for (PetType p1 : SwManager.petsBestiary.values()) {
                if (!petList.contains(p1)) {
                    petList.add(p1);
                }
            }
        }
        ((DefaultTableModel) jTableAllPets.getModel()).setNumRows(0);

        jTextTotalPets.setText("" + petList.size());
        jTableAllPets.getColumnModel().getColumn(1).setCellRenderer(new Application.IconRenderer());
        //if (true) return;

        DefaultTableModel model = (DefaultTableModel) jTableAllPets.getModel();
        int numCol = jTableAllPets.getColumnModel().getColumnCount();

        jTableAllPets.getColumnModel().getColumn(1).setPreferredWidth(50);
        jTableAllPets.getColumnModel().getColumn(0).setPreferredWidth(80);

        Application.setupTable(jTableAllPets);
        for (int i = 0; i < petList.size(); i++) {
            model.addRow(new Object[numCol]);
        }

        for (int j1 = 0; j1 < petLabels.length; j1++) {
            jTableAllPets.getColumnModel().getColumn(j1 + 2).setHeaderValue(RuneType.slabelsMainDisplay[j1].replace("%", ""));
        }
        //jTableAllPets.setRowHeight(i, 50);
        jTableAllPets.setRowHeight(50);

        for (int i = 0; i < petList.size(); i++) {
            PetType p1 = petList.get(i);
            jTableAllPets.getModel().setValueAt(p1.name, i, 0);
            jTableAllPets.getModel().setValueAt(p1.getPetIcon(), i, 1);
            for (int j1 = 0; j1 < p1.statfixRune.length; j1++) {
                if (showBaseStat) {
                    jTableAllPets.getModel().setValueAt(p1.baseStats[j1], i, j1 + 2);
                } else if (p1.currentEquip != null) {
                    jTableAllPets.getModel().setValueAt(p1.currentEquip.statfixRune[j1], i, j1 + 2);
                }

            }
            RuneSet.runePet = p1;
            jTableAllPets.getModel().setValueAt(p1.currentEquip.effectiveHP(), i, 10);
            jTableAllPets.getModel().setValueAt(p1.currentEquip.finalDamage(), i, 11);
        }

    }

    public boolean filterSkill(RuneSkill r1) {
        if (r1.skillName.contains("Passive"))
            return true;
        if (jComboSkillFilter.getSelectedIndex() == 1 && !r1.isAoe) {
            return true;
        }
        if (jComboSkillFilter.getSelectedIndex() == 2 && !r1.ignoreDmg) {
            return true;
        }
        if (jComboSkillFilter.getSelectedIndex() == 3 && r1.numHits <= 1) {
            return true;
        }
        if (jComboSkillFilter.getSelectedIndex() == 4 && !r1.isAoeRandom) {
            return true;
        }
        if (jComboSkillFilter.getSelectedIndex() == 5 && !r1.ignoreChloe) {
            return true;
        }
        if (jComboSkillFilter.getSelectedIndex() == 6 && !(r1.ignoreChance || r1.ignoreDmg)) {
            return true;
        }
        //single target
        if (jComboSkillFilter.getSelectedIndex() == 7 && !(!r1.isAoeRandom && !r1.isAoe)) {
            return true;
        }

        if (jComboSkillScale.getSelectedIndex() == 1 && (!r1.skillMulty.contains("TARGET_TOT_HP") || r1.type == 3)) {
            return true;
        }
        if (jComboSkillScale.getSelectedIndex() == 2 &&
                !(r1.skillMulty.contains("ATTACK_SPEED") || r1.skillMulty.contains("Spd"))) {
            return true;
        }
        if (jComboSkillScale.getSelectedIndex() == 3 && !r1.skillMulty.contains("DEF")) {
            return true;
        }
        if (jComboSkillScale.getSelectedIndex() == 4 && !(r1.debuffScale > 0 || r1.debuffIncre > 0)) {
            return true;
        }
        if (jComboSkillScale.getSelectedIndex() == 5 && !
                (r1.skillMulty.contains("ATTACK_TOT_HP") || r1.skillMulty.contains("ATTACK_CUR_HP"))) {
            return true;
        }
        if (jComboSkillScale.getSelectedIndex() == 6 && !r1.isAoe) {
            return true;
        }

        return false;
    }

    public void updateDmgPetsTable() {
        if (loadingData) {
            return;
        }
        if (jTabbedAllPets.getSelectedIndex() != 1) {
            return;
        }

        int curRow = jTableFullDamage.getRowCount();
        List<PetType> petList = new ArrayList();
        boolean showBaseStat = jCheckShowBaseStat.isSelected();

        if (jCheckOnlyPets.isSelected() || true) {
            for (PetType p1 : SwManager.pets.values()) {
                if (p1.stars > 4) {
                    petList.add(p1);
                }
            }
        } else {
            showBaseStat = true;
            for (PetType p1 : SwManager.petsBestiary.values()) {
                if (!petList.contains(p1)) {
                    petList.add(p1);
                }
            }
        }
        ((DefaultTableModel) jTableFullDamage.getModel()).setNumRows(0);

        jTextTotalPets.setText("" + petList.size());
        jTableFullDamage.getColumnModel().getColumn(1).setCellRenderer(new Application.IconRenderer());
        //if (true) return;

        DefaultTableModel model = (DefaultTableModel) jTableFullDamage.getModel();
        int numCol = jTableFullDamage.getColumnModel().getColumnCount();

        jTableFullDamage.getColumnModel().getColumn(1).setPreferredWidth(40);
        jTableFullDamage.getColumnModel().getColumn(0).setPreferredWidth(40);
        jTableFullDamage.getColumnModel().getColumn(4).setPreferredWidth(10);
        jTableFullDamage.getColumnModel().getColumn(5).setPreferredWidth(10);
        jTableFullDamage.getColumnModel().getColumn(3).setPreferredWidth(100);

        int row = -1;
        for (int i = 0; i < petList.size(); i++) {

            PetType p1 = petList.get(i);
            jTableFullDamage.setRowHeight(50);
            for (RuneSkill r1 : p1.skillList) {
                p1.skillItem = r1;
                if (filterSkill(r1)) {
                    continue;
                }
                model.addRow(new Object[numCol]);
                row++;
                jTableFullDamage.getModel().setValueAt(p1.name, row, 0);
                jTableFullDamage.getModel().setValueAt(p1.getPetIcon(), row, 1);
                RuneSet.runePet = p1;

                jTableFullDamage.getModel().setValueAt(p1.skillItem.skillName, row, 2);
                jTableFullDamage.getModel().setValueAt(Application.formatSkill(p1.skillItem.skillMulty), row, 3);
                jTableFullDamage.getModel().setValueAt(p1.skillItem.hitStr, row, 4);
                jTableFullDamage.getModel().setValueAt(p1.skillItem.skillUp, row, 5);

                p1.leader_skill = 0;
                p1.atk_buff = 1.0;

                long bossHp = curBoss.hp;
                if (curBoss.name.contains("Raid") || curBoss.name.contains("Rift")) {
                    bossHp = bossHp / 10;
                }

                int numDebuff = jComboNumDebuff.getSelectedIndex();

                boolean isRift = false;
                double groggyState = 1.0;
                if (curBoss.name.contains("Rift")) {
                    isRift = true;
                    jTableFullDamage.getColumnModel().getColumn(7).setHeaderValue("Normal");
                    jTableFullDamage.getColumnModel().getColumn(9).setHeaderValue("Groggy");
                    p1.atk_buff = 1.5;
                    if (curBoss.name.contains("1"))
                        groggyState = 0.1;
                    if (curBoss.name.contains("2"))
                        groggyState = 0.35;
                } else {
                    jTableFullDamage.getColumnModel().getColumn(7).setHeaderValue("No Buff");
                    jTableFullDamage.getColumnModel().getColumn(9).setHeaderValue("ATK+,DEF-");
                }

                long dmg = p1.currentEquip.dmgVsBoss(curBoss.def, bossHp);
                jTableFullDamage.getModel().setValueAt("  " + p1.skillItem.numHits + "*" + dmg, row, 6);
                jTableFullDamage.getModel().setValueAt(p1.skillItem.numHits * dmg, row, 7);

                if (jComboLeaderSkill.getSelectedIndex() == 0) {
                    p1.atk_leader = 0;
                    p1.def_leader = 0;
                }
                if (r1.skillMulty.contains("DEF")) {
                    p1.atk_buff = 1.0;
                    p1.def_buff = 1.7;
                } else if (r1.skillMulty.contains("ATK")) {
                    p1.atk_buff = 1.5;
                    p1.def_buff = 1.7;
                }

                dmg = p1.currentEquip.dmgVsBoss(curBoss.def * 0.3 * groggyState, bossHp);
                if (r1.debuffScale > 0) {
                    dmg = dmg + dmg * numDebuff * r1.debuffScale / 100;
                }
                if (r1.debuffIncre > 0) {
                    dmg = dmg + dmg * r1.debuffIncre / 100;
                }

                jTableFullDamage.getModel().setValueAt("  " + p1.skillItem.numHits + "*" + dmg, row, 8);
                jTableFullDamage.getModel().setValueAt(p1.skillItem.numHits * dmg, row, 9);
            }
        }

    }

    public void updateCompareDmgPetsTable() {
        if (loadingData) {
            return;
        }
        if (jTabbedAllPets.getSelectedIndex() != 2) {
            return;
        }
        if (jComboEquipRunes.getSelectedItem() == null)
            return;

        String curPetEquip = jComboEquipRunes.getSelectedItem().toString();
        PetType pp = SwManager.pets.get(curPetEquip.toLowerCase());

        if (pp != null) {
            mainEquipSet = pp.currentEquip;
            jTextRuneSetInfo.setText(mainEquipSet.runeSets + " : " + mainEquipSet.mainStat + " ; + " + mainEquipSet.spd + " spd ; "
                    + " +" + mainEquipSet.atk + "% Atk " + " +" + mainEquipSet.cd + "% CD ; " + " +" + mainEquipSet.cr + "% CR ; "
                    + " +" + mainEquipSet.def + "% DEF ; " + " +" + mainEquipSet.hp + "% HP ; ");
        }

        int curRow = jTablePetsCompare.getRowCount();
        List<PetType> petList = new ArrayList();
        boolean showBaseStat = jCheckShowBaseStat.isSelected();

        for (PetType p1 : SwManager.petsBestiary.values()) {
            if (!petList.contains(p1)) {
                if (p1.stars > 1) {
                    petList.add(p1);
                }
            }
        }
        ((DefaultTableModel) jTablePetsCompare.getModel()).setNumRows(0);

        jTextTotalPets.setText("" + petList.size());
        jTablePetsCompare.getColumnModel().getColumn(1).setCellRenderer(new Application.IconRenderer());
        //if (true) return;

        DefaultTableModel model = (DefaultTableModel) jTablePetsCompare.getModel();
        int numCol = jTablePetsCompare.getColumnModel().getColumnCount();

        jTablePetsCompare.getColumnModel().getColumn(1).setPreferredWidth(40);
        jTablePetsCompare.getColumnModel().getColumn(0).setPreferredWidth(40);
        jTablePetsCompare.getColumnModel().getColumn(4).setPreferredWidth(10);
        jTablePetsCompare.getColumnModel().getColumn(5).setPreferredWidth(10);
        jTablePetsCompare.getColumnModel().getColumn(3).setPreferredWidth(80);

        //mainWindow.setupTable(jTableFullDamage);
        for (int i = 0; i < petList.size(); i++) {
            //model.addRow(new Object[numCol]);
        }

        int row = -1;
        long t1 = System.currentTimeMillis();
        jTablePetsCompare.setRowHeight(50);
        for (int i = 0; i < petList.size(); i++) {
            PetType p1 = petList.get(i);
            if (jComboPetElement.getSelectedIndex() > 0) {
                if (!p1.attribute.equalsIgnoreCase("" + jComboPetElement.getSelectedItem()))
                    continue;
            }
            for (RuneSkill r1 : p1.skillList) {
                p1.skillItem = r1;
                if (filterSkill(r1)) {
                    continue;
                }
                if (r1.noWikiMulty) {
                    continue;
                }

                model.addRow(new Object[numCol]);
                row++;
                RuneSet.runePet = p1;
                int colPos = 0;
                jTablePetsCompare.getModel().setValueAt(p1.name, row, 0);
                jTablePetsCompare.getModel().setValueAt(p1.getPetIcon(), row, ++colPos);
                //jTablePetsCompare.getModel().setValueAt("No img", row, ++colPos);
                jTablePetsCompare.getModel().setValueAt(p1.skillItem.skillName, row, ++colPos);
                jTablePetsCompare.getModel().setValueAt(Application.formatSkill(p1.skillItem.skillMulty), row, ++colPos);
                jTablePetsCompare.getModel().setValueAt(p1.skillItem.skillUp, row, ++colPos);
                jTablePetsCompare.getModel().setValueAt(p1.skillItem.hitStr, row, ++colPos);
                p1.leader_skill = 0;
                p1.atk_buff = 1.0;

                if (mainEquipSet != null) {
                    mainEquipSet.equipOnPet(p1);
                    p1.currentEquip = mainEquipSet;
                }
                long bossHp = curBoss.hp;
                if (curBoss.name.contains("Raid")) {
                    bossHp = bossHp / 10;
                }

                double aoeIndex = 1.0;
                String skillFilter = jComboSkillFilter.getSelectedItem().toString().toLowerCase();
                if (skillFilter.contains("aoe")) {
                    aoeIndex = r1.aoeIndex;
                }
                long dmg = 0;
                p1.atk_buff = 1.0;
                p1.def_buff = 1.0;
                dmg = Math.round(p1.currentEquip.dmgVsBoss(curBoss.def, bossHp) * aoeIndex);
                //jTablePetsCompare.getModel().setValueAt("  " + p1.skillItem.numHits + "*" + dmg, row, 5);
                //jTablePetsCompare.getModel().setValueAt(p1.skillItem.numHits * dmg, row, 6);

                if (jCheckShowBaseStat.isSelected()) {
                    jTablePetsCompare.getModel().setValueAt(p1.b_cd, row, ++colPos);
                    jTablePetsCompare.getModel().setValueAt(p1.b_spd, row, ++colPos);
                    jTablePetsCompare.getModel().setValueAt(p1.b_atk, row, ++colPos);
                    jTablePetsCompare.getModel().setValueAt(p1.b_def, row, ++colPos);
                } else {
                    jTablePetsCompare.getModel().setValueAt(p1.currentEquip.pet_cd + r1.extra_cd * 100, row, ++colPos);
                    jTablePetsCompare.getModel().setValueAt(p1.currentEquip.pet_spd, row, ++colPos);
                    jTablePetsCompare.getModel().setValueAt(p1.currentEquip.pet_atk + r1.extra_atk * p1.b_atk, row, ++colPos);
                    jTablePetsCompare.getModel().setValueAt(p1.currentEquip.pet_def, row, ++colPos);
                }

                if (jComboLeaderSkill.getSelectedIndex() == 0) {
                    p1.atk_leader = 0;
                    p1.def_leader = 0;
                }

                jTablePetsCompare.getModel().setValueAt(p1.skillItem.numHits * dmg, row, ++colPos);

                if (r1.skillMulty.contains("DEF")) {
                    p1.atk_buff = 1.0;
                    p1.def_buff = 1.7;
                } else if (r1.skillMulty.contains("ATK")) {
                    p1.atk_buff = 1.5;
                    p1.def_buff = 1.7;
                }
                double defbreak = 0.3;
                if (jComboSkillFilter.getSelectedIndex() == 6)
                    defbreak = 1.0;
                dmg = Math.round(p1.currentEquip.dmgVsBoss(curBoss.def * defbreak, bossHp) * aoeIndex);
                int numDebuff = jComboNumDebuff.getSelectedIndex();
                if (r1.debuffScale > 0) {
                    dmg = dmg + dmg * numDebuff * r1.debuffScale / 100;
                }
                if (r1.debuffIncre > 0) {
                    dmg = dmg + dmg * r1.debuffIncre / 100;
                }
                if (jComboSkillFilter.getSelectedIndex() == 6) {
                    jTablePetsCompare.getColumnModel().getColumn(10).setHeaderValue("% Ignore");
                    jTablePetsCompare.getModel().setValueAt(r1.ignoreChanceNum + "%", row, 10);
                    if (r1.ignoreDmg)
                        jTablePetsCompare.getModel().setValueAt("100%", row, 10);
                    jTablePetsCompare.getColumnModel().getColumn(11).setHeaderValue("Normal DMG");
                    jTablePetsCompare.getColumnModel().getColumn(12).setHeaderValue("1 hit Ignore");
                    jTablePetsCompare.getModel().setValueAt(p1.skillItem.numHits * dmg, row, ++colPos);
                    if (p1.skillItem.numHits > 1) {
                        jTablePetsCompare.getModel().setValueAt((p1.skillItem.numHits - 1) * dmg + p1.currentEquip.finalDamage(0, 0, 0), row, ++colPos);
                    } else
                        jTablePetsCompare.getModel().setValueAt(dmg, row, ++colPos);
                } else {
                    jTablePetsCompare.getModel().setValueAt("  " + p1.skillItem.numHits + "*" + dmg, row, ++colPos);
                    jTablePetsCompare.getModel().setValueAt(p1.skillItem.numHits * dmg, row, ++colPos);
                }
            }
        }
        //System.out.println("Finish in " + (System.currentTimeMillis() - t1));
        //System.out.println("Num rows : " + jTablePetsCompare.getRowCount());
    }

    public TreeSet<String> sortString(TreeSet<PetType> tr1) {
        TreeSet<String> tr2 = new TreeSet();
        for (PetType p1 : tr1) {
            tr2.add(p1.name);
        }
        return tr2;
    }

    void loadData() {
        loadingData = true;
        if (SwManager.runes.size() == 0) {
            SwManager.getInstance().loadPets("optimizer.json");
        }
        loadingData = false;

        updateAllPetsTable();

        jComboPetHave.removeAllItems();
        TreeSet<String> tr1 = new TreeSet();
        for (PetType p : SwManager.pets.values()) {
            if (p.stars > 1) {
                tr1.add(p.name);
            }
        }
        for (String s1 : tr1) {
            jComboPetHave.addItem(s1);
        }

        jComboEquipRunes.removeAllItems();
        TreeSet<PetType> tr2 = new TreeSet();
        for (PetType p : SwManager.pets.values()) {
            if (p.stars > 4) {
                if (SwManager.getPet(p.name.toLowerCase()) != null) {
                    RuneSet s1 = getPetRune(p.id);
                    RuneSet.runePet = p;
                    s1.equipOnPet(p);

                    p.finalValue = s1.atk * s1.cd + s1.def * s1.cd;
                    if (p.name.equalsIgnoreCase("Arang") || p.name.equalsIgnoreCase("Akhamamir")) {
                        //System.out.println(p.name + " : " + p.finalValue + " ; " + p.currentEquip.details());
                        //System.out.println("f_atk : " + s1.f_atk);
                        //System.out.println("pet_atk : " + s1.pet_atk);
                        //System.out.println("b_atk : " + p.b_atk);
                        //System.out.println("f_cd : " + s1.f_cd);
                        //System.out.println("pet_cd : " + s1.pet_cd);
                        //System.out.println("multy : " + p.skillItem.skillMulty);
                    }
                    if (p.currentEquip != null && p.currentEquip.numRune == 6) {
                        tr2.add(p);
                    }
                }
            }
        }
        int count = 0;
        tr1.clear();
        equipList.clear();
        for (PetType s1 : tr2) {
            count++;
            if (count < 80) {
                //System.out.println(s1.name+" : "+s1.finalValue+" ; "+s1.currentEquip);
                equipList.add(s1.currentEquip);
                tr1.add(s1.name);
            }
        }
        for (String s3 : tr1) {
            jComboEquipRunes.addItem(s3);
        }
        selectRuneEquip(x -> (x.atk * x.cd));

        BossList.clear();
        BossList.add(new PetType("Faimon Hell : HellHound", 400, 433, 130));
        BossList.add(new PetType("Faimon Hell : Fire Inu", 491, 582, 128));
        BossList.add(new PetType("Chikura Hell : Flower", 579, 1218, 128));
        BossList.add(new PetType("Chikura Hell : Scorpion", 837, 868, 139));

        BossList.add(new PetType("Rift Fire 1", 400, 200000.0 / 15, 100));
        BossList.add(new PetType("Rift Fire 2", 600, 300000.0 / 15, 100));
        BossList.add(new PetType("Rift Fire 3", 800, 400000.0 / 15, 100));
        BossList.add(new PetType("Giant B10", 1796, 11338, 75));
        BossList.add(new PetType("Dragon B10", 2502, 11333, 135));
        BossList.add(new PetType("Necro B10", 1098, 9995, 90));
        BossList.add(new PetType("Water B10", 694, 18510 * 6, 114));
        BossList.add(new PetType("Fire B10", 1630, 1542, 114));
        BossList.add(new PetType("Wind B10", 1630, 9872, 114));
        BossList.add(new PetType("Raid 4", 2331, 45218, 186));
        BossList.add(new PetType("Raid 5", 2670, 57560, 192));

        BossList.add(new PetType("Chasun", 585, 27291, 112, 2));
        BossList.add(new PetType("Rakan", 430, 26795, 47, 2));
        BossList.add(new PetType("Perna", 220, 16278, 85, 2));
        BossList.add(new PetType("Theomars", 154, 5956, 107, 2));
        BossList.add(new PetType("Verad", 1140, 18886, 146, 2));
        BossList.add(new PetType("Eladriel", 785, 28260, 48, 2));
        BossList.add(new PetType("Chloe", 715, 14504, 164, 2));
        jComboBoss.removeAllItems();
        for (PetType p1 : BossList) {
            jComboBoss.addItem(p1.name);
        }
    }

    public void updatePetMath(PetType p2, String petSkill, RuneSet runeEquip) {
        if (p2 != null) {
            for (RuneSkill r1 : p2.skillList) {
                //System.out.println(r1.skillName);
                if (r1.skillName.equalsIgnoreCase(petSkill)) {
                    jTextSkillDesc.setText(r1.skillDesc);
                    //System.out.println("Found skill : " + r1.skillDesc);
                    String mainText = "";
                    String dmg = "(Final_CritDmg+1)*" + r1.skillMulty + "*(Defense Reduction)";
                    p2.skillItem = r1;
                    RuneSet.runePet = p2;

                    RuneSet mainEquipSet = runeEquip;
                    if (mainEquipSet != null) {
                        mainEquipSet.equipOnPet(p2);
                        p2.currentEquip = mainEquipSet;
                    }
                    mainText += r1.skillName + " : " + r1.skillMulty + "    ;  Main damage = " + dmg;
                    long bossHp = curBoss.hp;
                    if (r1.skillMulty.contains("TARGET_TOT_HP") && (curBoss.name.startsWith("Rift") || curBoss.name.startsWith("Raid"))) {
                        mainText += "\n Scale HP skill only 10% effective againt Rift boss. This boss hp is : " + curBoss.hp + " will be cut to " + curBoss.hp / 10;
                        bossHp = bossHp / 10;
                    }

                    int skill_up = r1.skillUp;
                    int glory_cd = ConfigInfo.getInstance().glory_cd;
                    int glory_atk = ConfigInfo.getInstance().glory_atk;
                    int glory_def = ConfigInfo.getInstance().glory_def;
                    int glory_spd = ConfigInfo.getInstance().glory_spd;
                    int ele_atk = ConfigInfo.getInstance().gloryAtkElement[p2.element];

                    if (jComboLeaderSkill.getSelectedIndex() == 0) {
                        p2.atk_leader = 0;
                        p2.def_leader = 0;
                    }

                    int leader_atk = (int) p2.atk_leader;
                    int leader_def = 0;

                    long f_atk = p2.b_atk + p2.r_atk + p2.b_atk * (glory_atk + ele_atk + leader_atk) / 100;
                    //long f_atk = (p2.b_atk * (glory_atk + ele_atk + 0 + (int)p2.atk_leader) / 100 + mainEquipSet.pet_atk);
                    long f_def = p2.b_def + p2.r_def + p2.b_def * (glory_def + leader_def) / 100;
                    double f_cd = p2.b_cd + p2.r_cd + glory_cd + skill_up + r1.extra_cd * 100;

                    mainText += "\n\nGlory Atack = " + glory_atk + "% ; Glory Element " + p2.attribute + " Atack : " + ele_atk + "%"
                            + " ; Glory CD = " + glory_cd + " ; Glory SPD = " + glory_spd + " ; skill_up = " + skill_up + " ; LeaderSkill = " + jComboLeaderSkill.getSelectedItem();
                    if (r1.skillMulty.contains("ATK")) {
                        mainText += "\nFinal Attack = RuneAtk +BaseAtk*(100+Glory_Atk+Ele_Atack+Leader_Atk)/100 = "
                                + p2.r_atk + "+" + p2.b_atk + "*(100+" + glory_atk + "+" + ele_atk + "+" + leader_atk + ")/100 = " + f_atk + ",+ Atk buff(*1.5) = " + f_atk * 1.5;
                    }
                    if (r1.skillMulty.contains("DEF")) {
                        mainText += "\nFinal_Def = RuneDef +BaseDef*(100+Glory_Def)/100 = "
                                + p2.r_def + "+" + p2.b_def + "*(100+" + glory_def + ")/100 = " + f_def + "+def buff(*1.7)=" + f_def * 1.7;
                    }
                    mainText += "\nFinal_CritDmg = BaseCD +RuneCD + Glory_CD+Skill_up+(skill_bonus_cd) = "
                            + (p2.r_cd + p2.b_cd) + " + " + glory_cd + "+" + skill_up + "+" + r1.extra_cd * 100 + " = " + f_cd + "% = " + f_cd / 100;

                    mainEquipSet.enemy_hp = bossHp;
                    //System.out.println("damageMultySkill : " + r1.damageMultySkill);
                    long skillDamage = Math.round(r1.damageMultySkill.apply(mainEquipSet));

                    //1000/(1140+boss_def*3.5) http://summonerswar.wikia.com/wiki/Equations
                    long dmgReduce = Math.round(1140 + curBoss.def * 0.3 * 3.5);
                    if (r1.ignoreDmg) {
                        dmgReduce = 1140;
                        mainText += "\nDamage Reduction (This skill Ignore damage) = 1000/" + dmgReduce;
                    } else {
                        mainText += "\nDamage Reduction (defbreak lose 70%->*0.3) = 1000/(1140+enemy_def*0.3*3.5) = 1000/(1140+" + curBoss.def + "0.3*3.5)=1000/" + dmgReduce;
                    }

                    if (r1.skillMulty.contains("DEF")) {
                        p2.atk_buff = 1.0;
                        p2.def_buff = 1.7;
                        f_def = Math.round(f_def * 1.7);
                    } else if (r1.skillMulty.contains("ATK")) {
                        p2.atk_buff = 1.5;
                        p2.def_buff = 1.0;
                        f_atk = Math.round(f_atk * 1.5);
                    }

                    if (r1.skillMulty.contains("DICE")) {
                        mainText += "\n Dice value random 1..6. Let avg Dice=3.5";
                    }
                    mainText += "\nSkill Multy = " + r1.skillMulty + " = "
                            + r1.skillMulty.replace("ATK", "" + f_atk).replace("ATTACK_DEF", "" + f_def)
                            .replace("DEF", "" + f_def).replace("TARGET_TOT_HP", "" + bossHp)
                            + " = " + skillDamage;
                    String moreHits = "";
                    long fhits = Math.round((f_cd / 100 + 1) * skillDamage * 1000 / dmgReduce);
                    long fhit = fhits;
                    if (r1.numHits > 1) {
                        moreHits = " ; Multyhits = " + fhits + "x" + r1.numHits + " = " + (fhits * r1.numHits);
                        fhit = fhits * r1.numHits;
                    }
                    mainText += "\n\nFinal Damage on defbreak = " + dmg + " = " + (f_cd / 100 + 1) + "*" + skillDamage + "*1000/" + dmgReduce + " = " + fhits + moreHits;
                    int nd = jComboNumDebuff.getSelectedIndex();
                    if (r1.debuffScale > 0) {
                        mainText += "\n This skill scale with debuf : " + r1.debuffScale + "% ; Numdebuf = " + nd;
                        mainText += "    ;    Final dmg on defbreak = dmg*(1+numDebuff*" + r1.debuffScale + "% )= " + fhit + "*(1+"
                                + nd + "*" + r1.debuffScale + ")=" + (fhit + fhit * r1.debuffScale * nd / 100);
                    }
                    if (r1.debuffIncre > 0) {
                        mainText += "\n This skill scale increase when have debuf : " + r1.debuffIncre + "% ;";
                        mainText += "    ;    Final dmg on defbreak = dmg*(1+" + r1.debuffIncre + "% )= " + fhit + "*(1+"
                                + r1.debuffIncre + ")=" + (fhit + fhit * r1.debuffIncre / 100);
                    }
                    if (r1.ignoreChance) {
                        dmgReduce = Math.round(1140 + curBoss.def * 3.5);
                        fhits = Math.round((f_cd / 100 + 1) * skillDamage * 1000 / dmgReduce);
                        long total = (r1.numHits - 1) * fhits + p2.currentEquip.finalDamage(0, 0, 0);

                        mainText += "\n This skill ignore defense with " + r1.ignoreChanceNum + "% chance at least 1 hit ignore ";
                        mainText += "\nFinal dmg on no-debreak units, no ignore defense : " + r1.numHits * fhits;
                        mainText += "\nFinal dmg on no-debreak units, with 1 ignore defense hit = " + total;
                    }
                    String skillFilter = jComboSkillFilter.getSelectedItem().toString().toLowerCase();
                    if (r1.aoeIndex != 1.0) {
                        mainText += "\n This skill only do " + r1.aoeIndex + " * aoe dmg : " + fhit * r1.aoeIndex;
                    }
                    jTextSkillExplain.setText(mainText);

                    //System.out.println("MainText : " + mainText);
                    double f_cd2 = (1 + ((double) mainEquipSet.pet_cd + glory_cd + skill_up) / 100.0);
                    //System.out.println("DMG vs boss : " + p2.name + " : " + mainEquipSet.dmgVsBoss(curBoss.def * 0.3, curBoss.hp));
                    //System.out.println("Pure DMG : " + mainEquipSet.pureDamage());
                    //System.out.println("f_cd : " + mainEquipSet.f_cd);
                    //System.out.println("f_atk : " + Math.round(mainEquipSet.f_atk));
                    //System.out.println("pet_atk : " + mainEquipSet.pet_atk);
                    //System.out.println("leader_skill : " + p2.leader_skill);
                    //System.out.println("pet_cd : " + mainEquipSet.pet_cd);
                    //System.out.println("f_cd2 : " + f_cd2);
                    //System.out.println("f_cd : " + f_cd);
                }
            }
        }
    }
    // End of variables declaration//GEN-END:variables

    final class RenderWrapText extends DefaultTableCellRenderer {

        JTextArea textarea;

        @Override
        public Component getTableCellRendererComponent(
                JTable aTable, Object aNumberValue, boolean aIsSelected,
                boolean aHasFocus, int aRow, int aColumn) {
            String value = (String) aNumberValue;

            textarea = new JTextArea();
            aTable.add(textarea);

            textarea.setWrapStyleWord(true);
            textarea.setLineWrap(true);
            DefaultCaret caret = (DefaultCaret) textarea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

            textarea.setText(value);
            aTable.setRowHeight(90);

            if (aNumberValue == null) {
                return this;
            }

            Component renderer = super.getTableCellRendererComponent(
                    aTable, aNumberValue, aIsSelected, aHasFocus, aRow, aColumn
            );
            return this;
        }
    }

    private class ListSelectionListenerImpl implements ListSelectionListener {

        public ListSelectionListenerImpl() {
        }

        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (jTablePetsCompare.getSelectedRow() > -1) {
                // print first column value from selected row
                //System.out.println(jTableAllRunes.getValueAt(jTableAllRunes.getSelectedRow(), 1).toString());
                String petName = jTablePetsCompare.getValueAt(jTablePetsCompare.getSelectedRow(), 0).toString();
                String petSkill = jTablePetsCompare.getValueAt(jTablePetsCompare.getSelectedRow(), 2).toString();
                //System.out.println("petSkill : " + petSkill);
                jTextPetName.setText(petName);

                PetType p2 = SwManager.petsBestiary.get(petName);
                updatePetMath(p2, petSkill, mainEquipSet);
            }
        }
    }
}
