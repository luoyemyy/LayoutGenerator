import com.ao.layout.generator.utils.Generator;
import com.ao.layout.generator.utils.ParseXml;
import com.ao.layout.generator.utils.RecyclerGenerator;
import com.ao.layout.generator.view.View;
import com.ao.layout.generator.window.Content;
import com.ao.layout.generator.window.Footer;
import com.ao.layout.generator.window.Header;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class LayoutGenerator extends BaseGenerateAction {

    private JFrame mDialog;
    private List<View> mViews;

    private PsiClass mClass;
    private PsiFile mFile;


    public LayoutGenerator() {
        super(null);
    }

    public LayoutGenerator(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project mProject = event.getData(PlatformDataKeys.PROJECT);
        Editor mEditor = event.getData(PlatformDataKeys.EDITOR);
        if (mEditor == null || mProject == null) {
            return;
        }

        mFile = PsiUtilBase.getPsiFileInEditor(mEditor, mProject);
        if (mFile == null) {
            return;
        }

        mClass = getTargetClass(mEditor, mFile);

        mViews = new ParseXml(mProject, mEditor, mFile).parse();
        if (mViews == null) {
            return;
        }

        Logger.getInstance("Generator").info(mViews.toString());

        showDialog();
    }

    private void showDialog() {

        Header header = new Header();
        final Content content = new Content(mViews);
        Footer footer = new Footer(new Footer.Listener() {
            @Override
            public void close() {
                mDialog.setVisible(false);
                mDialog.dispose();
            }

            @Override
            public void sure() {
                generator();
                close();
            }

            @Override
            public void recycler() {
                generatorRecycler();
                close();
            }

            @Override
            public void allFind(boolean select) {
                content.allFind(select);
            }

            @Override
            public void allClick(boolean select) {
                content.addClick(select);
            }
        });

        Logger.getInstance("Generator").info("mDialog");

        mDialog = new JFrame();
        mDialog.setTitle("Generator Layout");
        mDialog.setLayout(new BorderLayout());
        mDialog.getContentPane().add(header, BorderLayout.NORTH);
        mDialog.getContentPane().add(content, BorderLayout.CENTER);
        mDialog.getContentPane().add(footer, BorderLayout.SOUTH);
        mDialog.pack();
        mDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mDialog.setLocationRelativeTo(null);
        mDialog.setVisible(true);
    }

    private void generator() {
        new Generator(mFile, mClass, mViews).execute();
    }

    private void generatorRecycler(){
        new RecyclerGenerator(mFile,mClass,mViews).execute();
    }
}
