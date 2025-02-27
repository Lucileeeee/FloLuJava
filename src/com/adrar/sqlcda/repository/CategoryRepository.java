package com.adrar.sqlcda.repository;

import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private static final Connection connection = Bdd.getConnection();

    //Méthode pour ajouter une Categorie
    public static Category save(Category addCategory) {
        //Créer un objet categorie
        Category newCat = null;
        try {
            //Requête
            String sql = "INSERT INTO category(category_name) VALUE(?)";
            //Préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Bind les paramètres
            preparedStatement.setString(1, addCategory.getCategoryName());

            //Exécuter la requête
            int nbrRows = preparedStatement.executeUpdate();

            //vérifier si la requête est bien passé
            if(nbrRows > 0){
                newCat = new Category(
                        addCategory.getCategoryName()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  newCat;
    }

    //Méthode pour vérifier si une Categorie existe
    public static boolean isExist(String categoryName) {
        boolean getCat = false;
        try {
            String sql = "SELECT id FROM category WHERE category_name = ?"; //si ça plante essayer en CamelCase categoryName
            //préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind le paramètre
            preparedStatement.setString(1, categoryName);
            //récupérer le resultat de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            //Vérification du résultat
            while(resultSet.next()){
                getCat = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return getCat;
    }

    //Méthode pour récupérer un compte par son Email
    public static Category catFindByName(String categoryName) {
        Category catFindByName = null;
        try {
            String sql = "SELECT id, category_name category WHERE category_name = ?";
            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setString(1, categoryName);
            //Exécuter la requête
            ResultSet resultSet = prepare.executeQuery();
            if(resultSet.next()){
                catFindByName = new Category();
                catFindByName.setId(resultSet.getInt("id"));
                catFindByName.setCategoryName(resultSet.getString("category_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return catFindByName;
    }

    //Méthode pour récupérer tous les comptes User dans une List
    public static List<Category> findAll(){
        List<Category> ListCats = new ArrayList<>();
        try {
            String sql = "SELECT id, category_name FROM category";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Ajout dans la liste des User
            while(resultSet.next()){
               Category cat = new Category();
                cat.setId(resultSet.getInt("id"));
                cat.setCategoryName(resultSet.getString("category_name"));
                ListCats.add(cat);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ListCats ;
    }

}


