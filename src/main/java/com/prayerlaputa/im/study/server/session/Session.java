package com.prayerlaputa.im.study.server.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenglong.yu
 * created on 2020/9/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    private String userId;
    private String userName;

}
