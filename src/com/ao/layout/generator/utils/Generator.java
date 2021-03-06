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
    private boolean isKotlin = true;

    public Generator isKotlin(boolean isKotlin) {
        this.isKotlin = isKotlin;
        return this;
    }

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
            mClass.add(mFactory.createFieldFromText(String.format(isKotlin ? k_template0 : template0, view.getViewName(), view.getFieldName()), mClass));
        }
    }

    private static final String template0 = "private %1$s %2$s;\n";
    private static final String k_template0 = "private lateinit %2$s: %1$s\n";


    private static final String template1_f = "%s = v.findViewById(%s);\n";
    private static final String template1_a = "%s = findViewById(%s);\n";
    private static final String template2 = "%s.setOnClickListener(this);\n";
    private static final String template3_f = "v.findViewById(%s).setOnClickListener(this);\n";
    private static final String template3_a = "findViewById(%s).setOnClickListener(this);\n";

    private static final String template4_a = "@Override protected void initViewAndPresenter() {%s}\n";
    private static final String k_template4_a = "override fun initViewAndPresenter() {%s}\n";
    private static final String template4_f = "@Override protected View initViewAndPresenter(View v) { %s return v;}\n";
    private static final String k_template4_f = "override fun initViewAndPresenter(v: View): View { %s return v}\n";

    private static final String template5 = "@Override public void onClick(View v) { %s }\n";
    private static final String k_template5 = "override fun onClick(v: View) { %s }\n";
    private static final String template6 = "%s if(v.getId()==%s){}\n";
    private static final String k_template6 = "%s if(v.id==%s){}\n";

    private void fragment(String existBlock) {
        String findBody = findBody(template1_f) + clickBody(template3_f) + (existBlock == null ? "" : existBlock);
        String methodString = String.format(isKotlin ? k_template4_f : template4_f, findBody);
        mClass.add(mFactory.createMethodFromText(methodString, mClass));
    }

    private void activity(String existBlock) {
        String findBody = findBody(template1_a) + clickBody(template3_a) + (existBlock == null ? "" : existBlock);
        String methodString = String.format(isKotlin ? k_template4_a : template4_a, findBody);
        mClass.add(mFactory.createMethodFromText(methodString, mClass));
    }

    private String findBody(String template) {
        StringBuilder sb = new StringBuilder();
        for (View view : mViews) {
            if (!view.isSelect()) {
                continue;
            }
            sb.append(String.format(template, view.getFieldName(), view.getFullId()));
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
                sb.append(String.format(template2, view.getFieldName()));
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
            sb.append(String.format(isKotlin ? k_template6 : template6, args1, view.getFullId()));
        }
        String clickMethodString = String.format(isKotlin ? k_template5 : template5, sb.toString());
        mClass.add(mFactory.createMethodFromText(clickMethodString, mClass));
    }
}
