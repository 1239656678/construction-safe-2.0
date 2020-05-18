import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @ValidatedNotEmpty
    @ValidatedSize(max = 5, message = "姓名长度应在{min}~{max}之间")
    private String name;

    @ValidatedNotEmpty
    @ValidatedSize(max = 2, message = "年龄长度应在{min}~{max}之间")
    private int age;

    private List<Children> childrens;

    private Children children;
}
