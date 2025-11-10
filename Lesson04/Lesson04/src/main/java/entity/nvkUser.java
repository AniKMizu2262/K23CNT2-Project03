package entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter

public class nvkUser {
    Long nvkId;
    String nvkUsername;
    String nvkPassword;
    String nvkFullName;
    LocalDate nvkBirthDay;
    String nvkEmail;
    String nvkPhone;
    int nvkAge;
    Boolean nvkStatus;
}