package controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import transaction.Application;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.web.context.WebApplicationContext;
import transaction.model.Transaction;
import org.junit.Assert;
import transaction.utils.Roots;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by sb on 10.12.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TransactionControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    private Map<Long, Transaction> testTransactionMap = new HashMap<>();
    List<Transaction> testList = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        Transaction firstTransaction = new Transaction();
        firstTransaction.setAmount(1000.0);
        firstTransaction.setParent_id(0);
        firstTransaction.setType("cars");

        Transaction secondTransaction = new Transaction();
        secondTransaction.setAmount(500.0);
        secondTransaction.setParent_id(10);
        secondTransaction.setType("cars");

        this.testTransactionMap.put(10L, firstTransaction);
        this.testTransactionMap.put(11L, secondTransaction);

        this.testList.add(firstTransaction);
        this.testList.add(secondTransaction);
    }

    @Test
    public void putATransaction() throws Exception {
        Transaction firstTransaction = new Transaction();
        firstTransaction.setAmount(1000.0);
        firstTransaction.setParent_id(0);
        firstTransaction.setType("cars");

        this.mockMvc.perform(put("/transactionservice/transaction/3")
                .content(this.json(firstTransaction))
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void getATransaction() throws Exception {
        this.mockMvc.perform(get("/transactionservice/transaction/3"))
                .andExpect(status().isOk());
    }

    @Test
    public void methodIsNotAllowerd() throws Exception {
        this.mockMvc.perform(put("/transactionservice/transaction")
                .content(this.json(new Transaction()))
                .contentType(contentType))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void getTransactions() throws Exception {
        this.mockMvc.perform(get(Roots.GET_ALL_TRANSACTIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void getAllUniqeTypes() throws Exception {
        this.mockMvc.perform(get(Roots.GET_ALL_UNIQE_TYPES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void gettingTypesMethodIsNotAllowed() throws Exception {
        this.mockMvc.perform(post(Roots.GET_ALL_UNIQE_TYPES))
                .andExpect(status().isMethodNotAllowed());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
