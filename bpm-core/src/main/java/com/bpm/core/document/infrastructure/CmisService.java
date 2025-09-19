package com.bpm.core.document.infrastructure;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.server.domain.Server;

import org.apache.chemistry.opencmis.commons.data.ContentStream;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class CmisService {

    public String uploadDocument(Server server, AuthConfig auth, String fileName, byte[] content) {
        Map<String, String> params = new HashMap<>();
        params.put(SessionParameter.USER, auth.getUsername());
        params.put(SessionParameter.PASSWORD, auth.getPassword());
        params.put(SessionParameter.ATOMPUB_URL, server.getAtomPubUrl());
        params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        params.put(SessionParameter.REPOSITORY_ID, server.getRepoId());

        Session session = SessionFactoryImpl.newInstance().createSession(params);
        Folder folder = (Folder) session.getObjectByPath("/Sites/my-site/documentLibrary");

        ContentStream cs = session.getObjectFactory()
                .createContentStream(fileName, content.length, "application/octet-stream", new ByteArrayInputStream(content));

        Document doc = folder.createDocument(Map.of(PropertyIds.OBJECT_TYPE_ID, "cmis:document", PropertyIds.NAME, fileName),
                cs, null);

        return doc.getPaths().get(0);
    }
}

