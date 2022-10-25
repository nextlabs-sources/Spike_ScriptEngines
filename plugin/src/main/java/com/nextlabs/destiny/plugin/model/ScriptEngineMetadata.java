/**
 * 
 */
package com.nextlabs.destiny.plugin.model;

/**
 * @author kyu
 *
 */
public class ScriptEngineMetadata {
    private long tenantId;
    private String entityType;
    private String modelType;
    private String attributeName;

    public ScriptEngineMetadata(String entityType, String modelType, String attributeName) {
        super();
        this.entityType = entityType;
        this.modelType = modelType;
        this.attributeName = attributeName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
        result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
        result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
        result = prime * result + (int) (tenantId ^ (tenantId >>> 32));
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScriptEngineMetadata other = (ScriptEngineMetadata) obj;
        if (attributeName == null) {
            if (other.attributeName != null)
                return false;
        } else if (!attributeName.equals(other.attributeName))
            return false;
        if (entityType == null) {
            if (other.entityType != null)
                return false;
        } else if (!entityType.equals(other.entityType))
            return false;
        if (modelType == null) {
            if (other.modelType != null)
                return false;
        } else if (!modelType.equals(other.modelType))
            return false;
        if (tenantId != other.tenantId)
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ScriptEngineMetadata [tenantId=" + tenantId + ", entityType=" + entityType + ", modelType=" + modelType + ", attributeName=" + attributeName
                + "]";
    }

}
