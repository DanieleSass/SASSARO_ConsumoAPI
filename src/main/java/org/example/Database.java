package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database instance = null;
    private Connection connection;

    private Database() throws SQLException {
        connection= DriverManager.getConnection("jdbc:sqlite:pokemon.db");
        creaTabelle();
    }

    public static Database getInstance() throws SQLException {
        if(instance==null){
            instance= new Database();
        }
        return instance;
    }

    private void creaTabelle() throws SQLException {
        String query= """
                CREATE TABLE IF NOT EXISTS Pokemon
                (
                    Id    INTEGER      not null
                        constraint Pokemon_pk
                            primary key,
                    Nome  VARCHAR(100) not null
                        constraint Pokemon_pk_2
                            unique,
                    Tipo1 VARCHAR(100) not null,
                    Tipo2 VARCHAR(100)
                );
                """;

        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(query);
        }
    }

    public void insertPokemon(Pokemon pokemon){

        //test connessione
        if(! testConnection()){
            throw new RuntimeException("Impossibile connettersi al db");
        }
        String query="INSERT INTO Pokemon (Id, Nome, Tipo1, Tipo2) VALUES (?,?,?,?)";

        try(PreparedStatement statement=connection.prepareStatement(query)){
            statement.setInt(1,pokemon.id());
            statement.setString(2, pokemon.name());
            statement.setString(3,pokemon.types().get(0).type().name());
            if(pokemon.types().size() > 1){
                statement.setString(4, pokemon.types().get(1).type().name());
            }
            else{
                    //indica che sarebbe varchar se non fosse null
                statement.setNull(4, java.sql.Types.VARCHAR);
            }

            statement.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException("Errore esecuzione operazione sul db");
        }
    }

    public Pokemon getPokemon(String nome){
        if (!testConnection()) {
            throw new RuntimeException("Impossibile connettersi al db");
        }

        String query = "SELECT Id, Nome, Tipo1, Tipo2 FROM Pokemon WHERE Nome=?";
        try(PreparedStatement statement=connection.prepareStatement(query)){
            statement.setString(1,nome);
            try(ResultSet resultset=statement.executeQuery()){

                //non serve il while xk non devo scorrere molti risultati ma solo 1 perchè
                //c'è condizione nel where con un campo unique->
                //-> max 1 solo risultato, posso usare if
                if(resultset.next()){
                    int id=resultset.getInt("Id");
                    String nomePokmeon=resultset.getString("Nome");
                    String tipo1=resultset.getString("Tipo1");
                    String tipo2=resultset.getString("Tipo2");

                    List<Types> types = new ArrayList<>();
                    types.add(new Types(new Type(tipo1)));
                    if(tipo2!=null){
                        types.add(new Types(new Type(tipo2)));
                    }

                    return new Pokemon(id, nomePokmeon, types);

                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Errore esecuzione operazione sul db");
        }
        return null;    //se non presente nel db lo prende consumando api
    }

    public Pokemon getPokemon(int id){
        if (!testConnection()) {
            throw new RuntimeException("Impossibile connettersi al db");
        }

        String query = "SELECT Id, Nome, Tipo1, Tipo2 FROM Pokemon WHERE Id=?";
        try(PreparedStatement statement=connection.prepareStatement(query)){
            statement.setInt(1,id);
            try(ResultSet resultset=statement.executeQuery()){

                //non serve il while xk non devo scorrere molti risultati ma solo 1 perchè
                //c'è condizione nel where con una chiave primaria->
                //-> max 1 solo risultato, posso usare if
                if(resultset.next()){
                    int idPokemon=resultset.getInt("Id");
                    String nome=resultset.getString("Nome");
                    String tipo1=resultset.getString("Tipo1");
                    String tipo2=resultset.getString("Tipo2");

                    List<Types> types = new ArrayList<>();
                    types.add(new Types(new Type(tipo1)));
                    if(tipo2!=null){
                        types.add(new Types(new Type(tipo2)));
                    }

                    return new Pokemon(idPokemon, nome, types);
                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Errore esecuzione operazione sul db");
        }
        return null;
    }

    private boolean testConnection(){
        try{
            return connection.isValid(4);
        }
        catch (SQLException e ){
            return false;
        }
    }

}
