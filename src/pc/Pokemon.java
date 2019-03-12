/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pc;

/**
 *
 * @author csnd2356
 */
public class Pokemon {
    private String nom_pok;
    private int id_pok;
    private int level;
    private static int CPT = 0;
    private String annonce =" " + this.nom_pok + ", " + this.nom_pok ;
    
    public Pokemon(String nom, int level){        
        this.nom_pok = nom;      
        this.level = level;
        this.annonce = annonce;
    }
    public Pokemon(int idpok, String nom, int level){  
        this.id_pok = idpok;
        this.nom_pok = nom;      
        this.level = level;
        this.annonce = annonce;
    }
    

    public String getNom_pok() {
        return nom_pok;
    }

    public void setNom_pok(String nom_pok) {
        this.nom_pok = nom_pok;
    }

    public int getId_pok() {
        return id_pok;
    }

    public void setId_pok(int num_pok) {
        this.id_pok = num_pok;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAnnonce() {
        return annonce;
    }

    public void setAnnonce(String annonce) {
        this.annonce = annonce;
    }
    
    
    
   
    
    
}
