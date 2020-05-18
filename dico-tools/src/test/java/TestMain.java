import com.dico.Exception.ErrorUsedException;
import com.dico.qrcode.QRCodeAttribute;
import com.dico.result.ValidatedResult;
import com.dico.util.FilterBeanUtils;
import com.dico.util.QRCodeUtils;
import com.dico.util.ValiDatedUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.*;

/**
 * @author Gaodl
 * @version v1.0
 * 文件名称: $file_name$
 * 创建时间: $date$
 */
public class TestMain {
    public static void main(String[] args) {
//        List<Children> childrenList = new ArrayList<>();
//        Children childrenZs = new Children("张三", 10);
//        Children childrenLs = new Children("李四", 11);
//        Children childrenNull = null;
//        childrenList.add(childrenLs);
//        childrenList.add(childrenZs);
//        Children childrenLs1 = new Children("李四", 11);
//        Person person1 = new Person("王麻子", 30, childrenList, childrenNull);
//        try {
//            ValidatedResult validatedResult = ValiDatedUtils.valiDatedBean(person1);
//            System.out.println(validatedResult.getMessage());
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ErrorUsedException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
        QRCodeAttribute qrCodeAttribute = new QRCodeAttribute();
        qrCodeAttribute.setImagePath("E:/cloud-services/nginx/static/qr-code/");
        qrCodeAttribute.setImageViewText("111111");
        qrCodeAttribute.setContent("http://180.76.150.16:8080/sms-equipment/smsEquipmentBaseInfo/dataInfo?smsEquipmentBaseInfoId=2c9180886afc87c6016afde96d770001");
        QRCodeUtils qrCodeUtils = new QRCodeUtils(qrCodeAttribute);
        qrCodeUtils.ganertorTextImage();
    }
}
