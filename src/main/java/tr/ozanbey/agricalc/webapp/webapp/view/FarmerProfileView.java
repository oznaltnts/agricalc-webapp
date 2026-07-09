package tr.ozanbey.agricalc.webapp.webapp.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumFarmerType;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FarmerProfileView implements Serializable {

    private EnumFarmerType farmerType;
    private String tcknVkn;
    private String name;
    private String phone;
    private String email;
    private LocalDate birthEstablishmentDate;

    private Long userCityId;
    private String cityName;
    private String district;
    private String village;
    private String neighborhood;


}
