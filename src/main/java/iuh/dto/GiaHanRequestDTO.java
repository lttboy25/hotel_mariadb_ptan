/*
 * @ (#) GiaHanRequestDTO.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * @description:
 * @author: Duc Thuan
 * @date: 3/5/2026
 * @version:    1.0
 * @created:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiaHanRequestDTO implements Serializable {
    private Long chiTietId;
    private LocalDateTime newEndTime;
}
