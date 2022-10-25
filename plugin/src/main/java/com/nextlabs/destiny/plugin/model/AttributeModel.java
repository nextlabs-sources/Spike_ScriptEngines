package com.nextlabs.destiny.plugin.model;

import java.util.List;

public class AttributeModel {
    private String attrName;
    private String attrValue;

    /**
     * time to live, in seconds
     */
    private long ttl;

    private List<AttributeModel> siblingAttributes;

    /**
     * @return the attrName
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * @param attrName
     *            the attrName to set
     */
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * @return the attrValue
     */
    public String getAttrValue() {
        return attrValue;
    }

    /**
     * @param attrValue
     *            the attrValue to set
     */
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * @return the ttl
     */
    public long getTtl() {
        return ttl;
    }

    /**
     * @param ttl
     *            the ttl to set
     */
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * @return the siblingAttributes
     */
    public List<AttributeModel> getSiblingAttributes() {
        return siblingAttributes;
    }

    /**
     * @param siblingAttributes
     *            the siblingAttributes to set
     */
    public void setSiblingAttributes(List<AttributeModel> siblingAttributes) {
        this.siblingAttributes = siblingAttributes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AttributeModel [attrName=" + attrName + ", attrValue=" + attrValue + ", ttl=" + ttl + ", siblingAttributes=" + siblingAttributes + "]";
    }

}
