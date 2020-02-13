package com.example.MeterMaid.controller;

import com.example.MeterMaid.Model.MeterData;
import com.example.MeterMaid.Model.MeterDataWithValues;
import com.example.MeterMaid.Model.MeterSum;
import com.example.MeterMaid.Model.MeterValue;
import com.example.MeterMaid.dao.MeterDataRepository;
import com.example.MeterMaid.dao.MeterValueRepository;
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
    MeterDataRepository meterDataRepository;

    @Autowired
    MeterValueRepository meterValueRepository;

    @PostMapping("/meterdata/")
    public ResponseEntity<MeterDataWithValues> saveMeterDataAndValues(@RequestBody @Valid MeterDataWithValues meterDataWithValues){
        try{
            if(meterDataWithValues.getId() == null){
                meterDataWithValues.setId(UUID.randomUUID());
            }
            MeterData meterData = new MeterData(meterDataWithValues);
            MeterData returnedMeterData = meterDataRepository.SaveMeterData(meterData);
            Map<Instant, Double> returnedMap = new HashMap<>();
            for (Map.Entry<Instant,Double> entry : meterDataWithValues.getValues().entrySet()){
                MeterValue meterValue = new MeterValue(
                        returnedMeterData.getId(),
                        returnedMeterData.getMeter_id(),
                        returnedMeterData.getCustomer_id(),
                        entry.getKey(),
                        entry.getValue()
                );
                MeterValue returnedMeterValue = meterValueRepository.saveMeterValue(meterValue);
                returnedMap.put(returnedMeterValue.getHour(),returnedMeterValue.getValue());
            }
            MeterDataWithValues returnedMeterDataWithValues = new MeterDataWithValues(meterData, returnedMap);
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
            List<MeterData> returnedMeterData = meterDataRepository.GetAll();
            for (int i = 0; i < returnedMeterData.size(); i++) {
                MeterDataWithValues returnedMeterDataWithValues = new MeterDataWithValues(
                        returnedMeterData.get(i),
                        new HashMap<Instant, Double>()
                );
                List<MeterValue> returnedMeterValues = meterValueRepository.getMeterValuesByMeterDataId(returnedMeterData.get(i).getId());
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
        List<MeterData> returnedMeterData = meterDataRepository.GetMeterDataFromDateToDate(from, to);
        for (int i = 0; i < returnedMeterData.size(); i++) {
            MeterDataWithValues returnedMeterDataWithValues = new MeterDataWithValues(
                    returnedMeterData.get(i),
                    new HashMap<Instant, Double>()
            );
            List<MeterValue> returnedMeterValues = meterValueRepository.getMeterValuesByMeterDataId(returnedMeterData.get(i).getId());
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
            List<MeterValue> returnedMeterValues = meterValueRepository.getMeterValueFromDateToDateByMeterId(from, to, id);
            for (int i = 0; i < returnedMeterValues.size(); i++) {
                meterSum.addSum(returnedMeterValues.get(i).getValue());
            }
            return new ResponseEntity<>(meterSum, HttpStatus.OK);
        }

    }

    @GetMapping("/meterdata/{from}/{to}/customerid:{id}")
    public ResponseEntity<MeterSum> getMeterDataSumFromDateToDateByCustomerId(@PathVariable final Instant from, @PathVariable final Instant to, @PathVariable final String id){
        if(from.isAfter(to)){
            return new ResponseEntity<>(new MeterSum(), HttpStatus.BAD_REQUEST);
        } else{
            List<MeterDataWithValues> returnedMeterDataWithValueList = new ArrayList<>();
            MeterSum meterSum = new MeterSum();
            meterSum.setFrom(from);
            meterSum.setTo(to);
            meterSum.setId(id);
            meterSum.setTypeOfSum("Sum for customer: " + id + ", in period: " + from.toString() + " to: " + to.toString() + ".");

            List<MeterValue> returnedMeterValues = meterValueRepository.getMeterValueFromDateToDateByCustomerId(from, to, id);
            for (int i = 0; i < returnedMeterValues.size(); i++) {
                meterSum.addSum(returnedMeterValues.get(i).getValue());
            }

            return new ResponseEntity<>(meterSum, HttpStatus.OK);
        }

    }
}
