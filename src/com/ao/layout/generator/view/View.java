package com.ao.layout.generator.view;

public class View {

    private String id;
    private String fullId;
    private String viewName;
    private String fieldName;
    private boolean select = true;
    private boolean click;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }
}
