/*
 * @ (#) DoiPhongRequestDTO.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.dto;


import lombok.*;

import java.io.Serializable;

/*
 * @description:
 * @author: Duc Thuan
 * @date: 3/5/2026
 * @version:    1.0
 * @created:
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DoiPhongRequestDTO implements Serializable {
    private String maPDP;
    private String maPhongCu;
    private String maPhongMoi;


}
