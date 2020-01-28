package com.example.MeterMaid;

import com.example.MeterMaid.Model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MeterMaidApplicationTests {

	@Test
	void contextLoads() {

	}

	@Test
	public void createSingleUser(){
		User user = new User("Spanky", "Spanks", "spanky@spanks.com");

	}


}
