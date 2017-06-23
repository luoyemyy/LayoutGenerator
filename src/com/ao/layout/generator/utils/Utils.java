package com.ao.layout.generator.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

public class Utils {

    public static String createFieldName(String s) {
        String[] strings = s.split("_");
        StringBuilder sb = new StringBuilder();
        sb.append("m");
        for (String word : strings) {
            char[] cs = word.toCharArray();
            cs[0] -= 32;
            sb.append(String.valueOf(cs));
        }
        return sb.toString();
    }


    public static void showErrorNotification(Project project, String text) {
        showNotification(project, MessageType.ERROR, text);
    }

    public static void showNotification(Project project, MessageType type, String text) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(text, type, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }

}
