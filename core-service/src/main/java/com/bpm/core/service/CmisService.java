package com.bpm.core.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bpm.core.repository.CmisRepository;

@Service
public class CmisService {

	@Autowired
	private CmisRepository repository;
	
	public CmisService(CmisRepository repository) {
		this.repository = repository;
	}

	public String upload(MultipartFile file, String folderPath, String objectTypeId, Map<String, Object> customProps) {
		try {
			return repository.upload(file, folderPath, objectTypeId, customProps);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objectTypeId;
	}
}
