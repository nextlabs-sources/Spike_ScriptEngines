/**
 * 
 */
package com.nextlabs.destiny.plugin.model;

/**
 * @author kyu
 *
 */
public class EntityAttributeKey {

    private long tenantId;
    private String entityType;
    private String modelType;
    private String attributeName;
    private String entityId;

    public EntityAttributeKey(String entityType, String modelType, String attributeName, String entityId) {
        super();
        this.entityType = entityType;
        this.modelType = modelType;
        this.attributeName = attributeName;
        this.entityId = entityId;
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
        result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
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
        EntityAttributeKey other = (EntityAttributeKey) obj;
        if (attributeName == null) {
            if (other.attributeName != null)
                return false;
        } else if (!attributeName.equals(other.attributeName))
            return false;
        if (entityId == null) {
            if (other.entityId != null)
                return false;
        } else if (!entityId.equals(other.entityId))
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

}
