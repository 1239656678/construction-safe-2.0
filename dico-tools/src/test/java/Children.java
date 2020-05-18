import com.dico.annotation.FilterBean;
import com.dico.annotation.ValidatedNotEmpty;
import com.dico.annotation.ValidatedSize;
import com.dico.annotation.ViewField;
import com.dico.enums.DeviceEnum;
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
@FilterBean
@NoArgsConstructor
@AllArgsConstructor
public class Children {

    @ValidatedNotEmpty
    @ValidatedSize(max = 5, message = "姓名长度应在{min}~{max}之间")
    @ViewField(targets = {DeviceEnum.WEB})
    private String name;

    @ValidatedNotEmpty
    @ValidatedSize(max = 2, message = "年龄长度应在{min}~{max}之间")
    @ViewField(targets = {DeviceEnum.MOBILE})
    private int age;
}
