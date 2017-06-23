package com.ao.layout.generator.window;

import com.ao.layout.generator.view.View;

import javax.swing.*;
import java.awt.*;

public class Item extends JPanel {

    private View view;
    private JCheckBox findIdCheckBox;
    private JCheckBox clickCheckBox;

    public Item(View view) {

        this.view = view;

        setLayout(new FlowLayout());

        findIdCheckBox = new JCheckBox();
        findIdCheckBox.setPreferredSize(new Dimension(75, 26));
        findIdCheckBox.setSelected(true);
        findIdCheckBox.addChangeListener(e -> view.setSelect(!view.isSelect()));

        clickCheckBox = new JCheckBox();
        clickCheckBox.setPreferredSize(new Dimension(75, 26));
        clickCheckBox.addChangeListener(e -> view.setClick(!view.isClick()));

        JLabel typeLabel = new JLabel(view.getViewName());
        typeLabel.setPreferredSize(new Dimension(150, 26));

        JLabel idLabel = new JLabel(view.getId());
        idLabel.setPreferredSize(new Dimension(150, 26));

        JLabel fieldLabel = new JLabel(view.getFieldName());
        fieldLabel.setPreferredSize(new Dimension(150, 26));

        add(findIdCheckBox);
        add(clickCheckBox);
        add(typeLabel);
        add(idLabel);
        add(fieldLabel);
    }

    public void find(boolean select){
        findIdCheckBox.setSelected(select);
        view.setSelect(select);
    }

    public void click(boolean select){
        clickCheckBox.setSelected(select);
        view.setClick(select);
    }

}
