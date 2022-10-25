/**
 * 
 */
package com.nextlabs.destiny.plugin.domain;

import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.bluejungle.framework.expressions.IEvalValue;
import com.bluejungle.pf.domain.destiny.subject.IDSubject;
import com.bluejungle.pf.domain.epicenter.subject.ISubjectType;

/**
 * @author kyu
 *
 */
public class TestSubject implements IDSubject {

    private String name;

    public TestSubject() {
        super();
        this.name = UUID.randomUUID().toString();
    }

    public TestSubject(String name) {
        super();
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bluejungle.pf.domain.epicenter.subject.ISubject#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bluejungle.pf.domain.epicenter.subject.ISubject#getSubjectType()
     */
    @Override
    public ISubjectType getSubjectType() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bluejungle.framework.domain.IHasId#getId()
     */
    @Override
    public Long getId() {
        return System.nanoTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bluejungle.pf.domain.destiny.subject.IDSubject#getUniqueName()
     */
    @Override
    public String getUniqueName() {
        return getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.bluejungle.pf.domain.destiny.subject.IDSubject#getAttribute(java.lang
     * .String)
     */
    @Override
    public IEvalValue getAttribute(String paramString) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bluejungle.pf.domain.destiny.subject.IDSubject#getUid()
     */
    @Override
    public String getUid() {
        return getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bluejungle.pf.domain.destiny.subject.IDSubject#getEntrySet()
     */
    @Override
    public Set<Entry<String, IEvalValue>> getEntrySet() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bluejungle.pf.domain.destiny.subject.IDSubject#getGroups()
     */
    @Override
    public IEvalValue getGroups() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bluejungle.pf.domain.destiny.subject.IDSubject#isCacheable()
     */
    @Override
    public boolean isCacheable() {
        // TODO Auto-generated method stub
        return false;
    }

}
