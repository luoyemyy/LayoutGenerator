package com.ao.layout.generator.utils;

import com.ao.layout.generator.view.View;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.List;

public class RecyclerGenerator extends WriteCommandAction.Simple {

    private PsiFile mFile;
    private Project mProject;
    private PsiClass mClass;
    private List<View> mViews;
    private PsiElementFactory mFactory;
    private boolean isKotlin = true;

    public RecyclerGenerator isKotlin(boolean isKotlin) {
        this.isKotlin = isKotlin;
        return this;
    }

    public RecyclerGenerator(PsiFile mFile, PsiClass mClass, List<View> mViews) {
        super(mClass.getProject(), "generator recycler layout");
        this.mFile = mFile;
        this.mProject = mClass.getProject();
        this.mClass = mClass;
        this.mViews = mViews;
        this.mFactory = JavaPsiFacade.getElementFactory(mProject);
    }

    @Override
    protected void run() throws Throwable {
        generatorLayoutCode();
        // reformat class
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(mFile);
        styleManager.shortenClassReferences(mClass);
        new ReformatCodeProcessor(mProject, mClass.getContainingFile(), null, false).runWithoutProgress();
    }

    private static final String template0 = "%1$s %2$s;\n";
    private static final String template1 = "%s = itemView.findViewById(%s);\n";
    private static final String k_template1 = "internal var %1$s: %2$s = itemView.findViewById(%3$s)\n";
    private static final String template2 = "public void inject(View itemView) {%s}\n";
    private static final String k_template2 = "public fun inject(itemView: View) {%s}\n";


    private String findBody() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (View view : mViews) {
            if (!view.isSelect()) {
                continue;
            }
            if (isKotlin) {
                sb2.append(String.format(k_template1, view.getFieldName(), view.getViewName(), view.getFullId()));
            } else {
                sb1.append(String.format(template0, view.getViewName(), view.getFieldName()));
                sb2.append(String.format(template1, view.getFieldName(), view.getFullId()));
            }
        }
        return sb1.toString() + sb2.toString();
    }

    private void generatorLayoutCode() {

        PsiMethod[] methods = mClass.findMethodsByName("inject", false);
        if (methods.length > 0) {
            methods[0].delete();
        }
        String body = findBody();
        mClass.add(mFactory.createMethodFromText(String.format(isKotlin ? k_template2 : template2, body), mClass));
    }
}
