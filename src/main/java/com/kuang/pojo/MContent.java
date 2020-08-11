package com.kuang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wonders on 2020-07-17 14:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MContent {

    private String title;
    private String img;
    private String price;
    private String eval;
    private String site;
}
