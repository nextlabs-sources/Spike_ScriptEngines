/**
 * 
 */
package com.nextlabs.destiny.plugin.domain;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.bluejungle.framework.expressions.IEvalValue;
import com.bluejungle.pf.domain.epicenter.resource.IMResource;
import com.bluejungle.pf.domain.epicenter.resource.IResource;

/**
 * @author kyu
 *
 */
public class TestResource implements IResource {

    /**
     * 
     */
    private static final long serialVersionUID = 5661356045046229620L;

    /* (non-Javadoc)
     * @see com.bluejungle.pf.domain.epicenter.resource.IResource#getIdentifier()
     */
    @Override
    public Serializable getIdentifier() {
        return UUID.randomUUID().toString();
    }

    /* (non-Javadoc)
     * @see com.bluejungle.pf.domain.epicenter.resource.IResource#hasAttribute(java.lang.String)
     */
    @Override
    public boolean hasAttribute(String paramString) {
        return false;
    }

    /* (non-Javadoc)
     * @see com.bluejungle.pf.domain.epicenter.resource.IResource#clone()
     */
    @Override
    public IMResource clone() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.bluejungle.pf.domain.epicenter.resource.IResource#getEntrySet()
     */
    @Override
    public Set<Entry<String, IEvalValue>> getEntrySet() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.bluejungle.pf.domain.epicenter.resource.IResource#getAttribute(java.lang.String)
     */
    @Override
    public IEvalValue getAttribute(String paramString) {
        return null;
    }

}
