package com.ao.layout.generator.window;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Footer extends JPanel {

    public Footer(Listener listener) {


        JCheckBox allFind = new JCheckBox("all find");
        allFind.setPreferredSize(new Dimension(75, 26));
        allFind.setSelected(true);
        allFind.addChangeListener(e ->
                listener.allFind(allFind.isSelected())
        );

        JCheckBox allClick = new JCheckBox("all click");
        allClick.setPreferredSize(new Dimension(100, 26));
        allClick.addChangeListener(e ->
                listener.allClick(allClick.isSelected())
        );

        JButton cancel = new JButton();
        cancel.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.close();
            }
        });
        cancel.setPreferredSize(new Dimension(120, 35));
        cancel.setText("Cancel");
        cancel.setVisible(true);

        JButton confirm = new JButton();
        confirm.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.sure();
            }
        });
        confirm.setPreferredSize(new Dimension(120, 35));
        confirm.setForeground(JBColor.blue);
        confirm.setText("Confirm");
        confirm.setVisible(true);

        setLayout(new BorderLayout());

        JPanel checkPanel = new JPanel();
        checkPanel.add(allFind);
        checkPanel.add(allClick);

        JPanel btnPanel = new JPanel();
        btnPanel.add(cancel);
        btnPanel.add(confirm);

        add(checkPanel, BorderLayout.WEST);
        add(btnPanel, BorderLayout.EAST);
    }

    public interface Listener {
        void close();

        void sure();

        void allFind(boolean select);

        void allClick(boolean select);
    }
}