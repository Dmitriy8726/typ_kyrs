package com.company;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

class VerticalLayout implements LayoutManager
{
    private Dimension size = new Dimension();

    // Следующие два метода не используются
    public void addLayoutComponent   (String name, Component comp) {}
    public void removeLayoutComponent(Component comp) {}

    // Метод определения минимального размера для контейнера
    public Dimension minimumLayoutSize(Container c) {
        return calculateBestSize(c);
    }
    // Метод определения предпочтительного размера для контейнера
    public Dimension preferredLayoutSize(Container c) {
        return calculateBestSize(c);
    }
    // Метод расположения компонентов в контейнере
    public void layoutContainer(Container container)
    {
        // Список компонентов
        Component list[] = container.getComponents();
        int currentY = 5;
        for (int i = 0; i < list.length; i++) {
            // Определение предпочтительного размера компонента
            Dimension pref = list[i].getPreferredSize();
            // Размещение компонента на экране
            list[i].setBounds(5, currentY, pref.width, pref.height);
            // Учитываем промежуток в 5 пикселов
            currentY += 5;
            // Смещаем вертикальную позицию компонента
            currentY += pref.height;
        }
    }
    // Метод вычисления оптимального размера контейнера
    private Dimension calculateBestSize(Container c)
    {
        // Вычисление длины контейнера
        Component[] list = c.getComponents();
        int maxWidth = 0;
        for (int i = 0; i < list.length; i++) {
            int width = list[i].getWidth();
            // Поиск компонента с максимальной длиной
            if ( width > maxWidth )
                maxWidth = width;
        }
        // Размер контейнера в длину с учетом левого отступа
        size.width = maxWidth + 5;
        // Вычисление высоты контейнера
        int height = 0;
        for (int i = 0; i < list.length; i++) {
            height += 5;
            height += list[i].getHeight();
        }
        size.height = height;
        return size;
    }
}


public class Main {
    private static String jt_str_term, jt_str_noterm, jt_str_begin, jt_str_numb, jt_str_nume;
    private static int num_begin, num_end;
    private static int index = 0;
    private static JFrame myWindow;
    private static JPanel jp_main;
    private static JPanel jp_rule;
    private static JPanel jp_gramm;
    private static JPanel jp_chain;
    private static ArrayList <String> str_temp_term = new ArrayList<>(); //терминальные символы
    private static ArrayList <String> str_temp_noterm = new ArrayList<>(); // нетерминальные символы канон вида
    private static HashMap <String, ArrayList<String>> rule = new HashMap<>(); //Содержит перечень правил канон вида
    private static HashMap <String, ArrayList<String>> answer_bas = new HashMap<>(); //Содержит перечень ответов простого вида
    private static HashMap <String, ArrayList<String>> answer_Hom = new HashMap<>(); //Содержит перечень ответов вида Хомского
    private static HashMap <String, ArrayList<String>> rule_Hom = new HashMap<>(); //Содержит перечень правил вида Хомского
    private static ArrayList <String> str_temp_noter_Hom = new ArrayList<>(); // нетерминальные символы вида Хомского
    private static HashMap<Integer, HashMap<String, String>> tree_number = new HashMap<>(); // Содержит номера правил
    private static HashMap<Integer, HashMap<String, String>> tree_number_Hom = new HashMap<>(); // Содержит номера правил для Хомского
    private static JMenuItem avtor;

    public static void main(String[] args) {
        myWindow = new JFrame("KC");
        myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menubar = new JMenuBar();
        // создаем меню
        menubar.add(info(myWindow));
        menubar.add(file(myWindow));
        // добавляем панель меню в окно
        myWindow.setJMenuBar(menubar);
        // панель для внесения данных
        jp_main = new JPanel(new VerticalLayout());
        jp_rule = new JPanel(new VerticalLayout());
        jp_gramm = new JPanel(new VerticalLayout());
        jp_chain = new JPanel(new VerticalLayout());
        //Панель для терминальных
        JPanel panel1 = new JPanel();
        JLabel lb_term = new JLabel("Терминальные символы: ");
        JTextField jt_term = new JTextField(15);
        panel1.add(lb_term);
        panel1.add(jt_term);
        jp_main.add(panel1);
        //Панель для не терминальных
        panel1 = new JPanel();
        JLabel lb_noterm = new JLabel("Нетерминальные символы: ");
        JTextField jt_noterm = new JTextField(15);
        panel1.add(lb_noterm);
        panel1.add(jt_noterm);
        jp_main.add(panel1);
        //Начальный символ
        panel1 = new JPanel();
        JLabel jl_begin = new JLabel("Начальный символ: ");
        JTextField jt_begin = new JTextField(15);
        panel1.add(jl_begin);
        panel1.add(jt_begin);
        jp_main.add(panel1);
        //Диапазон
        panel1 = new JPanel();
        JLabel jl_diap_beg = new JLabel("От: ");
        JTextField jt_diap_beg = new JTextField(2);
        panel1.add(jl_diap_beg);
        panel1.add(jt_diap_beg);
        JLabel jl_diap_end = new JLabel("До: ");
        JTextField jt_diap_end = new JTextField(2);
        panel1.add(jl_diap_end);
        panel1.add(jt_diap_end);
        jp_main.add(panel1);
        myWindow.add(jp_main);
        //Добавление кнопки для проверки данных
        JButton check = new JButton("Next");
        check.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                jt_str_term = jt_term.getText();
                jt_str_noterm = jt_noterm.getText();
                jt_str_begin = jt_begin.getText();
                jt_str_numb = jt_diap_beg.getText();
                jt_str_nume = jt_diap_end.getText();
                for (String std:jt_str_term.split(" ")) {
                    str_temp_term.add(std);
                }
                for (String std:jt_str_noterm.split(" ")) {
                    str_temp_noterm.add(std);
                    str_temp_noter_Hom.add(std);
                }
                String regex = "-?\\d+(\\.\\d+)?";
                if (!(jt_str_numb.matches(regex)) || !(jt_str_nume.matches(regex))) {
                    str_temp_noterm.clear();
                    str_temp_term.clear();
                    str_temp_noter_Hom.clear();
                    JOptionPane.showMessageDialog(myWindow, "ОШИБКА, ДИАПАЗОН ЗАДАЕТСЯ ТОЛЬКО ЧИСЛАМИ");
                    return;
                } else {
                    num_begin = Integer.parseInt(jt_str_numb);
                    num_end = Integer.parseInt(jt_str_nume);
                    if (num_begin == 0 || num_end == 0 || num_end - num_begin <= 0) {
                        str_temp_noterm.clear();
                        str_temp_term.clear();
                        str_temp_noter_Hom.clear();
                        JOptionPane.showMessageDialog(myWindow, "ОШИБКА, ДИАПАЗОН ЗАДАН НЕ ВЕРНО");
                        return;
                    }
                }

                if (str_temp_noterm.isEmpty()) {
                    str_temp_noterm.clear();
                    str_temp_term.clear();
                    str_temp_noter_Hom.clear();
                    JOptionPane.showMessageDialog(myWindow, "ОШИБКА, ПУСТОЕ ПОЛЕ");
                    return;
                }
                if (str_temp_term.isEmpty()) {
                    str_temp_noterm.clear();
                    str_temp_term.clear();
                    str_temp_noter_Hom.clear();
                    JOptionPane.showMessageDialog(myWindow, "ОШИБКА, ПУСТОЕ ПОЛЕ");
                    return;
                }
                //Проверка на вшивость
                boolean flag = true;
                for (String std: str_temp_noterm) {
                    if (std.equals(jt_str_begin)) {
                        flag = false;
                    }
                }
                if (flag) {
                    str_temp_noterm.clear();
                    str_temp_term.clear();
                    str_temp_noter_Hom.clear();
                    JOptionPane.showMessageDialog(myWindow, "ОШИБКА, НАЧАЛЬНЫЙ  СИМВОЛ НЕ ПРИНАДЛЕЖИТ НЕТЕРМИНАЛЬНЫМ");
                    return;
                }

                flag = false;
                for (String std: str_temp_noterm) {
                    for (String std1 : str_temp_term) {
                        if (std.equals(std1)) {
                            flag = true;
                        }
                    }
                }

                if (flag) {
                    str_temp_noterm.clear();
                    str_temp_term.clear();
                    str_temp_noter_Hom.clear();
                    JOptionPane.showMessageDialog(myWindow, "ОШИБКА, ТЕРМИНАЛЬНЫЕ СИВМОЛЫ   ПРИНАДЛЕЖАТ НЕТЕРМИНАЛЬНЫМ");
                    return;
                }
                jp_main.setVisible(false);
                rules();

            }
        });
        jp_main.add(check);
        myWindow.setVisible(true);
        myWindow.setSize(800, 300);
        myWindow.setResizable(false);
    }

    public static void rules() {
        JLabel pt = new JLabel();
        pt.setText(str_temp_noterm.get(index));
        JTextField pp = new JTextField(14);
        JButton ne = new JButton("Next");
        ne.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (str_temp_noterm.size() == index + 1) {
                    if(check_Rules(pp.getText())) {
                        if (check_Kanon()) {
                            jp_rule.setVisible(false);
                            homski();
                        } else {
                            rule.clear();
                            index = 0;
                            jp_rule.removeAll();
                            jp_rule.updateUI();
                            rules();
                        }
                    } else {
                        rule.clear();
                        index = 0;
                        jp_rule.removeAll();
                        jp_rule.updateUI();
                        rules();
                    }
                } else {
                    if(check_Rules(pp.getText())) {
                        index++;
                    } else {
                        rule.clear();
                        index = 0;
                    }
                    jp_rule.removeAll();
                    jp_rule.updateUI();
                    rules();
                }
            }
        });
        jp_rule.add(pt);
        jp_rule.add(pp);
        jp_rule.add(ne);
        myWindow.add(jp_rule);
    }

    public static boolean check_Rules(String pp) {
        if (pp.equals("")) {
            JOptionPane.showMessageDialog(myWindow, "ВВИД ГРАММАТИКИ НЕ В  КАНОНИЧЕСКОМ ВИДЕ");
            return false;
        }
        ArrayList<String> templ = new ArrayList<>();
        for (String std: pp.split(" ")) {
            templ.add(std);
        }

        for (String std: templ) {
            for (String s:jt_str_term.split(" ")) {
                std = std.replaceAll(s, "");
            }
            for (String s:jt_str_noterm.split(" ")) {
                std = std.replaceAll(s, "");
            }
            if (!(std.equals(""))) {
                JOptionPane.showMessageDialog(myWindow, "В ПРАВИЛЕ ЕСТЬ СИМВОЛЫ КОТОРЫХ НЕТУ В ГРАММАТИКЕ");
                return false;
            }
        }
        rule.put(str_temp_noterm.get(index), templ);
        return true;
    }

    public static boolean check_Kanon() {
        // цепные правила
        for (int i = 0; i < str_temp_noterm.size(); i++) {
            for (String str : rule.get(str_temp_noterm.get(i))) {
                for (String st: str_temp_noterm) {
                    if (str.equals(st)) {
                        JOptionPane.showMessageDialog(myWindow, "ВВИД ГРАММАТИКИ НЕ В  КАНОНИЧЕСКОМ ВИДЕ 4 " + str_temp_noterm.get(i) );
                        return false;
                    }
                }
            }
        }
        // все недостижимые символы
        String ttt = jt_str_begin;
        for (int i = 0; i < str_temp_noterm.size(); i++) {
            for (String str : rule.get(str_temp_noterm.get(i))) {
                for (String st: str_temp_noterm) {
                    if (str.contains(st) && !ttt.contains(st)) {
                      ttt += st;
                    }
                }
            }
        }
        if (!(ttt.length() == str_temp_noterm.size())) {
            JOptionPane.showMessageDialog(myWindow, "ВВИД ГРАММАТИКИ НЕ В  КАНОНИЧЕСКОМ ВИДЕ 2");
            return false;
        }

        //все бесплодные символы
        String strp = "";
        int counter = 0;
        int intz = 0, intf;
        do {
            intf = intz;
            for (int i = 0; i < str_temp_noterm.size(); i++) {
                for (String str : rule.get(str_temp_noterm.get(i))) {
                    for (String st : str_temp_noterm) {
                        if (!str.contains(st) || strp.contains(st)) {
                            counter++;
                        }
                    }
                    if (counter == str_temp_noterm.size()) {
                        if (!strp.contains(str_temp_noterm.get(i))) {
                            strp += str_temp_noterm.get(i);
                            intz++;
                        }
                    }
                    counter = 0;
                }
            }
        } while (intz - intf > 0);
        if (intz != str_temp_noterm.size()) {
            JOptionPane.showMessageDialog(myWindow, "ВВИД ГРАММАТИКИ НЕ В  КАНОНИЧЕСКОМ ВИДЕ 1");
            return false;
        }

        return true;
    }

    public static void homski() {
        int[] term;
        int[] noterm;
        for (int i = 0; i < str_temp_noterm.size(); i++) {
            ArrayList<String> templ = new ArrayList<>();
            ArrayList<String> pp = new ArrayList<>();
            for (String str : rule.get(str_temp_noterm.get(i))) {
                noterm = new int[]{0, 0};
                term = new int[]{0, 0};
                // 5 правило
                boolean flag = true;
                if (str.length() > 2) {
                    String s = str.substring(0, 1);
                    for (String nstr: str_temp_term) {
                        if (str.substring(0, 1).equals(nstr)) {
                            s = "<" + str.substring(0, 1) + ">";
                            for (String sss: str_temp_noter_Hom) {
                                if (sss.equals(s)) {
                                    flag = false;
                                }
                            }
                            if (flag) {
                                str_temp_noter_Hom.add(s);
                                flag = true;
                            }
                            pp.add(str.substring(0, 1));
                            rule_Hom.put(s, pp);
                            pp = new ArrayList<>();
                        }
                    }
                    templ.add(s + "<" + str.substring(1) + ">");
                    int p = str.length() - 2;
                    str = str.substring(1);
                    for (int j = 0; j < p; j++) {
                        s = str.substring(0, 1);
                        for (String nstr: str_temp_term) {
                            if (str.substring(0, 1).equals(nstr)) {
                                s = "<" + str.substring(0, 1) + ">";
                                for (String sss: str_temp_noter_Hom) {
                                    if (sss.equals(s)) {
                                        flag = false;
                                    }
                                }
                                if (flag) {
                                    str_temp_noter_Hom.add(s);
                                    flag = true;
                                }
                                pp.add(str.substring(0, 1));
                                rule_Hom.put(s, pp);
                                pp = new ArrayList<>();
                            }
                        }
                        pp.add(s + str.substring(1));
                        for (String sss: str_temp_noter_Hom) {
                            if (sss.equals("<" + str + ">")) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            str_temp_noter_Hom.add("<" + str + ">");
                            flag = true;
                        }
                        rule_Hom.put("<" + str + ">", pp);

                        pp = new ArrayList<>();
                        str = str.substring(1);
                    }

                } else {
                    // 1 правило
                    for (String nstr: str_temp_term) {
                        countChar(str, nstr.charAt(0), term);
                    }
                    for (String nstr: str_temp_noterm) {
                        countChar(str, nstr.charAt(0), noterm);
                    }
                    if ((noterm[0] == 1 && noterm[1] == 1 && term[0] == 0 && term[1] == 0) ||
                            (noterm[0] == 0 && noterm[1] == 0 && term[0] == 1 && term[1] == 0)) {
                        templ.add(str);

                    }
                    // 2 правило
                    flag = true;
                    if (noterm[0] == 0 && noterm[1] == 1 && term[0] == 1 && term[1] == 0) {
                        String s = "<" + str.substring(0, 1) + ">";
                        for (String sss: str_temp_noter_Hom) {
                            if (sss.equals(s)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            str_temp_noter_Hom.add(s);
                        }
                        templ.add(s + str.substring(1));
                        pp.add(str.substring(0, 1));
                        rule_Hom.put(s, pp);
                        pp = new ArrayList<>();
                    }
                    // 3 правило
                    flag = true;
                    if (noterm[0] == 1 && noterm[1] == 0 && term[0] == 0 && term[1] == 1) {
                        String s = "<" + str.substring(1) + ">";
                        for (String sss: str_temp_noter_Hom) {
                            if (sss.equals(s)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            str_temp_noter_Hom.add(s);
                        }
                        templ.add(str.substring(0, 1) + s);
                        pp.add(str.substring(1));
                        rule_Hom.put(s, pp);
                        pp = new ArrayList<>();
                    }
                    //4 правило
                    flag = true;
                    if (noterm[0] == 0 && noterm[1] == 0 && term[0] == 1 && term[1] == 1) {
                        String s = "<" + str.substring(0, 1) + ">";
                        for (String sss: str_temp_noter_Hom) {
                            if (sss.equals(s)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            str_temp_noter_Hom.add(s);
                        }
                        flag = true;
                        String t = "<" + str.substring(1) + ">";
                        for (String sss: str_temp_noter_Hom) {
                            if (sss.equals(t)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            str_temp_noter_Hom.add(t);
                        }
                        templ.add(s + t);
                        pp.add(str.substring(0, 1));
                        rule_Hom.put(s, pp);
                        pp = new ArrayList<>();
                        pp.add(str.substring(1));
                        rule_Hom.put(t, pp);
                        pp = new ArrayList<>();
                    }
                }


            }
            rule_Hom.put(str_temp_noterm.get(i), templ);
        }
        String s = new String();
        JTextArea textArea2 = new JTextArea(5, 30);
        s += "Правила канонического вида \n";
        for (int i = 0; i < rule.size(); i++) {
            s += str_temp_noterm.get(i) + " = ";
            for (String std: rule.get(str_temp_noterm.get(i))) {
                s += std + " | ";
            }
            s += "\n";
        }
        textArea2.append(s);
        textArea2.setEnabled(false);
        JScrollPane scroll = new JScrollPane(textArea2,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jp_gramm.add(scroll);
        s = new String();
        JTextArea textArea1 = new JTextArea(5, 30);
        s += "Правила вида нормальной форме Хомского \n";
        for (int i = 0; i < rule_Hom.size(); i++) {
            s += str_temp_noter_Hom.get(i) + " = ";
            for (String std: rule_Hom.get(str_temp_noter_Hom.get(i))) {
                s += std + " | ";
            }
            s += "\n";
        }
        textArea1.append(s);
        textArea1.setEnabled(false);
        JScrollPane scroll1 = new JScrollPane(textArea1,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jp_gramm.add(scroll1);
        JButton bat = new JButton("Next");
        bat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jp_gramm.setVisible(false);
                generation(rule, str_temp_noterm, answer_bas, tree_number);
                generation(rule_Hom, str_temp_noter_Hom, answer_Hom, tree_number_Hom);
                JTextArea textArea = new JTextArea(5, 30);
                int ind = 0;
                for (int i = num_begin; i <= num_end; i++) {
                    textArea.append(i + "\n");
                    for (String str: answer_bas.get(Integer.toString(i))) {
                        for (String str1: answer_Hom.get(Integer.toString(i))) {
                            if (str.equals(str1)) {
                                ind++;
                            }
                        }
                    }
                    if (ind == answer_bas.get(Integer.toString(i)).size() && ind == answer_bas.get(Integer.toString(i)).size()) {
                        for (String str: answer_bas.get(Integer.toString(i))) {
                            textArea.append(str + " ");
                        }
                        textArea.append("\n");
                        for (String str: answer_Hom.get(Integer.toString(i))) {
                            textArea.append(str + " ");
                        }
                    } else {
                        JOptionPane.showMessageDialog(myWindow, "НЕСОВПАДЕНИЕ ЦЕПОЧЕК " + ind);
                        return;
                    }
                    ind = 0;
                    textArea.append(" \n");
                }
                JButton batton = new JButton("Tree");
                JPanel panel1 = new JPanel();
                JTextField text_tree = new JTextField(2);
                batton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        jp_chain.setEnabled(false);
                        JFrame myWindow = new JFrame("Tree");
                        myWindow.addWindowListener(new WindowAdapter() {
                            public void windowClosing(WindowEvent e) {
                                jp_chain.setEnabled(true);
                            }
                        });
                        myWindow.setVisible(true);
                        myWindow.setSize(500, 500);
                        myWindow.setResizable(false);
                        String s = text_tree.getText();
                        int lenght = s.length();
                        int counter = 2;
                        String str = null;
                        try {
                            str = tree_number.get(lenght).get(s);
                        } catch (Exception ef) {
                            JOptionPane.showMessageDialog(myWindow, "ЭТО МНОЖЕСТВО НЕ ПРЕНАДЛЕЖИТ К КОНЕЧНЫМ ЦЕПОЧКАМ");
                            return;
                        }
                        String ROOT = null;
                        for ( String key : tree_number.get(lenght).keySet() ) {
                            if (tree_number.get(lenght).get(key).equals(str.substring(0, 1))) {
                                ROOT = key;
                            }
                        }
                        String num_rul = str.substring(0, 1);
                        String time_root = null;
                        boolean flag = false;
                        boolean flag_term = true;
                        DefaultMutableTreeNode root = new DefaultMutableTreeNode(ROOT);
                        while(counter <= str.length()) {
                            Enumeration en = root.depthFirstEnumeration();
                            while (en.hasMoreElements()) {
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
                                if (node.isLeaf()) {
                                    flag = false;
                                    String s_non_term = null;
                                    for (String str_1: str_temp_noterm) {
                                        if (str_1.equals(node.toString())) {
                                            flag = true;
                                            s_non_term = str_1;
                                        }
                                    }
                                    if (flag) {
                                        String temp_one = null;
                                        String temo_two = null;
                                        for ( String key : tree_number.get(lenght).keySet() ) {
                                            if (tree_number.get(lenght).get(key).equals(num_rul)) {
                                                temp_one = key;
                                            }
                                        }
                                        for ( String key : tree_number.get(lenght).keySet() ) {
                                            if (tree_number.get(lenght).get(key).equals(str.substring(0, counter))) {
                                                temo_two = key;
                                            }
                                        }
                                        flag_term = true;
                                        if (temp_one.length() == temo_two.length()) {
                                            for (String sss: str_temp_term) {
                                                if (temo_two.substring(temp_one.indexOf(s_non_term) , temp_one.indexOf(s_non_term) + 1).equals(sss)) {
                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(temp_one.indexOf(s_non_term) , temp_one.indexOf(s_non_term) + 1), false));
                                                    flag_term = false;
                                                }
                                            }
                                            if (flag_term) {
                                                node.add(new DefaultMutableTreeNode(temo_two.substring(temp_one.indexOf(s_non_term), temp_one.indexOf(s_non_term) + 1)));
                                            }
                                        } else if (temp_one.indexOf(s_non_term) == 0){
                                            for(int i = 0;i < temo_two.length() - temp_one.length() + 1; i++) {
                                                flag_term = true;
                                                for (String sss: str_temp_term) {
                                                    if (temo_two.substring(i , i + 1).equals(sss)) {
                                                        node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), false));
                                                        flag_term = false;
                                                    }
                                                }
                                                if (flag_term) {
                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), true));
                                                }
                                            }
                                        } else if (temp_one.indexOf(s_non_term) == temp_one.length() - 1) {
                                            flag_term = true;
                                            for(int i = temp_one.length() - 1;i < temo_two.length() ; i++) {
                                                for (String sss: str_temp_term) {
                                                    if (temo_two.substring(i , i + 1).equals(sss)) {
                                                        node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), false));
                                                        flag_term = false;
                                                    }
                                                }
                                                if (flag_term) {
                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), true));
                                                }
                                            }
                                        } else {
                                            for(int i = temp_one.indexOf(s_non_term); i < temo_two.length() - temp_one.length() + 2 ; i++) {
                                                flag_term = true;
                                                for (String sss: str_temp_term) {
                                                    if (temo_two.substring(i , i + 1).equals(sss)) {
                                                        node.add(new DefaultMutableTreeNode(temo_two.substring(i , i + 1), false));
                                                        flag_term = false;
                                                    }
                                                }
                                                if (flag_term) {
                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), true));
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            num_rul = str.substring(0, counter);
                            counter++;
                        }


                        DefaultTreeModel treeModel1 = new DefaultTreeModel(root, true);
                        JTree tree1 = new JTree(treeModel1);
                        JPanel contents = new JPanel(new GridLayout(1, 2));


                        try {
                            str = tree_number_Hom.get(lenght).get(s);
                        } catch (Exception ef) {
                            JOptionPane.showMessageDialog(myWindow, "ЭТО МНОЖЕСТВО НЕ ПРЕНАДЛЕЖИТ К КОНЕЧНЫМ ЦЕПОЧКАМ");
                            return;
                        }
                        String ROOT1 = null;
                        for ( String key : tree_number_Hom.get(lenght).keySet() ) {
                            if (tree_number_Hom.get(lenght).get(key).equals(str.substring(0, 1))) {
                                ROOT1 = key;
                            }
                        }
                        num_rul = str.substring(0, 1);
                        counter = 2;
                        DefaultMutableTreeNode root1 = new DefaultMutableTreeNode(ROOT1);
                        boolean flag_end = false;
                        boolean flag_meddle = false;
                        while(counter <= str.length()) {
                            Enumeration en = root1.depthFirstEnumeration();
                            while (en.hasMoreElements()) {
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
                                if (node.isLeaf()) {
                                    flag = false;
                                    String s_non_term = null;
                                    for (String str_1: str_temp_noter_Hom) {
                                        if (str_1.equals(node.toString())) {
                                            flag = true;
                                            s_non_term = str_1;
                                        }
                                    }
                                    if (flag) {
                                        String temp_one = null;
                                        String temo_two = null;
                                        for ( String key : tree_number_Hom.get(lenght).keySet() ) {
                                            if (tree_number_Hom.get(lenght).get(key).equals(num_rul)) {
                                                temp_one = key;
                                            }
                                        }
                                        for ( String key : tree_number_Hom.get(lenght).keySet() ) {
                                            if (tree_number_Hom.get(lenght).get(key).equals(str.substring(0, counter))) {
                                                temo_two = key;
                                            }
                                        }
                                        flag_term = true;
                                        if (temp_one.substring(temp_one.length() - 1).equals(">")) {
                                            System.out.println(temp_one);
                                            for(int i = temp_one.length() - 1; i > 0; i-- ) {
                                                if (temp_one.substring(i, i + 1).equals("<")) {
                                                    if (temp_one.indexOf(s_non_term) == i) {
                                                        flag_end = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        if (countCharall(temp_one, str_temp_noter_Hom) == countCharall(temo_two, str_temp_noter_Hom)) {
                                            System.out.println(temp_one.indexOf(s_non_term) + " " + temp_one + " " + s_non_term);
                                                for (String sss : str_temp_term) {
                                                    if (temo_two.substring(temp_one.indexOf(s_non_term), temp_one.indexOf(s_non_term) + 1).equals(sss)) {
                                                        node.add(new DefaultMutableTreeNode(temo_two.substring(temp_one.indexOf(s_non_term), temp_one.indexOf(s_non_term) + 1), false));
                                                        flag_term = false;
                                                    }
                                                }
                                                if (flag_term) {
                                                    if (temo_two.substring(temp_one.indexOf(s_non_term), temp_one.indexOf(s_non_term) + 1).equals("<")) {
                                                        String p = "";
                                                        int i = temp_one.indexOf(s_non_term);
                                                        int j = temp_one.indexOf(s_non_term) + 1;
                                                        while (!temo_two.substring(i , j).equals(">")) {
                                                            i++;
                                                            j++;
                                                        }
                                                        p += temo_two.substring(temp_one.indexOf(s_non_term) , j);
                                                        node.add(new DefaultMutableTreeNode(p, true));
                                                    } else {
                                                        node.add(new DefaultMutableTreeNode(temo_two.substring(temp_one.indexOf(s_non_term), temp_one.indexOf(s_non_term) + 1)));
                                                    }
                                                }

                                        } else if (temp_one.indexOf(s_non_term) == 0 || temp_one.indexOf("<") == 0){
                                            for(int i = 0;i < temo_two.length() - temp_one.length() + 1; i++) {
                                                if (temo_two.substring(i, i + 1).equals("<")) {
                                                    String p = "";
                                                    while (!temo_two.substring(i, i + 1).equals(">")) {
                                                        p += temo_two.substring(i, i + 1);
                                                        i++;
                                                    }
                                                    p += temo_two.substring(i, i + 1);
                                                    node.add(new DefaultMutableTreeNode(p, true));
                                                } else {
                                                    flag_term = true;
                                                    for (String sss : str_temp_term) {
                                                        if (temo_two.substring(i, i + 1).equals(sss)) {
                                                            node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), false));
                                                            flag_term = false;
                                                        }
                                                    }
                                                    if (flag_term) {
                                                        if (temo_two.substring(i, i + 1) == "<") {
                                                            String p = null;
                                                            while (temo_two.substring(i, i + 1) != ">") {
                                                                p += temo_two.substring(i, i + 1);
                                                                i++;
                                                            }
                                                            node.add(new DefaultMutableTreeNode(p, true));
                                                        } else {
                                                            node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), true));
                                                        }
                                                    }
                                                }
                                            }
                                        } else
                                            if (temp_one.indexOf(s_non_term) == temp_one.length() - 1 || flag_end) {
                                                if (flag_end) {
                                                    for (int i = temp_one.indexOf(s_non_term);i < temo_two.length();i++) {
                                                        if (temo_two.substring(i, i + 1).equals("<")) {
                                                            String p = "";
                                                            while (!temo_two.substring(i, i + 1).equals(">")) {
                                                                p += temo_two.substring(i, i + 1);
                                                                i++;
                                                            }
                                                            p += temo_two.substring(i, i + 1);
                                                            node.add(new DefaultMutableTreeNode(p, true));
                                                        } else {
                                                            flag_term = true;
                                                            for (String sss : str_temp_term) {
                                                                if (temo_two.substring(i, i + 1).equals(sss)) {
                                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), false));
                                                                    flag_term = false;
                                                                }
                                                            }
                                                            if (flag_term) {
                                                                if (temo_two.substring(i, i + 1) == "<") {
                                                                    String p = null;
                                                                    while (temo_two.substring(i, i + 1) != ">") {
                                                                        p += temo_two.substring(i, i + 1);
                                                                        i++;
                                                                    }
                                                                    node.add(new DefaultMutableTreeNode(p, true));
                                                                } else {
                                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), true));
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    flag_term = true;
                                                    for (int i = temp_one.length() - 1; i < temo_two.length(); i++) {
                                                        if (temo_two.substring(i, i + 1).equals("<")) {
                                                            String p = "";
                                                            while (!temo_two.substring(i, i + 1).equals(">")) {
                                                                p += temo_two.substring(i, i + 1);
                                                                i++;
                                                            }
                                                            p += temo_two.substring(i, i + 1);
                                                            node.add(new DefaultMutableTreeNode(p, true));
                                                        } else {
                                                            flag_term = true;
                                                            for (String sss : str_temp_term) {
                                                                if (temo_two.substring(i, i + 1).equals(sss)) {
                                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), false));
                                                                    flag_term = false;
                                                                }
                                                            }
                                                            if (flag_term) {
                                                                if (temo_two.substring(i, i + 1) == "<") {
                                                                    String p = null;
                                                                    while (temo_two.substring(i, i + 1) != ">") {
                                                                        p += temo_two.substring(i, i + 1);
                                                                        i++;
                                                                    }
                                                                    node.add(new DefaultMutableTreeNode(p, true));
                                                                } else {
                                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), true));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                            for(int i = temp_one.indexOf(s_non_term); i < temo_two.length() - temp_one.length() + 2 ; i++) {

                                                flag_term = true;
                                                for (String sss: str_temp_term) {
                                                    if (temo_two.substring(i , i + 1).equals(sss)) {
                                                        node.add(new DefaultMutableTreeNode(temo_two.substring(i , i + 1), false));
                                                        flag_term = false;
                                                    }
                                                }
                                                if (flag_term) {
                                                    node.add(new DefaultMutableTreeNode(temo_two.substring(i, i + 1), true));
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            num_rul = str.substring(0, counter);
                            counter++;
                        }


                        DefaultTreeModel treeModel2 = new DefaultTreeModel(root1, true);
                        JTree tree2 = new JTree(treeModel2);
                        contents.add(new JScrollPane(tree1));
                        contents.add(new JScrollPane(tree2));
                        myWindow.setContentPane(contents);


                    }
                });
                panel1.add(text_tree);
                panel1.add(batton);
                JScrollPane scroll1 = new JScrollPane(textArea,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                jp_chain.add(scroll1);
                jp_chain.add(panel1);
                avtor.setEnabled(true);
                myWindow.add(jp_chain);
            }
        });
        jp_gramm.add(bat);
        myWindow.add(jp_gramm);
    }

    public static void generation(HashMap <String, ArrayList<String>> map,  ArrayList <String> list,
                                  HashMap <String, ArrayList<String>> ans, HashMap<Integer, HashMap<String, String>> tree) {
        boolean flag_all = true;
        boolean flag_term = true;
        boolean flag_add = true;
        int counter = 0;
        int add_tree = 1;
        ArrayList<String> m1 = new ArrayList<>();
        ArrayList<String> m2 = new ArrayList<>();
        ArrayList<String> answer = new ArrayList<>();
        String split1 = "", split2 = "";
        String s = null;
        String ss = null;
        HashMap <String, String> tree_tamp = new HashMap<>();
        m1.add(jt_str_begin);
        for (int f = num_begin; f <= num_end; f++) {
            answer = new ArrayList<>();
            flag_all = true;
            flag_term = true;
            flag_add = true;
            m1.clear();
            m2.clear();
            split1 = "";
            split2 = "";
            s = null;
            ss = null;
            m1.add(jt_str_begin);
            counter = 0;
            tree_tamp = new HashMap<>();
            tree_tamp.put("S", "1");
            while (flag_all) {
                for (String str1 : m1) {
                    split1 = "";
                    split2 = "";
                    counter = 0;
                    flag_term = true;
                    add_tree = 1;
                    while (flag_term) {
                        s = str1.substring(counter, counter + 1);
                        if (s.equals("<")) {
                            //Хомский
                            int counter_temp = counter;
                            counter++;
                            while (!(str1.substring(counter, counter + 1).equals(">"))) {
                                s += str1.substring(counter, counter + 1);
                                counter++;
                            }
                            s += str1.substring(counter, counter + 1);
                            if (counter_temp == 0) {
                                split2 = str1.substring(counter + 1);
                            } else {
                                split2 = str1.substring(counter + 1);
                                split1 = str1.substring(0, counter_temp);
                            }
                            flag_term = false;
                            counter = 0;
                        } else {
                            for (String str2 : list) {
                                if (str2.equals(s)) {
                                    flag_term = false;
                                    if (counter == 0) {
                                        split2 = str1.substring(counter + 1);
                                    } else {
                                        split2 = str1.substring(counter + 1);
                                        split1 = str1.substring(0, counter);
                                    }
                                    counter = 0;
                                }
                            }
                        }
                        counter++;
                    }

                    for (String str : map.get(s)) {
                        flag_add = true;
                        ss = split1 + str + split2;
                        if (countCharsimple(ss) > f) {
                            continue;
                        }
                        if (countCharall(ss, list) > f) {
                            continue;
                        }

                        for (String str3 : list) {
                            if (ss.contains(str3)) {
                                flag_add = false;
                                m2.add(ss);
                                tree_tamp.put(ss, tree_tamp.get(str1) + add_tree);
                                add_tree++;
                                break;
                            }
                        }

                        boolean flag = true;
                        if (flag_add && ss.length() == f) {
                            tree_tamp.put(ss, tree_tamp.get(str1) + add_tree);
                            add_tree++;
                            for (String sss : answer) {
                                if (sss.equals(ss)) {
                                    flag = false;
                                }
                            }
                            if (flag) {
                                answer.add(ss);
                            }
                        }
                    }
                }

                m1.clear();
                for (String str : m2) {
                    m1.add(str);
                }
                m2.clear();
                if (m1.isEmpty()) {
                    flag_all = false;
                }
            }
            ans.put(Integer.toString(f), answer);
            tree.put(f, tree_tamp);
        }
    }

    public static JMenu  info(JFrame glav) {
        JMenu file = new JMenu("Инфо");
        JMenuItem avtor = new JMenuItem(new AbstractAction("Автор") {
            public void actionPerformed(ActionEvent e) {
                glav.setEnabled(false);
                JFrame myWindow = new JFrame("Автор");
                myWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        glav.setEnabled(true);
                    }
                });
                JPanel panel = new JPanel();
                JLabel lab = new JLabel("<html><p align=\"center\">Студент 4 курса . группы ИП-613<br />Чистов Д.А.</p></html>");
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                panel.add(Box.createHorizontalGlue());
                panel.add(lab);
                panel.add(Box.createHorizontalGlue());
                myWindow.setContentPane(panel);
                myWindow.setVisible(true);
                myWindow.setSize(220, 100);
                myWindow.setResizable(false);
            }
        });

        JMenuItem notet = new JMenuItem(new AbstractAction("Правила") {
            public void actionPerformed(ActionEvent e) {
                glav.setEnabled(false);
                JFrame myWindow = new JFrame("Правила");
                myWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        glav.setEnabled(true);
                    }
                });
                JPanel panel = new JPanel();
                JLabel lab = new JLabel("<html><p>Ввод производить через пробел, запрещенные символы:скобки, символы не принадлежащие латинице, не больше 1 символа<br /></p></html>");
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                panel.add(Box.createHorizontalGlue());
                panel.add(lab);
                panel.add(Box.createHorizontalGlue());
                myWindow.setContentPane(panel);
                myWindow.setVisible(true);
                myWindow.setSize(220, 100);
                myWindow.setResizable(false);
            }
        });
        JMenuItem tema = new JMenuItem(new AbstractAction("Тема") {
            public void actionPerformed(ActionEvent e) {
                glav.setEnabled(false);
                JFrame myWindow = new JFrame("Тема");
                myWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        glav.setEnabled(true);
                    }
                });
                JPanel panel = new JPanel();

                JLabel lab = new JLabel("<html><p align=\"justify \">Написать программу, которая будет принимать  на вход контекст-" +
                        "<br />но-свободную грамматику в каноническом виде (проверить коррект-" +
                        "<br />ность задания и при отрицательном результате выдать соответствую-" +
                        "<br />щее сообщение) и приведёт её к нормальной форме Хомского. Програм-" +
                        "<br />ма должна проверить построенную грамматику (БНФ) на эквивалент-" +
                        "<br />ность исходной: по обеим грамматикам сгенерировать множества всех "+
                        "<br />цепочек в заданном пользователем диапазоне длин и проверить их на "+
                        "<br />идентичность. Для подтверждения корректности выполняемых действий "+
                        "<br />предусмотреть возможность корректировки любого из построенных мно-"+
                        "<br />жеств пользователем (изменение цепочки, добавление, удаление…). "+
                        "<br />При обнаружении несовпадения должна выдаваться диагностика разли-"+
                        "<br />чий – где именно несовпадения и в чём они состоят. Построить дере-"+
                        "<br />во вывода для любой выбранной цепочки из числа сгенерированных."+
                        "</p></html>");
                panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                panel.add(Box.createHorizontalGlue());
                panel.add(lab);
                panel.add(Box.createHorizontalGlue());
                myWindow.setContentPane(panel);
                myWindow.setVisible(true);
                myWindow.setSize(465, 300);
                myWindow.setResizable(false);
            }
        });
        file.add(avtor);
        file.add(tema);
        file.add(notet);
        return file;
    }

    public static JMenu file(JFrame glav) {
        JMenu file = new JMenu("Файл");
        avtor = new JMenuItem(new AbstractAction("Сохранить") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(myWindow, "ФАЙЛ СОХРАНЕН");
                try {
                    FileWriter writer = new FileWriter("save.txt", false);
                    writer.write("Канонический ввид ");
                    writer.append('\n');
                    writer.write(String.valueOf(rule));
                    writer.append('\n');
                    writer.write("Нормальная форма Хомского ");
                    writer.append('\n');
                    writer.write(String.valueOf(rule_Hom));
                    writer.append('\n');
                    writer.write("Цепочки канон. вида");
                    writer.append('\n');
                    writer.write(String.valueOf(answer_bas));
                    writer.append('\n');
                    writer.write("Цепочки Хомс. вида");
                    writer.append('\n');
                    writer.write(String.valueOf(answer_Hom));
                    writer.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        avtor.setEnabled(false);
        file.add(avtor);
        return file;
    }

    public static int[] countChar(String str, char c, int[] count)
    {

        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            count[i] = 1;
        }

        return count;
    }

    public static int countCharsimple(String str)
    {
        int count = 0;
        for (String c: str_temp_term) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '<') {
                    for (int j = i; j < str.length(); j++) {
                        if (str.charAt(j) == '>') {
                            i = j;
                            break;
                        }
                    }
                }
                if (str.charAt(i) == c.charAt(0))
                    count++;
            }
        }

        return count;
    }

    public static int countCharall(String str, ArrayList<String> list)
    {
        int count = 0;
        boolean flag = true;
        for (String c: str_temp_term) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '<') {
                    for (int j = i; j < str.length(); j++) {
                        if (str.charAt(j) == '>') {
                            i = j;
                            break;
                        }
                    }
                }
                if (str.charAt(i) == c.charAt(0))
                    count++;
            }
        }
        int f = 0;
        for (String c: list) {
            if (str.contains(c)) {
                for (int i = 0; i < str.length(); i++) {
                    if (c.charAt(0) == str.charAt(i) && c.charAt(0) == '<') {
                        flag = true;
                        f = i;
                        for (int j = 0; j < c.length(); j++, i++) {
                            if (c.charAt(j) != str.charAt(i)) {
                                flag = false;
                                i = f + 1;
                                break;
                            }
                        }
                        if (flag) {
                            count++;
                        }
                    } else if (c.charAt(0) == str.charAt(i)){
                        count++;
                    } else if (str.charAt(i) == '<') {
                        for (int j = i; j < str.length(); j++) {
                            if (str.charAt(j) == '>') {
                                i = j;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return count;
    }


}
