package com.ippon.boardatjob.web.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ippon.boardatjob.domain.UserProfile;
import com.ippon.boardatjob.repository.UserProfileRepository;
import com.ippon.boardatjob.web.rest.dto.UploadDTO;

@RestController
@RequestMapping("/resume")
public class ResumeResource {

	@Inject
	private UserProfileRepository userProfileRepository;

	@RequestMapping(value = "/upload/{profileId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public UploadDTO uploadFile(@PathVariable Long profileId,
			@RequestParam MultipartFile file) throws JSONException {
		UploadDTO response = new UploadDTO();
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				UserProfile profile = userProfileRepository.findOne(profileId);
				profile.setResume(bytes);
				profile.setResumeDate(DateTime.now());
				userProfileRepository.save(profile);
				response.setMessage("You successfully uploaded your resume!");
				
			} catch (Exception e) {
				response.setMessage("Upload failed => " + e.getMessage());
			}
		} else {
			response.setMessage("Upload failed because the file was empty.");
		}
		return response;
	}

	@RequestMapping(value = "/download/{profileId}", method = RequestMethod.GET, produces = "application/pdf")
	public void getFile(@PathVariable Long profileId,
			HttpServletResponse response) {
		try {
			UserProfile profile = userProfileRepository.findOne(profileId);
			InputStream is = new ByteArrayInputStream(profile.getResume());
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}

	}
}
