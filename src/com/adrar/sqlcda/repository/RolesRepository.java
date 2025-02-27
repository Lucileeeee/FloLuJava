package com.adrar.sqlcda.repository;

import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.Roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RolesRepository {

    private static Connection connection = Bdd.getConnection();

    public static Roles save(Roles roles) {
        Roles newRoles = null;
        try {
            String sql = "INSERT INTO roles(roles_name) VALUE(?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, roles.getRolesName());
            int nbrRows = preparedStatement.executeUpdate();
            if (nbrRows > 0) {
                newRoles = new Roles();
                newRoles.setRolesName(roles.getRolesName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newRoles;
    }

    public static boolean isExist(String rolesName) {
        boolean getRoles = false;
        try {
            String sql = "SELECT id FROM roles WHERE roles_name = ?";
            //préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind le paramètre
            preparedStatement.setString(1, rolesName);
            //récupérer le resultat de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            //Vérification du résultat
            while (resultSet.next()) {
                getRoles = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getRoles;
    }

    public static Roles findByName(String rolesName){
        Roles newRoles = new Roles();
        try {
            if (isExist(rolesName)) {
                String sql = "SELECT id FROM roles WHERE roles_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, rolesName);
                ResultSet resultSet = preparedStatement.executeQuery();
                newRoles.setId(resultSet.getInt(1));
                newRoles.setRolesName(rolesName);
            } else {
                System.out.println("Ce compte n'existe pas.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newRoles;
    }

    public static List<Roles> findAll(){
        List<Roles> liste = new ArrayList<Roles>();
        try{
            String sql = "SELECT id,roles_name FROM roles";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Roles newRoles = new Roles();
                newRoles.setRolesName(resultSet.getString(2));
                newRoles.setId(resultSet.getInt(1));
                liste.add(newRoles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return liste;
    }
}
