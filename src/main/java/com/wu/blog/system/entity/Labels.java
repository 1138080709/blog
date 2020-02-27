package com.wu.blog.system.entity;

public class Labels {
    private Integer labelId;

    private String labelName;

    private String labelAlias;

    private String labelRemark;

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName == null ? null : labelName.trim();
    }

    public String getLabelAlias() {
        return labelAlias;
    }

    public void setLabelAlias(String labelAlias) {
        this.labelAlias = labelAlias == null ? null : labelAlias.trim();
    }

    public String getLabelRemark() {
        return labelRemark;
    }

    public void setLabelRemark(String labelRemark) {
        this.labelRemark = labelRemark == null ? null : labelRemark.trim();
    }
}