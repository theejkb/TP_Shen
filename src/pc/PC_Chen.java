/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author csnd2356
 */
public class PC_Chen {

    private Dresseur utilisateur;
    private static String URL = "jdbc:mysql://127.0.0.1/pokemon?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static String LOGIN = "root";
    private static String PASSWORD = "";
    private ArrayList<Pokemon> Pokemon;
    private Connection connection;
    private Statement stmt;

    public void PC_Chen(Dresseur d) {
        this.utilisateur = d;
    }

    public boolean connection() {
        boolean result = false;
        connection = null;

        try {
            connection = DriverManager
                    .getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connexion ratée! Lisez la suite:");
            e.printStackTrace();
            return connection();
        }
        if (connection != null) {
            System.out.println("Ok, tout semble cool");
            result = true;
            return result;
        } else {
            System.out.println("Nope, pas de connexion autorisée");
            return result;
        }
    }

    public boolean connecteDresseur(String d) throws SQLException {
        boolean rep = false;
        this.connection();
        this.stmt = this.connection.createStatement();
        String query = "SELECT * FROM dresseur WHERE nom_dress like '" + d + "'";
        //On crée l'objet avec la requête en paramètre                 
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String nom = rs.getString("nom_dress");
            int id = rs.getInt("id_dress");
            this.utilisateur = new Dresseur(id, nom);
            rep = true;
        }
        return rep;
    }

    public ArrayList<Pokemon> getPokemonDetenu() throws SQLException {
        ArrayList<Pokemon> Pok = new ArrayList<>();
        this.connecteDresseur(this.utilisateur.getNom_dress());
        this.stmt = this.connection.createStatement();
        String query = "SELECT * "
                + "FROM pokemon p, dresseur d, detientpokemon dp "
                + "WHERE p.id_pok = dp.id_pok AND d.id_dress = dp.id_dress "
                + "AND d.nom_dress like '" + this.utilisateur.getNom_dress() + "'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String nompok = rs.getString("nom_pok");
            int levelpok = rs.getInt("lvl_pok");
            int idpok = rs.getInt("id_pok");
            Pokemon p = new Pokemon(idpok, nompok, levelpok);
            Pok.add(p);
        }
        return Pok;
    }

    public ArrayList<Pokemon> getPokemonBoite(int nb) throws SQLException {
        ArrayList<Pokemon> PokBoite = new ArrayList<>();
        this.connecteDresseur(this.utilisateur.getNom_dress());
        int id = this.utilisateur.getId_dress();
        this.stmt = this.connection.createStatement();
        String query = "SELECT sp.id_pok, nom_pok, sp.lvl_pok "
                + "FROM pokemon p, stockpokemon sp "
                + "WHERE p.id_pok = sp.id_pok "
                + "AND sp.num_boite like " + nb + " "
                + "AND sp.id_dress like " + id + "";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String nompok = rs.getString("nom_pok");
            int levelpok = rs.getInt("lvl_pok");
            int idpok = rs.getInt("id_pok");
            Pokemon p = new Pokemon(idpok, nompok, levelpok);
            PokBoite.add(p);
        }
        return PokBoite;
    }

    public boolean stockerPokemon(Pokemon p, int boite) throws SQLException {
        this.connecteDresseur(this.utilisateur.getNom_dress());
        this.stmt = this.connection.createStatement();
        String query = "INSERT INTO stockpokemon"
                + " values (" + this.utilisateur.getId_dress() + "," + p.getId_pok() + "," + p.getLevel() + "," + boite + ")";
        stmt.executeUpdate(query);
        query = "DELETE FROM detientpokemon "
                + "WHERE id_dress like " + this.utilisateur.getId_dress() + " AND id_pok like " + p.getId_pok() + "";
        stmt.executeUpdate(query);
        String query3 = "Select count(id_dress)as nbpok from detientpokemon where id_dress like " + this.utilisateur.getId_dress() + "";
        ResultSet rs = stmt.executeQuery(query3);
        while (rs.next()) {
            int nbpok = rs.getInt("nbpok");
            if (nbpok <= 6) {
                return true;
            } else if (nbpok > 6) {
                return false;
            }
        }

        return false;
    }

    public boolean retierPokemon(Pokemon p, int boite) throws SQLException {
        this.connecteDresseur(this.utilisateur.getNom_dress());
        this.stmt = this.connection.createStatement();
        String query = "INSERT INTO detientpokemon"
                + " values (" + this.utilisateur.getId_dress() + "," + p.getId_pok() + "," + p.getLevel() + ")";
        stmt.executeUpdate(query);
        String query2 = "DELETE FROM stockpokemon "
                + "WHERE id_dress = " + this.utilisateur.getId_dress() + " AND id_pok = " + p.getId_pok();
        stmt.executeUpdate(query2);
        String query3 = "Select count(id_pok)as nbpok from stockpokemon where id_dress like " + this.utilisateur.getId_dress() + "";
        ResultSet rs = stmt.executeQuery(query3);
        while (rs.next()) {
            int nbpok = rs.getInt("nbpok");
            if (nbpok <= 15) {
                return true;
            } else if (nbpok > 15) {
                return false;
            }
        }

        return true;
    }

    //Fonction pour récupérer le numéro de la boite d'un pokémon
    public int getNumBoite(Pokemon p) throws SQLException {
        int numboite = 0;
        this.connecteDresseur(this.utilisateur.getNom_dress());
        this.stmt = this.connection.createStatement();
        String query = "SELECT num_boite FROM stockpokemon WHERE"
                + " id_pok = " + p.getId_pok();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            numboite = rs.getInt("num_boite");
        }
        return numboite;
    }
}
