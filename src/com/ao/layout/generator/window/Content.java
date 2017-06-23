package com.ao.layout.generator.window;

import com.ao.layout.generator.view.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Content extends JPanel {

    private List<Item> items;

    public Content(List<View> views) {

        setLayout(new GridLayout(views.size(), 1));

        items = new ArrayList<>(views.size());

        for (View v : views) {
            Item item = new Item(v);
            items.add(item);
            add(item);
        }
    }

    public void allFind(boolean select) {
        items.forEach(item -> item.find(select));
    }

    public void addClick(boolean select) {
        items.forEach(item -> item.click(select));
    }
}
