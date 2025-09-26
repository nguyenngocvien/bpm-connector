package com.bpm.core.cmis.helper;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.data.ContentStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

public class CmisHelper {

    private Session session;

    public void connect(String url, String username, String password, String repositoryId) {
        Map<String, String> params = new HashMap<>();
        params.put(SessionParameter.USER, username);
        params.put(SessionParameter.PASSWORD, password);
        params.put(SessionParameter.ATOMPUB_URL, url);
        params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        params.put(SessionParameter.REPOSITORY_ID, repositoryId);

        SessionFactory factory = SessionFactoryImpl.newInstance();
        this.session = factory.createSession(params);
    }

    public Session getSession() {
        return session;
    }
    
    public Document uploadDocument(String folderPath, String name, byte[] content, String mimeType) {
        Folder folder = (Folder) session.getObjectByPath(folderPath);

        Map<String, Object> props = new HashMap<>();
        props.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        props.put(PropertyIds.NAME, name);

        InputStream stream = new ByteArrayInputStream(content);
        ContentStream cs = session.getObjectFactory().createContentStream(name, content.length, mimeType, stream);

        return folder.createDocument(props, cs, VersioningState.MAJOR);
    }

    /**
     * Download document by id
     */
    public byte[] downloadDocument(String docId) throws Exception {
        Document doc = (Document) session.getObject(docId);
        ContentStream cs = doc.getContentStream();

        try (InputStream in = cs.getStream()) {
            return in.readAllBytes();
        }
    }

    /**
     * Query CMIS
     */
    public ItemIterable<QueryResult> query(String cmisQuery) {
        ItemIterable<QueryResult> results = session.query(cmisQuery, false);
        return results.getPage(Integer.MAX_VALUE).getPage();
    }

    /**
     * Delete document by id
     */
    public void deleteDocument(String docId) {
        CmisObject obj = session.getObject(docId);
        if (obj instanceof Document) {
            ((Document) obj).delete(true);
        }
    }
}