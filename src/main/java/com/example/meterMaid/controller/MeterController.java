package com.example.meterMaid.controller;

import com.example.meterMaid.Model.MeterData;
import com.example.meterMaid.Model.MeterDataWithValues;
import com.example.meterMaid.Model.MeterSum;
import com.example.meterMaid.Model.MeterValue;
import com.example.meterMaid.dao.MeterDataRepositoryImpl;
import com.example.meterMaid.dao.MeterValueRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping("/metermaid/api")
public class MeterController {
    @Autowired
    MeterDataRepositoryImpl meterDataRepositoryImpl;

    @Autowired
    MeterValueRepositoryImpl meterValueRepositoryImpl;

    @PostMapping("/meterdata/")
    public ResponseEntity<MeterDataWithValues> saveMeterDataAndValues(@RequestBody @Valid MeterDataWithValues meterDataWithValues){
        try{
            if(meterDataWithValues.getId() == null){
                meterDataWithValues.setId(UUID.randomUUID());
            }
            MeterData meterData = new MeterData(meterDataWithValues);
            MeterData returnedMeterData = meterDataRepositoryImpl.saveMeterData(meterData);
            List<MeterValue> values = new ArrayList<>();
            Map<Instant, Double> valuesFromMeterDataWithValues = meterDataWithValues.getValues();
            for (Map.Entry<Instant, Double> entry : valuesFromMeterDataWithValues.entrySet()) {
                MeterValue meterValue = new MeterValue(
                        UUID.randomUUID(),
                        meterData.getId(),
                        meterData.getMeter_id(),
                        meterData.getCustomer_id(),
                        entry.getKey(),
                        entry.getValue()
                );
                values.add(meterValue);
            }
            List<MeterValue> returnedValues = meterValueRepositoryImpl.saveMeterValues(values);
            Map<Instant, Double> mapOfReturnedValues = new HashMap<>();
            for (MeterValue theValue: returnedValues
                 ) {
                mapOfReturnedValues.put(theValue.getHour(), theValue.getValue());
            }
            MeterDataWithValues returnedMeterDataWithValues = new MeterDataWithValues(returnedMeterData,mapOfReturnedValues);
        return new ResponseEntity<>(returnedMeterDataWithValues, HttpStatus.CREATED);

        }catch(Exception ex){
            return new ResponseEntity(
                    ex,
                    HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/meterdata/")
    public ResponseEntity<List<MeterDataWithValues>> getAllMeterDataWithValues(){
        try{
            List<MeterDataWithValues> returnedMeterDataWithValueList = new ArrayList<>();
            List<MeterData> returnedMeterData = meterDataRepositoryImpl.getAll();
            for (int i = 0; i < returnedMeterData.size(); i++) {
                MeterDataWithValues returnedMeterDataWithValues = new MeterDataWithValues(
                        returnedMeterData.get(i),
                        new HashMap<Instant, Double>()
                );
                List<MeterValue> returnedMeterValues = meterValueRepositoryImpl.getMeterValuesByMeterDataId(returnedMeterData.get(i).getId());
                for (int j = 0; j < returnedMeterValues.size(); j++) {
                    returnedMeterDataWithValues.addValues(returnedMeterValues.get(j));
                }
                returnedMeterDataWithValueList.add(returnedMeterDataWithValues);
            }
            return new ResponseEntity<>(returnedMeterDataWithValueList, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity(
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/meterdata/{from}/{to}/")
    public ResponseEntity<List<MeterDataWithValues>> getMeterDataWithValuesByDate(@PathVariable final Instant from, @PathVariable final Instant to){
        List<MeterDataWithValues> returnedMeterDataWithValueList = new ArrayList<>();
        List<MeterData> returnedMeterData = meterDataRepositoryImpl.getMeterDataFromDateToDate(from, to);
        for (int i = 0; i < returnedMeterData.size(); i++) {
            MeterDataWithValues returnedMeterDataWithValues = new MeterDataWithValues(
                    returnedMeterData.get(i),
                    new HashMap<Instant, Double>()
            );
            List<MeterValue> returnedMeterValues = meterValueRepositoryImpl.getMeterValuesByMeterDataId(returnedMeterData.get(i).getId());
            for (int j = 0; j < returnedMeterValues.size(); j++) {
                returnedMeterDataWithValues.addValues(returnedMeterValues.get(j));
            }
            returnedMeterDataWithValueList.add(returnedMeterDataWithValues);
        }
        return new ResponseEntity<>(returnedMeterDataWithValueList, HttpStatus.OK);
    }

    @GetMapping("/meterdata/{from}/{to}/meterid:{id}")
    public ResponseEntity<MeterSum> getMeterDataSumFromDateToDateByMeterId(@PathVariable final Instant from, @PathVariable final Instant to, @PathVariable final String id){
        if(from.isAfter(to)){
            return new ResponseEntity<>(new MeterSum(), HttpStatus.BAD_REQUEST);
        } else{
            MeterSum meterSum = new MeterSum();
            meterSum.setFrom(from);
            meterSum.setTo(to);
            meterSum.setId(id);
            meterSum.setTypeOfSum("Sum for meter: " + id + ", in period: " + from.toString() + " to: " + to.toString() + ".");
            Double returnedSum = meterValueRepositoryImpl.getMeterValueFromDateToDateByMeterId(from, to, id);
            meterSum.setSum(returnedSum);
            return new ResponseEntity<>(meterSum, HttpStatus.OK);
        }

    }

    @GetMapping("/meterdata/{from}/{to}/customerid:{id}")
    public ResponseEntity<MeterSum> getMeterDataSumFromDateToDateByCustomerId(@PathVariable final Instant from, @PathVariable final Instant to, @PathVariable final String id){
        if(from.isAfter(to)){
            return new ResponseEntity<>(new MeterSum(), HttpStatus.BAD_REQUEST);
        } else{
            MeterSum meterSum = new MeterSum();
            meterSum.setFrom(from);
            meterSum.setTo(to);
            meterSum.setId(id);
            meterSum.setTypeOfSum("Sum for customer: " + id + ", in period: " + from.toString() + " to: " + to.toString() + ".");
            Double returnedSum = meterValueRepositoryImpl.getMeterValueFromDateToDateByCustomerId(from, to, id);
            meterSum.setSum(returnedSum);

            return new ResponseEntity<>(meterSum, HttpStatus.OK);
        }

    }
}
