package gmbh.conteco.serdeexample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Customer {
    @NonNull
    private int customerID;
    @NonNull
    private String customerName;
}
