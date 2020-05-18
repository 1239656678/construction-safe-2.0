package com.dico.feign.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Stephen
 * @since 2019/9/23
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeFeignDomain {

    private List<NoticeFeignPerson> noticeFeignPersonList;
    private String noticeTitle;
    private String noticeContent;
}
