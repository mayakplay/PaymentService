package com.mayakplay.payment.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayakplay.payment.PaymentApplication;
import com.mayakplay.payment.filter.AccountBalanceChangeFilter;
import com.mayakplay.payment.filter.AccountBalanceTransferFilter;
import com.mayakplay.payment.model.AccountIdModel;
import com.mayakplay.payment.model.BalanceModel;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PaymentApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("TEST")
@TestPropertySource(locations = "classpath:application-test.yml")
public class AccountControllerTest {

    private static final String BASE_PATH = "/account/";

    private static final String ACCOUNT_BALANCE_PATH = BASE_PATH + "balance";

    private static final String ACCOUNT_CREATE_PATH = BASE_PATH + "create";
    private static final String ACCOUNT_WITHDRAW_PATH = BASE_PATH + "withdraw";
    private static final String ACCOUNT_DEPOSIT_PATH = BASE_PATH + "deposit";
    private static final String ACCOUNT_TRANSFER_PATH = BASE_PATH + "transfer";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void accountShouldBeCreatedAfterRequest() throws Exception {
        performAccountCreationRequest();
    }

    @Test
    public void accountShouldReturnCorrectBalance() throws Exception {
        long accountId = performAccountCreationRequest();

        double balance = performAccountBalanceRequest(accountId);

        Assertions.assertEquals(0, balance);

        double updateAmount = 15D;

        performDepositRequest(accountId, updateAmount);

        double updatedBalance = performAccountBalanceRequest(accountId);

        Assertions.assertEquals(updatedBalance, updatedBalance);
    }

    @Test
    public void balanceShouldNotTransferBetweenAccountsWhenItsNotEnough() throws Exception {
        long fromAccountId = performAccountCreationRequest();
        long toAccountId = performAccountCreationRequest();

        double fromAccountBalance = performAccountBalanceRequest(fromAccountId);
        double toAccountBalance = performAccountBalanceRequest(toAccountId);

        double amount = fromAccountBalance + 1;

        AccountBalanceTransferFilter accountBalanceTransferFilter = new AccountBalanceTransferFilter();
        accountBalanceTransferFilter.setAccountIdFrom(fromAccountId);
        accountBalanceTransferFilter.setAccountIdTo(toAccountId);
        accountBalanceTransferFilter.setAmount(amount);

        MockHttpServletResponse response = mockMvc.perform(
                postMockMvcBuilderForPath(accountBalanceTransferFilter, ACCOUNT_TRANSFER_PATH)
        ).andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        Assertions.assertEquals(performAccountBalanceRequest(fromAccountId), fromAccountBalance);
        Assertions.assertEquals(performAccountBalanceRequest(toAccountId), toAccountBalance);
    }

    @Test
    public void balanceShouldNotTransferBetweenSameAccounts() throws Exception {
        long accountId = performAccountCreationRequest();

        AccountBalanceTransferFilter accountBalanceTransferFilter = new AccountBalanceTransferFilter();
        accountBalanceTransferFilter.setAccountIdFrom(accountId);
        accountBalanceTransferFilter.setAccountIdTo(accountId);
        accountBalanceTransferFilter.setAmount(0D);

        MockHttpServletResponse response = mockMvc.perform(
                postMockMvcBuilderForPath(accountBalanceTransferFilter, ACCOUNT_TRANSFER_PATH)
        ).andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void balanceShouldTransferBetweenAccounts() throws Exception {
        long fromAccountId = performAccountCreationRequest();
        long toAccountId = performAccountCreationRequest();

        final double transferAmount = 15D;

        performDepositRequest(fromAccountId, transferAmount);

        Assertions.assertEquals(performAccountBalanceRequest(fromAccountId), transferAmount);
        Assertions.assertEquals(performAccountBalanceRequest(toAccountId), 0);

        AccountBalanceTransferFilter accountBalanceTransferFilter = new AccountBalanceTransferFilter();
        accountBalanceTransferFilter.setAccountIdFrom(fromAccountId);
        accountBalanceTransferFilter.setAccountIdTo(toAccountId);
        accountBalanceTransferFilter.setAmount(transferAmount);

        MockHttpServletResponse response = mockMvc.perform(
                postMockMvcBuilderForPath(accountBalanceTransferFilter, ACCOUNT_TRANSFER_PATH)
        ).andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());

        Assertions.assertEquals(performAccountBalanceRequest(fromAccountId), 0);
        Assertions.assertEquals(performAccountBalanceRequest(toAccountId), transferAmount);
    }

    @Test
    public void accountShouldAcceptDepositOperation() throws Exception {
        long accountId = performAccountCreationRequest();

        double amount = 15D;

        performDepositRequest(accountId, amount);
        Assertions.assertEquals(performAccountBalanceRequest(accountId), amount);

        performDepositRequest(accountId, amount);
        Assertions.assertEquals(performAccountBalanceRequest(accountId), amount * 2);
    }

    @Test
    public void accountShouldNotAcceptWithdrawOperationWhenNotEnough() throws Exception {
        long accountId = performAccountCreationRequest();

        double amount = 15D;

        MockHttpServletResponse mockHttpServletResponse = performWithdrawRequest(accountId, amount);

        Assertions.assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void accountShouldAcceptWithdrawOperation() throws Exception {
        long accountId = performAccountCreationRequest();

        double amount = 15D;

        performDepositRequest(accountId, amount);

        MockHttpServletResponse mockHttpServletResponse = performWithdrawRequest(accountId, amount);

        Assertions.assertEquals(mockHttpServletResponse.getStatus(), HttpStatus.OK.value());
        Assertions.assertEquals(performAccountBalanceRequest(accountId), 0);
    }

    protected void performDepositRequest(long accountId, double amount) throws Exception {
        AccountBalanceChangeFilter accountBalanceChangeFilter = new AccountBalanceChangeFilter();
        accountBalanceChangeFilter.setAccountId(accountId);
        accountBalanceChangeFilter.setAmount(amount);

        MockHttpServletResponse response = mockMvc.perform(
                postMockMvcBuilderForPath(accountBalanceChangeFilter, ACCOUNT_DEPOSIT_PATH)
        ).andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    protected MockHttpServletResponse performWithdrawRequest(long accountId, double amount) throws Exception {
        AccountBalanceChangeFilter accountBalanceChangeFilter = new AccountBalanceChangeFilter();
        accountBalanceChangeFilter.setAccountId(accountId);
        accountBalanceChangeFilter.setAmount(amount);

        return mockMvc.perform(
                postMockMvcBuilderForPath(accountBalanceChangeFilter, ACCOUNT_WITHDRAW_PATH)
        ).andReturn().getResponse();
    }

    protected double performAccountBalanceRequest(long accountId) throws Exception {
        ResultActions perform = mockMvc.perform(
                getMockMvcBuilderForPath(ACCOUNT_BALANCE_PATH,
                        Pair.of("accountId", accountId + ""))
        );

        MockHttpServletResponse response = perform.andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
        BalanceModel balanceModel = objectMapper.readValue(response.getContentAsString(), BalanceModel.class);

        return balanceModel.getBalance();
    }

    protected long performAccountCreationRequest() throws Exception {
        ResultActions perform = mockMvc.perform(postMockMvcBuilderForPath(null, ACCOUNT_CREATE_PATH));

        MockHttpServletResponse response = perform.andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());

        return objectMapper.readValue(response.getContentAsString(), AccountIdModel.class).getAccountId();
    }

    @SneakyThrows
    protected MockHttpServletRequestBuilder postMockMvcBuilderForPath(Object body, String path) {
        return MockMvcRequestBuilders.post(path)
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON);
    }

    @SafeVarargs
    @SneakyThrows
    @SuppressWarnings("SameParameterValue")
    protected final MockHttpServletRequestBuilder getMockMvcBuilderForPath(String path, Pair<String, String>... queryParams) {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(path);

        if (queryParams != null) {
            LinkedMultiValueMap<String, String> stringStringLinkedMultiValueMap = new LinkedMultiValueMap<>();

            for (Pair<String, String> queryParam : queryParams) {
                stringStringLinkedMultiValueMap.add(queryParam.getFirst(), queryParam.getSecond());
            }

            mockHttpServletRequestBuilder.params(stringStringLinkedMultiValueMap);
        }

        return mockHttpServletRequestBuilder;
    }

}
