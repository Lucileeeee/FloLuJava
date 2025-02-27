package com.adrar.sqlcda.repository;

import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class UserRepository {
    /*
    * Attributs
    * */

    private static final Connection connection = Bdd.getConnection();

    /*
    * Méthodes (CRUD)
    * */
    //Méthode pour ajouter un User
    public static User save(User addUser) {
        //Créer un objet user
        User newUser = null;
        try {
            //Requête
            String sql = "INSERT INTO users(firstname, lastname, email, password) VALUE(?,?,?,?)";
            //Préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Bind les paramètres
            preparedStatement.setString(1, addUser.getFirstname());
            preparedStatement.setString(2, addUser.getLastname());
            preparedStatement.setString(3, addUser.getEmail());
            preparedStatement.setString(4, addUser.getPassword());

            //Exécuter la requête
            int nbrRows = preparedStatement.executeUpdate();

            //vérifier si la requête est bien passé
            if(nbrRows > 0){
                newUser = new User(
                        addUser.getFirstname(),
                        addUser.getLastname(),
                        addUser.getEmail(),
                        addUser.getPassword()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }

    //Méthode pour vérifier si un User existe
    public static boolean isExist(String email) {
        boolean getUser = false;
        try {
            String sql = "SELECT id FROM users WHERE email = ?";
            //préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind le paramètre
            preparedStatement.setString(1, email);
            //récupérer le resultat de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            //Vérification du résultat
            while(resultSet.next()){
                getUser = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return getUser;
    }

    //Méthode pour récupérer un compte par son Email
    public static User findByEmail(String email) {
        User findUser = null;
        try {
            String sql = "SELECT id, firstname, lastname, email FROM users WHERE email = ?";
            //Préparer la requête
            PreparedStatement prepare = connection.prepareStatement(sql);
            //Bind un paramètre
            prepare.setString(1, email);
            //Exécuter la requête
            ResultSet resultSet = prepare.executeQuery();
            if(resultSet.next()){
                findUser = new User();
                findUser.setId(resultSet.getInt("id"));
                findUser.setFirstname(resultSet.getString("firstname"));
                findUser.setLastname(resultSet.getString("lastname"));
                findUser.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findUser;
    }

    //Méthode pour récupérer tous les comptes User dans une List
    public static List<User> findAll(){
        List<User> findUsers = new ArrayList<>();
        try {
            String sql = "SELECT id, firstname, lastname, email FROM users";
            //Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Exécution de la requête
            ResultSet resultSet = preparedStatement.executeQuery();
            //Ajout dans la liste des User
            while(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstname(resultSet.getString("firstname"));
                user.setLastname(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                findUsers.add(user);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return findUsers;
    }

    //Méthode qui met à jour un User et retourne User modifié
    public static User update(User user, String email){
        User updateUser = null;
        try {
            String sql = "UPDATE users SET firstname = ?, lastname = ?, email = ? WHERE email = ?";
            //préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind des paramètres
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, email);
            //Exécution de la requête
            int  nbrRows = preparedStatement.executeUpdate();
            if(nbrRows > 0){
                updateUser = user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateUser;
    }
    public static User saveWithRoles(User addUser){
        User newUser = null;
        try {
            String sql = "INSERT INTO users (firstname, lastname, email, password, roles_id" +
                    "VALUE (?,?,?,?; (SELECT id FROM roles WHERE roles_name = ?))";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, addUser.getFirstname());
            preparedStatement.setString(2, addUser.getLastname());
            preparedStatement.setString(3, addUser.getEmail());
            preparedStatement.setString(4, addUser.getPassword());
            preparedStatement.setString(5, addUser.getRoles().getRolesName());

            int nbrRows = preparedStatement.executeUpdate();
            if(nbrRows > 0){
                newUser = addUser;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return newUser;
    }
}
