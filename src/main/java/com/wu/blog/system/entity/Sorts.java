package com.wu.blog.system.entity;

public class Sorts {
    private Integer sortId;

    private String sortName;

    private String sortAlias;

    private String sortRemark;

    private Integer parentSortId;

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName == null ? null : sortName.trim();
    }

    public String getSortAlias() {
        return sortAlias;
    }

    public void setSortAlias(String sortAlias) {
        this.sortAlias = sortAlias == null ? null : sortAlias.trim();
    }

    public String getSortRemark() {
        return sortRemark;
    }

    public void setSortRemark(String sortRemark) {
        this.sortRemark = sortRemark == null ? null : sortRemark.trim();
    }

    public Integer getParentSortId() {
        return parentSortId;
    }

    public void setParentSortId(Integer parentSortId) {
        this.parentSortId = parentSortId;
    }
}