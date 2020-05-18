package com.dico.modules.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * @author Administrator
 * @date 2020-04-21 14:19
 */

@Data
@Entity
@ApiModel(value = "报表word实体")
@Table(name = "word_info")
public class WordInfo {

    @Id
    @GeneratedValue(generator = "generatorUUID")
    @GenericGenerator(name = "generatorUUID", strategy = "uuid")
    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    @ApiModelProperty(value = "名称", name = "name", required = true)
    private String name;

    @ApiModelProperty(value = "类型（0月度报表1季度报表2年度报表）", name = "type", required = true)
    private int type;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "createDate", name = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "createUser", name = "创建人", hidden = true)
    private String createUser;
}
