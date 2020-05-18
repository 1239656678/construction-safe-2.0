package com.dico.api.argument;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 * @Package cn.diconet.common.search
 * @Description: 描述
 * @date 2018\3\29 002915:14
 */
public class SearchRequest {
    public enum Operator {
        EQ, NEQ, LIKE, GT, LT, GTE, LTE, IN, BETWEEN, ISNULL, ISNOTNULL
    }

    @Setter
    @Getter
    private String fieldName;

    @Setter
    @Getter
    private Object value;

    @Setter
    @Getter
    private Operator operator;

    public SearchRequest(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    /**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     */
    public static Map<String, SearchRequest> parse(Map<String, Object> searchParams) {
        Map<String, SearchRequest> filters = new HashMap<String, SearchRequest>();

        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isBlank(String.valueOf(value))) {
                continue;
            }
            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "_");
            if (names.length < 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }
            //String filedName = names[1];
            String filedName = key.substring(0, key.indexOf(names[names.length - 1]) - 1);
            Operator operator = Operator.valueOf(names[names.length - 1]);
            // 创建searchFilter
            SearchRequest filter = new SearchRequest(filedName, operator, value);
            if ((operator == Operator.BETWEEN)) {
                @SuppressWarnings("unchecked")
                Map<String, Date> mapDate = (Map<String, Date>) value;
                Set<String> keySet = mapDate.keySet();
                if (keySet.size() < 2) {
                    throw new IllegalArgumentException(key + " is not have enough com.dico.result count");
                }
                Date beginDate = mapDate.get("beginDate");
                Date endDate = mapDate.get("endDate");
                List<Date> objValue = new ArrayList<Date>();
                objValue.add(beginDate);
                objValue.add(endDate);
                filter = new SearchRequest(filedName, operator, objValue);
            }
            filters.put(key, filter);
        }
        return filters;
    }
}
