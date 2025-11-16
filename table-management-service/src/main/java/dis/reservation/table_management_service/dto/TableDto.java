package dis.reservation.table_management_service.dto;

import lombok.Data;

@Data
public class TableDto {

    private Long id;
    private int number;
    private int seats;
}
