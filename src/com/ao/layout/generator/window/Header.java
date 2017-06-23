package com.ao.layout.generator.window;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {

    public Header() {

        setLayout(new FlowLayout());

        JLabel findIdLabel = new JLabel("findId");
        findIdLabel.setPreferredSize(new Dimension(75, 26));
        add(findIdLabel);

        JLabel clickLabel = new JLabel("click");
        clickLabel.setPreferredSize(new Dimension(75, 26));
        add(clickLabel);

        JLabel typeLabel = new JLabel("类名");
        typeLabel.setPreferredSize(new Dimension(150, 26));
        add(typeLabel);

        JLabel idLabel = new JLabel("Id");
        idLabel.setPreferredSize(new Dimension(150, 26));
        add(idLabel);

        JLabel fieldLabel = new JLabel("属性名");
        fieldLabel.setPreferredSize(new Dimension(150, 26));
        add(fieldLabel);
    }
}
