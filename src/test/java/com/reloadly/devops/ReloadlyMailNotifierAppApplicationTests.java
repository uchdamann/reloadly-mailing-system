package com.reloadly.devops;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import static com.reloadly.devops.constants.NotificationType.*;

import static com.reloadly.devops.constants.AccountType.*;

import static com.reloadly.devops.constants.TransactionType.*;
import com.reloadly.devops.request.dtos.AccountCreationNotificationDTO;
import com.reloadly.devops.request.dtos.LoginNotificationDTO;
import com.reloadly.devops.request.dtos.TransactionNotificationDTO;
import com.reloadly.devops.utilities.ExternalCalls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
class ReloadlyMailNotifierAppApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	private ObjectWriter writer;
    @MockBean
    private ExternalCalls externalCalls;

	@BeforeEach
	public void setUp() {
		writer = new ObjectMapper().writer();
	}

	@Test
	void loginTest() throws Exception {
        LoginNotificationDTO loginDTO = new LoginNotificationDTO();
		loginDTO.setUsername("uchdamann@gmail.com");
        loginDTO.setLocation("Lagos");
        loginDTO.setCreatedOn(new Date());
        loginDTO.setNotificationType(LOGIN);

		Map<String, Object> validUser = new HashMap<>();
        validUser.put("firstname", "Uche");
        validUser.put("valid", true);

		String loginDTOString = writer.writeValueAsString(loginDTO);

        when(externalCalls.validateUserExternally(loginDTO.getUsername()))
                .thenReturn(Optional.of(validUser));

		MockHttpServletRequestBuilder mockLoginRequest = post("/api/mail/v1/send-login");
		mockLoginRequest.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(loginDTOString)
				.header("ChannelCode", "WEB");

		mockMvc.perform(mockLoginRequest)
				.andExpect(status().isOk());
	}

    @Test
    void accountCreationTest() throws Exception {
        AccountCreationNotificationDTO accountCreationNotificationDTO = new AccountCreationNotificationDTO();
        accountCreationNotificationDTO.setUsername("uchdamann@gmail.com");
        accountCreationNotificationDTO.setCreatedOn(new Date());
        accountCreationNotificationDTO.setNotificationType(SIGNUP);
        accountCreationNotificationDTO.setAccountType(ELITE);
        accountCreationNotificationDTO.setFirstname("Uche");
        accountCreationNotificationDTO.setLastname("Kamani");
        accountCreationNotificationDTO.setInitialBalance(new BigDecimal(5000));
        accountCreationNotificationDTO.setAccountNumber("0012345578");

        Map<String, Object> validUser = new HashMap<>();
        validUser.put("firstname", "Uche");
        validUser.put("valid", true);

        String accountCreationString = writer.writeValueAsString(accountCreationNotificationDTO);

        when(externalCalls.validateUserExternally(accountCreationNotificationDTO.getUsername()))
                .thenReturn(Optional.of(validUser));
        
        MockHttpServletRequestBuilder mockLoginRequest = post("/api/mail/v1/send-create");
        mockLoginRequest.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(accountCreationString)
                .header("ChannelCode", "WEB");
        
        mockMvc.perform(mockLoginRequest)
                .andExpect(status().isOk());
    }
    
    @Test
    void creditTransactionTest() throws Exception {
        TransactionNotificationDTO transactionNotificationDTO = new TransactionNotificationDTO();
        transactionNotificationDTO.setUsername("uchdamann@gmail.com");
        transactionNotificationDTO.setTransactionType(CREDIT);
        transactionNotificationDTO.setTransactionAmount(new BigDecimal(10000));
        transactionNotificationDTO.setNotificationType(TRANSACTION);
        transactionNotificationDTO.setFirstname("Uche");
        transactionNotificationDTO.setInitialBalance(new BigDecimal(2400));
        
        Map<String, Object> validUser = new HashMap<>();
        validUser.put("firstname", "Uche");
        validUser.put("valid", true);
        
        String transactionString = writer.writeValueAsString(transactionNotificationDTO);
        
        when(externalCalls.validateUserExternally(transactionNotificationDTO.getUsername()))
                .thenReturn(Optional.of(validUser));

        MockHttpServletRequestBuilder mockLoginRequest = post("/api/mail/v1/send-transact");
        mockLoginRequest.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(transactionString)
                .header("ChannelCode", "WEB");

        mockMvc.perform(mockLoginRequest)
                .andExpect(status().isOk());
    }

    @Test
    void debitTransactionTest() throws Exception {
        TransactionNotificationDTO transactionNotificationDTO = new TransactionNotificationDTO();
        transactionNotificationDTO.setUsername("uchdamann@gmail.com");
        transactionNotificationDTO.setTransactionType(DEBIT);
        transactionNotificationDTO.setTransactionAmount(new BigDecimal(10000));
        transactionNotificationDTO.setNotificationType(TRANSACTION);
        transactionNotificationDTO.setFirstname("Uche");
        transactionNotificationDTO.setInitialBalance(new BigDecimal(2400));

        Map<String, Object> validUser = new HashMap<>();
        validUser.put("firstname", "Uche");
        validUser.put("valid", true);

        String transactionString = writer.writeValueAsString(transactionNotificationDTO);

        when(externalCalls.validateUserExternally(transactionNotificationDTO.getUsername()))
                .thenReturn(Optional.of(validUser));

        MockHttpServletRequestBuilder mockLoginRequest = post("/api/mail/v1/send-transact");
        mockLoginRequest.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(transactionString)
                .header("ChannelCode", "WEB");

        mockMvc.perform(mockLoginRequest)
                .andExpect(status().isOk());
    }

    @Test
    void balanceTransactionTest() throws Exception {
        TransactionNotificationDTO transactionNotificationDTO = new TransactionNotificationDTO();
        transactionNotificationDTO.setUsername("uchdamann@gmail.com");
        transactionNotificationDTO.setTransactionType(BALANCE);
        transactionNotificationDTO.setTransactionAmount(new BigDecimal(10000));
        transactionNotificationDTO.setNotificationType(TRANSACTION);
        transactionNotificationDTO.setFirstname("Uche");
        transactionNotificationDTO.setInitialBalance(new BigDecimal(2400));

        Map<String, Object> validUser = new HashMap<>();
        validUser.put("firstname", "Uche");
        validUser.put("valid", true);

        String transactionString = writer.writeValueAsString(transactionNotificationDTO);

        when(externalCalls.validateUserExternally(transactionNotificationDTO.getUsername()))
                .thenReturn(Optional.of(validUser));

        MockHttpServletRequestBuilder mockLoginRequest = post("/api/mail/v1/send-transact");
        mockLoginRequest.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(transactionString)
                .header("ChannelCode", "WEB");

        mockMvc.perform(mockLoginRequest)
                .andExpect(status().isOk());
    }
}