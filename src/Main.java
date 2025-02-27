import com.adrar.sqlcda.model.User;
import com.adrar.sqlcda.repository.UserRepository;

public class Main {
    public static void main(String[] args) {
        User newUser = new User(
                "Mathieu",
                "Mithridate",
                "mathieu1@gmail.com",
                "123456"
        );

        User getUser = UserRepository.findByEmail("mathieu1@gmail.com");
        if (getUser != null) {
            System.out.println(getUser);
        } else {
            System.out.println("le compte n'existe pas");
        }
    }
}