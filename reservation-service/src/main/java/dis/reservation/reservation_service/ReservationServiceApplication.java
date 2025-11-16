package dis.reservation.reservation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "dis.reservation")
public class ReservationServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(ReservationServiceApplication.class, args);
    }

}
