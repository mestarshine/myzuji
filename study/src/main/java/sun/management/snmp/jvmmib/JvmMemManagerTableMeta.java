/*
 * Copyright (c) 2003, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package sun.management.snmp.jvmmib;

//
// Generated by mibgen version 5.0 (06/02/03) when compiling JVM-MANAGEMENT-MIB in standard metadata mode.
//

// java imports
//
import java.io.Serializable;

// jmx imports
//
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.sun.jmx.snmp.SnmpOid;
import com.sun.jmx.snmp.SnmpStatusException;

// jdmk imports
//
import com.sun.jmx.snmp.agent.SnmpMib;
import com.sun.jmx.snmp.agent.SnmpMibTable;
import com.sun.jmx.snmp.agent.SnmpMibSubRequest;
import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;

/**
 * The class is used for implementing the "JvmMemManagerTable" group.
 * The group is defined with the following oid: 1.3.6.1.4.1.42.2.145.3.163.1.1.2.100.
 */
public class JvmMemManagerTableMeta extends SnmpMibTable implements Serializable {

    static final long serialVersionUID = 5026520607518015233L;
    /**
     * Constructor for the table. Initialize metadata for "JvmMemManagerTableMeta".
     * The reference on the MBean server is updated so the entries created through an SNMP SET will be AUTOMATICALLY REGISTERED in Java DMK.
     */
    public JvmMemManagerTableMeta(SnmpMib myMib, SnmpStandardObjectServer objserv) {
        super(myMib);
        objectserver = objserv;
    }


    /**
     * Factory method for "JvmMemManagerEntry" entry metadata class.
     *
     * You can redefine this method if you need to replace the default
     * generated metadata class with your own customized class.
     *
     * @param snmpEntryName Name of the SNMP Entry object (conceptual row) ("JvmMemManagerEntry")
     * @param tableName Name of the table in which the entries are registered ("JvmMemManagerTable")
     * @param mib The SnmpMib object in which this table is registered
     * @param server MBeanServer for this table entries (may be null)
     *
     * @return An instance of the metadata class generated for the
     *         "JvmMemManagerEntry" conceptual row (JvmMemManagerEntryMeta)
     *
     **/
    protected JvmMemManagerEntryMeta createJvmMemManagerEntryMetaNode(String snmpEntryName, String tableName, SnmpMib mib, MBeanServer server)  {
        return new JvmMemManagerEntryMeta(mib, objectserver);
    }


    // ------------------------------------------------------------
    //
    // Implements the "createNewEntry" method defined in "SnmpMibTable".
    // See the "SnmpMibTable" Javadoc API for more details.
    //
    // ------------------------------------------------------------

    public void createNewEntry(SnmpMibSubRequest req, SnmpOid rowOid, int depth)
        throws SnmpStatusException {
        if (factory != null)
            factory.createNewEntry(req, rowOid, depth, this);
        else
            throw new SnmpStatusException(
                SnmpStatusException.snmpRspNoAccess);
    }



    // ------------------------------------------------------------
    //
    // Implements the "isRegistrationRequired" method defined in "SnmpMibTable".
    // See the "SnmpMibTable" Javadoc API for more details.
    //
    // ------------------------------------------------------------

    public boolean isRegistrationRequired()  {
        return false;
    }



    public void registerEntryNode(SnmpMib mib, MBeanServer server)  {
        node = createJvmMemManagerEntryMetaNode("JvmMemManagerEntry", "JvmMemManagerTable", mib, server);
    }


    // ------------------------------------------------------------
    //
    // Implements the "addEntry" method defined in "SnmpMibTable".
    // See the "SnmpMibTable" Javadoc API for more details.
    //
    // ------------------------------------------------------------

    public synchronized void addEntry(SnmpOid rowOid, ObjectName objname,
                 Object entry)
        throws SnmpStatusException {
        if (! (entry instanceof JvmMemManagerEntryMBean) )
            throw new ClassCastException("Entries for Table \"" +
                           "JvmMemManagerTable" + "\" must implement the \"" +
                           "JvmMemManagerEntryMBean" + "\" interface.");
        super.addEntry(rowOid, objname, entry);
    }


    // ------------------------------------------------------------
    //
    // Implements the "get" method defined in "SnmpMibTable".
    // See the "SnmpMibTable" Javadoc API for more details.
    //
    // ------------------------------------------------------------

    public void get(SnmpMibSubRequest req, SnmpOid rowOid, int depth)
        throws SnmpStatusException {
        JvmMemManagerEntryMBean entry = (JvmMemManagerEntryMBean) getEntry(rowOid);
        synchronized (this) {
            node.setInstance(entry);
            node.get(req,depth);
        }
    }

    // ------------------------------------------------------------
    //
    // Implements the "set" method defined in "SnmpMibTable".
    // See the "SnmpMibTable" Javadoc API for more details.
    //
    // ------------------------------------------------------------

    public void set(SnmpMibSubRequest req, SnmpOid rowOid, int depth)
        throws SnmpStatusException {
        if (req.getSize() == 0) return;

        JvmMemManagerEntryMBean entry = (JvmMemManagerEntryMBean) getEntry(rowOid);
        synchronized (this) {
            node.setInstance(entry);
            node.set(req,depth);
        }
    }

    // ------------------------------------------------------------
    //
    // Implements the "check" method defined in "SnmpMibTable".
    // See the "SnmpMibTable" Javadoc API for more details.
    //
    // ------------------------------------------------------------

    public void check(SnmpMibSubRequest req, SnmpOid rowOid, int depth)
        throws SnmpStatusException {
        if (req.getSize() == 0) return;

        JvmMemManagerEntryMBean entry = (JvmMemManagerEntryMBean) getEntry(rowOid);
        synchronized (this) {
            node.setInstance(entry);
            node.check(req,depth);
        }
    }

    /**
     * check that the given "var" identifies a columnar object.
     */
    public void validateVarEntryId( SnmpOid rowOid, long var, Object data )
        throws SnmpStatusException {
        node.validateVarId(var, data);
    }

    /**
     * Returns true if "var" identifies a readable scalar object.
     */
    public boolean isReadableEntryId( SnmpOid rowOid, long var, Object data )
        throws SnmpStatusException {
        return node.isReadable(var);
    }

    /**
     * Returns the arc of the next columnar object following "var".
     */
    public long getNextVarEntryId( SnmpOid rowOid, long var, Object data )
        throws SnmpStatusException {
        long nextvar = node.getNextVarId(var, data);
        while (!isReadableEntryId(rowOid, nextvar, data))
            nextvar = node.getNextVarId(nextvar, data);
        return nextvar;
    }

    // ------------------------------------------------------------
    //
    // Implements the "skipEntryVariable" method defined in "SnmpMibTable".
    // See the "SnmpMibTable" Javadoc API for more details.
    //
    // ------------------------------------------------------------

    public boolean skipEntryVariable( SnmpOid rowOid, long var, Object data, int pduVersion) {
        try {
            JvmMemManagerEntryMBean entry = (JvmMemManagerEntryMBean) getEntry(rowOid);
            synchronized (this) {
                node.setInstance(entry);
                return node.skipVariable(var, data, pduVersion);
            }
        } catch (SnmpStatusException x) {
            return false;
        }
    }


    /**
     * Reference to the entry metadata.
     */
    private JvmMemManagerEntryMeta node;

    /**
     * Reference to the object server.
     */
    protected SnmpStandardObjectServer objectserver;

}
