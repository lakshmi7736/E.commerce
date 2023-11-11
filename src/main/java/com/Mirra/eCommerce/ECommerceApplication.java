package com.Mirra.eCommerce;

import com.Mirra.eCommerce.Configure.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ECommerceApplication {
    @Autowired
    private TwilioConfig twilioConfig;
    @PostConstruct
    public void setup() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }


    public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

}



