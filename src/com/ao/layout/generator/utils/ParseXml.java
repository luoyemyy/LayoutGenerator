package com.ao.layout.generator.utils;

import com.ao.layout.generator.view.View;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ParseXml {

    private Project project;
    private Editor editor;
    private PsiFile file;

    public ParseXml(Project project, Editor editor, PsiFile file) {
        this.project = project;
        this.editor = editor;
        this.file = file;
    }

    public List<View> parse() {

        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        if (element == null) {
            Utils.showErrorNotification(project, "parse:26,null");
            return null;
        }

        String layoutName = element.getText() + ".xml";
        PsiFile layoutFile = getLayoutFile(project, element, layoutName);
        if (layoutFile == null) {
            Utils.showErrorNotification(project, "parse:33,null");
            return null;
        }

        List<View> views = new ArrayList<>();
        parseLayout(views, layoutFile);

        return views;
    }

    @Nullable
    private PsiFile getLayoutFile(Project project, PsiElement element, String layoutName) {
        PsiFile[] files;

        Module module = ModuleUtil.findModuleForPsiElement(element);
        if (module == null) {
            files = FilenameIndex.getFilesByName(project, layoutName, new EverythingGlobalScope(project));
        } else {
            files = FilenameIndex.getFilesByName(project, layoutName, module.getModuleWithDependenciesAndLibrariesScope(false));
        }

        if (files == null || files.length == 0) {
            Utils.showErrorNotification(project, "getLayoutFile:null");
            return null;
        }
        return files[0];
    }

    private void parseLayout(List<View> list, PsiFile file) {

        file.accept(new XmlRecursiveElementVisitor() {

            @Override
            public void visitElement(final PsiElement element) {
                super.visitElement(element);

                if (!(element instanceof XmlTag)) {
                    return;
                }
                XmlTag tag = (XmlTag) element;

                if (tag.getName().equalsIgnoreCase("include")) {
                    XmlAttribute layout = tag.getAttribute("layout", null);
                    if (layout == null) {
                        return;
                    }

                    String id = getId(layout.getValue());
                    if (id == null) {
                        return;
                    }
                    String includeName = id + ".xml";

                    PsiFile include = getLayoutFile(file.getProject(), file, includeName);

                    if (include != null) {
                        parseLayout(list, include);
                        return;
                    }
                }

                // get element ID
                XmlAttribute id = tag.getAttribute("android:id", null);
                if (id == null) {
                    return; // missing android:id attribute
                }
                String idValue = getId(id.getValue());
                if (idValue == null) {
                    return; // empty value
                }

                View view = new View();
                view.setId(idValue);
                view.setFullId("R.id." + idValue);
                view.setFieldName(Utils.createFieldName(idValue));
                view.setViewName(getViewName(tag.getName()));

                Logger.getInstance("Generator").info("parseLayout:" + idValue + "," + view.getFieldName());

                list.add(view);
            }
        });

    }

    private String getId(String value) {
        if (value == null) {
            return null;
        }
        String[] strings = value.split("/");
        if (strings.length != 2) {
            return null;
        }
        Logger.getInstance("Generator").info("getId:" + value);
//        Utils.showErrorNotification(project,strings[1]);
        return strings[1];
    }

    private String getViewName(String value) {
        if (value.contains(".")) {
            String[] strings = value.split(".");
            return strings[strings.length - 1];
        } else {
            return value;
        }

    }

}
