package com.example.MeterMaid;

import com.example.MeterMaid.Model.MeterDataWithValues;
import com.example.MeterMaid.Model.MeterValue;
import com.example.MeterMaid.dao.MeterDataRepository;
import com.example.MeterMaid.dao.MeterValueRespository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeterDataRepository meterDataRepository;

    @Autowired
    private MeterValueRespository meterValueRespository;

    @Test
    void creationWorksThroughAllLayers() throws Exception{
        MeterDataWithValues testData = new MeterDataWithValues(
                "2eaa222",
                "nsd943",
                "Hour",
                Instant.parse("2018-08-15T00:00:00Z"),
                Instant.parse("2018-08-15T23:00:00Z"),
                new HashMap<Instant, Double>()
        );
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T01:00:00Z"), 12.3));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T02:00:00Z"), 13.5));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T03:00:00Z"), 15.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T04:00:00Z"), 16.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T05:00:00Z"), 20.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T06:00:00Z"), 30.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T07:00:00Z"), 35.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T08:00:00Z"), 45.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T09:00:00Z"), 22.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T10:00:00Z"), 24.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T11:00:00Z"), 15.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T12:00:00Z"), 17.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T13:00:00Z"), 18.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T14:00:00Z"), 19.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T15:00:00Z"), 40.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T16:00:00Z"), 50.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T17:00:00Z"), 60.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T18:00:00Z"), 67.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T19:00:00Z"), 73.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T20:00:00Z"), 55.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T21:00:00Z"), 58.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T22:00:00Z"), 40.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T23:00:00Z"), 25.4));
        testData.addValues(new MeterValue(testData.getId(), testData.getMeter_id(), testData.getCustomer_id(),Instant.parse("2018-08-15T00:00:00Z"), 12.4));


        MvcResult result =  mockMvc.perform(post(("/metermaid/api/meterdata"))
                .contentType("application/json")
        .content(objectMapper.writeValueAsString(testData))).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        Assert.assertEquals(testData, content);
    }

}
