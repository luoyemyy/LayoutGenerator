package com.ao.layout.generator.utils;

import com.ao.layout.generator.view.View;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.List;

public class Generator extends WriteCommandAction.Simple {

    private PsiFile mFile;
    private Project mProject;
    private PsiClass mClass;
    private List<View> mViews;
    private PsiElementFactory mFactory;
    private boolean click;

    public Generator(PsiFile mFile, PsiClass mClass, List<View> mViews) {
        super(mClass.getProject(), "generator layout");
        this.mFile = mFile;
        this.mProject = mClass.getProject();
        this.mClass = mClass;
        this.mViews = mViews;
        this.mFactory = JavaPsiFacade.getElementFactory(mProject);
    }

    @Override
    protected void run() throws Throwable {
        generateFields();
        generatorLayoutCode();
        // reformat class
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(mFile);
        styleManager.shortenClassReferences(mClass);
        new ReformatCodeProcessor(mProject, mClass.getContainingFile(), null, false).runWithoutProgress();
    }

    private void generateFields() {
        for (View view : mViews) {
            if (!view.isSelect()) {
                continue;
            }
            mClass.add(mFactory.createFieldFromText("private " + view.getViewName() + " " + view.getFieldName() + ";", mClass));
        }
    }

    private static final String template1 = "%s = (%s)v.findViewById(%s);\n";
    private static final String template2 = "%s = (%s)findViewById(%s);\n";
    private static final String template3 = "%s.setOnClickListener(this);\n";
    private static final String template4 = "v.findViewById(%s).setOnClickListener(this);\n";
    private static final String template5 = "findViewById(%s).setOnClickListener(this);\n";

    private static final String template6 = "@Override protected void initViewAndPresenter() {%s}\n";
    private static final String template7 = "@Override protected View initViewAndPresenter(View v) { %s return v;}\n";

    private static final String template8 = "@Override public void onClick(View v) { %s }\n";
    private static final String template9 = "%s if(v.getId()==%s){}\n";

    private void fragment(String existBlock) {
        String findBody = findBody(template1) + clickBody(template4) + (existBlock == null ? "" : existBlock);
        String methodString = String.format(template7, findBody);
        mClass.add(mFactory.createMethodFromText(methodString, mClass));
    }

    private void activity(String existBlock) {
        String findBody = findBody(template2) + clickBody(template5) + (existBlock == null ? "" : existBlock);
        String methodString = String.format(template6, findBody);
        mClass.add(mFactory.createMethodFromText(methodString, mClass));
    }

    private String findBody(String template) {
        StringBuilder sb = new StringBuilder();
        for (View view : mViews) {
            if (!view.isSelect()) {
                continue;
            }
            sb.append(String.format(template, view.getFieldName(), view.getViewName(), view.getFullId()));
        }
        return sb.toString();
    }

    private String clickBody(String template) {
        StringBuilder sb = new StringBuilder();
        for (View view : mViews) {
            if (!view.isClick()) {
                continue;
            }
            click = true;
            if (view.isSelect()) {
                sb.append(String.format(template3, view.getFieldName()));
            } else {
                sb.append(String.format(template, view.getFullId()));
            }
        }
        return sb.toString();
    }

    private void generatorLayoutCode() {

        boolean f = mClass.getName().contains("Fragment");

        PsiMethod[] methods = mClass.findMethodsByName("initViewAndPresenter", false);
        String existBlock = null;
        if (methods.length > 0) {
            PsiCodeBlock codeBlock = methods[0].getBody();
            if (codeBlock != null) {
                PsiStatement[] statements = codeBlock.getStatements();
                StringBuilder stringBuilder = new StringBuilder();
                for (PsiStatement statement : statements) {
                    String text = statement.getText();
                    if (!text.contains("return")) {
                        stringBuilder.append(statement.getText());
                    }
                }
                existBlock = "\n\n" + stringBuilder.toString();
            }
            methods[0].delete();
        }

        if (f) {
            fragment(existBlock);
        } else {
            activity(existBlock);
        }

        if (click) {
            clickMethod();
        }
    }

    private void clickMethod() {
        StringBuilder sb = new StringBuilder();
        boolean hasElse = false;
        for (View view : mViews) {
            if (!view.isClick()) {
                continue;
            }
            String args1 = "";
            if (hasElse) {
                args1 = "else";
            } else {
                hasElse = true;
            }
            sb.append(String.format(template9, args1, view.getFullId()));
        }
        String clickMethodString = String.format(template8, sb.toString());
        mClass.add(mFactory.createMethodFromText(clickMethodString, mClass));
    }
}
