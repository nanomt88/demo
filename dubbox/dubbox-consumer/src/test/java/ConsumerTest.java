import com.nanomt88.demo.common.User;
import com.nanomt88.demo.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试远程调用dubbo类
 *
 * @author nanomt88@gmail.com
 * @create 2017-07-25 22:20
 **/
public class ConsumerTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "dubbo-consumer.xml" });
        context.start();

        UserService userService = (UserService) context.getBean("userService");
        User user = userService.getUser(100L);
        System.out.println("get:"+user);

        User user1 = userService.postUser("200");
        System.out.println("post:"+user1);
    }
}
