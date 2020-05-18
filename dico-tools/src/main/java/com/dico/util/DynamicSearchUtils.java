package com.dico.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.dico.api.argument.SearchRequest;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Thomas
 * @Description: 将条件查询参数的map 转换为 Example
 * @date 2018\4\4 0004
 */
public class DynamicSearchUtils {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Specification<T> getSpecification(Map<String, Object> params, Class clazz) {

        final Map<String, SearchRequest> request = SearchRequest.parse(params);

        Specification<T> specification = new Specification<T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                if (null == request) {
                    return cb.conjunction();
                }

                List<Predicate> predicates = new ArrayList<Predicate>();
                for (Iterator<Map.Entry<String, SearchRequest>> iter = request.entrySet().iterator(); iter.hasNext(); ) {

                    Map.Entry<String, SearchRequest> condition = iter.next();
                    // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
                    String[] names = StringUtils.split(condition.getValue().getFieldName(), ".");
                    Path expression = root.get(names[0]);
                    for (int i = 1; i < names.length; i++) {
                        expression = expression.get(names[i]);
                    }

                    // logic operator
                    switch (condition.getValue().getOperator()) {
                        case EQ:
                            predicates.add(cb.equal(expression, condition.getValue().getValue()));
                            break;
                        case NEQ:
                            predicates.add(cb.notEqual(expression, condition.getValue().getValue()));
                            break;
                        case LIKE:
                            predicates.add(cb.like(expression, "%" + condition.getValue().getValue() + "%"));
                            break;
                        case GT:
                            predicates.add(cb.greaterThan(expression, (Comparable) condition.getValue().getValue()));
                            break;
                        case LT:
                            predicates.add(cb.lessThan(expression, (Comparable) condition.getValue().getValue()));
                            break;
                        case GTE:
                            predicates.add(cb.greaterThanOrEqualTo(expression, (Comparable) condition.getValue().getValue()));
                            break;
                        case LTE:
                            predicates.add(cb.lessThanOrEqualTo(expression, (Comparable) condition.getValue().getValue()));
                            break;
                        case IN:
                            predicates.add(cb.and(expression.in((Object[]) condition.getValue().getValue())));
                            break;
                        case BETWEEN:
                            predicates.add(cb.between(expression, (Comparable) ((Object) ((List) condition.getValue().getValue()).get(0)), (Comparable) ((Object) ((List) condition.getValue().getValue()).get(1))));
                            break;
                        case ISNULL:
                            predicates.add(cb.and(cb.isNull(expression)));
                            break;
                        case ISNOTNULL:
                            predicates.add(cb.and(cb.isNotNull(expression)));
                            break;
                    }
                }
                // 将所有条件用 and 联合起来
                if (predicates.size() > 0) {
                    return cb.and(predicates.toArray(new Predicate[predicates.size()]));
                }
                return cb.conjunction();
            }
        };

        return specification;
    }
}
