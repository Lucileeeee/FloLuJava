package com.adrar.sqlcda.repository;
import com.adrar.sqlcda.model.User;
import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.Task;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class TaskRepository {
    //ATTRIBUTS
    private static final Connection connection = Bdd.getConnection();

    public static Task saveWithUser(Task addTask) {
        //Créer un objet user
        Task newTask = null;
        try {
            //Requête
            String sql = "INSERT INTO task(title, content, user_id) VALUE(?,?;(SELECT id FROM users WHERE id = ?))";

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
}


