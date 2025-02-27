package com.adrar.sqlcda.repository;
import com.adrar.sqlcda.model.Category;
import com.adrar.sqlcda.model.User;
import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.Task;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskRepository {
    //ATTRIBUTS
    private static final Connection connection = Bdd.getConnection();

    public static Task saveWithUser(Task addTask) {
        //Créer un objet user
        Task newTask = null;
        try {
            //Requête
            String sql = "INSERT INTO task(title, content, user_id) VALUE(?,?(SELECT id FROM users WHERE id = ?))";

            //Préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Bind les paramètres
            preparedStatement.setString(1, addTask.getTitle());
            preparedStatement.setString(2, addTask.getContent());
            preparedStatement.setString(3, addTask.getUser().getId());

            //Exécuter la requête
            int nbrRows = preparedStatement.executeUpdate();

            //vérifier si la requête est bien passé
            if(nbrRows > 0){
                newTask = addTask;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newTask;
   }
    //Méthode pour vérifier si une Task existe
    public static boolean isExist(String title, Date create_at) {
        boolean getTask = false;
        try {
            String sql = "SELECT id FROM task WHERE title = ? AND create_at = ?";
            //préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind le paramètre
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, String.valueOf(create_at));
            //récupérer le resultat de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            //Vérification du résultat
            while(resultSet.next()){
                getTask = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return getTask;
    }
    public static Task findByTitle(String taskTitle, Date dateC){
        Task newTask = new Task();
        try {
            if (isExist(taskTitle, dateC)) {
                String sql = "SELECT t.id AS tId,t.content,t.end_date,t.`status`,\n" +
                        "u.id AS uId,u.firstname,u.lastname,\n" +
                        "GROUP_CONCAT(c.id) AS catId, GROUP_CONCAT(c.category_name) AS catName\n" +
                        "FROM task_category AS tc \n" +
                        "INNER JOIN task AS t ON tc.task_id = t.id\n" +
                        "RIGHT JOIN category AS c ON tc.category_id = c.id\n" +
                        "RIGHT JOIN users AS u ON t.users_id = u.id\n" +
                        "WHERE t.title = ? AND t.create_at = ?\n" +
                        "GROUP BY t.id";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, taskTitle);
                preparedStatement.setString(2, dateC.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                newTask.setId(resultSet.getInt("tId"));
                newTask.setContent(resultSet.getString("content"));
                newTask.setEndDate(resultSet.getDate("end_date"));
                newTask.setCreateAt(dateC);
                newTask.setStatus(resultSet.getBoolean("status"));
                newTask.setTitle(taskTitle);
                if (resultSet.getInt("uId")>0) {
                    User u = new User();
                    u.setId(resultSet.getInt("uId"));
                    u.setFirstname(resultSet.getString("firstname"));
                    u.setLastname(resultSet.getString("lastname"));
                    newTask.setUser(u);
                }
                if (resultSet.getString("catId")!=null) {
                    String[] catIdS = resultSet.getString("catId").split(",");
                    String[] catNameS = resultSet.getString("catName").split(",");
                    for (int i = 0; i < catIdS.length; i++) {
                        Category c = new Category();
                        c.setId(Integer.parseInt(catIdS[i]));
                        c.setCategoryName(catNameS[i]);
                        newTask.addCategory(c);
                    }
                }
            } else {
                System.out.println("Ce compte n'existe pas.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newTask;
    }
    public static List<Task> findAll(){
        List<Task> liste = new ArrayList<Task>();
        try{
            String sql = "SELECT t.id AS tId,t.title,t.create_at,t.content,t.end_date,t.`status`,\n" +
                    "u.id AS uId,u.firstname,u.lastname,\n" +
                    "GROUP_CONCAT(c.id) AS catId, GROUP_CONCAT(c.category_name) AS catName\n" +
                    "FROM task_category AS tc \n" +
                    "INNER JOIN task AS t ON tc.task_id = t.id\n" +
                    "RIGHT JOIN category AS c ON tc.category_id = c.id\n" +
                    "RIGHT JOIN users AS u ON t.users_id = u.id\n" +
                    "GROUP BY t.id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Task newTask = new Task();
                newTask.setId(resultSet.getInt("tId"));
                newTask.setContent(resultSet.getString("content"));
                newTask.setEndDate(resultSet.getDate("end_date"));
                newTask.setCreateAt(resultSet.getDate("create_at"));
                newTask.setStatus(resultSet.getBoolean("status"));
                newTask.setTitle(resultSet.getString("title"));
                if (resultSet.getInt("uId")>0) {
                    User u = new User();
                    u.setId(resultSet.getInt("uId"));
                    u.setFirstname(resultSet.getString("firstname"));
                    u.setLastname(resultSet.getString("lastname"));
                    newTask.setUser(u);
                }
                if (resultSet.getString("catId")!=null) {
                    String[] catIdS = resultSet.getString("catId").split(",");
                    String[] catNameS = resultSet.getString("catName").split(",");
                    for (int i = 0; i < catIdS.length; i++) {
                        Category c = new Category();
                        c.setId(Integer.parseInt(catIdS[i]));
                        c.setCategoryName(catNameS[i]);
                        newTask.addCategory(c);
                    }
                }
                liste.add(newTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }
}


