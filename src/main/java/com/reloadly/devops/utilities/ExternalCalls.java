package com.reloadly.devops.utilities;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.reloadly.devops.exceptions.AppException;
import com.reloadly.devops.response.dtos.OauthDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalCalls {
	private final AppProperties props;
	private final RestTemplate restTemplate;

	public OauthDTO generateAuthServeTokenClientCredentialsGrantType() {
		OauthDTO oauthDTO = null;
		String basicAuth = props.getClientId() + ":" + props.getClientSecret();

		HttpHeaders requestHeader = new HttpHeaders();
		requestHeader.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeader.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(basicAuth.getBytes()));

		log.info("---->>> Initiating process to get oauth token from auth-server");

		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

		requestBody.put("grant_type", Arrays.asList(props.getGrantTypeClientCredentials()));

		log.info("Auth-server request: {}", requestBody);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeader);
		ResponseEntity<String> response = restTemplate.postForEntity(props.getAuthServerUrl(), requestEntity,
				String.class);

		if (response != null) {
			oauthDTO = JsonBuilder.toClass(response.getBody(), OauthDTO.class);
			log.info("---->>> OauthDTO: {}", oauthDTO);
		} else {
			log.info("---->>> No Response from authorization server");
			throw new AppException("---->>> No Response from authorization server");
		}

		return oauthDTO;
	}
	
	public Optional<Map<String, Object>> validateUserExternally(String username) {
		log.info("---->>> Initiating process to externally validate username:, {}", username);
		Map<String, Object> responseDTO = null;

		HttpHeaders requestHeader = new HttpHeaders();
		final String URL = props.getAccountCreationService() + username;

		requestHeader.setContentType(MediaType.APPLICATION_JSON);
		requestHeader.add("Authorization",
				"Bearer " + generateAuthServeTokenClientCredentialsGrantType().getAccessToken());
		requestHeader.add("ChannelCode", props.getWebChannelCode());

		ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);

		if (response != null) {
			responseDTO = JsonBuilder.toClassTypeReference(response.getBody(), new TypeReference<Map<String, Object>>() {});

			return Optional.of(responseDTO);

		} else {
			log.info("---->>> No Response from escrow server");
			throw new AppException("---->>> No Response from escrow server");
		}
	}
}